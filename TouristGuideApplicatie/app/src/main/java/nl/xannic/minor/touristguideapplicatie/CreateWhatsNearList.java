package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Xander on 12/3/2014.
 */
public class CreateWhatsNearList extends Fragment {
    public static ListView  lvOverview;
    String[] names;
    int[] categories;
    String[] stringDist;

    public static Context context;
    private static lvItemAdapter itemAdapter;
    public static Activity activity;


    private List<String> strCategories;
    private List<Category> strCategoriesFinal;

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
        this.activity = activity;
    }

    private void makeList(final ArrayList<Item> itemList )
    {
        int size = itemList.size();

        LinkedList<Integer> lstCategories = new LinkedList<Integer>();
        final LinkedList<String> lstNames = new LinkedList<String>();
        final LinkedList<String> lstDescriptions = new LinkedList<String>();
        final LinkedList<String> lstImages = new LinkedList<String>();
        stringDist = new String[10];

        for(int n = 0; n < size; n++) {

            if (itemList.get(n).getCategoryID() == 1 && Data.categoriesList.contains("Monumenten") )
            {
                lstCategories.add(itemList.get(n).getCategoryID());
                lstNames.add(itemList.get(n).getName());
                lstDescriptions.add(itemList.get(n).getInformation());
                lstImages.add(itemList.get(n).getImage());
            }

            else if (itemList.get(n).getCategoryID() == 2 && Data.categoriesList.contains("Eetcafes") )
            {
                lstCategories.add(itemList.get(n).getCategoryID());
                lstNames.add(itemList.get(n).getName());
                lstDescriptions.add(itemList.get(n).getInformation());
                lstImages.add(itemList.get(n).getImage());
            }

            else if (itemList.get(n).getCategoryID() == 3 && Data.categoriesList.contains("Evenementen") )
            {
                lstCategories.add(itemList.get(n).getCategoryID());
                lstNames.add(itemList.get(n).getName());
                lstDescriptions.add(itemList.get(n).getInformation());
                lstImages.add(itemList.get(n).getImage());
            }

            else if (itemList.get(n).getCategoryID() == 4 && Data.categoriesList.contains("Musea") )
            {
                lstCategories.add(itemList.get(n).getCategoryID());
                lstNames.add(itemList.get(n).getName());
                lstDescriptions.add(itemList.get(n).getInformation());
                lstImages.add(itemList.get(n).getImage());
            }
        }


        categories = new int[lstCategories.size()];
        for(int n = 0; n< lstCategories.size(); n++)
        {
            categories[n] = lstCategories.get(n);
        }

        names = lstNames.toArray(new String[lstNames.size()]);

        lvOverview = (ListView) rootView.findViewById(R.id.lvOverview);

        itemAdapter = new lvItemAdapter(context, categories, names);
        lvOverview.setAdapter(itemAdapter);

        lvOverview.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Launching new Activity on selecting single List Item
                Intent intent = new Intent(context, ItemActivity.class);
                // sending data to new activity
                intent.putExtra("name", lstNames.get(position));
                intent.putExtra("description", lstDescriptions.get(position));
                intent.putExtra("imageSource", lstImages.get(position));

                startActivity(intent);
                activity.finish();
            }
        });




        //lvOverview.setListAdapter(new lvItemAdapter(context.getApplicationContext(), cat, schoolbag, schoolbag));
        //lvOverview.setAdapter(new lvItemAdapter(getActivity().getApplicationContext(), categories, names, stringDist));

    }

    public static void UpdateList() {
        lvOverview.invalidate();
        //itemAdapter.notifyDataSetChanged();
    }
}
