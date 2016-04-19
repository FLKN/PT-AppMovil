package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.ptappmovil.upiita.pt_appmovil.Adapters.ServiceAdapter;
import com.ptappmovil.upiita.pt_appmovil.Items.ServiceItem;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private ListView service_listview;
    private TextView service_list_label;
    private TextView service_header;

    private ArrayList<Integer> order_ids;
    private ArrayList<String> order_list;
    private float order_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.service_list_label = (TextView)findViewById(R.id.service_list_label);
        this.service_listview = (ListView)findViewById(R.id.service_listview);
        this.service_header = (TextView)findViewById(R.id.service_header);

        final List items = new ArrayList();

        // Elementos de la lista deben estar estrictamente ordenados por id
        items.add(new ServiceItem("1","Pizza de pepperoni", BitmapFactory.decodeResource(getResources(), R.drawable.pizza),"15.00"));
        items.add(new ServiceItem("2","Papas a la francesa con ketchup y salsa valentina", BitmapFactory.decodeResource(getResources(), R.drawable.papas), "25.00"));
        items.add(new ServiceItem("3","Tacos de pastor con su cebollita y su cilantrito", BitmapFactory.decodeResource(getResources(), R.drawable.tacos), "150.00"));
        items.add(new ServiceItem("4","Pizza de pepperoni", BitmapFactory.decodeResource(getResources(), R.drawable.pizza),"15.00"));
        items.add(new ServiceItem("5","Papas a la francesa con ketchup y salsa valentina", BitmapFactory.decodeResource(getResources(), R.drawable.papas), "25.00"));
        items.add(new ServiceItem("6", "Tacos de pastor con su cebollita y su cilantrito", BitmapFactory.decodeResource(getResources(), R.drawable.tacos), "150.00"));

        final ServiceAdapter adapter = new ServiceAdapter(this, items);
        this.service_listview.setAdapter(adapter);

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

}
