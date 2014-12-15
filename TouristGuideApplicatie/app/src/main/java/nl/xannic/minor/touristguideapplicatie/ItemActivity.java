package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvCategory = (TextView) findViewById(R.id.tvCategory);
        TextView tvImageSource = (TextView) findViewById(R.id.tvImageSource);
        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);

        Intent intent = getIntent();
        // getting attached intent data
        String name = intent.getStringExtra("name");
        String category = intent.getStringExtra("category");
        String imageSource = intent.getStringExtra("imageSource");
        // displaying selected product name
        tvName.setText("Name: " + name);
        tvCategory.setText("Category: " + category);
        tvImageSource.setText("Image: " + imageSource);
        int loader = R.drawable.loader;
        ImageLoader imageLoader = new ImageLoader(getApplicationContext());

        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView
        imageLoader.DisplayImage(imageSource, loader, ivImage);
        //ivImage.setImageResource(R.drawable.purple);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
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
}
