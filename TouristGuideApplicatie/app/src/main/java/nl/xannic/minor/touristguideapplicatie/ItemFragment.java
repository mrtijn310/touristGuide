package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Xander on 1/15/2015.
 */
public class ItemFragment extends Fragment {
    String name,description,imageSource;
    Activity activity;
    public void setVariables(String name, String description, String imagesource, Activity activity){
        this.name = name;
        this.description = description;
        this.imageSource = imagesource;
        this.activity = activity;
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_item, container, false);
        Data.inItem = true;
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
        tvName.setText(name);
        tvDescription.setText(description);
        int loader = R.drawable.empty_image;
        ImageLoader imageLoader = new ImageLoader(activity.getApplicationContext());
        imageLoader.DisplayImage(imageSource, loader, ivImage);
        return view;
    }

    public void onBackPressed(FragmentManager fragmentManager) {
        Data.inItem = false;
        if(!Data.inWhatsNearList) {
            Fragment fragment = new CreateMapFragment();
            if(fragment!=null) {
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                } else {
                    //FragmentManager ==  null
                }
            }
        }

        else {
            Fragment fragment = new CreateWhatsNearList();
            if(fragment!=null) {
                if(fragmentManager!=null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }else{
                    //FragmentManager ==  null
                }
            }
        }
    }
}
