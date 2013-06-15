package at.fhj.mobcomp.trackerjacker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Receives and sends SMS/text message.<br />
 *
 * Based on:
 * <ul>
 * <li><a href="http://www.slideshare.net/androidstream/android-application-component- broadcastreceiver-tutorial
 * ">http://www.slideshare.net/androidstream/android-application-component- broadcastreceiver-tutorial</a></li>
 * <li><a href="http://www.anddev.org/recognize-react_on_incoming_sms-t295.html">
 * http://www.anddev.org/recognize-react_on_incoming_sms-t295.html</a></li>
 * <li><a href= "http://stackoverflow.com/questions/1973071/broadcastreceiver-sms-received" >http
 * ://stackoverflow.com/questions/1973071/broadcastreceiver-sms-received</a></li>
 * </ul>
 * <br />
 * Gets GPS coordinates based on:
 * <ul>
 * <li><a
 * href="http://stackoverflow.com/questions/5240246/broadcastreceiver-for-location">http://stackoverflow.com/questions
 * /5240246/broadcastreceiver-for-location</a></li>
 * <li><a href="http://www.vogella.com/articles/AndroidLocationAPI/article.html">http://www.vogella.com/articles/
 * AndroidLocationAPI/article.html</a></li>
 * </ul>
 *
 * @author Stefan Eder (<a href="mailto:adenoidhynkel666@gmail.com">adenoidhynkel666
 * @gmail.com</a>)
 *
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = SMSReceiver.class.getSimpleName();
    private static final String PDUS_KEY = "pdus";

    /**
     * Prefix which indicates this is a message from the Tracker Jacker app.
     */
    private static final String TJ_PREFIX = "tj";

    /**
     * Indicates the command for the getting the location. TODO Move this somewhere global (also relevant for sending)
     * and make configurable?
     */
    private static final String TJ_GET_LOCATION_CMD = "whereareyou";

    /**
     * Indicates the location message of the application.
     */
    private static final String TJ_GET_LOCATION_MSG = TJ_PREFIX + ":" + TJ_GET_LOCATION_CMD;

    private static final String TJ_KNOWN_LOCATION_MSG = TJ_PREFIX + ":loc(%f:%f)";
    private static final String TJ_UNKNOWN_LOCATION_MSG = TJ_PREFIX + ":loc(unknown)";

    /**
     * Send the location as text message to the given destination address.
     *
     * @param location
     *            Specifies the location to use.
     * @param destinationAddress
     *            Specifies the address which should receive the location.
     */
    private void sendLocation(Location location, String destinationAddress) {
        final String text;

        if (location != null) {
            text = String.format(TJ_KNOWN_LOCATION_MSG, location.getLongitude(), location.getLatitude());
        } else {
            text = TJ_UNKNOWN_LOCATION_MSG;
        }

        Log.d(TAG, "Sending location: " + text + " to: " + destinationAddress);
        SmsManager.getDefault().sendTextMessage(destinationAddress, null, text, null, null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Message received (" + intent.getAction() + ")");

        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final Object[] pdus = (Object[]) bundle.get(PDUS_KEY);

            Log.d(TAG, "Processing " + pdus.length + " messages.");
            for (int i = 0; i < pdus.length; i++) {
                final SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                final String messageBody = message.getMessageBody();
                final String originatingAddress = message.getOriginatingAddress();

                Log.d(TAG, "From: " + originatingAddress + " Body: " + messageBody);

                // TODO also add check whether source of SMS is authorized to get location? (configurable number list)
                if (TJ_GET_LOCATION_MSG.equals(messageBody)) {
                    Log.d(TAG, "Message (" + messageBody + ") equals location message. Sending back location...");

                    // get gps coordinates and send back message to originator
                    // TODO probably better to start this in a service and own thread/process
                    // TODO it is possible deactivated -> let user activate first? in app?
                    // get the location manager from the context
                    final LocationManager locationManager = (LocationManager) context
                            .getSystemService(Context.LOCATION_SERVICE);
                    final String provider = locationManager.getBestProvider(new Criteria(), true);
                    Log.d(TAG, "Found best location provider: " + provider);

                    // request a single update of the location with provider.
                    // locationManager.getLastKnownLocation(provider); <-- did not work with emulator
                    // TODO getLastKnownLocation as fallback? (dont know if this works)
                    final Location location = locationManager.getLastKnownLocation(provider);

                    if (location == null) {
                        Log.d(TAG, "Using last known location...");
                        sendLocation(location, originatingAddress);
                    } else {
                        Log.d(TAG, "Last known location unkown. Waiting for location update...");
                        locationManager.requestSingleUpdate(provider, new LocationListener() {

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                // if (status != LocationProvider.AVAILABLE) {
                                // Log.d(TAG,
                                // "Location service not available. Trying to use last known location as fallback.");
                                // sendLocation(locationManager.getLastKnownLocation(provider), originatingAddress);
                                // }
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }

                            @Override
                            public void onLocationChanged(Location location) {
                                sendLocation(location, originatingAddress);
                            }
                        }, null);
                    }

                    // do not let other apps get the TJ message
                    // TODO do not abort if more than one message. other messages need to be passed on...
                    abortBroadcast();
                }
            }
        }
    }
}
