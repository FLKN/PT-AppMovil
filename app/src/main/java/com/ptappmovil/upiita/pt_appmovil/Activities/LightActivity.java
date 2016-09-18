package com.ptappmovil.upiita.pt_appmovil.Activities;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.ptappmovil.upiita.pt_appmovil.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LightActivity extends AppCompatActivity {

    private float lumen;
    private int room;

    private HoloCircleSeekBar light_intensity;
    private TextView light_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle light_intent = getIntent().getExtras();
        room = light_intent.getInt("room");

        light_intensity = (HoloCircleSeekBar) findViewById(R.id.light_picker);

        light_label = (TextView)findViewById(R.id.light_text);

        LumenRequest get_lumen = new LumenRequest();
        get_lumen.execute(new Integer(room));

        light_intensity.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) { }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                lumen = holoCircleSeekBar.getValue();
                light_label.setText("Luminosidad: " + lumen + "%");

                new LumenRefreshRequest(room,lumen).execute();
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

    private class LumenRequest extends AsyncTask<Integer, Integer, JSONObject> {

        protected JSONObject doInBackground(Integer... params) {

            JSONObject resul = null;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://ptserver.southcentralus.cloudapp.azure.com:9090/sensors/get_light");
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
                    light_intensity.setValue((float)result.getDouble("lumen"));
                    light_label.setText("Luminosidad: " + result.getDouble("lumen") + "%");
                }
                else
                    Toast.makeText(LightActivity.this,result.getString("msg"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class LumenRefreshRequest extends AsyncTask<Void, Void, JSONObject> {
        int room;
        float lumen;

        public LumenRefreshRequest(int room, float lumen) {
            this.room = room;
            this.lumen = lumen;
        }
        protected JSONObject doInBackground(Void... params) {

            JSONObject resul = null;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://ptserver.southcentralus.cloudapp.azure.com:9090/sensors/update_light");
            post.setHeader("content-type", "application/json");

            try {
                JSONObject dato = new JSONObject();

                dato.put("room", room);
                dato.put("lumen", lumen);

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
                 Toast.makeText(LightActivity.this,result.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
