package nl.xannic.minor.touristguideapplicatie;


import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Martijn on 20-11-2014.
 */
public class NavigationDrawerItemAdapter extends BaseAdapter {
    Context context;
    int[] categories;
    String[] titles;
    String[] distances;

    private static LayoutInflater inflater = null;

    public NavigationDrawerItemAdapter(Context context, int[] categories, String[] titles) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.categories = categories;
        this.titles = titles;
        //this.distances = distances;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.lv_navdrawer_row, null);

        ImageView imgToggle = (ImageView) vi.findViewById(R.id.imgToggle);
        TextView tvTitle = (TextView) vi.findViewById(R.id.tvTitle);

        switch(categories[position]) {

            case 1:
                imgToggle.setVisibility(View.GONE);
                break;
            case 2:
                imgToggle.setVisibility(View.VISIBLE);
                break;
        }

        tvTitle.setText(titles[position]);
        //tvDistance.setText(distances[position]);



        return vi;
    }


}