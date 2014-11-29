package nl.xannic.minor.touristguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xander.mijntestapplicatie.R;
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

import nl.xannic.minor.data.Data;
import nl.xannic.minor.data.Dataholder;

public class OverviewActivity extends Activity {

    ListView lvOverview;
    double[] lats;
    double[] lons;
    String[] names;
    int[] IDs;
    int categories[];
    double distances[];
    String imageSources[];


    double lat;
    double lon;
    double newLat;
    double newLon;
    LocationManager locManager;
    LocationListener locListener;
    ArrayList<Item> itemList = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

//        int[] categories = new int[]{1, 2, 3, 4, 5, 6};
//        String[] titles = new String[]{"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"};
//        String[] distances = new String[]{"11", "22", "33", "44", "55", "66"};
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new locationListener();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10, locListener);


        lvOverview = (ListView) findViewById(R.id.lvOverview);
        //lvOverview.setAdapter(new lvItemAdapter(this, categories, titles, distances));


        lvOverview.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String product = "ID: " + itemList.get(position).getID();
                product += "\nName: " + itemList.get(position).getName();

                //Toast.makeText(getApplicationContext(), product, Toast.LENGTH_SHORT).show();

                // Launching new Activity on selecting single List Item
                Intent intent = new Intent(getApplicationContext(), itemActivity.class);
                // sending data to new activity
                intent.putExtra("name", itemList.get(position).getName());
                intent.putExtra("category", itemList.get(position).getCategory());
                intent.putExtra("imageSource", itemList.get(position).getImageSource());

                startActivity(intent);
            }
        });

    }



    private void makeList()
    {
        int size = itemList.size();
        String[] stringCat = new String[size];
        String[] stringDist = new String[size];

        for(int n = 0; n < size; n++) {
            categories[n] = itemList.get(n).getCategory();
            names[n] = itemList.get(n).getName();
            stringDist[n] = Double.toString(itemList.get(n).getDistance());
        }

        lvOverview.setAdapter(new lvItemAdapter(this, categories, names, stringDist));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                break;
            case R.id.goToMaps:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }






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
            String url = "http://xannic.nl/api/json2.php";
            url += "?q=SELECT%20*%20FROM%20StatueAndMonuments%20WHERE%20ID%20=%20908";
            getCoordinates.execute(url);
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
    public class GetCoordinates extends AsyncTask<String, Void, List<Data>> {
        Gson gson =  new Gson();

        protected void onPostExecute(List<Data> data) {

            //Deze regel gaat het mis, zie ook regel 162 - 165, data == null 
            int size = data.size();

            IDs = new int[size];
            categories = new int[size];
            imageSources = new String[size];
            distances = new double[size];
            lats = new double[size];
            lons = new double[size];
            names = new String[size];
            float[] results = new float[3];


            for(int i = 0;  i < data.size(); i++){

                int ID = data.get(i).getID();
                int category = data.get(i).getCategoryID();
                String imageSource = data.get(i).getImage();
                String name = data.get(i).getName();
                double latitude = data.get(i).getLat();
                double longitude = data.get(i).getLon();

//              IDs[i] = ID;
//              categories[i] = category;
//              imageSources[i] = imageSource;
//              names[i] = name;
//              lats[i] = latitude;
//              lons[i] = longitude;
                Location.distanceBetween(lat, lon, latitude, longitude, results);
//              distances[i] =  results[0];
                double distance = results[0];

                Item item = new Item(ID, category, longitude, latitude, distance,  name, imageSource);
                itemList.add(item);

            }
            Collections.sort(itemList, new Comparator() {

                public int compare(Object o1, Object o2) {
                    Item item1 = (Item) o1;
                    Item item2 = (Item) o2;
                    return Double.compare(item1.getDistance(), item2.getDistance());
                }
            });
            makeList();
        }

        protected List<Data> doInBackground(String... params) {
            Dataholder holder = null;
            List<Data> data = null;
            String output = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
                String json = output;
                holder = gson.fromJson(json, Dataholder.class);
                data = holder.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }
    }
}
