package com.ptappmovil.upiita.pt_appmovil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ptappmovil.upiita.pt_appmovil.Activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP  = 0;

    @InjectView(R.id.user_text) EditText userText;
    @InjectView(R.id.password_text) EditText passwordText;
    @InjectView(R.id.btn_signin) Button loginButton;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Ocurrio un problema, intente otra vez");
            return;
        }

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        try {
            //Construimos el objeto cliente en formato JSON
            JSONObject dato = new JSONObject();

            dato.put("user_name", user);
            dato.put("app_pass", password);

            this.doLoginRequest(dato,"http://pt-backend.azurewebsites.net/login");

        } catch(Exception ex) {
            Log.d("Error","Error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String msg, String user, int room, int level) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

        Intent main_intent = new Intent(this, MainActivity.class);
        main_intent.putExtra("user",user);
        main_intent.putExtra("room",room);
        main_intent.putExtra("level",level);
        startActivity(main_intent);

        finish();
    }

    public void onLoginFailed(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

    }

    public boolean validate() {
        boolean valid = true;

        String usuario = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (usuario.isEmpty()) {
            userText.setError("Escriba el numbre de usuario");
            valid = false;
        } else {
            userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("La contraseña debe tener entre 4 y 10 caracteres");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    private void doLoginRequest(JSONObject params, String url) {
        progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            if(response.getBoolean("authorized"))
                                onLoginSuccess(response.getString("msg"), response.getString("name"), response.getInt("room"), response.getInt("level"));
                            else
                                onLoginFailed(response.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
        };

// add it to the RequestQueue
        queue.add(getRequest);
    }
}

