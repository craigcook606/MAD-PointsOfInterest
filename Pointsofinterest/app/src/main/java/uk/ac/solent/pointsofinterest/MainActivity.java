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

import java.util.ArrayList;

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
        if (item.getItemId() == R.id.savePoint) {
            poilist.save();

            return true;
        }

        if (item.getItemId() == R.id.loadPoint) {
            POIList.load();
            addmarkers();
            return true;
        }
        if (item.getItemId() == R.id.pref) {
            Intent intent = new Intent(this, Pointsofinterest.class);
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
    }

    protected void ActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                NewPOI poi = new NewPOI();
                MapView mv = findViewById(R.id.map1);
                Bundle extras = intent.getExtras();
                String etn = extras.getString("name");
                String ett = extras.getString("type");
                String etd = extras.getString("description");

                Double lat = mv.getMapCenter().getLatitude();
                Double lon = mv.getMapCenter().getLongitude();
                poi.setName(name);
                poi.setType(type);
                poi.setDescription(description);
                poi.setLatitude(lat);
                poi.setlongitude(lon);

                Log.d("Assignment", "lat=" + lat + "lon=" + lon);
                POIList.add(poi);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                boolean autoupload = prefs.getBoolean("autoupload", false);
                if (autoupload) {
                    MyTask t = new MyTask();
                    t.execute(poi);
                }

                addmarkers();

            }
        }
    }

    private void addmarkers() {
        markerGestureListener = new MarkerGesture();


        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        for (NewPOI poi : POIList.getPoiList()) {
            System.out.println("adding poi to items:" + poi);
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
        POIList.save();
        super.onDestroy();
    }


                };



