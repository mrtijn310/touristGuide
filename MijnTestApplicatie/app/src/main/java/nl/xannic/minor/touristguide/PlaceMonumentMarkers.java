package nl.xannic.minor.touristguide;

import android.os.AsyncTask;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

import nl.xannic.minor.data.Data;
import nl.xannic.minor.data.Dataholder;

/**
 * Created by Xander on 11/24/2014.
 */
public class PlaceMonumentMarkers extends AsyncTask<String, Void, List<Data>> {
        GoogleMap mMap;
        Gson gson =  new Gson();

        PlaceMonumentMarkers(GoogleMap mMap) {
            this.mMap = mMap;
        }

        protected void onPostExecute(List<Data> data) {
            for(int i = 0;  i < data.size(); i++){
                String name = data.get(i).getName();
                double lat = data.get(i).getLat();
                double lon = data.get(i).getLon();

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name));

            }
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
