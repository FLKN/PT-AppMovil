package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import com.ptappmovil.upiita.pt_appmovil.Adapters.ActionAdapter;
import com.ptappmovil.upiita.pt_appmovil.Items.ActionItem;
import com.ptappmovil.upiita.pt_appmovil.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Variables de name de usuario y numero de habitacion
    private String name;
    private String room;
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
        String user = user_info.getString("user");
        /* Consulta usando user para obtener esta informacion */
        name = "David Pérez Espino";
        room = "room 2";
        level = 1;

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
        TextView name_user = (TextView)header.findViewById(R.id.name);
        TextView room_user = (TextView)header.findViewById(R.id.room);
        name_user.setText(this.name);
        room_user.setText(this.room);

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
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                }
                else if (position == 1){
                    /*Intent control_intent = new Intent();
                    control_intent.setClass(MainActivity.this,AgendaActivity.class);
                    control_intent.putExtra("level",level);
                    startActivity(control_intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);*/
                }
                else if (position == 2){
                    Intent control_intent = new Intent();
                    control_intent.setClass(MainActivity.this,ServiceActivity.class);
                    control_intent.putExtra("level",level);
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

        // Condicionales para identificar acciones del Navigataion Drawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
