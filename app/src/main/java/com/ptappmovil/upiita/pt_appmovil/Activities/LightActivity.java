package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.ptappmovil.upiita.pt_appmovil.R;

public class LightActivity extends AppCompatActivity {

    private float lumen;

    private HoloCircleSeekBar light_intensity;
    private TextView light_label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtener valor con consulta
        lumen = (float)19.5;

        light_intensity = (HoloCircleSeekBar) findViewById(R.id.light_picker);
        light_intensity.setValue(lumen);

        light_label = (TextView)findViewById(R.id.light_text);
        light_label.setText("Luminosidad: " + lumen);

        light_intensity.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) { }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                lumen = holoCircleSeekBar.getValue();
                // Actualizar base con intensity

                light_label.setText("Luminosidad: " + lumen + "%");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
