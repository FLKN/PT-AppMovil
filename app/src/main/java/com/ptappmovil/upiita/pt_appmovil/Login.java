package com.ptappmovil.upiita.pt_appmovil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.usuario) EditText usuarioText;
    @InjectView(R.id.password) EditText passwordText;
    @InjectView(R.id.email_sign_in_button) Button loginButton;

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

        final String usuario = usuarioText.getText().toString();
        final String password = passwordText.getText().toString();

        //Aqui se conecta a la BD para verificar los datos

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    //Este if debe de hacerse con la validacion de arriba
                    if( usuario.equals("david.perez") && password.equals("pass123") )
                        onLoginSuccess();
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

    public void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "Bienvenido", Toast.LENGTH_LONG).show();

        Intent main_intent = new Intent(this, MainActivity.class);
        startActivity(main_intent);




        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Ocurrió un error", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String usuario = usuarioText.getText().toString();
        String password = passwordText.getText().toString();

        if (usuario.isEmpty()) {
            usuarioText.setError("Escriba el numbre de usuario");
            valid = false;
        } else {
            usuarioText.setError(null);
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

