package com.example.longtest;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText ed_email,ed_password;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    PermissionManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if(firebaseUser == null)
        {
            setContentView(R.layout.activity_main);
            ed_email = (EditText)findViewById(R.id.input_email);
            ed_password = (EditText)findViewById(R.id.input_password);
            reference = FirebaseDatabase.getInstance().getReference().child("Children");
            manager = new PermissionManager() {};
            manager.checkAndRequestPermissions(this);

        }

        else
        {
            Intent myIntent = new Intent(MainActivity.this,MapActivity.class);
            startActivity(myIntent);
            finish();
        }

        // check if email is found in user_table or not,, if found,, okay we sign in the user.

    }

    private void initActivity(){
        //Starting HomeActivity from Authentication Activity if user is logged in.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void LoginUser(View v)
    {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        //   mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        //onLoginSuccess();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public boolean validate() {
        boolean valid = true;

        String email = ed_email.getText().toString();
        String password = ed_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError("enter a valid email address");
            valid = false;
        } else {
            ed_password.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            ed_password.setError("between 6 and 15 alphanumeric characters");
            valid = false;
        } else {
            ed_password.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        //   mLoginButton.setEnabled(true);
    }


    //TODO onLoginSuccess() start
    public void onLoginSuccess() {
        //  mLoginButton.setEnabled(true);
        //TODO Login logic here

        final String emailAddress = ed_email.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();
        if (!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(password))
        {


            Query queryAdmin = reference.orderByChild("email").equalTo(emailAddress);

            queryAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        auth.signInWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                                    finish();

                                } else if (!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Wrong username/password combination entered.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(),"Wrong email or password entered",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode,permissions,grantResults);


        ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;

        if(denied_permissions.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Location permission granted.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Location permission is not granted.Please grant the permission.",Toast.LENGTH_SHORT).show();
        }
    }






}
