package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ptappmovil.upiita.pt_appmovil.R;

public class LockActivity extends AppCompatActivity {

    private Switch lock_switch;
    private TextView lock_text;

    private int status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lock_switch = (Switch)findViewById(R.id.lock_switch);
        lock_text = (TextView)findViewById(R.id.lock_text);

        status = 1;
        if (status == 1){
            lock_switch.setChecked(true);
            lock_text.setText("Estado: Puerta Abierta");
        }
        else{
            lock_switch.setChecked(false);
            lock_text.setText("Estado: Puerta Cerrada");
        }

        lock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    status = 1; // Enviar a Azure
                    lock_text.setText("Estado: Puerta Abierta");
                }
                else {
                    status = 2; // Enviar a Azure
                    lock_text.setText("Estado: Puerta Cerrada");
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
