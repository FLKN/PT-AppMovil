package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ptappmovil.upiita.pt_appmovil.Adapters.ServiceAdapter;
import com.ptappmovil.upiita.pt_appmovil.Items.ServiceItem;
import com.ptappmovil.upiita.pt_appmovil.Login;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceActivity extends AppCompatActivity {

    private TextView service_list_label;
    private ListView service_listview;
    private TextView service_header;

    private ArrayList<Integer> order_ids;
    private ArrayList<String> order_list;
    List items = new ArrayList();
    ServiceAdapter adapter = null;
    private float order_cost;
    private int room;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle control_info =  getIntent().getExtras();
        this.room = control_info.getInt("room");

        this.service_list_label = (TextView)findViewById(R.id.service_list_label);
        this.service_listview = (ListView)findViewById(R.id.service_listview);
        this.service_header = (TextView)findViewById(R.id.service_header);

        //doGetDishesRequest("http://192.168.1.68:3000/dishes/get_dishes");
        doGetDishesRequest("http://pt-backend.azurewebsites.net/dishes/get_dishes");


        this.order_ids = new ArrayList<Integer>();
        this.order_list = new ArrayList<String>();

        this.service_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selected_item = (TextView)view.findViewById(R.id.dish_name);
                TextView selected_id = (TextView)view.findViewById(R.id.dish_id);
                TextView cost_item = (TextView)view.findViewById(R.id.dish_cost);

                order_cost += Float.valueOf(cost_item.getText().toString());
                service_header.setText("Tu Orden: $" + order_cost);
                order_ids.add(Integer.valueOf(selected_id.getText().toString()));
                order_list.add(selected_item.getText().toString());

                service_list_label.setText(service_list_label.getText() + "\n" + selected_item.getText());

                items.remove(position);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

            }
        });

    }

    public void makeOrder(View v){
        Intent makeOrder_intent = new Intent();
        makeOrder_intent.setClass(ServiceActivity.this,OrderActivity.class);
        makeOrder_intent.putIntegerArrayListExtra("order_ids", order_ids);
        makeOrder_intent.putExtra("room", room);
        makeOrder_intent.putExtra("order_cost", order_cost);
        makeOrder_intent.putExtra("order_list",order_list);
        startActivityForResult(makeOrder_intent,1);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }

    private void doGetDishesRequest(String url) {
        progressDialog = new ProgressDialog(ServiceActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Obteniendo lista de platillos...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // display response
                        try {
                            JSONObject json = new JSONObject(response);
                            if(json.getBoolean("action")){
                                String dishes = json.getString("dishes");

                                JSONArray dishes_obj = new JSONArray(json.getString("dishes"));

                                for (int i=0; i < dishes_obj.length(); i++) {
                                    JSONObject dishObj = new JSONObject(dishes_obj.getString(i));

                                    items.add(new ServiceItem(
                                            dishObj.getString("id"),
                                            dishObj.getString("nombre"),
                                            dishObj.getString("precio")
                                    ));
                                }

                            } else {
                                Toast.makeText(ServiceActivity.this,"Ocurrio un error, intente una vez más",Toast.LENGTH_SHORT).show();
                            }


                            adapter = new ServiceAdapter(ServiceActivity.this, items);
                            service_listview.setAdapter(adapter);

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
                        Toast.makeText(ServiceActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
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
