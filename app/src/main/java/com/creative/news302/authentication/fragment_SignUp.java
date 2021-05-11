package com.creative.news302.authentication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.R;
import com.creative.news302.home;
import com.creative.news302.homepromo;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;


public class fragment_SignUp extends Fragment {

    static final int google_signin = 123;


    public fragment_SignUp() {

    }

    GoogleSignInClient mgoogleSignInClient;

    Button b_signUp, b_uploaddata, b_savedata;
    ImageView imv_googleSignUp, imv_facebookSignUp;
    EditText e_name, e_email, e_password;
    TextView t_message, t_skip,b_usePhone;
    String def = "default";

    Intent HomeActivity;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference imageReference;
    private DatabaseReference userDatabaseReference, userUIDReference;
    private FirebaseUser user;
    private String userID = null;
    CallbackManager mcallbackManager;
    Intent i;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment__sign_up, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        i = new Intent(getActivity(), home.class);
        i.putExtra("isSigned", "true");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        e_name = view.findViewById(R.id.edName);
        e_email = view.findViewById(R.id.edEmailAdress);
        e_password = view.findViewById(R.id.edPassword);

        b_signUp = view.findViewById(R.id.bsignUp);
        b_uploaddata = view.findViewById(R.id.buploaddata);
        b_savedata = view.findViewById(R.id.bsavedata);
        b_usePhone = view.findViewById(R.id.bUsePhone);
        imv_googleSignUp = view.findViewById(R.id.imageView_google);
        imv_facebookSignUp = view.findViewById(R.id.imageView_facebook);
        t_skip = view.findViewById(R.id.tvskipsignup);

        progressBar = view.findViewById(R.id.p_circle);
        progressBar.setVisibility(View.GONE);

        HomeActivity = new Intent(getActivity(), home.class);
        HomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


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


        t_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), home.class);
                a.putExtra("isSigned", "false");
                startActivity(a);
            }
        });
        b_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), homepromo.class);
                startActivity(a);
                //registeruser();
            }
        });
        b_savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveusername();
            }
        });
        b_uploaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveusername();
            }
        });
        imv_googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                googlesignin();
            }
        });
        imv_facebookSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent fac = new Intent(getActivity(),facbook_login.class);
               startActivity(fac);
            }
        });
        b_usePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),activity_otp.class));
            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        //progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();
        progressBar.setVisibility(View.GONE);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void facebooklogin() {
        Log.d("fffff", "logging started");
        progressBar.setVisibility(View.VISIBLE);

    }

    public void registeruser() {
        boolean ret = false;
        progressBar.setVisibility(View.VISIBLE);
        final String name = e_name.getText().toString().trim();
        final String email = e_email.getText().toString().trim();
        String password = e_password.getText().toString();

        if (name.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            e_name.setError("name is required!");
            e_name.requestFocus();
            ret = true;
        } else if (name.length() < 3) {
            progressBar.setVisibility(View.GONE);
            e_name.setError("Write your full name");
            e_name.requestFocus();
            ret = true;
        }

        if (email.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            e_email.setError("Email is required!");
            e_email.requestFocus();
            ret = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressBar.setVisibility(View.GONE);
            e_email.setError("Invalid Email!");
            e_email.requestFocus();
            ret = true;
        }

        if (password.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            e_password.setError("Password is required!");
            e_password.requestFocus();
            ret = true;
        } else if (password.length() < 6) {
            progressBar.setVisibility(View.GONE);
            e_password.setError("Password too short!");
            e_password.requestFocus();
            ret = true;
        }

        if (ret) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        b_signUp.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    userID = user.getUid();
                    userUIDReference = userDatabaseReference.child(userID);

                    //savedata();
                     saveusername();

                } else {
                    b_signUp.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthWeakPasswordException e) {
                        e_password.setError("week password");
                        e_password.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        e_email.setError("invalid email");
                        e_email.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        e_email.setError("User Already Exists");
                        e_email.requestFocus();
                    } catch (FirebaseNetworkException e) {
                        Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }
//        public void savedata() {
//    progressBar.setVisibility(View.VISIBLE);
//    userUIDReference.setValue(userProfileModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//        @Override
//        public void onComplete(@NonNull Task<Void> task) {
//            if (task.isSuccessful()) {
//                progressBar.setVisibility(View.GONE);
//                b_savedata.setBackgroundResource(R.drawable.button_back_ripple_round_green);
//                b_savedata.setText("Authentication Completed");
//                b_savedata.setEnabled(false);
//                saveusername();
//
//            } else {
//                progressBar.setVisibility(View.GONE);
//                try {
//                    throw task.getException();
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    b_savedata.setVisibility(View.VISIBLE);
//                    b_savedata.requestFocus();
//                }
//            }
//        }
//    });
//}
//    //to do ui operations
//
//}

    public void saveusername() {
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(e_name.getText().toString())
                    .build();

            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        // b_uploaddata.setBackgroundResource(R.drawable.button_back_ripple_round_green);
                        b_uploaddata.setText("Data Saved");
                        b_uploaddata.setEnabled(false);
                        HomeActivity.putExtra("isSigned", "true");
                        HomeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(HomeActivity);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            b_uploaddata.setVisibility(View.VISIBLE);
                            b_uploaddata.requestFocus();
                        }
                    }
                }
            });
        }
    }


    private void googlesignin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        SignIn();

    }

    public void SignIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, google_signin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mcallbackManager!=null)
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == google_signin) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleSignin", "Google sign in failed", e);
                Toast.makeText(getContext(), "Signin Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebaseSignIn", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("firebaseSignIn", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.INVISIBLE);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("firebaseSignIn", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            updateUI(null);
                            Toast.makeText(getContext(), "Signin Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }


    public void updateUI(FirebaseUser user) {
        //startActivity
        if (user != null) {
            Intent a = new Intent(getActivity(), home.class);
            a.putExtra("isSigned", "true");
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().finish();
            startActivity(a);
        }

    }


}
