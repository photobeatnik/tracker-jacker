package at.fhj.mobcomp.trackerjacker.queen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import at.fhj.mobcomp.trackerjacker.commons.Constants;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMapActivity extends FragmentActivity {

    private static final String TAG = ShowMapActivity.class.getSimpleName();

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        final Intent intent = getIntent();

        if (intent != null) {

            // get data from intent
            final String address = intent.getStringExtra(Constants.KEY_ADDRESS);
            final String method = intent.getStringExtra(Constants.KEY_METHOD);
            final String provider = intent.getStringExtra(Constants.KEY_PROVIDER);
            final Double latitude = intent.getDoubleExtra(Constants.KEY_LATITUDE, 0.0);
            final Double longitude = intent.getDoubleExtra(Constants.KEY_LONGITUDE, 0.0);

            final LatLng position = new LatLng(latitude, longitude);

            MarkerOptions options = new MarkerOptions() //
                    .position(position) //
                    .title(PhoneNumberUtils.formatNumber(address)) //
                    .snippet("Provider: " + provider + " Method: " + method) //
                    .visible(true) //
                    .draggable(false);

            // SupportMapFragment to support older APIs (http://stackoverflow.com/a/14128296/2174032)
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            map.addMarker(options);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        } else {
            Log.e(TAG, "There was no intent...");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.queen, menu);
        return true;
    }

}
