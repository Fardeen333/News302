package com.creative.news302;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creative.news302.adapters.home_viewPagerAdapter;
import com.creative.news302.admin.addData;
import com.creative.news302.utility.CustomDrawerButton;
import com.creative.news302.utility.Dialouge;
import com.creative.news302.utility.MyService;
import com.creative.news302.utility.notificat;
import com.creative.news302.utility.touchlstener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;


public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private home_viewPagerAdapter adapter;
    CustomDrawerButton customDrawerButton;
    private DrawerLayout drawer;
    private Intent serviceIntent;
    private Dialouge m_dialouge;
    TextView tvname, tvemail, tvinitial;
    CircleImageView cmvprofile;
    CoordinatorLayout c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Log.d("aaaaaaa", "hfg");
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        customDrawerButton = (CustomDrawerButton) findViewById(R.id.dbutton);
        customDrawerButton.setDrawerLayout(drawer);
        customDrawerButton.getDrawerLayout().addDrawerListener(customDrawerButton);
        m_dialouge = new Dialouge(home.this);
        tvname = navigationView.getHeaderView(0).findViewById(R.id.name);
        tvemail = navigationView.getHeaderView(0).findViewById(R.id.email);
        tvinitial = navigationView.getHeaderView(0).findViewById(R.id.tvinitial);
        cmvprofile = navigationView.getHeaderView(0).findViewById(R.id.imvdp);
        c = findViewById(R.id.llout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutHome);
        viewPager = (ViewPager) findViewById(R.id.viewPagerHome);

        adapter = new home_viewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new fragment_Dashboard(), "Dashboard");
        adapter.AddFragment(new fragment_home(viewPager), "Feed");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.setOnTouchListener(new touchlstener(this) {
            public void onSwipeLeft() {
                customDrawerButton.changeState();

            }

        });


        customDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDrawerButton.changeState();
            }
        });
        getCurrentinfo();

        if (!checkServiceRunning(getClass())) {
            serviceIntent = new Intent(getApplicationContext(), com.creative.news302.utility.MyService.class);
            startService(serviceIntent);
           // Toast.makeText(this, "service running", Toast.LENGTH_SHORT).show();
        }
        checkforupdate();
    }

    public boolean checkServiceRunning(Class<?> serviceClass) {
        String s = "com.creative.news302.home com.creative.news302.utility.MyService";
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d("serviceinfo", serviceClass.getName() + " " + service.service.getClassName());
            if ((serviceClass.getName() + " " + service.service.getClassName()).equals(s)) {
                Log.d("infi", "true");
                return true;
            }
        }
        Log.d("infi", "false");

        return false;
    }

    private void checkforupdate() {
        DatabaseReference dbupdate = FirebaseDatabase.getInstance().getReference().child("update");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long vc = (long) dataSnapshot.getValue();

                if (vc > getVersionInfo()) {
                    Snackbar snackbar = Snackbar
                            .make(c, "Update Available", Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(Color.WHITE).setAction("Update ", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });
                    snackbar.getView().setBackgroundResource(R.color.colorPrimaryDark);
                    snackbar.show();
                    // m_dialouge.showUpdate(home.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbupdate.addValueEventListener(eventListener);
    }

    private int getVersionInfo() {
        String versionName = "";
        int versionCode = 1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //   textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));

        return versionCode;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Toast.makeText(this, "Loading this", Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.nav_Settings:
                Intent i_order = new Intent(this, settings.class);
                startActivity(i_order);
                break;



            case R.id.nav_privacy:
                Intent i_ss = new Intent(this, privacypolicy.class);
                startActivity(i_ss);
                break;

//            case R.id.nav_addnews:
//                Intent i_sxs = new Intent(this, addData.class);
//                startActivity(i_sxs);
//                break;
//
//            case R.id.nav_notify:
//                Intent i_s1s = new Intent(this, notificat.class);
//                startActivity(i_s1s);
//                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                //stopService(serviceIntent);
                break;



        }

        return true;
    }

//    private void getphoto(){
//        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
//        myUserDetails.name = firebaseAuth.getCurrentUser().getDisplayName();
//        myUserDetails.email = firebaseAuth.getCurrentUser().getEmail();
//
//        String photoUrl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
//        for (UserInfo profile : firebaseAuth.getCurrentUser().getProviderData()) {
//            System.out.println(profile.getProviderId());
//            // check if the provider id matches "facebook.com"
//            if (profile.getProviderId().equals("facebook.com")) {
//
//                String facebookUserId = profile.getUid();
//
//                myUserDetails.sigin_provider = profile.getProviderId();
//                // construct the URL to the profile picture, with a custom height
//                // alternatively, use '?type=small|medium|large' instead of ?height=
//
//                photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
//
//            } else if (profile.getProviderId().equals("google.com")) {
//                myUserDetails.sigin_provider = profile.getProviderId();
//                ((HomeActivity) getActivity()).loadGoogleUserDetails();
//            }
//        }
//        myUserDetails.profile_picture = photoUrl;
//
//    }

    private void getCurrentinfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // for (UserInfo profile : user.getProviderData()) {
            // Id of the provider (ex: google.com)
            String providerId = user.getProviderId();

            // UID specific to the provider
            String uid = user.getUid();
            String name = "N";
            name = user.getDisplayName();
            // Name, email address, and profile photo Url
            tvname.setText(name);
            Uri photoUrl = user.getPhotoUrl();
            tvemail.setText(user.getEmail());

            //setimage
            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).crossFade().centerCrop().listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).into(cmvprofile);
            }
            if (photoUrl == null) {
                // Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
                cmvprofile.setImageResource(R.color.color_white);
                try {


                tvinitial.setText(name.substring(0, 1).trim());
            }catch (Exception e){
                    tvinitial.setText("U");

                }
                tvinitial.setVisibility(View.VISIBLE);
            }

            // }
        }

    }
}