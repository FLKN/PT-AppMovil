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

    private ListView sensor_listview;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.level = getIntent().getIntExtra("level",0);
        Log.d("ControlActivity","level "+this.level);

        this.sensor_listview = (ListView) findViewById(R.id.sensor_list);

        List items = new ArrayList();

        if (this.level == 1) {
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


        this.sensor_listview.setAdapter(new SensorAdapter(items, this));

        sensor_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                if (position == 0) {
                    Intent control_intent = new Intent();
                    control_intent.setClass(ControlActivity.this,LightActivity.class);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                /*if (position == 1) {
                    Intent control_intent = new Intent();
                    control_intent.setClass(ControlActivity.this,LockActivity.class);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                if (position == 2) {
                    Intent control_intent = new Intent();
                    control_intent.setClass(ControlActivity.this,AirActivity.class);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                if (position == 3) {
                    Intent control_intent = new Intent();
                    control_intent.setClass(ControlActivity.this,AccessActivity.class);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }*/

            }
        });
    }


}
