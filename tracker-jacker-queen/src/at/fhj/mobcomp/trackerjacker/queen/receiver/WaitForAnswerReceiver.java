package at.fhj.mobcomp.trackerjacker.queen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import at.fhj.mobcomp.trackerjacker.commons.Constants;
import at.fhj.mobcomp.trackerjacker.queen.ShowMapActivity;

public class WaitForAnswerReceiver extends BroadcastReceiver {

    private static final String TAG = WaitForAnswerReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Message received (" + intent.getAction() + ")");

        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final Object[] pdus = (Object[]) bundle.get(Constants.PDUS_KEY);

            Log.d(TAG, "Processing " + pdus.length + " messages.");
            for (int i = 0; i < pdus.length; i++) {
                final SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                final String messageBody = message.getMessageBody();
                final String originatingAddress = message.getOriginatingAddress();

                Log.d(TAG, "From: " + originatingAddress + " Body: " + messageBody);

                // TODO also add check whether source of SMS is authorized to get location? (configurable number list)
                if (messageBody.startsWith(Constants.LOCATION_MESSAGE)) {
                    abortBroadcast();

                    // "parse" the location message
                    final int start = messageBody.indexOf("(") + 1;
                    final int end = messageBody.lastIndexOf(")");

                    final String[] values = messageBody.substring(start, end).split(Constants.SEPARATOR);
                    final String method = values[0];
                    final String provider = values[1];
                    final Double latitude = Double.valueOf(values[2]);
                    final Double longitude = Double.valueOf(values[3]);

                    final Intent showMapIntent = new Intent(context, ShowMapActivity.class);
                    showMapIntent.putExtra(Constants.KEY_ADDRESS, originatingAddress);
                    showMapIntent.putExtra(Constants.KEY_LATITUDE, latitude);
                    showMapIntent.putExtra(Constants.KEY_LONGITUDE, longitude);
                    showMapIntent.putExtra(Constants.KEY_METHOD, method);
                    showMapIntent.putExtra(Constants.KEY_PROVIDER, provider);
                    context.startActivity(showMapIntent);

                    context.unregisterReceiver(this);
                }
            }
        }
    }

}
