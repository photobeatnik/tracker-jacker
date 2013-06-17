package at.fhj.mobcomp.trackerjacker.queen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 *
 * http://developer.android.com/training/basics/intents/result.html < contact data
 * http://stackoverflow.com/questions/1981156/android-loading-please-wait < bitte warten screen?
 * http://stackoverflow.com/questions/5554077/need-to-show-loading-screen-while-app-queries-server < loading screen?
 */
public class SendMessageActivity extends Activity {

    private static final String TAG = SendMessageActivity.class.getSimpleName();

    private EditText phoneNumberEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        phoneNumberEdit = (EditText) findViewById(R.id.phoneEdit);
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
        // read tracking text (zauberwort) from edit field
        // read phone number from edit field

        final String destinationAddress = phoneNumberEdit.getText().toString();
        Log.d(TAG, destinationAddress);

        // send text message
        // TODO send with delivery and sent intent to make loading screen react to it!!
        // SmsManager.getDefault().sendTextMessage(destinationAddress, null, "tj:wherareyou", null, null);

        // Additionally:
        // create please wait screen/disable edit fields buttons on activity?
        // or switch activity to "please wait" screen (with timeout?)

        // this is just a place holder... for playing with progress dialog
        AsyncTask<Integer, String, Void> task = new AsyncTask<Integer, String, Void>() {
            ProgressDialog dialog;

            protected void onPreExecute() {
                dialog = new ProgressDialog(SendMessageActivity.this);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Integer... params) {
                List<String> messages = new ArrayList<String>();
                messages.add("Sending tracking SMS...");
                messages.add("Tracking message sent...");
                messages.add("Delivered! Waiting for answer...");

                for (String message : messages) {
                    try {
                        // dialog.setMessage(message);
                        publishProgress(message);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                dialog.setMessage(values[0]);
            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();
            }
        };

        task.execute(1);
    }
}
