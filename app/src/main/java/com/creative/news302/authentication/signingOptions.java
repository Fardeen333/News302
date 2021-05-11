package com.creative.news302.authentication;

import android.os.Bundle;

import com.creative.news302.R;
import com.creative.news302.adapters.auth_viewpagerAdapter;
import com.firebase.client.Firebase;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class signingOptions extends AppCompatActivity {
  //  private com.creative.givlimps2.utility.prerequisites prerequisites;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private auth_viewpagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_options);
       // prerequisites = new prerequisites();
        Firebase.setAndroidContext(this);


//        if (!prerequisites.checknetwork(this)) {
//            Toast.makeText(this, "No network!", Toast.LENGTH_LONG).show();
//        }

        tabLayout=(TabLayout)findViewById(R.id.tabLayoutHome);
        viewPager=(ViewPager)findViewById(R.id.viewPagerHome);

        adapter=new auth_viewpagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new fragment_SignUp(),"Sign Up");
        adapter.AddFragment(new fragment_SignIn(),"Sign In");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
