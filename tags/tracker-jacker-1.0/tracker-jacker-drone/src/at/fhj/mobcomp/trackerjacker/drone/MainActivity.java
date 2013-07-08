package at.fhj.mobcomp.trackerjacker.drone;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import at.fhj.mobcomp.trackerjacker.commons.Constants;

public class MainActivity extends Activity {

	private EditText locationEdit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationEdit = (EditText) findViewById(R.id.commandEdit);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String defaultCmd = getString(R.string.default_cmd);
        final String locationCmd = prefs.getString(Constants.GET_LOCATION_KEY, defaultCmd);

        locationEdit.setText(locationCmd);
    }
    
    public void saveCommand(View view) {
    	final String locationCmd = locationEdit.getText().toString();
    	
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	Editor edit = preferences.edit();
    	edit.putString(Constants.GET_LOCATION_KEY, locationCmd);
    	edit.commit(); 
	}

}
