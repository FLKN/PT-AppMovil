package com.ptappmovil.upiita.pt_appmovil;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.*;
import android.graphics.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btn_control = (Button) findViewById(R.id.icon_control);
        Button btn_agenda = (Button) findViewById(R.id.icon_agenda);
        Button btn_servicios = (Button) findViewById(R.id.icon_servicios);

        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        btn_control.setTypeface(fontawesome);
        btn_control.setText("\uf236 \n Control de la Habitación");
        btn_agenda.setTypeface(fontawesome);
        btn_agenda.setText("\uf278 \n Agenda de Visitas");
        btn_servicios.setTypeface(fontawesome);
        btn_servicios.setText("\uf0f5 \n Servicio a la Habitación");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    public void doControl(View v) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        v.startAnimation(animAlpha);

    }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
