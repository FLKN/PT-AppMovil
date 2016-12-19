package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

public class LockActivity extends AppCompatActivity {

    private Switch lock_switch;
    private TextView lock_text;

    private int status;
    private int room;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle lock_intent = getIntent().getExtras();
        room = lock_intent.getInt("room");

        lock_switch = (Switch)findViewById(R.id.lock_switch);
        lock_text = (TextView)findViewById(R.id.lock_text);

        JSONObject params = new JSONObject();
        try {
            params.put("room",room);
            doGetLockStatus("http://pt-backend.azurewebsites.net/sensors/get_lock",params);
            //doGetLockStatus("http://192.168.1.68:3000/sensors/get_lock",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    JSONObject params = new JSONObject();
                    try {
                        params.put("room",room);
                        params.put("lock_state","g");
                        params.put("power",1);
                        doSetLockStatus("http://pt-backend.azurewebsites.net/sensors/update_lock",params);
                        //doGetLockStatus("http://192.168.1.68:3000/sensors/get_lock",params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lock_text.setText("Puerta Abierta");
                }
                else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("room",room);
                        params.put("lock_state","h");
                        params.put("power",0);
                        doSetLockStatus("http://pt-backend.azurewebsites.net/sensors/update_lock",params);
                        //doGetLockStatus("http://192.168.1.68:3000/sensors/get_lock",params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lock_text.setText("Puerta Cerrada");
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

    public void doGetLockStatus(String url, JSONObject params) {
        progressDialog = new ProgressDialog(LockActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Obteniendo estado...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        progressDialog.dismiss();

                        try {
                            lock_text.setText(response.getString("msg"));

                            lock_switch.setChecked(response.getBoolean("lock_state"));
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
                        Toast.makeText(LockActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }

    public void doSetLockStatus(String url, JSONObject params) {
        progressDialog = new ProgressDialog(LockActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Cambiando modo...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        progressDialog.dismiss();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(LockActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }

}
