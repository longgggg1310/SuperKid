package com.example.longtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterAdmin extends AppCompatActivity {

    EditText ed_name,ed_email,ed_password;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_admin);

        ed_name = (EditText)findViewById(R.id.input_name);
        ed_email = (EditText)findViewById(R.id.input_email);
        ed_password = (EditText)findViewById(R.id.input_password);
        auth = FirebaseAuth.getInstance();


    }
    private void onSignupSuccess()
    {

        Intent myIntent = new Intent(RegisterAdmin.this,UsernameActivity.class);
        myIntent.putExtra("name",ed_name.getText().toString());
        myIntent.putExtra("email",ed_email.getText().toString());
        myIntent.putExtra("password",ed_password.getText().toString());
        myIntent.putExtra("type","parent");

        startActivity(myIntent);
    }

    public boolean validate() {
        boolean valid = true;

        String name = ed_name.getText().toString();
        String email = ed_email.getText().toString();
        String password = ed_password.getText().toString();


        if (name.isEmpty() || name.length() < 5) {
            ed_name.setError("at least 5 characters");
            valid = false;
        } else {
            ed_name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError("enter a valid email address");
            valid = false;
        } else {
            ed_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            ed_password.setError("between 6 and 15 alphanumeric characters");
            valid = false;
        } else {
            ed_password.setError(null);
        }


        return valid;
    }
    public void gotoLogin(View v)
    {
        Intent myIntent = new Intent(RegisterAdmin.this,MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    public void createAccount(View v)
    {
        if (!validate()) {
            //   onSignupFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(RegisterAdmin.this,R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //    onSignupSuccess();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);


    }

}