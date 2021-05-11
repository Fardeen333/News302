package com.creative.news302.authentication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.R;
import com.creative.news302.home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class fragment_SignIn extends Fragment {

    Intent HomeActivity2,passwordresetactivity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Button b_auth;

    ProgressBar progressBar;

    EditText e_email,e_password;

    TextView t_forgotpass;


    public fragment_SignIn() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__sign_in, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        // getSupportActionBar().hide();

        HomeActivity2 = new Intent(getActivity(), home.class);
        HomeActivity2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        passwordresetactivity = new Intent(getActivity(),password_reset.class);

        b_auth = view.findViewById(R.id.bsignIn);

        t_forgotpass = view.findViewById(R.id.tvforgotPassword);
        t_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(passwordresetactivity);
            }
        });

        progressBar = view.findViewById(R.id.p_circle2);
        progressBar.setVisibility(View.GONE);

        e_email = view.findViewById(R.id.edEmailAdress2);
        e_password = view.findViewById(R.id.edPassword2);

        b_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    //redirect
                   // Toast.makeText(getContext(), "bcas", Toast.LENGTH_SHORT).show();
                    //HomeActivity2.putExtra("isSigned","true");
                    //startActivity(HomeActivity2);
                    //getActivity().finish();

                } else {
                    HomeActivity2.putExtra("isSigned","false");
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    // updateUI(null);
                }

            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.GONE);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            //redirect
           // HomeActivity2.putExtra("isSigned","true");
            //startActivity(HomeActivity2);
            //getActivity().finish();

        } else {
            // User is signed out
            //Log.d(TAG, "onAuthStateChanged:signed_out");
            // updateUI(null);
        }
        // updateUI(currentUser);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        progressBar.setVisibility(View.GONE);

    }


    public void loginuser() {
        boolean retu = false;

        String email = e_email.getText().toString().trim();
        String password = e_password.getText().toString();


        if (email.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            e_email.setError("Email is required!");
            e_email.requestFocus();
            retu = true;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressBar.setVisibility(View.GONE);
            e_email.setError("Invalid Email!");
            e_email.requestFocus();
            retu = true;
        }

        if (password.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            e_password.setError("Password is required!");
            e_password.requestFocus();
            retu = true;

        }
        else if (password.length() < 6) {
            progressBar.setVisibility(View.GONE);
            e_password.setError("Wrong Password!");
            e_password.requestFocus();
            retu = true;
        }

        if (retu) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Login Succesful", Toast.LENGTH_SHORT).show();
                    HomeActivity2.putExtra("isSigned","true");
                    HomeActivity2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().finish();
                    startActivity(HomeActivity2);


                } else {
                    progressBar.setVisibility(View.GONE);
                    try {

                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        e_email.setError("User does not exist");
                        e_email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        e_password.setError("Wrong Password");
                        e_password.requestFocus();
                    } catch (FirebaseNetworkException e) {
                        Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
