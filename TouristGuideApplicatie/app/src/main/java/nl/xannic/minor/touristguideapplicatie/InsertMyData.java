package nl.xannic.minor.touristguideapplicatie;

import android.os.AsyncTask;
import android.util.Log;

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
public class InsertMyData extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            String url = null;
            android.os.Debug.waitForDebugger();
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                url = params[0].replaceAll(" ", "%20");
                Log.e("link:  --->  ", params[0]);
                HttpGet httpPost = new HttpGet(url);
                httpClient.execute(httpPost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
}
