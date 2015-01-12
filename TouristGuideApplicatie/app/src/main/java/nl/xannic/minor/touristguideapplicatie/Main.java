package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class Main extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private GoogleMap mMap;
    private List<Item> item;
    private List<String> categories;
    private List<Category> categoriesFinal;
    private ImageView imgToggle;
    LayoutInflater inflater;

    Data data;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new Data();

        //totdat we realtime data ophalen in splash screen
        createDummyData();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        imgToggle =  (ImageView) findViewById(R.id.imgToggle);
        inflater = this.getLayoutInflater();


        Intent intent = new Intent();
        if(intent.getStringExtra("goToList") == "TRUE") {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new CreateWhatsNearList())
                    .commit();
        }



    }

    private void createDummyData(){
//        item = new ArrayList<Item>();
//        Item dataItem;
//        dataItem = new Item(1, "Kerk", "Prachtige kerk in Schelluinen", 51.841928, 4.925339, 1, 1, null);
//        item.add(dataItem);
//        dataItem = new Item(2, "Snackbar", "De lekkerste frietjes in Schelluinen", 51.841528, 4.825339, 1, 2, null);
//        item.add(dataItem);
//        dataItem = new Item(3, "Schaats Disco", "Iedereen tussen 12 en 18 is welkom", 51.831928, 4.725339, 1, 3, null);
//        item.add(dataItem);

        item = Data.itemList;

        categoriesFinal = new ArrayList<Category>();
        categoriesFinal.add(new Category(1, "Monumenten"));
        categoriesFinal.add(new Category(2, "Eetcafes"));
        categoriesFinal.add(new Category(3, "Evenementen"));
        categoriesFinal.add(new Category(4, "Musea"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        Log.e("Onresume", "Resume");
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                mMap.setMyLocationEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Data.lat, Data.lon),
                        13));
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            private final View contents = getLayoutInflater().inflate(R.layout.windowlayout, null);
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                TextView tvTitel = (TextView) contents.findViewById(R.id.tvInfoTitle);
                Button btMoreInfo = (Button) contents.findViewById(R.id.btMoreInfo);
                ImageView ivIcon = (ImageView) contents.findViewById(R.id.ivPlaatje);
                String test = marker.getTitle();
                String itemId = marker.getSnippet();

                List<Item> list = Data.itemList;
                final Item data = list.get(Integer.parseInt(itemId));
                tvTitel.setText(data.getName());
                int catId = data.getCategoryID();
                switch (catId){
                    case 1:
                        ivIcon.setImageResource(R.drawable.infowindowmonument);
                        break;
                    case 2:
                        ivIcon.setImageResource(R.drawable.infowindowfood);
                        break;
                    case 3:
                        ivIcon.setImageResource(R.drawable.infowindowevent);
                        break;
                    case 4:
                        ivIcon.setImageResource(R.drawable.infowindowmusea);
                        break;
                }
                return contents;
            }
        });
        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener(){
                    public void onInfoWindowClick(Marker marker){
                        String itemId = marker.getSnippet();

                        List<Item> list = Data.itemList;
                        Item data = list.get(Integer.parseInt(itemId));
                        goToDetailWindow(data);
                    }
                });
        for(int i = 0; i < item.size(); i++) {
            double lat = item.get(i).getLat();
            double lon = item.get(i).getLon();
            String title = item.get(i).getName();
            int catId = item.get(i).getCategoryID();
            int itemId = i;

            String catName = categoriesFinal.get(catId-1).getName();
            if(Data.categoriesList!=null){
                if (Data.categoriesList.contains(catName)) {
                    switch(catId){
                        case 1:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title).icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.statue_and_monuments)).snippet(String.valueOf(itemId)));
                            break;
                        case 2:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title).icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.food_and_drinks)).snippet(String.valueOf(itemId)));
                            break;
                        case 3:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title).icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.event)).snippet(String.valueOf(itemId)));
                            break;
                        case 4:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title).icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.museum)).snippet(String.valueOf(itemId)));
                            break;
                    }
                }
            }
        }
    }

    public void goToDetailWindow(Item data){
        Intent intent = new Intent(this, ItemActivity.class);
        // sending data to new activity
        intent.putExtra("name", data.getName());
        intent.putExtra("description", data.getInformation());
        intent.putExtra("imageSource", data.getImage());
        startActivity(intent);
        finish();
    }

    public void removeCatFromMap(Category cat){
        int catID = cat.getID();
        String name = cat.getName();
        if(Data.categoriesList!=null){
            if(Data.categoriesList.contains(name))
            {
                Data.categoriesList.remove(name);
                //Data.isCatVisible[catID] = false;
            }
            else
            {
                Data.categoriesList.add(name);
                //Data.isCatVisible[catID] = true;
            }
        }
        else{
            Data.categoriesList.add(name);
        }
        mMap.clear();
        setUpMap();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Category cat;
        Fragment fragment = null;
        switch(position) {
            case 0:
                Data.inWhatsNearList = false;
                fragment = new CreateMapFragment();
                Log.e("Fragment : ", "Map");
                break;
            case 1:
                cat = categoriesFinal.get(0);
                removeCatFromMap(cat);

                if(Data.inWhatsNearList)
                {
                    fragment = new CreateWhatsNearList();
                }


//                Activity cwnlActivity = CreateWhatsNearList.activity
//                cwnlActivity.updateDrawer();

                break;
            case 2:
                cat = categoriesFinal.get(1);
                removeCatFromMap(cat);
                if(Data.inWhatsNearList)
                {
                    fragment = new CreateWhatsNearList();
                }


                break;
            case 3:
                cat = categoriesFinal.get(2);
                removeCatFromMap(cat);
                if(Data.inWhatsNearList)
                {
                    fragment = new CreateWhatsNearList();
                }



                break;
            case 4:
                cat = categoriesFinal.get(3);
                removeCatFromMap(cat);
                if(Data.inWhatsNearList)
                {
                    fragment = new CreateWhatsNearList();
                }




                break;
            case 5:
                Data.inWhatsNearList = true;
                fragment = new CreateWhatsNearList();
                Log.e("Fragment : ", "Lijst");

                break;
            case 6:
                fragment = new CreateSubmit();
                Log.e("Fragment : ", "Submit");
                break;
        }
        if(fragment!=null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Main) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    }
