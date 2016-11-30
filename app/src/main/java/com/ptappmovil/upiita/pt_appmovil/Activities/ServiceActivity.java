package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServiceActivity extends AppCompatActivity {

    ArrayList<Integer> order_ids;
    ArrayList<Integer> order_cant;
    ArrayList<Float> order_unit;
    ArrayList<String> order_list;
    List items = new ArrayList();
    TextView order_label;
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

        order_label = (TextView)findViewById(R.id.order_label);
        order_label.setTypeface(null, Typeface.BOLD);

        doGetDishesRequest("http://pt-backend.azurewebsites.net/dishes/get_dishes");


        this.order_ids = new ArrayList<Integer>();
        this.order_cant = new ArrayList<Integer>();
        this.order_list = new ArrayList<String>();
        this.order_unit = new ArrayList<Float>();

    }
    public void placeOrder(View v){
        if(order_list.size() == 0){
            Toast.makeText(ServiceActivity.this,"Debes seleccionar al menos un producto",Toast.LENGTH_LONG).show();
        } else {
            Intent makeOrder_intent = new Intent();
            makeOrder_intent.setClass(ServiceActivity.this,OrderActivity.class);
            makeOrder_intent.putIntegerArrayListExtra("order_ids", order_ids);
            makeOrder_intent.putIntegerArrayListExtra("order_ids", order_cant);
            makeOrder_intent.putExtra("room", room);
            makeOrder_intent.putExtra("order_cost", order_cost);
            makeOrder_intent.putExtra("order_list",order_list);
            makeOrder_intent.putExtra("order_unit",order_unit.toArray());
            startActivityForResult(makeOrder_intent,1);
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
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
                        Log.d("aaa",response.toString());
                        try {
                            JSONObject json = new JSONObject(response);
                            if(json.getBoolean("action")){

                                JSONArray dishes_obj = new JSONArray(json.getString("dishes"));

                                LinearLayout layout = (LinearLayout)findViewById(R.id.content_service);

                                for (int i=0; i < dishes_obj.length(); i++) {
                                    final JSONObject dishObj = new JSONObject(dishes_obj.getString(i));

                                    CheckBox id_item = new CheckBox(ServiceActivity.this);
                                    id_item.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    id_item.setText(dishObj.getString("nombre") + " - $" + dishObj.getString("precio") + "MXN");
                                    final int id = dishObj.getInt("id");
                                    final String name = dishObj.getString("nombre");
                                    final String price = dishObj.getString("precio");
                                    id_item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            boolean isChecked = ((CheckBox)view).isChecked();

                                            if (isChecked) {
                                                final Dialog d = new Dialog(ServiceActivity.this, R.style.AppTheme_Dark_Dialog);
                                                d.setTitle("Selecciona la cantidad");
                                                d.setContentView(R.layout.number_dialog);
                                                d.setCanceledOnTouchOutside(false);
                                                d.setCancelable(false);
                                                Button b1 = (Button) d.findViewById(R.id.button1);
                                                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                                                np.setMaxValue(10);
                                                np.setMinValue(1);
                                                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                                    @Override
                                                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {}
                                                });
                                                b1.setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View v) {
                                                        order_cost += Float.valueOf(price) * np.getValue();
                                                        order_label.setText("$"+order_cost+"MXN");
                                                        order_unit.add(Float.valueOf(price) * np.getValue());
                                                        order_cant.add(np.getValue());
                                                        d.dismiss();
                                                    }
                                                });
                                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                lp.copyFrom(d.getWindow().getAttributes());
                                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                                d.show();
                                                d.getWindow().setAttributes(lp);

                                                order_ids.add(id);
                                                order_list.add(name);
                                            }
                                            else {
                                                for(int i = 0; i < order_list.size(); i++) {
                                                    String aux = order_list.get(i);
                                                    float aux2 = order_unit.get(i);
                                                    if(aux.equals(name)) {
                                                        order_list.remove(i);
                                                        order_cant.remove(i);
                                                        order_unit.remove(i);
                                                        order_ids.remove(i);
                                                        order_cost -= aux2;
                                                        order_label.setText("$"+order_cost+"MXN");
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });

                                    TextView description_item = new TextView(ServiceActivity.this);
                                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    llp.setMargins(0,0,0,30);
                                    description_item.setLayoutParams(llp);
                                    description_item.setText(dishObj.getString("descripcion"));
                                    description_item.setTypeface(null, Typeface.ITALIC);

                                    layout.addView(id_item);
                                    layout.addView(description_item);
                                }
                            } else {
                                Toast.makeText(ServiceActivity.this,"Ocurrio un error, intente una vez más",Toast.LENGTH_SHORT).show();
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
