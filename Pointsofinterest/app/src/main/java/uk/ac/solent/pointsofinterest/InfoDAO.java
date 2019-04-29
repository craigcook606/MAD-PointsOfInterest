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

    private static List<Pointofinterest> poilist = new ArrayList<Pointofinterest>();

    public static List<Pointofinterest> getPoilist() {
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
                    Pointofinterest poi = new Pointofinterest();
                    //do more
                    poi.setDescription(description);

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

    public static void save() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.csv"));
            // The Crown,pub,nice pub,-1.4011,50.9319
            for (Pointofinterest poi : poilist) {
                String line = poi.getName() + "," + poi.getType();
                pw.println(line);
            }


        } catch (Exception e) {
            throw new RuntimeException("problem loading file:", e);

        } finally {
            if (pw != null) pw.close(); // close the file to ensure data is flushed to file
        }
    }

}

