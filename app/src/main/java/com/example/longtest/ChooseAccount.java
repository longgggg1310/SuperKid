package com.example.longtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ChooseAccount extends AppCompatActivity {
    ImageView imageView1, imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_account);

        imageView1 = (ImageView)findViewById(R.id.imageView);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
    }

    public void registerAdmin(View v) {
        Intent intent = new Intent(ChooseAccount.this, RegisterAdmin.class);
        startActivity(intent);

    }

    public void registerUser(View v) {
        Intent intent = new Intent(ChooseAccount.this, RegisterUser.class);
        startActivity(intent);

    }

    public void loginMainPage(View v) {
        Intent intent = new Intent(ChooseAccount.this, MainActivity.class);
        startActivity(intent);
    }
}