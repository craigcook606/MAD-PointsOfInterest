package uk.ac.solent.pointsofinterest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

import android.widget.Button;
import android.widget.EditText;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.location.LocationManager;
import android.location.Location;

public class NewPOI extends AppCompatActivity implements View.OnClickListener

{

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpoints);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Button standard = (Button) findViewById(R.id.btn1);
        standard.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();


        final EditText etn = (EditText) findViewById(R.id.etn);
        final EditText ett = (EditText) findViewById(R.id.ett);
        final EditText etd = (EditText) findViewById(R.id.etd);

        bundle.putString("name", etn.getText().toString());
        bundle.putString("type", ett.getText().toString());
        bundle.putString("description", etd.getText().toString());

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();


    }
}











