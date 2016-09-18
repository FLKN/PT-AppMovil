package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ptappmovil.upiita.pt_appmovil.Adapters.SensorAdapter;
import com.ptappmovil.upiita.pt_appmovil.Items.SensorItem;
import com.ptappmovil.upiita.pt_appmovil.R;

import java.util.ArrayList;
import java.util.List;

public class ControlActivity extends AppCompatActivity {

    private ListView sensor_listview;
    private int level;
    private int room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle control_info =  getIntent().getExtras();
        this.level = control_info.getInt("level");
        this.room = control_info.getInt("room");

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
                    control_intent.putExtra("room",room);
                    control_intent.setClass(ControlActivity.this,LightActivity.class);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                if (position == 1) {
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
                }

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
