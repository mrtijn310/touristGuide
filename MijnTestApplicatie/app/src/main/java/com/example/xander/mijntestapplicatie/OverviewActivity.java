package com.example.xander.mijntestapplicatie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class OverviewActivity extends Activity {

    ListView lvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        int[] categories = new int[]{1, 2, 3, 4, 5, 6};
        String[] titles = new String[]{"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"};
        String[] distances = new String[]{"11", "22", "33", "44", "55", "66"};
        lvOverview = (ListView) findViewById(R.id.lvOverview);
        lvOverview.setAdapter(new lvItemAdapter(this, categories, titles, distances));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                break;
            case R.id.goToMaps:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
