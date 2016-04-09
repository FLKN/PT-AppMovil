package com.ptappmovil.upiita.pt_appmovil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

public class AirActivity extends AppCompatActivity {

    private float intensity;
    private HoloCircleSeekBar air_intensity;
    private TextView air_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtener valor con consulta
        intensity = (float)19.5;

        air_intensity = (HoloCircleSeekBar) findViewById(R.id.air_picker);
        air_intensity.setValue(intensity);

        air_label = (TextView)findViewById(R.id.air_text);
        air_label.setText("Luminosidad: " + intensity);

        air_intensity.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                intensity = holoCircleSeekBar.getValue();
                // Actualizar base con intensity

                air_label.setText("Nivel: " + intensity);
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
