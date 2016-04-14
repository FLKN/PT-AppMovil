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
import android.widget.ImageSwitcher;
import android.widget.ViewSwitcher;

import com.ptappmovil.upiita.pt_appmovil.R;

public class AccessActivity extends AppCompatActivity {

    private ViewSwitcher window_switcher;
    private ViewSwitcher door_switcher;

    private int window_status;
    private int door_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        door_switcher = (ViewSwitcher)findViewById(R.id.door_status_indicator);
        window_switcher = (ViewSwitcher)findViewById(R.id.window_status_indicator);

        window_status = 1; // Obtener de Azure
        door_status = 1; // Obtener de Azure

        if (window_switcher.getDisplayedChild() == 1 && window_status == 0)
            window_switcher.setDisplayedChild(0);
        else  if (window_switcher.getDisplayedChild() == 0 && window_status == 1)
            window_switcher.setDisplayedChild(1);

        if (door_switcher.getDisplayedChild() == 1 && door_status == 0)
            door_switcher.setDisplayedChild(0);
        else  if (door_switcher.getDisplayedChild() == 0 && door_status == 1)
            door_switcher.setDisplayedChild(1);

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

