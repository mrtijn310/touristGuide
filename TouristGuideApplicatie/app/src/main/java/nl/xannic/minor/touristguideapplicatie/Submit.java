package nl.xannic.minor.touristguideapplicatie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Xander on 12/7/2014.
 */
public class Submit extends Activity{

    TextView tvNaam, tvCategorie;
    Spinner spinnerCategorie;
    EditText etNaam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit);
        tvNaam = (TextView) findViewById(R.id.tvNaam);
        tvCategorie = (TextView) findViewById(R.id.tvCategorie);
        spinnerCategorie = (Spinner) findViewById(R.id.spinnerCategorie);
        etNaam = (EditText) findViewById(R.id.etNaam);
    }

    public void setNaam(String naam){
        tvNaam.setText(naam);
    }
}
