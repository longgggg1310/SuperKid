package com.example.longtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class RegisterUser extends AppCompatActivity {
    EditText ed_name, ed_email, ed_password;

    FirebaseAuth auth;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_user);

        ed_name = (EditText)findViewById(R.id.input_name);
        ed_email = (EditText)findViewById(R.id.input_email);
        ed_password = (EditText)findViewById(R.id.input_password);

        //   title = (TextView)findViewById(R.id.registration_title);

        auth = FirebaseAuth.getInstance();
    }


    public void createUserAccount(View v)
    {
        if (!validate()) {
            //   onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(RegisterUser.this,R.style.AppTheme);
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

    private void onSignupSuccess()
    {

        Intent myIntent = new Intent(RegisterUser.this,UsernameActivity.class);
        myIntent.putExtra("name",ed_name.getText().toString());
        myIntent.putExtra("email",ed_email.getText().toString());
        myIntent.putExtra("password",ed_password.getText().toString());
        myIntent.putExtra("type","children");

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

    public void userGoLogin(View v) {
        Intent i =new Intent(RegisterUser.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}