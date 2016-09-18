package com.ptappmovil.upiita.pt_appmovil;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ptappmovil.upiita.pt_appmovil.Activities.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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

        loginButton.setEnabled(false);


        final String user = userText.getText().toString();
        final String password = passwordText.getText().toString();

        //JSONObject json = this.doLogin(user,password);
        LoginRequest make_login = new LoginRequest();
        make_login.execute(user, password);

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

        loginButton.setEnabled(true);
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

    private class LoginRequest extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Iniciando Sesión...");
            progressDialog.show();
        }

        protected JSONObject doInBackground(String... params) {

            JSONObject resul = null;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://ptserver.southcentralus.cloudapp.azure.com:9090/login");
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("user_name", params[0]);
                dato.put("app_pass", params[1]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                progressDialog.dismiss();

                if(!respStr.equals("true"))
                    resul = new JSONObject(respStr);
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = null;
            }

            return resul;
        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try {
                if(result.getBoolean("authorized"))
                    onLoginSuccess(result.getString("msg"), result.getString("name"), result.getInt("room"), result.getInt("level"));
                else
                    onLoginFailed(result.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

