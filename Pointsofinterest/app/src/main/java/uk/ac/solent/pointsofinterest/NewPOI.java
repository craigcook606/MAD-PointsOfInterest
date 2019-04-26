package uk.ac.solent.pointsofinterest;
import android.app.Activity;

import android.content.Context;
import android.gesture.Gesture;
import android.os.Bundle;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import java.util.ArrayList;
import android.widget.EditText;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import android.app.AlertDialog;
import android.os.Environment;

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



    }

    @Override
    public void onClick(View v) {
        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location loc =  mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

       pointofinterest poi = new pointofinterest();

       final EditText etn=(EditText)findViewById(R.id.etn);
       final EditText ett=(EditText)findViewById(R.id.ett);
       final EditText etd=(EditText)findViewById(R.id.etd);
       poi.setName(etn.toString());
       poi.setType(ett.toString());
       poi.setDescription(etd.toString());
       poi.setLatitude(loc.getLatitude());
       poi.setLongitude(loc.getLongitude());

       pointofinterest.loadsave(poi);
    }
}








