package uk.ac.solent.pointsofinterest;

import android.content.Context;
import android.os.Bundle;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

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



    }

    @Override
    public void onClick(View v) {
        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location loc =  mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);

       Pointofinterest poi = new Pointofinterest();

       final EditText etn=(EditText)findViewById(R.id.etn);
       final EditText ett=(EditText)findViewById(R.id.ett);
       final EditText etd=(EditText)findViewById(R.id.etd);
       poi.setName(etn.toString());
       poi.setType(ett.toString());
       poi.setDescription(etd.toString());
       poi.setLatitude(loc.getLatitude());
       poi.setLongitude(loc.getLongitude());

InfoDAO.getPoilist().add(poi);
InfoDAO.save();
    }
}








