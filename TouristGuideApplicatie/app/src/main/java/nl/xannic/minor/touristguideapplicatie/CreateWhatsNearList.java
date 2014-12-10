package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

/**
 * Created by Xander on 12/3/2014.
 */
public class CreateWhatsNearList extends Fragment {

    ListView  lvOverview;
    double[] lats;
    double[] lons;
    String[] names;
    int[] IDs;
    int[] categories;
    String[] schoolbag;
    double[] distances;
    String[] stringDist;
    Context context;

    double lat;
    double lon;
    double newLat;
    double newLon;
    View rootView;


    public CreateWhatsNearList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.whatsnearlist, container, false);
        Data data = new Data();
        makeList(data.getSortedList(Data.itemList));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=activity;
    }

    private void makeList(ArrayList<Item> itemList )
    {
        int size = itemList.size();
        String[] stringCat = new String[size];
       // String[] stringDist = new String[size];

        categories = new int[size];
        names = new String[size];
        stringDist = new String[10];




        for(int n = 0; n < size; n++) {
            categories[n] = itemList.get(n).getCategoryID();
            names[n] = itemList.get(n).getName();
            stringDist[n] = Double.toString(itemList.get(n).getDistance());
        }

        int[] catttt = new int[]{1, 2, 3, 4, 5, 6};
        String[] titlesss = new String[]{"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"};
        String[] distancesss = new String[]{"11", "22", "33", "44", "55", "66"};

        lvOverview = (ListView) rootView.findViewById(R.id.lvOverview);

        lvItemAdapter itemAdapter = new lvItemAdapter(context, categories, names, stringDist);
        lvOverview.setAdapter(itemAdapter);


        //lvOverview.setListAdapter(new lvItemAdapter(context.getApplicationContext(), cat, schoolbag, schoolbag));
        //lvOverview.setAdapter(new lvItemAdapter(getActivity().getApplicationContext(), categories, names, stringDist));
    }
}
