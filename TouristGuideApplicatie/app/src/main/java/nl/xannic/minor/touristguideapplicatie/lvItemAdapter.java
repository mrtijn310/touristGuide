package nl.xannic.minor.touristguideapplicatie;


import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Martijn on 20-11-2014.
 */
public class lvItemAdapter extends BaseAdapter {

    Context context;
    int[] categories;
    String[] titles;
    String[] distances;

    private static LayoutInflater inflater = null;

    public lvItemAdapter(Context context, int[] categories, String[] titles) {
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
            vi = inflater.inflate(R.layout.lv_overview_row, null);

        ImageView ivColorImage = (ImageView) vi.findViewById(R.id.ivColorImage);
        TextView tvTitle = (TextView) vi.findViewById(R.id.tvTitle);
        //TextView tvDistance = (TextView) vi.findViewById(R.id.tvDistance);

        switch(categories[position]) {

            case 1:
                ivColorImage.setImageResource(R.drawable.red);
                break;
            case 2:
                ivColorImage.setImageResource(R.drawable.green2);
                break;
            case 3:
                ivColorImage.setImageResource(R.drawable.blue);
                break;
            case 4:
                ivColorImage.setImageResource(R.drawable.yellow);
                break;
            case 5:
                ivColorImage.setImageResource(R.drawable.purple);
                break;
            case 6:
                ivColorImage.setImageResource(R.drawable.cyan);
                break;
            default:
                ivColorImage.setImageResource(R.drawable.white);
                break;

        }

        tvTitle.setText(titles[position]);
        //tvDistance.setText(distances[position]);



        return vi;
    }
}