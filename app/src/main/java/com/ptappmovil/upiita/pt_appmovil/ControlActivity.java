package com.ptappmovil.upiita.pt_appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ControlActivity extends AppCompatActivity {

    private ListView listView;
    private int nivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.nivel = getIntent().getIntExtra("nivel",0);
        Log.d("ControlActivity","Nivel "+this.nivel);

        this.listView = (ListView) findViewById(R.id.sensor_list);

        List items = new ArrayList();

        if (this.nivel == 1) {
            items.add(new SensorItem("Luz", 1));
            items.add(new SensorItem("Cerradura", 2));
            items.add(new SensorItem("Aire acondicionado", 3));
            items.add(new SensorItem("Accesos del cuarto", 4));
        }
        else {
            items.add(new SensorItem("Luz", 1));
            items.add(new SensorItem("Aire acondicionado", 3));
            items.add(new SensorItem("Accesos del cuarto", 4));
        }


        this.listView.setAdapter(new SensorAdapter(items, this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                Toast.makeText(getBaseContext(), "Sensor" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
