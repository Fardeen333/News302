package com.creative.news302.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.creative.news302.R;
import com.creative.news302.home;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class facbook_login extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference imageReference;
    private DatabaseReference userDatabaseReference, userUIDReference;
    private FirebaseUser user;
    private String userID = null;
    CallbackManager mcallbackManager;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facbook_login);

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mcallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("fffff", "success");
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("fffff", "cancelled");
                Toast.makeText(facbook_login.this, "Signin Failed: " + "cancelled by user", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException error) {
                Log.d("fffff", "retry");
                Log.d("fffff", error.getMessage());
                finish();
                Toast.makeText(facbook_login.this, "Signin Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();

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

                if (user != null) {
                    //HomeActivity.putExtra("isSigned", "true");
                    //startActivity(HomeActivity);
                    // getActivity().finish();

                } else {


                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookToken(AccessToken accessToken) {

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("fffff", "login fsucksailed");
                    Toast.makeText(facbook_login.this, "Signin Successful ", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(facbook_login.this, home.class);
                    a.putExtra("isSigned", "true");

                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                } else {
                    Log.d("failed", Objects.requireNonNull(task.getException()).getMessage());
                    Toast.makeText(facbook_login.this, "Signin Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    Log.d("fffff", "login failed");
                    finish();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
