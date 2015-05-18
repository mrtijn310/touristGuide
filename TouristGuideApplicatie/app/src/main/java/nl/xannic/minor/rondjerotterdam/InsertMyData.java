package nl.xannic.minor.rondjerotterdam;

import android.os.AsyncTask;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Xander on 12/11/2014.
 */
public class InsertMyData extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            String url = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                url = params[0].replaceAll(" ", "%20");
                HttpGet httpPost = new HttpGet(url);
                httpClient.execute(httpPost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
}
