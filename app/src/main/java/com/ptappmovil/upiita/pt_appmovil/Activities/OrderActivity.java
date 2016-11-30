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
import com.ptappmovil.upiita.pt_appmovil.Login;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private TextView order_box;
    private TextView order_count;

    private ArrayList<Integer> order_ids;
    private ArrayList<String> order_list;
    private ArrayList<Integer> order_cant;
    private float[] order_unit;
    private float order_cost;
    private int room;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.order_cost = getIntent().getFloatExtra("order_cost", 0);
        this.order_ids = getIntent().getIntegerArrayListExtra("order_ids");
        this.order_cant = getIntent().getIntegerArrayListExtra("order_cant");
        this.order_list = getIntent().getStringArrayListExtra("order_list");

        this.order_unit = getIntent().getFloatArrayExtra("order_unit");

        this.room = getIntent().getIntExtra("room",0);

        this.order_box = (TextView)findViewById(R.id.order_box);
        this.order_count = (TextView)findViewById(R.id.order_count);

        for(String dish : order_list)
            order_box.setText( order_box.getText() + "\n" + dish );

        order_count.setText("$"+order_cost);
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

    public void doSetOrderRequest(JSONObject params, String url) {
        progressDialog = new ProgressDialog(OrderActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Enviando orden...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        progressDialog.dismiss();

                        OrderActivity.this.setResult(RESULT_OK, null);
                        OrderActivity.this.finish();
                        Toast.makeText(OrderActivity.this, "Orden confirmada", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(OrderActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(getRequest);
    }

    public void confirmOrder(View v){
        JSONObject params = new JSONObject();
        try {
            params.put("room",room);
            params.put("total",order_cost);
            params.put("dishes",order_ids);

            doSetOrderRequest(params,"http://pt-backend.azurewebsites.net/dishes/set_order");
            //doSetOrderRequest(params,"http://192.168.1.68:3000/dishes/set_order");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
