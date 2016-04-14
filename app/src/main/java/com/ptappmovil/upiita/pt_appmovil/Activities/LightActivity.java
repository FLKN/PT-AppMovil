package com.ptappmovil.upiita.pt_appmovil.Activities;

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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.ptappmovil.upiita.pt_appmovil.R;
import com.microsoft.windowsazure.mobileservices.*;

import java.net.MalformedURLException;

public class LightActivity extends AppCompatActivity {

    private float lumen;
    private MobileServiceClient mClient;

    private HoloCircleSeekBar light_intensity;
    private TextView light_label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*try {
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://pt-mobileservices.azure-mobile.net/",
                    "KnHbSbIvsSzonahbvcxAfUpDsLvxAF94",
                    this).withFilter(new ProgressFilter());


        } catch (MalformedURLException e) {
            Log.e("Error", "There was an error creating the Mobile Service. Verify the URL");
        } catch (Exception e){
            Log.e("Error",e.toString());
        }
        */
        // Obtener valor con consulta
        lumen = (float)19.5;

        light_intensity = (HoloCircleSeekBar) findViewById(R.id.light_picker);
        light_intensity.setValue(lumen);

        light_label = (TextView)findViewById(R.id.light_text);
        light_label.setText("Luminosidad: " + lumen);

        light_intensity.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) { }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) { }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                lumen = holoCircleSeekBar.getValue();
                // Actualizar base con intensity

                light_label.setText("Luminosidad: " + lumen + "%");
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

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }

}
