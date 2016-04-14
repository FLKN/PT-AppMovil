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

import com.ptappmovil.upiita.pt_appmovil.Activities.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP  = 0;

    @InjectView(R.id.user_text) EditText userText;
    @InjectView(R.id.password_text) EditText passwordText;
    @InjectView(R.id.btn_signin) Button loginButton;

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
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();

        final String user = userText.getText().toString();
        final String password = passwordText.getText().toString();
        //Aqui se conecta a la BD para verificar los datos

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    //Este if debe de hacerse con la validacion de arriba
                    if( user.equals("david.perez") && password.equals("pass123") )
                        onLoginSuccess("david.perez");
                    else
                        onLoginFailed();
                    progressDialog.dismiss();
                }
            }, 3000
        );
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

    public void onLoginSuccess(String user) {
        Toast.makeText(getBaseContext(), "Bienvenido", Toast.LENGTH_LONG).show();

        Intent main_intent = new Intent(this, MainActivity.class);
        main_intent.putExtra("user",user);
        startActivity(main_intent);




        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Ocurrió un error", Toast.LENGTH_LONG).show();

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
}

