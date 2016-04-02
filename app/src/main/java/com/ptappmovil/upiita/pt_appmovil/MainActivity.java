package com.ptappmovil.upiita.pt_appmovil;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Variables de nombre de usuario y numero de habitacion
    private String nombre;
    private String habitacion;
    private int nivel;

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
        nombre = "David Pérez Espino";
        habitacion = "Habitacion 2";
        nivel = 1;

        // Apuntando a los botones para añadirles fuente
        Button btn_control = (Button) findViewById(R.id.icon_control);
        Button btn_agenda = (Button) findViewById(R.id.icon_agenda);
        Button btn_servicios = (Button) findViewById(R.id.icon_servicios);

        // Agregando FontAwesome
        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        // Agregando la fuente a los botones
        btn_control.setTypeface(fontawesome);
        btn_control.setText("\uf236 \n Control de Habitación");
        btn_agenda.setTypeface(fontawesome);
        btn_agenda.setText("\uf278 \n Agenda de Visitas");
        btn_servicios.setTypeface(fontawesome);
        btn_servicios.setText("\uf0f5 \n Servicio a la Habitación");

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
        TextView nombre_usuario = (TextView)header.findViewById(R.id.nombre);
        TextView habitacion_usuario = (TextView)header.findViewById(R.id.habitacion);
        nombre_usuario.setText(this.nombre);
        habitacion_usuario.setText(this.habitacion);

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

    // Listener del boton para control del cuarto
    public void doControl(View v) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        v.startAnimation(animAlpha);

        Intent control_intent = new Intent();
        control_intent.setClass(MainActivity.this,ControlActivity.class);
        control_intent.putExtra("nivel",this.nivel);
        startActivity(control_intent);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);


    }
    // Listener del boton para hacer la agenda
    public void doAgenda(View v) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        v.startAnimation(animAlpha);

    }
    public void doServicios(View v) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        v.startAnimation(animAlpha);

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
