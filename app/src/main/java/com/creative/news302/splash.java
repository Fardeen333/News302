package com.creative.news302;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.authentication.signingOptions;
import com.creative.news302.utility.Dialouge;
import com.creative.news302.utility.prerequisites;
import com.creative.news302.utility.sharedData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class splash extends AppCompatActivity {

    private Intent iActivity;
    sharedData sharedData;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private Dialouge m_dialouge;
    private Button buttonProceed;
    private Context ctx;
    private int DELAY = 5000;
    private prerequisites prerequisites;
    Intent HomeActivity, RegisterActivity, GuideActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = new sharedData(this);

        if (Objects.equals(sharedData.retrieveTheme(), "Day")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (Objects.equals(sharedData.retrieveTheme(), "Night")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        ctx = getApplicationContext();
        m_dialouge = new Dialouge(splash.this);
        prerequisites = new prerequisites();
        buttonProceed = findViewById(R.id.b_proceed);
        HomeActivity = new Intent(this, home.class);
        HomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        RegisterActivity = new Intent(this, signingOptions.class);
        RegisterActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!prerequisites.checknetwork(ctx)) {
            Toast.makeText(ctx, "No network!", Toast.LENGTH_LONG).show();
            m_dialouge.showPopUp(splash.this);
            buttonProceed.setVisibility(View.VISIBLE);

        }

        mAuth = FirebaseAuth.getInstance();


        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prerequisites.checknetwork(ctx)) {
                    Toast.makeText(ctx, "No network!", Toast.LENGTH_LONG).show();
                    m_dialouge.showPopUp(splash.this);

                } else reload();
            }
        });
    }





    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mAuth.getCurrentUser();

                if (user == null) {
                    // User is signed in
                    //redirect
                    moveToAuthActivity();

                } else if (user != null) {

                    moveToHomeActivity();

                    //checkFirstTime();

                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void moveToHomeActivity() {

        if (!prerequisites.checknetwork(this)) {
            buttonProceed.setVisibility(View.VISIBLE);
            DELAY = 5000;
            Toast.makeText(this, "No network!", Toast.LENGTH_LONG).show();
            m_dialouge.showPopUp(splash.this);
            return;
        }

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                startActivity(HomeActivity);
                finish();
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }, DELAY);


    }

    public void moveToAuthActivity() {
        if (!prerequisites.checknetwork(this)) {
            buttonProceed.setVisibility(View.VISIBLE);
            DELAY = 5000;
            Toast.makeText(this, "No network!", Toast.LENGTH_LONG).show();
            m_dialouge.showPopUp(splash.this);
            return;
        }

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                startActivity(RegisterActivity);
                finish();
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }, DELAY);

    }
}
