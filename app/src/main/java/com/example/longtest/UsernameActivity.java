package com.example.longtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.longtest.model.AdminRegister;
import com.example.longtest.model.UserRegister;
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

public class UsernameActivity extends AppCompatActivity {
    String name, email, password, type;
    EditText editText;


    FirebaseAuth auth;

    DatabaseReference databaseReferenceAdmin, databaseReferenceUser;
    FirebaseUser firebaseUser;

    String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_username);
        editText = (EditText) findViewById(R.id.etUsername);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        type = intent.getStringExtra("type");

        editText.setText(type);

        auth = FirebaseAuth.getInstance();

        databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference().child("Parent");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Children");
    }
    public void createAccount(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(UsernameActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        // register in admin


        if (type.equals("parent"))

        {
            Query queryAdmin = databaseReferenceAdmin.orderByChild("username").equalTo(editText.getText().toString());


            queryAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else

                    {

                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser = auth.getCurrentUser();
                                            current_user_id = firebaseUser.getUid();

                                            AdminRegister adminRegister = new AdminRegister(name, editText.getText().toString(), email, password,current_user_id);
                                            databaseReferenceAdmin.child(current_user_id).setValue(adminRegister)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getApplicationContext(), "Admin account created", Toast.LENGTH_LONG).show();

                                                                finish();
                                                                Intent myIntent = new Intent(UsernameActivity.this, MapActivity.class);
                                                                startActivity(myIntent);
                                                            }
                                                        }
                                                    });

                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        } else if (type.equals("children")) {
            // insert in user table

            Query queryUser = databaseReferenceUser.orderByChild("username").equalTo(editText.getText().toString());
            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser = auth.getCurrentUser();
                                            current_user_id = firebaseUser.getUid();

                                            UserRegister userRegister = new UserRegister(name,editText.getText().toString(),email,password,"na","na","false","na",firebaseUser.getUid());
                                            databaseReferenceUser.child(current_user_id).setValue(userRegister)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getApplicationContext(), "User account created", Toast.LENGTH_LONG).show();

                                                                finish();
                                                                Intent myIntent = new Intent(UsernameActivity.this, MapActivity.class);
                                                                startActivity(myIntent);
                                                            }
                                                        }
                                                    });

                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        }


    }

}