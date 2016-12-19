package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.ProgressDialog;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AccessActivity extends AppCompatActivity {

    private ViewSwitcher window_switcher;
    private ViewSwitcher door_switcher;
    private ProgressDialog progressDialog;
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

    }
    public void clickRefresh(View v){
        doGetAccessStatus("http://pt-backend.azurewebsites.net/sensors/get_access");

    }

    public void doGetAccessStatus(String url) {
        progressDialog = new ProgressDialog(AccessActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Obteniendo estado...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        progressDialog.dismiss();

                        try {
                            if (response.getInt("states") == 0){
                                window_switcher.setDisplayedChild(0);
                                door_switcher.setDisplayedChild(0);

                            } else if (response.getInt("states") == 1){
                                window_switcher.setDisplayedChild(0);
                                door_switcher.setDisplayedChild(1);

                            } else if (response.getInt("states") == 2){
                                window_switcher.setDisplayedChild(1);
                                door_switcher.setDisplayedChild(0);

                            } else if (response.getInt("states") == 3){
                                window_switcher.setDisplayedChild(1);
                                door_switcher.setDisplayedChild(1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AccessActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
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

