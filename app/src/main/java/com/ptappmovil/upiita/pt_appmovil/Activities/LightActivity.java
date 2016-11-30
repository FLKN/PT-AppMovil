package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class LightActivity extends AppCompatActivity implements View.OnClickListener{

    private float lumen;
    private int room;

    private Button btn_off,btn_weak,btn_medium,btn_high;
    private TextView preset_label;
    private TextView light_label;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle light_intent = getIntent().getExtras();
        room = light_intent.getInt("room");


        light_label = (TextView)findViewById(R.id.light_text);
        preset_label = (TextView)findViewById(R.id.preset_text);

        btn_off = (Button) findViewById(R.id.btn_off);
        btn_weak = (Button) findViewById(R.id.btn_weak);
        btn_medium = (Button) findViewById(R.id.btn_medium);
        btn_high = (Button) findViewById(R.id.btn_high);

        btn_off.setOnClickListener(this);
        btn_weak.setOnClickListener(this);
        btn_medium.setOnClickListener(this);
        btn_high.setOnClickListener(this);

        JSONObject params = new JSONObject();
        try {
            params.put("room",room);
            params.put("lumen",4);
            //doGetLightStatus("http://pt-backend.azurewebsites.net/sensors/get_light",params);
            doGetLightStatus("http://192.168.43.71:3000/sensors/get_light",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void doGetLightStatus(String url, JSONObject params) {
        progressDialog = new ProgressDialog(LightActivity.this, R.style.AppTheme_Dark_Dialog);
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

                        String preset = "";
                        try {
                            switch (response.getInt("preset")) {
                                case 0:
                                    preset = "Apagado";
                                    btn_off.setEnabled(false);
                                    break;
                                case 1:
                                    preset = "Tenue";
                                    btn_weak.setEnabled(false);
                                    break;
                                case 2:
                                    preset = "Medio";
                                    btn_medium.setEnabled(false);
                                    break;
                                case 3:
                                    preset = "Alto";
                                    btn_high.setEnabled(false);
                                    break;
                            }
                            light_label.setText("Luminosidad: " + response.getDouble("lumen"));
                            preset_label.setText("Nivel: " + preset);
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
                        Toast.makeText(LightActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }

    public void doSetLightStatus(String url, JSONObject params) {
        progressDialog = new ProgressDialog(LightActivity.this, R.style.AppTheme_Dark_Dialog);
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
                        Toast.makeText(LightActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }

    @Override
    public void onClick(View v) {
        JSONObject params = new JSONObject();

        switch (v.getId()) {
            case R.id.btn_off:
                preset_label.setText("Nivel: Apagado");
                btn_off.setEnabled(false);
                btn_weak.setEnabled(true);
                btn_medium.setEnabled(true);
                btn_high.setEnabled(true);
                try {
                    params.put("room",room);
                    params.put("lumen",0);
                    doSetLightStatus("http://pt-backend.azurewebsites.net/sensors/update_light",params);
                    //doSetLightStatus("http://192.168.1.68:3000/sensors/update_light",params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_weak:
                preset_label.setText("Nivel: Tenue");
                btn_off.setEnabled(true);
                btn_weak.setEnabled(false);
                btn_medium.setEnabled(true);
                btn_high.setEnabled(true);
                try {
                    params.put("room",room);
                    params.put("lumen",1);
                    doSetLightStatus("http://pt-backend.azurewebsites.net/sensors/update_light",params);
                    //doSetLightStatus("http://192.168.1.68:3000/sensors/update_light",params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_medium:
                preset_label.setText("Nivel: Medio");
                btn_off.setEnabled(true);
                btn_weak.setEnabled(true);
                btn_medium.setEnabled(false);
                btn_high.setEnabled(true);
                try {
                    params.put("room",room);
                    params.put("lumen",2);
                    doSetLightStatus("http://pt-backend.azurewebsites.net/sensors/update_light",params);
                    //doSetLightStatus("http://192.168.1.68:3000/sensors/update_light",params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_high:
                preset_label.setText("Nivel: Alto");
                btn_off.setEnabled(true);
                btn_weak.setEnabled(true);
                btn_medium.setEnabled(true);
                btn_high.setEnabled(false);
                try {
                    params.put("room",room);
                    params.put("lumen",3);
                    doSetLightStatus("http://pt-backend.azurewebsites.net/sensors/update_light",params);
                    //doSetLightStatus("http://192.168.1.68:3000/sensors/update_light",params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
