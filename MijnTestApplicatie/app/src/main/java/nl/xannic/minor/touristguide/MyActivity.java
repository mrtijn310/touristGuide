package nl.xannic.minor.touristguide;

import android.app.Activity;
//import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.xander.mijntestapplicatie.R;
import com.google.gson.Gson;


public class MyActivity extends Activity {

    int mId = 0;
    NotificationCompat.Builder mBuilder;

    Button btMaps;
    Button btEditPush;
    Button btGoToOverview;
    Button btGoToLocation;
    Button btRoutePlanTest;
    Button btRoutePlanTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        pushNotificationButton();
        btMaps = (Button) findViewById(R.id.btGoogle);
        btMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapsIntent();
            }
        });
        Log.e("Status: ", "onCreate");
    }

    public void startMapsIntent() {
        Intent mapsIntent = new Intent(this, MapsActivity.class);
        startActivity(mapsIntent);
    }

    Button btPush;
    private void pushNotificationButton(){

        btPush =  (Button) findViewById(R.id.btnNewIntent);
        btPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });

        btEditPush = (Button) findViewById(R.id.btEditNotification);
        btEditPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotification();
            }
        });

        btGoToOverview = (Button) findViewById(R.id.btGoToOverview);
        btGoToOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOverview();
            }
        });

        btGoToLocation = (Button) findViewById(R.id.btGoToLocation);
        btGoToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation();
            }
        });

        btRoutePlanTest = (Button) findViewById(R.id.btRoutePlanTest);
        btRoutePlanTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + 51.908 + "," + 4.43715 + "&daddr=" + 51.938 + "," + 4.4826+ "&mode=walking"));
//                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
//                startActivity(intent);
                startMapsNow();
            }
        });
        btRoutePlanTest2 = (Button) findViewById(R.id.btRoutePlanTest2);
        btRoutePlanTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + 51.908 + "," + 4.43715 + "&daddr=" + 51.938 + "," + 4.4826+ "&mode=walking"));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);
//                startMapsNow();
            }
        });
    }


    protected void onStart(){
        super.onStart();
        Log.e("Status: ","onStart");
    }

    protected void onResume(){
        super.onResume();
        Log.e("Status: ","onResume");
    }

    protected void onPause(){
        super.onPause();
        Log.e("Status: ","onPause");
    }

    protected void onStop(){
        super.onStop();
        Log.e("Status: ","onStop");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.e("Status: ","onDestroy");
    }

    private void goToOverview() {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }

    private void goToLocation() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    private void startMapsNow(){
        Intent intent = new Intent(this, PathGoogleMapActivity.class);
        startActivity(intent);
    }

    private void editNotification(){
        mBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("My edited notification, ")
                    .setContentText("This Notification Is Modified").setAutoCancel(true);
            NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(mId, mBuilder.build());
    }

    private void showNotification()
    {
        mId++;
        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification, " + mId)
                        .setContentText("Hello World!").setAutoCancel(true).setOnlyAlertOnce(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MyActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MyActivity.class);
//Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
       NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
        btPush.setText("MyButton");
    }
}
