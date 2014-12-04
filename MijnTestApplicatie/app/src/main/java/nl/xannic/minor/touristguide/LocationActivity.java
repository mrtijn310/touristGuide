package nl.xannic.minor.touristguide;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xander.mijntestapplicatie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import nl.xannic.minor.data.Data;
import nl.xannic.minor.data.Dataholder;

public class LocationActivity extends Activity {

    LocationManager locManager;
    LocationListener locListener;
    TextView tvShowLocation;
    int notificationID = 0;
    NotificationCompat.Builder notificatitionBuilder;
    double[] lats;
    double[] lons;
    String[] names;
    double lat;
    double lon;
    double newLat;
    double newLon;
    String notificationText;
    String info;
    int locationUpdateTijdMiliseconden = 300000;
    int locationUpdateMeter = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new locationListener();
        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, locationUpdateTijdMiliseconden, locationUpdateMeter, locListener);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);
        notificationText = "Notificatie";

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNotification()
    {
        notificationID++;
        notificatitionBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification, " + notificationID)
                        .setContentText(notificationText).setAutoCancel(true).setOnlyAlertOnce(true);
        Intent resultIntent = new Intent(this, MyActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MyActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificatitionBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, notificatitionBuilder.build());
    }

    private void  checkLocations()
    {
        //51.818366, 4.654808       //Eigen huis
        //51.818400, 4.654671       //10 meter verder

        for(int n = 0; n < names.length; n++)
            if(newLat + 0.001 > lats[n] && newLat - 0.001 < lats[n] && newLon + 0.001 > lons[n] && newLon - 0.001 < lons[n] )
            {
                notificationText = names[n];
                showNotification();
                info += "\n" + names[n];
                tvShowLocation.setText(info);
            }
        String cityName=null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address>  addresses;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName=addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        info += "\n" + cityName;
        tvShowLocation.setText(info);

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

            info ="My current location is: \n"+
            "Latitude ="+lat + " ->  " + newLat +
            "\nLongitude ="+lon + " ->  " + newLon;
            tvShowLocation.setText(info);

            String s =  String.format("%.4f", lat);



            GetCoordinates getCoordinates = new GetCoordinates();
            String sql = "http://xannic.nl/api/json2.php";
            sql += "SELECT * FROM StatueAndMonuments ORDER BY abs(lat - ("+ lat +")) + abs( lon - ("+lon+")) LIMIT 30";

            getCoordinates.execute(sql);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(),
                    "Gps Disabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(),
                    "Gps Enabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    // AsyncTask GetCoordinates
    public class GetCoordinates extends AsyncTask<String, Void, List<Data>> {
        Gson gson =  new Gson();

        protected void onPostExecute(List<Data> data) {
            int size = data.size();

            lats = new double[size];
            lons = new double[size];
            names = new String[size];

            for(int i = 0;  i < data.size(); i++){
                String name = data.get(i).getName();
                double lat = data.get(i).getLat();
                double lon = data.get(i).getLon();

                names[i] = name;
                lats[i] = lat;
                lons[i] = lon;
            }

            checkLocations();
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
