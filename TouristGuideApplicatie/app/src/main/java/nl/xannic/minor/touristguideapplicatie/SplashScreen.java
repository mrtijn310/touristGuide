package nl.xannic.minor.touristguideapplicatie;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import nl.xannic.minor.touristguideapplicatie.Item;

public class SplashScreen extends Activity {

    double[] lats;
    double[] lons;
    String[] names;
    double lat;
    double lon;
    double newLat;
    double newLon;
    LocationManager locManager;
    LocationListener locListener;
//    List<Item> itemList = new ArrayList<Item>();
    ImageView imgSplash;
    int locationUpdateTimeMilliseconds = 300000;
    int locationUpdateMeter = 10;
    boolean ISTEST = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imgSplash = (ImageView) findViewById(R.id.imgSplash);
        imgSplash.setImageResource(R.drawable.splash);
        Config.context = this;
        Button btnGoToMain = (Button) findViewById(R.id.btnGoToMain);
        btnGoToMain.setOnClickListener(btnClickGoToMain);



        if (ISTEST)
        {
            ArrayList<Item> testItems = new ArrayList<Item>();
            Item dataItem;
            //int ID, int CategoryID, double lon, double lat, double distance, String Name , String Image
            dataItem = new Item(1, 1, 51.841928, 4.925339, 1.2, "Prachtige kerk in Schelluinen", "http://www.kerktijden.nl/fotos_kerken/realsize/kerk2840_1.jpg");
            testItems.add(dataItem);
            dataItem = new Item(2, 2, 51.841528, 4.825339, 3.4, "De lekkerste frietjes in Schelluinen", "");
            testItems.add(dataItem);
            dataItem = new Item(3, 3, 51.831928, 4.725339, 5.6, "Iedereen tussen 12 en 18 is welkom", "");
            testItems.add(dataItem);

            //Data data  = new Data();
            Data.cityName = "City";
            Data.itemList = testItems;
            Data.lat = 51.9166667;
            Data.lon = 4.5;
            goToMain();
        }

        else
           {
            getLocation();
//            locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            locListener = new locationListener();
//            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationUpdateTimeMilliseconds, locationUpdateMeter, locListener);

//            Data mData = new Data();
//            mData.getLocation();
//           goToMain();
        }
    }

    public void getLocation() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new locationListener();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationUpdateTimeMilliseconds, locationUpdateMeter, locListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToMain() {
        Intent intentMain = new Intent(this, Main.class);
        intentMain.putExtra("goToSplashScreen", "false");
        startActivity(intentMain);
        finish();
    }



    View.OnClickListener btnClickGoToMain =
            new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    goToMain();
                }};





    // locationListener
    public class locationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();

            newLat = lat * 1000;
            newLat = Math.round(newLat);
            newLat /= 1000;

            newLon = lon * 1000;
            newLon = Math.round(newLon);
            newLon /= 1000;

            GetCoordinates getCoordinates = new GetCoordinates();
//            //sql += "?q=SELECT%20*%20FROM%20StatueAndMonuments";
//            sql += "?q=SELECT%20*%20FROM%20StatueAndMonuments%20ORDER%20BY%20ID%20DESC%20LIMIT%2010";

            String sql = "http://xannic.nl/api/json2.php";
            sql += "?q=";
            sql += "SELECT ID, Name, CategoryID, lat, lon, isVisible";
            sql += " FROM Events";
            sql += " WHERE isVisible = 1";
            sql += " UNION ALL";
            sql += " SELECT ID, Name, CategoryID, lat, lon, isVisible";
            sql += " FROM FoodsAndDrinks";
            sql += " WHERE isVisible = 1";
            sql += " UNION ALL";
            sql += " SELECT ID, Name, CategoryID, lat, lon, isVisible";
            sql += " FROM MuseaAndBuildings";
            sql += " WHERE isVisible = 1";
            sql += " UNION ALL";
            sql += " SELECT ID, Name, CategoryID, lat, lon, isVisible";
            sql += " FROM StatueAndMonuments";
            sql += " WHERE isVisible = 1";
            //sql += " ORDER BY abs(lat - (51.818400)) + abs( lon - (4.654671))";
            sql += " LIMIT 10";
            sql = sql.replaceAll(" ", "%20");
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

            lats = new double[size];
            lons = new double[size];
            names = new String[size];

            for(int i = 0;  i < items.size(); i++){
                String name = items.get(i).getName();
                double lat = items.get(i).getLat();
                double lon = items.get(i).getLon();

                names[i] = name;
                lats[i] = lat;
                lons[i] = lon;
            }
        }

        protected List<Item> doInBackground(String... params) {
            ItemHolder holder = null;
            List<Item> items = null;
            String output = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
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
            goToMain();
            return items;
        }
    }
}