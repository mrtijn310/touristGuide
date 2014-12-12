package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Martijn on 8-12-2014.
 */
public class Data{

    public static ArrayList<Item> itemList = new ArrayList<Item>();
    public static String cityName = "City";
    public static double lat, lon;
    public static double newLat, newLon;

    LocationManager locManager;
    LocationListener locListener;

    int locationUpdateTimeMilliseconds = 300000;
    int locationUpdateMeter = 10;


    public ArrayList getSortedList(ArrayList<Item> list) {
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Item item1 = (Item) o1;
                Item item2 = (Item) o2;
                return Double.compare(item1.getDistance(), item2.getDistance());
            }
        });

        return list;
    }

    public void getLocation() {

        String context = Context.LOCATION_SERVICE;
        Context anotherContext = Config.context;
        locManager = (LocationManager) anotherContext.getSystemService(context);
        locListener = new locationListener();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationUpdateTimeMilliseconds, locationUpdateMeter, locListener);
    }

    // locationListener
    public class locationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();

//            newLat = lat * 1000;
//            newLat = Math.round(newLat);
//            newLat /= 1000;
//
//            newLon = lon * 1000;
//            newLon = Math.round(newLon);
//            newLon /= 1000;

            GetCoordinates getCoordinates = new GetCoordinates();
            String sql = "http://xannic.nl/api/json2.php";
            //sql += "?q=SELECT%20*%20FROM%20StatueAndMonuments";
            sql += "?q=SELECT%20*%20FROM%20StatueAndMonuments%20ORDER%20BY%20ID%20DESC%20LIMIT%2010";
            //sql += "%20ORDER%20BY%20abs(lat%20-%20("+ lat +"))%20+%20abs(%20lon%20-%20("+lon+"))%20LIMIT%2030";

            getCoordinates.execute(sql);
        }

        @Override
        public void onProviderDisabled(String provider) {
//            Toast.makeText(getApplicationContext(),
//                    "Gps Disabled",
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
//            Toast.makeText(getApplicationContext(),
//                    "Gps Enabled",
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    // AsyncTask GetCoordinates
    public class GetCoordinates extends AsyncTask<String, Void, List<Item>> {
        Gson gson =  new Gson();

        protected void onPostExecute(List<Item> items) {
            int size = items.size();

//            lats = new double[size];
//            lons = new double[size];
//            names = new String[size];
//
//            for(int i = 0;  i < items.size(); i++){
//                String name = items.get(i).getName();
//                double lat = items.get(i).getLat();
//                double lon = items.get(i).getLon();
//
//                names[i] = name;
//                lats[i] = lat;
//                lons[i] = lon;
//            }
        }

        protected List<Item> doInBackground(String... params) {
            ItemHolder holder = null;
            List<Item> items = null;
            String output = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
                String json = output;
                holder = gson.fromJson(json, ItemHolder.class);
                items = holder.getItem();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Data data  = new Data();
            Data.cityName = "City";
            Data.itemList = (ArrayList<Item>)items;
            Data.lat = newLat;
            Data.lon = newLon;
            //goToMain();
            return items;
        }
    }

}
