package com.example.longtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.longtest.Adapter.ConnectedUserAdapter;
import com.example.longtest.model.UserRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConnectedUsersActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth auth;
    DatabaseReference connectedReference,usersReference;
    ArrayList<UserRegister> nameList;
    FirebaseUser firebaseUser;
    UserRegister userRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connected_users);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        // myList = new ArrayList<>();

        nameList = new ArrayList<>();

        recyclerView.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        connectedReference = FirebaseDatabase.getInstance().getReference().child("Parent").child(firebaseUser.getUid()).child("ConnectedUsers");
        usersReference = FirebaseDatabase.getInstance().getReference().child("Children");

        connectedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameList.clear();

                if(dataSnapshot.exists())
                {
                    for(DataSnapshot dss: dataSnapshot.getChildren())
                    {
                        String memberUserid = dss.child("circleMemberID").getValue(String.class);

                        usersReference.child(memberUserid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {   userRegister = dataSnapshot.getValue(UserRegister.class);
                                    nameList.add(userRegister);
                                    //    Toast.makeText(getApplicationContext(),userRegister.name,Toast.LENGTH_LONG).show();
                                    //     Toast.makeText(getApplicationContext(),"size="+nameList.size(),Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView.Adapter adaptermy = new ConnectedUserAdapter(nameList,ConnectedUsersActivity.this);
                            recyclerView.setAdapter(adaptermy);
                            adaptermy.notifyDataSetChanged();

                        }
                    },800);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You have not joined any user circle yet!",Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }
}