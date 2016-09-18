package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.os.AsyncTask;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ptappmovil.upiita.pt_appmovil.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LockActivity extends AppCompatActivity {

    private Switch lock_switch;
    private TextView lock_text;

    private int status;
    private int room;


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

        LockRequest get_status = new LockRequest();
        get_status.execute(new Integer(room));

        lock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    new LockRefreshRequest(room,1).execute();
                    lock_text.setText("Puerta Abierta");
                }
                else {
                    new LockRefreshRequest(room,0).execute();
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

    private class LockRequest extends AsyncTask<Integer, Integer, JSONObject> {

        protected JSONObject doInBackground(Integer... params) {

            JSONObject resul = null;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://ptserver.southcentralus.cloudapp.azure.com:9090/sensors/get_lock");
            post.setHeader("content-type", "application/json");

            try {
                JSONObject dato = new JSONObject();

                dato.put("room", params[0].intValue());

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = new JSONObject(respStr);
            }
            catch(Exception ex) {
                Log.e("ServicioRest","Error!", ex);
                resul = null;
            }

            return resul;
        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try {
                if(result.getBoolean("action")){
                    if (result.getInt("lock_state") == 1){
                        lock_switch.setChecked(true);
                        lock_text.setText(result.getString("msg"));
                    }
                    else{
                        lock_switch.setChecked(false);
                        lock_text.setText(result.getString("msg"));
                    }
                }
                else
                    Toast.makeText(LockActivity.this,result.getString("msg"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class LockRefreshRequest extends AsyncTask<Void, Void, JSONObject> {
        int room;
        int lock_state;

        public LockRefreshRequest(int room, int lock_state) {
            this.room = room;
            this.lock_state = lock_state;
        }
        protected JSONObject doInBackground(Void... params) {

            JSONObject resul = null;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://ptserver.southcentralus.cloudapp.azure.com:9090/sensors/update_lock");
            post.setHeader("content-type", "application/json");

            try {
                JSONObject dato = new JSONObject();

                dato.put("room", room);
                dato.put("lock_state", lock_state);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    resul = new JSONObject(respStr);
            }
            catch(Exception ex) {
                Log.e("ServicioRest","Error!", ex);
                resul = null;
            }

            return resul;
        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try {
                Toast.makeText(LockActivity.this,result.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
