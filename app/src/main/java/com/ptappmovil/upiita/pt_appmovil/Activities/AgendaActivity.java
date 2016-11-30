package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgendaActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ArrayList<String> agenda_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.agenda_list = new ArrayList<String>();

        doGetPlacesRequest("http://pt-backend.azurewebsites.net/events/get_events");
    }

    public void placeAgenda(View v) {
        String aux = "";
        for(int i = 0; i < agenda_list.size(); i++) {
            aux += "+to:"+agenda_list.get(i);
        }
        aux += "+to:UPIITA";
        final String uri = "http://maps.google.com/maps?saddr=UPIITA&daddr="+aux;
        final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        if (intent.resolveActivity(AgendaActivity.this.getPackageManager()) != null) {
            startActivity(intent);
        }
        Toast.makeText(this,"Creando recorrido...",Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Recuerda que puedes guardar tu recorrido en el escritorio usando las opciones del menú",Toast.LENGTH_SHORT).show();
    }

    private void doGetPlacesRequest(String url) {
        progressDialog = new ProgressDialog(AgendaActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Obteniendo lista de platillos...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("aaa",response.toString());
                        try {
                            JSONObject json = new JSONObject(response);
                            if(json.getBoolean("action")){

                                JSONArray events_obj = new JSONArray(json.getString("events"));

                                LinearLayout layout = (LinearLayout)findViewById(R.id.content_agenda);

                                for (int i=0; i < events_obj.length(); i++) {
                                    final JSONObject eventObj = new JSONObject(events_obj.getString(i));

                                    CheckBox item = new CheckBox(AgendaActivity.this);
                                    item.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));

                                    item.setText(eventObj.getString("nombre"));

                                    final String name = eventObj.getString("nombre");
                                    item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            boolean isChecked = ((CheckBox)view).isChecked();

                                            if (isChecked) {
                                                agenda_list.add(name);
                                                Log.d("aaa",name);
                                            }
                                            else {
                                                for(int i = 0; i < agenda_list.size(); i++) {
                                                    String aux = agenda_list.get(i);
                                                    if(aux.equals(name)) {
                                                        agenda_list.remove(i);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });


                                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    TextView direction_item = new TextView(AgendaActivity.this);
                                    llp.setMargins(0,0,0,20);
                                    direction_item.setLayoutParams(llp);
                                    direction_item.setText(eventObj.getString("ubicacion"));
                                    direction_item.setTypeface(null, Typeface.ITALIC);

                                    TextView description_item = new TextView(AgendaActivity.this);
                                    llp.setMargins(0,0,0,20);
                                    description_item.setLayoutParams(llp);
                                    description_item.setText(eventObj.getString("descripcion"));
                                    description_item.setTypeface(null, Typeface.ITALIC);

                                    layout.addView(item);
                                    layout.addView(direction_item);
                                    layout.addView(description_item);
                                }
                            } else {
                                Toast.makeText(AgendaActivity.this,"Ocurrio un error, intente una vez más",Toast.LENGTH_SHORT).show();
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
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AgendaActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }
}
