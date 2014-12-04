package nl.xannic.minor.touristguideapplicatie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Xander on 12/3/2014.
 */
public class CreateWhatsNearList extends android.app.Fragment {

    public CreateWhatsNearList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.whatsnearlist, container, false);

        return rootView;
    }
}
