package at.fhj.mobcomp.trackerjacker.queen;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import at.fhj.mobcomp.trackerjacker.commons.Constants;
import at.fhj.mobcomp.trackerjacker.queen.receiver.MessageDeliveredReceiver;
import at.fhj.mobcomp.trackerjacker.queen.receiver.MessageSentReceiver;

/**
 *
 * http://developer.android.com/training/basics/intents/result.html < contact data
 * http://stackoverflow.com/questions/1981156/android-loading-please-wait < bitte warten screen?
 * http://stackoverflow.com/questions/5554077/need-to-show-loading-screen-while-app-queries-server < loading screen?
 */
public class SendMessageActivity extends Activity {

    private static final String TAG = SendMessageActivity.class.getSimpleName();

    private EditText messageEdit;
    private EditText phoneEdit;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        messageEdit = (EditText) findViewById(R.id.trackingMessage);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_message, menu);
        return true;
    }

    /**
     * Sends the tracking text message to the specified phone number.
     */
    public void sendTrackingMessage(final View view) {
        statusTextView.setText(R.string.status_sending);

        String trackingMessage = Constants.PREFIX + Constants.SEPARATOR + messageEdit.getText().toString();
        String destinationAddress = phoneEdit.getText().toString();

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(Constants.SMS_SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(Constants.SMS_DELIVERED), 0);

        Log.d(TAG, "Registering sending status receiver.");
        registerReceiver(new MessageSentReceiver(this), new IntentFilter(Constants.SMS_SENT));

        Log.d(TAG, "Registering delivery status receiver.");
        registerReceiver(new MessageDeliveredReceiver(this), new IntentFilter(Constants.SMS_DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        Log.d(TAG, "Sending message: " + trackingMessage + " to: " + destinationAddress);
        sms.sendTextMessage(destinationAddress, null, trackingMessage, sentPI, deliveredPI);
    }
}
