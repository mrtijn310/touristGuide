package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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

    private List<String> strCategories;
    private List<Category> strCategoriesFinal;


    double lat;
    double lon;
    double newLat;
    double newLon;
    View rootView;
    ArrayList<Item> itemList;
    Data data;


    public CreateWhatsNearList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.whatsnearlist, container, false);
        data = new Data();
        itemList = Data.itemList;
        makeList(data.getSortedList(itemList));

        strCategories = new ArrayList<String>();
        strCategories.add("Monumenten");
        strCategories.add("Eetcafes");
        strCategories.add("Evenementen");
        strCategories.add("Musea");

        strCategoriesFinal = new ArrayList<Category>();
        strCategoriesFinal.add(new Category(1, "Monumenten"));
        strCategoriesFinal.add(new Category(2, "Eetcafes"));
        strCategoriesFinal.add(new Category(3, "Evenementen"));
        strCategoriesFinal.add(new Category(4, "Musea"));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=activity;
    }

    private void makeList(final ArrayList<Item> itemList )
    {
        int size = itemList.size();
        String[] stringCat = new String[size];

        LinkedList<Integer> lstCategories = new LinkedList<Integer>();
        LinkedList<String> lstNames = new LinkedList<String>();
        stringDist = new String[10];

        for(int n = 0; n < size; n++) {
            int catId = itemList.get(n).getCategoryID();
            if (Data.isCatVisible[catId-1] == true)
            {
                lstCategories.add(itemList.get(n).getCategoryID());
                lstNames.add(itemList.get(n).getName());
            }
        }


        categories = new int[lstCategories.size()];
        for(int n = 0; n< lstCategories.size(); n++)
        {
            categories[n] = lstCategories.get(n);
        }

        names = lstNames.toArray(new String[lstNames.size()]);

        lvOverview = (ListView) rootView.findViewById(R.id.lvOverview);

        lvItemAdapter itemAdapter = new lvItemAdapter(context, categories, names);
        lvOverview.setAdapter(itemAdapter);

        lvOverview.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String product = "ID: " + itemList.get(position).getID();
                product += "\nName: " + itemList.get(position).getName();

                //Toast.makeText(getApplicationContext(), product, Toast.LENGTH_SHORT).show();

                // Launching new Activity on selecting single List Item
                Intent intent = new Intent(context, ItemActivity.class);
                // sending data to new activity
                intent.putExtra("name", itemList.get(position).getName());
                intent.putExtra("description", itemList.get(position).getInformation());
                intent.putExtra("imageSource", itemList.get(position).getImage());

                startActivity(intent);
            }
        });


        //lvOverview.setListAdapter(new lvItemAdapter(context.getApplicationContext(), cat, schoolbag, schoolbag));
        //lvOverview.setAdapter(new lvItemAdapter(getActivity().getApplicationContext(), categories, names, stringDist));

    }
}
