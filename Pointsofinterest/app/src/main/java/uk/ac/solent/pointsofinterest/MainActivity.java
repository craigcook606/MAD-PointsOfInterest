package uk.ac.solent.pointsofinterest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;
import android.content.Context;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.os.AsyncTask;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.widget.EditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity implements LocationListener {

    MapView mv;

    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    ItemizedIconOverlay<OverlayItem> items;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        System.out.println("debug loc=" + loc);

        mv = findViewById(R.id.map1);

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(loc.getLatitude(), loc.getLongitude()));
    }

    public void onLocationChanged(Location newLoc) {
        mv = findViewById(R.id.map1);
        mv.getController().setCenter(new GeoPoint(newLoc.getLatitude(), newLoc.getLongitude()));
        Toast.makeText
                (this, "Location=" +
                        newLoc.getLatitude() + " " +
                        newLoc.getLongitude(), Toast.LENGTH_LONG).show();
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addpoints) {
            // react to the menu item being selected...
            Intent intent = new Intent(this, NewPOI.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (item.getItemId() == R.id.savepoints) {
            InfoDAO poilist = null;
            poilist.save();

            return true;
        }

        if (item.getItemId() == R.id.loadpoints) {
            InfoDAO.load();
            addmarkers();
            return true;
        }
        if (item.getItemId() == R.id.preferences) {
            Intent intent = new Intent(this, ActivityPreferences.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    class MarkerGesture implements ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {

        public boolean longPress(OverlayItem item) {
            Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
            return true;
        }

        public boolean singleTap(int i, OverlayItem item) {
            Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onItemSingleTapUp(int index, OverlayItem item) {
            return false;
        }

        @Override
        public boolean onItemLongPress(int index, OverlayItem item) {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Pointsofinterest poi = new Pointsofinterest();
                MapView mv = findViewById(R.id.map1);
                Bundle extras = intent.getExtras();
                String etn = extras.getString("names");
                String ett = extras.getString("types");
                String etd = extras.getString("descriptions");

                Double lat = mv.getMapCenter().getLatitude();
                Double lon = mv.getMapCenter().getLongitude();
                poi.setType(ett);
                poi.setName(etn);
                poi.setDescription(etd);
                poi.setLongitude(lat);
                poi.setlongitude(lon);

                Log.d("Assignment", "lat=" + lat + "lon=" + lon);
                InfoDAO.addPOI(poi);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                boolean autoupload = prefs.getBoolean("autoupload", false);
                if (autoupload) {
                    System.out.print("auto-upload enabled");
                }

                addmarkers();

            }
        }
    }

    private void addmarkers() {
        markerGestureListener = new MarkerGesture();


        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        for (Pointsofinterest poi : InfoDAO.getPoilist()) {
            System.out.println("items being added:" + poi);
            OverlayItem marker = new OverlayItem(poi.getName(), poi.getType(), poi.getDescription(), new GeoPoint(poi.getLatitude(), poi.getLongitude()));
            items.addItem(marker);
        }
        mv.getOverlays().clear();
        mv.getOverlays().add(items);
    }

    public void onResume() {


        super.onResume();
    }

    public void onDestroy() {
        InfoDAO.save();
        super.onDestroy();
    }


                };



