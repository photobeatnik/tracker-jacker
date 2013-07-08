package at.fhj.mobcomp.trackerjacker.queen.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;
import at.fhj.mobcomp.trackerjacker.commons.Constants;
import at.fhj.mobcomp.trackerjacker.queen.R;

public class MessageSentReceiver extends BroadcastReceiver {

    private static final String TAG = MessageSentReceiver.class.getSimpleName();

    private final Activity parent;

    public MessageSentReceiver(final Activity parent) {
        this.parent = parent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final TextView statusTextView = (TextView) parent.findViewById(R.id.statusTextView);
        final int resultCode = getResultCode();

        if (resultCode == Activity.RESULT_OK) {
            statusTextView.setText(R.string.status_sent);

            Log.d(TAG, "Message was sent. Registering receiver to wait for location answer.");
            IntentFilter filter = new IntentFilter(Constants.SMS_RECEIVED);
            filter.setPriority(Constants.HIGHEST_PRIORITY);
            context.registerReceiver(new WaitForAnswerReceiver(), filter);
        } else {
            Log.e(TAG, "Message was not sent. Error code: " + resultCode);
            statusTextView.setText(R.string.status_error_sent);
        }
    }

}
