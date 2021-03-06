package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptappmovil.upiita.pt_appmovil.Adapters.ActionAdapter;
import com.ptappmovil.upiita.pt_appmovil.Items.ActionItem;
import com.ptappmovil.upiita.pt_appmovil.R;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Variables de name de usuario y numero de habitacion
    private String name;
    private int room;
    private int level;

    private ListView action_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtener user del login
        Bundle user_info =  getIntent().getExtras();
        name = user_info.getString("user");
        room = user_info.getInt("room");
        level = user_info.getInt("level");

        // Creando el Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Se infla el nac_header_main para poder tener un View a cual apntar
        View header = navigationView.getHeaderView(0);

        // Apuntando TextViews para la informacion de usuario
        TextView name_user = (TextView)header.findViewById(R.id.nav_name);
        TextView room_user = (TextView)header.findViewById(R.id.nav_room);
        name_user.setText(this.name);
        room_user.setText("Cuarto: " + this.room);

        this.action_listview = (ListView) findViewById(R.id.action_list);

        List items = new ArrayList();

        items.add(new ActionItem("\uf236", "Control de Habitación"));
        items.add(new ActionItem("\uf278","Agenda de Visitas"));
        items.add(new ActionItem("\uf0f5","Servicio al Cuarto"));


        this.action_listview.setAdapter(new ActionAdapter(this, items));

        action_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                final Animation animAlpha = AnimationUtils.loadAnimation(getBaseContext(),R.anim.anim_alpha);
                view.startAnimation(animAlpha);

                if (position == 0){
                    Intent control_intent = new Intent();
                    control_intent.setClass(MainActivity.this,ControlActivity.class);
                    control_intent.putExtra("level",level);
                    control_intent.putExtra("room",room);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                else if (position == 1){
                    Intent control_intent = new Intent();
                    control_intent.setClass(MainActivity.this,AgendaActivity.class);
                    control_intent.putExtra("room",room);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                else if (position == 2){
                    Intent control_intent = new Intent();
                    control_intent.setClass(MainActivity.this,ServiceActivity.class);
                    control_intent.putExtra("room",room);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent control_intent = new Intent();

        switch (id){
            case R.id.see_events:
                control_intent.setClass(MainActivity.this,AgendaActivity.class);
                control_intent.putExtra("room",room);
                startActivity(control_intent);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                break;
            case R.id.place_order:
                control_intent.setClass(MainActivity.this,ServiceActivity.class);
                control_intent.putExtra("room",room);
                startActivity(control_intent);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                break;
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:58985468"));
                if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(MainActivity.this,"No hay permisos para hacer llamadas",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
