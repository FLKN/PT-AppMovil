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

import com.ptappmovil.upiita.pt_appmovil.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private TextView order_box;
    private TextView order_count;


    private ArrayList<Integer> order_ids;
    private ArrayList<String> order_list;
    private float order_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.order_cost = getIntent().getFloatExtra("order_cost", 0);
        this.order_ids = getIntent().getIntegerArrayListExtra("order_ids");
        this.order_list = getIntent().getStringArrayListExtra("order_list");

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

    public void confirmOrder(View v){
        final ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Procesando...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // Envio de ids para confirmar orden

                        progressDialog.dismiss();
                        OrderActivity.this.setResult(RESULT_OK, null);
                        OrderActivity.this.finish();
                        Toast.makeText(OrderActivity.this, "Orden confirmada", Toast.LENGTH_LONG).show();
                    }
                }, 3000
        );


    }
}
