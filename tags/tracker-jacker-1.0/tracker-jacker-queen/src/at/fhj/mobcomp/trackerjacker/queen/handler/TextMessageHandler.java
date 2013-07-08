package at.fhj.mobcomp.trackerjacker.queen.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class TextMessageHandler extends AsyncTask<String, String, Void> {

    ProgressDialog dialog;

    public TextMessageHandler(Context context) {
        super();
        dialog = new ProgressDialog(context);
    }

    protected void onPreExecute() {
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params == null || params.length != 2) {
            throw new IllegalArgumentException("Please specifiy the destination number and the tracking message");
        }

        // final String destinationAddress = params[0];
        // final String trackingMessage = params[1];
        //
        //
        // publishProgress("Sending '" + trackingMessage + "' to '" + destinationAddress + "'...");
        //
        // PendingIntent pi;
        // SmsManager.getDefault().sendTextMessage(destinationAddress, null, text, sentIntent, deliveryIntent)
        //
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

}
