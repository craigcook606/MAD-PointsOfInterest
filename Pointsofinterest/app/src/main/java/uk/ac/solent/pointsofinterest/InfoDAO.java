package uk.ac.solent.pointsofinterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.preference.PreferenceManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class InfoDAO {

    private static List<Pointsofinterest> poilist = new ArrayList<Pointsofinterest>();

    public static List<Pointsofinterest> getPoilist() {
        return poilist;
    }

    public static void load() {

        BufferedReader reader = null;
        try {
            poilist.clear();
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.csv");
            if( f.exists()) {
                FileReader fr = new FileReader(f);
                reader = new BufferedReader(fr);
                String line = "";
                // The Crown,pub,nice pub,-1.4011,50.9319

                while ((line = reader.readLine()) != null) {
                    String[] columns = line.split(",");
                    String name = columns[0];
                    String type = columns[1];
                    String description = columns[2];
                    String latStr = columns[3];
                    double lat = Double.parseDouble(latStr);
                    String lonStr = columns[4];
                    double lon = Double.parseDouble(lonStr);
                    Pointsofinterest poi = new Pointsofinterest();

                    //do more
                    poi.setDescription(description);
                    poi.setName(name);
                    poi.setType(type);
                    poi.setLatitude(lat);
                    poi.setLongitude(lon);
                    poilist.add(poi);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("problem loading file:", e);

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

        }
    }
    public static void addPOI(Pointsofinterest poi) {
        load();
        poilist.add(poi);
        save();
    }

    public static void save() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.csv"));
            // The Crown,pub,nice pub,-1.4011,50.9319
            for (Pointsofinterest poi : poilist) {
                String line = poi.getName() + "," + poi.getType() + "," + poi.getDescription() + "," + poi.getLatitude() + "," + poi.getLongitude();
                pw.println(line);
            }


        } catch (Exception e) {
            throw new RuntimeException("problem loading file:", e);

        } finally {
            if (pw != null) pw.close(); // close the file to ensure data is flushed to file
        }
    }

}

