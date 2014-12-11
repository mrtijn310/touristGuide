package nl.xannic.minor.touristguideapplicatie;

import android.os.AsyncTask;

import com.google.android.gms.internal.ca;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xander on 12/11/2014.
 */
public class InsertMyData extends AsyncTask<String, Void, String>{
    String name, info, lat,lon,cityName,categoryId,imageUrl;

//    InsertMyData(String name, String info,String lat, String lon, String cityName, String categoryId, String imageUrl){
//        this.name = name;
//        this.info = info;
//        this.lat = lat;
//        this.lon = lon;
//        this.cityName = cityName;
//        this.categoryId = categoryId;
//        this.imageUrl = imageUrl;
//    }

        protected String doInBackground(String... params) {
            String output = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(params[0]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }
}
