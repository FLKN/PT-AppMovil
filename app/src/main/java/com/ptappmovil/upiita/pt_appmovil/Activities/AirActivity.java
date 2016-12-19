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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.ptappmovil.upiita.pt_appmovil.Login;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AirActivity extends AppCompatActivity implements View.OnClickListener{

    private float intensity, temperature;
    private TextView intensity_label,temperature_label;
    private int room;
    private Button btn_off,btn_medium,btn_high,btn_refresh;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle control_info =  getIntent().getExtras();
        this.room = control_info.getInt("room");

        intensity_label = (TextView)findViewById(R.id.intensity_text);
        temperature_label = (TextView)findViewById(R.id.temperatuer_text);

        btn_off = (Button)findViewById(R.id.btn_off);
        btn_medium = (Button)findViewById(R.id.btn_medium);
        btn_high = (Button)findViewById(R.id.btn_high);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);

        btn_off.setOnClickListener(this);
        btn_medium.setOnClickListener(this);
        btn_high.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

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

    private void doAirStateRequest(JSONObject params, String url) {
        progressDialog = new ProgressDialog(AirActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            if(response.getBoolean("action")){
                                intensity_label.setText("Intensidad: " + response.getInt("intensity"));
                                temperature_label.setText("Temperatura: " + response.getDouble("temperature"));
                            }
                            else{
                                finish();
                                Toast.makeText(AirActivity.this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("AirControl", "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AirActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(getRequest);
    }

    public void onClick(View v){
        JSONObject dato = new JSONObject();
        switch (v.getId()){
            case R.id.air_off:
                try {
                    dato.put("room", this.room);
                    dato.put("intensity", "k");
                    dato.put("power",0);
                    doAirUpdateRequest(dato,"http://pt-backend.azurewebsites.net/sensors/update_air");
                } catch(Exception ex) {
                    Log.d("Error","Error");
                }
                btn_off.setEnabled(false);
                btn_medium.setEnabled(true);
                btn_high.setEnabled(true);
                break;
            case R.id.air_medium:
                try {
                    dato.put("room", this.room);
                    dato.put("intensity", "l");
                    dato.put("power",1);
                    doAirUpdateRequest(dato,"http://pt-backend.azurewebsites.net/sensors/update_air");
                } catch(Exception ex) {
                    Log.d("Error","Error");
                }
                btn_off.setEnabled(true);
                btn_medium.setEnabled(false);
                btn_high.setEnabled(true);
                break;
            case R.id.air_high:
                try {

                    dato.put("room", this.room);
                    dato.put("intensity", "m");
                    dato.put("power",1);
                    doAirUpdateRequest(dato,"http://pt-backend.azurewebsites.net/sensors/update_air");
                } catch(Exception ex) {
                    Log.d("Error","Error");
                }
                btn_off.setEnabled(true);
                btn_medium.setEnabled(true);
                btn_high.setEnabled(false);
                break;
            case R.id.btn_refresh:
                doAirStateRequest(dato,"http://pt-backend.azurewebsites.net/sensors/get_air");
                break;
        }
    }

    private void doAirUpdateRequest(JSONObject params, String url) {
        progressDialog = new ProgressDialog(AirActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            if(response.getBoolean("action")){
                                Toast.makeText(AirActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                            else{
                                finish();
                                Toast.makeText(AirActivity.this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("AirControl", "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AirActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(getRequest);
    }
}
