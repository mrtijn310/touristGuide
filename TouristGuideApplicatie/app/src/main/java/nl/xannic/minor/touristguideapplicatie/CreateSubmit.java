package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Xander on 12/5/2014.
 */
public class CreateSubmit extends android.app.Fragment {
    TextView tvNaam, tvCategorie, tvPlace, tvInfo;
    Spinner spinnerCategorie;
    EditText etNaam, etInfo, etPlaceName;
    List<String> list;
    ImageView ivPicture;
    Button btPicture, btSubmit;
    View rootView;
    InsertMyData d;
    String url;
    boolean takenpicture = false;

    public CreateSubmit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.submit, container, false);
        OrientationUtils.lockOrientationPortrait(getActivity());
        tvNaam = (TextView) rootView.findViewById(R.id.tvNaam);
        tvCategorie = (TextView) rootView.findViewById(R.id.tvCategorie);
        spinnerCategorie = (Spinner) rootView.findViewById(R.id.spinnerCategorie);
        etNaam = (EditText) rootView.findViewById(R.id.etNaam);
        d = new InsertMyData();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.categorieën, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);
        spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        clearView();
                        setMonumentView();
                        break;
                    case 1:
                        clearView();
                        setFoodAndDrinkView();
//                        addFoodAndDrink();
                        break;
                    case 2:
                        clearView();
//                        setMuseaView();
//                        addMusea();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btPicture = (Button) rootView.findViewById(R.id.btPicture);
        btPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        ivPicture = (ImageView) rootView.findViewById(R.id.ivPicture);
        etInfo = (EditText) rootView.findViewById(R.id.etInfo);
        etPlaceName = (EditText) rootView.findViewById(R.id.etPlaceName);
        btSubmit = (Button) rootView.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(takenpicture==true){
                    UploadThisFile up = new UploadThisFile(file);
                    up.execute();
                }
                insertIntoDB();
            }
        });
        tvPlace = (TextView) rootView.findViewById(R.id.tvPlaceName);
        tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);
        return rootView;
    }

    public void clearView() {
        etInfo.setVisibility(View.GONE);
        etPlaceName.setVisibility(View.GONE);
        tvPlace.setVisibility(View.GONE);
        tvInfo.setVisibility(View.GONE);
        btSubmit.setVisibility(View.GONE);
        btPicture.setVisibility(View.GONE);
        etPlaceName.setText("");
        etInfo.setText("");
    }

    public void setFoodAndDrinkView() {
        tvPlace.setVisibility(View.VISIBLE);
        etPlaceName.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.VISIBLE);
        etInfo.setVisibility(View.VISIBLE);

    }

    public void setMonumentView() {
        tvInfo.setVisibility(View.VISIBLE);
        etInfo.setVisibility(View.VISIBLE);
        tvPlace.setVisibility(View.VISIBLE);
        etPlaceName.setVisibility(View.VISIBLE);
        btPicture.setVisibility(View.VISIBLE);
        btSubmit.setVisibility(View.VISIBLE);
        ivPicture.setVisibility(View.VISIBLE);
    }

    public void addMonument() {
        url = "http://www.xannic.nl/api/insertdata.php";
        String name = etNaam.getText().toString();
        String info = etInfo.getText().toString();
        String stringLat = String.valueOf(Data.lat);
        String stringLon = String.valueOf(Data.lon);
        String cityName = etPlaceName.getText().toString();
        long i = spinnerCategorie.getSelectedItemId() + 1;
        String categoryId = Long.toString(i);
        String imgUrl;
        if(takenpicture) {
            imgUrl = "http://www.xannic.nl/api/uploads/" +fileUri.getLastPathSegment();
        }
        else{
            imgUrl = null;
        }
        url += "?name=" + name + "&info=" + info + "&lat=" + stringLat + "&lon=" + stringLon + "&cityname=" + cityName + "&categoryid=" + categoryId + "&imageurl=" + imgUrl;
    }

    public void locationGetter() {
        // Here I want to send this ArrayList values into the call
        // method in activity class.


    }

    public void insertIntoDB() {
        long longCat = spinnerCategorie.getSelectedItemId();
        int i = (int) longCat;
        switch (i) {
            case 0:
                addMonument();
                break;
        }
        d.execute(url);
        //go back to map of tnx screen
    }

    private Uri fileUri;
    File file;

    public void cameraIntent() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        fileUri = Uri.fromFile(file); // create a file to save the image

        //Put extra werkt niet om 1 of andere reden...
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        Log.e("fileUri: ", "" + fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    private static File getOutputMediaFileUri(int type) {
        return getOutputMediaFile(type);
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TouringGuide");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TouringGuide", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Data is altijd null omdat de intent.putExtra niet werkt...
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //pakt de foto via de file uri... en zet hem vervolgens in de ImageView
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(rootView.getContext().getContentResolver(), fileUri);
                    ivPicture.setImageBitmap(bitmap);
                    takenpicture= true;
                } catch (Exception e) {
                    //catch filenotfoundexception
                    Log.e("Error : ", "File not Found denk ik");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }
}
