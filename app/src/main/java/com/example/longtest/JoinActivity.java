package com.example.longtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.longtest.model.CircleJoin;
import com.example.longtest.model.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinActivity extends AppCompatActivity {
    EditText editText;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,currentReference,circleReference,joinedReference;

    String current_userid,joinuserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join);
        editText = (EditText)findViewById(R.id.editText);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Children");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Children").child(user.getUid());
        currentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                current_userid = dataSnapshot.child("userid").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void join1(View v)
    {

        current_userid = user.getUid();

        Query query = reference.orderByChild("username").equalTo(editText.getText().toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    UserRegister userRegister = null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren())
                    {
                        userRegister = childDss.getValue(UserRegister.class);
                    }

                    joinuserid = userRegister.userid;

                    circleReference = FirebaseDatabase.getInstance().getReference().child("Children").child(joinuserid).child("CircleMembers");
                    joinedReference = FirebaseDatabase.getInstance().getReference().child("Parent").child(user.getUid()).child("ConnectedUsers");
                    CircleJoin circleJoin = new CircleJoin(current_userid);
                    final CircleJoin circleJoin1 = new CircleJoin(joinuserid);


                    circleReference.child(user.getUid()).setValue(circleJoin)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        joinedReference.child(joinuserid).setValue(circleJoin1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(),"You have joined this user circle successfully",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        Intent myIntent = new Intent(JoinActivity.this,MapActivity.class);
                                                        startActivity(myIntent);
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Could not join, try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Could not find such username",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

}

