package at.fhj.mobcomp.trackerjacker.drone;

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
import at.fhj.mobcomp.trackerjacker.commons.Constants;

/**
 * Tracks the location of a mobile device by waiting a specific text message and sending back the location to the origin
 * of the message. <br />
 * <br />
 *
 * SMS Sending/Receiving based on:
 * <ul>
 * <li><a href="http://www.slideshare.net/androidstream/android-application-component- broadcastreceiver-tutorial
 * ">http://www.slideshare.net/androidstream/android-application-component- broadcastreceiver-tutorial</a></li>
 * <li><a href="http://www.anddev.org/recognize-react_on_incoming_sms-t295.html">
 * http://www.anddev.org/recognize-react_on_incoming_sms-t295.html</a></li>
 * <li><a href= "http://stackoverflow.com/questions/1973071/broadcastreceiver-sms-received" >http
 * ://stackoverflow.com/questions/1973071/broadcastreceiver-sms-received</a></li>
 * </ul>
 * <br />
 * Location tracking based on:
 * <ul>
 * <li><a
 * href="http://stackoverflow.com/questions/5240246/broadcastreceiver-for-location">http://stackoverflow.com/questions
 * /5240246/broadcastreceiver-for-location</a></li>
 * <li><a href="http://www.vogella.com/articles/AndroidLocationAPI/article.html">http://www.vogella.com/articles/
 * AndroidLocationAPI/article.html</a></li>
 * </ul>
 */
public class LocationTracker extends BroadcastReceiver {

    private static final String TAG = LocationTracker.class.getSimpleName();

    /** Key for getting PDUs from bundle object. */
    private static final String PDUS_KEY = "pdus";

    /**
     * Send the location as text message to the given destination address.
     *
     * @param destinationAddress
     *            Specifies the address which should receive the location.
     * @param location
     *            Specifies the location to use.
     * @param method
     *            How the location was found either Constants.LAST_KNOWN_LOCATION_METHOD or
     *            Constants.ACUTAL_LOCATION_METHOD.
     */
    private void sendLocation(String destinationAddress, Location location, String method) {
        final String locationString;

        // if location object is not initialized, the location is unknown
        if (location == null) {
            locationString = Constants.UNKOWN_LOCATION;
        } else {
            // build string like <key>:<lat>:<long>
            // TODO add bearing, altitude and confidence?
            locationString = new StringBuilder() //
                    .append(method).append(Constants.SEPARATOR) //
                    .append(location.getLatitude()).append(Constants.SEPARATOR) //
                    .append(location.getLongitude()).toString();
        }

        // message will look like:
        // * tj:loc(<key>:<lat>:<long>)
        // * tj:loc(unknown)
        final String locationMessage = String.format(Constants.SEND_LOCATION_MSG, locationString);
        Log.d(TAG, "Sending location: " + locationMessage + " to: " + destinationAddress);
        SmsManager.getDefault().sendTextMessage(destinationAddress, null, locationMessage, null, null);
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
                if (Constants.GET_LOCATION_MSG.equals(messageBody)) {
                    Log.d(TAG, "Message (" + messageBody + ") equals location message. Sending back location...");

                    // get gps coordinates and send back message to originator
                    // TODO location service possible deactivated -> let user activate first? in app?
                    // get the location manager from the context
                    final LocationManager locationManager = (LocationManager) context
                            .getSystemService(Context.LOCATION_SERVICE);
                    final String provider = locationManager.getBestProvider(new Criteria(), true);
                    Log.d(TAG, "Found best location provider: " + provider);

                    // request a single update of the location with provider.
                    // locationManager.getLastKnownLocation(provider); <-- did not work with emulator
                    // TODO getLastKnownLocation as fallback and not fresh location? (maybe works on real device)
                    // TODO test this on real device
                    final Location location = locationManager.getLastKnownLocation(provider);

                    if (location != null) {
                        Log.d(TAG, "Using last known location...");
                        sendLocation(originatingAddress, location, Constants.LAST_KNOWN_LOCATION_METHOD + Constants.SEPARATOR + provider);
                    } else {
                        Log.d(TAG, "Last known location unkown. Waiting for location update...");
                        locationManager.requestSingleUpdate(provider, new LocationListener() {

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                // debug to see if this is called in case no update happens
                                Log.d(TAG, "Status changed: " + status);
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }

                            @Override
                            public void onLocationChanged(Location location) {
                                sendLocation(originatingAddress, location, Constants.ACTUAL_LOCATION_METHOD + Constants.SEPARATOR + provider);
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
