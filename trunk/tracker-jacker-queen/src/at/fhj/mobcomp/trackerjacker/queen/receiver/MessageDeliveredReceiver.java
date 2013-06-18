package at.fhj.mobcomp.trackerjacker.queen.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import at.fhj.mobcomp.trackerjacker.queen.R;

public class MessageDeliveredReceiver extends BroadcastReceiver {

    private static final String TAG = MessageDeliveredReceiver.class.getSimpleName();

    private final Activity parent;

    public MessageDeliveredReceiver(final Activity parent) {
        this.parent = parent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final TextView statusTextView = (TextView) parent.findViewById(R.id.statusTextView);
        final int resultCode = getResultCode();

        if (resultCode == Activity.RESULT_OK) {
            statusTextView.setText(R.string.status_delivered);
        } else {
            Log.e(TAG, "Message was not delivered. Error code: " + resultCode);
            statusTextView.setText(R.string.status_error_delivered);
        }
    }

}
