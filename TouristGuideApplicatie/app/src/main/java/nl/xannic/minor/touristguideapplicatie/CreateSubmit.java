package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Xander on 12/5/2014.
 */
public class CreateSubmit extends android.app.Fragment{
    TextView tvNaam, tvCategorie;
    Spinner spinnerCategorie;
    EditText etNaam;
    List<String> list;
    ImageView ivPicture;
    Button btPicture, btSubmit;
    View rootView;

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
        R.array.categorieën, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);
        btPicture = (Button) rootView.findViewById(R.id.btPicture);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        btPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
        ivPicture = (ImageView) rootView.findViewById(R.id.ivPicture);
        return rootView;
    }

    private Uri fileUri;

    public void cameraIntent(){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        //Put extra werkt niet om 1 of andere reden...
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        Log.e("fileUri: ", "" + fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TouringGuide");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("TouringGuide", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
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
                }catch(Exception e){
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
