package nl.xannic.minor.rondjerotterdam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import java.util.List;

public class SplashScreen extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{

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
    ImageView imgLoader;
    int locationUpdateTimeMilliseconds = 300000;
    int locationUpdateMeter = 10;
    boolean ISTEST = false;
    LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    boolean isGoToMain = true;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imgSplash = (ImageView) findViewById(R.id.imgSplash);
        imgLoader = (ImageView) findViewById(R.id.imgLoader);
        Config.context = this;

        Animation a = AnimationUtils.loadAnimation(this, R.anim.circleloader);
        a.setDuration(3000);
        imgLoader.startAnimation(a);

        List<String> categories = new ArrayList<String>();
        categories.add("Monumenten");
        categories.add("Eetcafes");
        categories.add("Evenementen");
        categories.add("Musea");
        Data.categoriesList = categories;

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

        else {
            initClient();
            initManager();
        }
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!ISTEST) {
            if (isLocationAvailable() && isNetworkAvailable()) {
                getLocation();
            }

            else
            {
                if (!isLocationAvailable()) {

                    final Context context = this;

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage("Uw GPS staat niet aan");
                    dialog.setPositiveButton("Ga naar instellingen", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                            //get gps
                        }
                    });
                    dialog.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            finish();

                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }

                if (!isNetworkAvailable()) {

                    final Context context = this;

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage("Uw telefoon is niet verbonden met het internet");
                    dialog.setPositiveButton("Ga naar instellingen", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            Intent myIntent = new Intent(Settings.ACTION_SETTINGS);
                            context.startActivity(myIntent);
                            //get gps
                        }
                    });
                    dialog.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            finish();

                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        }

    }

    private boolean isLocationAvailable()
    {
        boolean isGPSEnables = false, isNetworkEnables = false;

        try{
            isGPSEnables =  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){
            return false;
        }

        try{
            isNetworkEnables = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            return false;
        }

        if(!isGPSEnables && !isNetworkEnables) {
            return false;
        }

        else {
            return true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void goToMain() {
        if(isGoToMain==true){
            isGoToMain = false;
            Intent intentMain = new Intent(this, Main.class);
            intentMain.putExtra("goToSplashScreen", "false");
            startActivity(intentMain);
            finish();
        }
    }



    public void initClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }

    public void initManager() {
        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest
                .setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setInterval(locationUpdateTimeMilliseconds);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void connectClient() {
        mGoogleApiClient.connect();
    }

    public void disconnectClient() {
        mGoogleApiClient.disconnect();
    }

    public Location getLocation() {
        Location foundLocation;
        if (!mGoogleApiClient.isConnected()) {
            connectClient();
        }
        foundLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (foundLocation == null) {
            foundLocation = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return foundLocation;
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }



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

        String sql = "http://xannic.nl/api/json4.php";
        sql +="?getevents=1";
        sql +="&getfoodsanddrinks=1";
        sql +="&getmuseaandbuildings=1";
        sql +="&getstatueandmonuments=1";
        //sql +="&getovstations=1";

        // I Have switched the lat and lon on purpose, for safety reasons (sort of (-; ).
        sql +="&lattitude=" + lon;
        sql +="&longitude=" + lat;
        sql +="&distance=5";
        sql +="&limit=30";








        sql = sql.replaceAll(" ", "%20");
        getCoordinates.execute(sql);
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
            Data.lat = lat;
            Data.lon = lon;
            goToMain();
            return items;
        }
    }
}