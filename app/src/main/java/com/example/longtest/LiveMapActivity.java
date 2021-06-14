package com.example.longtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LiveMapActivity extends AppCompatActivity implements OnMapReadyCallback{


    String latitude,longitude,name,userid,date;
    DatabaseReference reference;
    GoogleMap mMap;
    String myName,myLat,myLng,myDate;
    LatLng friendLatLng;
    MarkerOptions myOptions;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live_map);

        Intent intent = getIntent();
        myOptions = new MarkerOptions();
        if(intent!=null)
        {
            latitude=intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
            name = intent.getStringExtra("name");
            userid = intent.getStringExtra("userid");
            date = intent.getStringExtra("date");
        }


        reference = FirebaseDatabase.getInstance().getReference().child("Children").child(userid);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);







        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //  Toast.makeText(getApplicationContext(),"onAdded",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //   CreateUser user = dataSnapshot.getValue(CreateUser.class);

                //  Toast.makeText(getApplicationContext(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myName = dataSnapshot.child("name").getValue(String.class);
                        myLat = dataSnapshot.child("latitude").getValue(String.class);
                        myLng = dataSnapshot.child("longitude").getValue(String.class);
                        myDate = dataSnapshot.child("lasttime").getValue(String.class);


                        friendLatLng = new LatLng(Double.parseDouble(myLat),Double.parseDouble(myLng));

                        myOptions.position(friendLatLng);
                        myOptions.snippet("Last seen: "+myDate);
                        myOptions.title(myName);

                        if(marker == null)
                        {
                            marker = mMap.addMarker(myOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));
                        }
                        else
                        {
                            marker.setPosition(friendLatLng);
                        }




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View row = getLayoutInflater().inflate(R.layout.custom_snippet,null);
                TextView nameTxt = row.findViewById(R.id.snippetName);
                TextView dateTxt = row.findViewById(R.id.snippetDate);

                if(myName == null && myDate == null)
                {
                    nameTxt.setText(name);
                    dateTxt.setText(dateTxt.getText().toString() + date);

                }
                else
                {
                    nameTxt.setText(myName);
                    dateTxt.setText(dateTxt.getText().toString() + myDate);
                }


                return row;
            }
        });

        friendLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        MarkerOptions optionsnew = new MarkerOptions();

        optionsnew.position(friendLatLng);
        optionsnew.title(name);
        optionsnew.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //  optionsnew.snippet("Last seen:"+prevdate);

        if(marker == null)
        {
            marker = mMap.addMarker(optionsnew);
        }
        else
        {
            marker.setPosition(friendLatLng);
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));




    }
}
