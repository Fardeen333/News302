package com.creative.news302.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.R;
import com.creative.news302.home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class activity_otp extends AppCompatActivity {

    private TextView tv_timer,tv_optinfo;
    private EditText edEnterPhone,edotp;
    private int m_counter = 10;
    private Button b_sendotp,b_verifyotp,b_proceed;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference imageReference;
    private DatabaseReference userDatabaseReference, userUIDReference;
    private FirebaseUser user;
    private String userID = null,temphone;
    private Map<String, Object> userMapphone = new HashMap<String, Object>();
    private String code = null;
    private int prcess = 0;


    private String ISD_Code = "91", phone_number = null,verificationOTP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        edEnterPhone = findViewById(R.id.edenterphone);
        edotp = findViewById(R.id.edotp);
        b_sendotp = findViewById(R.id.bresendotp);
        b_verifyotp = findViewById(R.id.bverifyotp);
        b_proceed = findViewById(R.id.bproceed);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //if user not enteri g firsy time--- sharedpreferences
                if (user != null) {
                    //startActivity(HomeActivity);
                    //getActivity().finish();

                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    // updateUI(null);
                }

            }
        };


        mAuth.addAuthStateListener(mAuthListener);

        tv_timer = findViewById(R.id.tvtimer);
        tv_optinfo = findViewById(R.id.tvoptinfo);
//        temphone = getIntent().getExtras().get("PHONE").toString();


       // b_sendotp.setEnabled(false);
        //b_verifyotp.setEnabled(false);

        //userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        //user = FirebaseAuth.getInstance().getCurrentUser();
        //userID = Objects.requireNonNull(user).getUid();
        //userUIDReference = userDatabaseReference.child(userID);

       // sendVerificationCode(phone_number);

        b_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temphone = edEnterPhone.getText().toString().trim();
                phone_number = "+" + ISD_Code + temphone;
                Toast.makeText(activity_otp.this, phone_number, Toast.LENGTH_SHORT).show();

                if(edEnterPhone.getText().toString().length()!=10)
                {
                    Toast.makeText(activity_otp.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendVerificationCode(phone_number);
                startTimer();
            }
        });

        b_verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(code);
            }
        });

        b_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updatephonenumber();
            }
        });

    }


    private void sendVerificationCode(String phoneNumber)
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startTimer();
            }
        }, 0);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                otp_callBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            otp_callBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationOTP = s;
            tv_optinfo.setText("Enter OTP Recieved and hit Verify Button");
            b_verifyotp.setEnabled(true);

            b_verifyotp.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
             code = phoneAuthCredential.getSmsCode();
             verifyCode(code);
            edotp.setText(code);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
           // p_two.setVisibility(View.GONE);
            tv_optinfo.setText(e.getMessage());

            //b_changephone.setEnabled(true);
        }
    };

    private void verifyCode(String code)
    {



        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationOTP, code);
            signInWithCredentials(credential);
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    public void signInWithCredentials(PhoneAuthCredential credential)
    {
       // mAuth.getCurrentUser().linkWithCredential(credential)
         mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())         {
                           // p_two.setVisibility(View.GONE);
                            //tv_ptwo.setText("Phone number successfully linked");
                            //b_verifyotp.setBackgroundResource(R.drawable.button_back_ripple_round_green);
                            tv_optinfo.setText("Verified");
                            b_sendotp.setVisibility(View.GONE);
                            b_verifyotp.setVisibility(View.GONE);
                            Intent HomeActivity2 = new Intent(activity_otp.this, home.class);
                            HomeActivity2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            HomeActivity2.putExtra("isSigned","true");
                            HomeActivity2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(HomeActivity2);


                            //updatephonenumber();

                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                //tv_ptwo.setText(e.getMessage());
                                //p_two.setVisibility(View.GONE);
                            }

                            tv_optinfo.setText("Wrong OTP Entered!");
                        }
                       // b_changephone.setEnabled(true);
                    }
                });
    }

    public void startTimer()
    {
        tv_timer.setTextSize(40);
        b_sendotp.setEnabled(false);
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(String.valueOf(m_counter));
                m_counter--;
            }

            public void onFinish() {
                tv_timer.setText("Click button to resend OTP");
                tv_timer.setTextSize(16);
                b_sendotp.setEnabled(true);
              //  b_sendotp.setBackgroundResource(R.drawable.button_back_ripple_round_orange);
            }
        }.start();

    }

    public void updatephonenumber()
    {
        userMapphone.put("phone", phone_number);
        userUIDReference.updateChildren(userMapphone).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(activity_otp.this, "phone updated", Toast.LENGTH_SHORT).show();
                        //startActivity(Home)
                }
                else
                {
                    try{
                        throw task.getException();
                    }catch (Exception e)
                    {
                        Toast.makeText(activity_otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //phone number not registered in database updste ui
                        b_proceed.setEnabled(true);
                        b_proceed.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE","null");
        setResult(1,intent);
        finish();
    }
}


/*
<LinearLayout
        android:id="@+id/ll5"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv5"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv5"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb5"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv5"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>

    <LinearLayout
        android:id="@+id/ll1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginVertical="3dp"
        android:paddingHorizontal="12dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView">



        <androidx.cardview.widget.CardView
            android:id="@+id/cv1"
            android:layout_width="110dp"
            android:layout_height="140dp"
            android:layout_marginTop="12dp"
            android:elevation="4dp"
            app:cardCornerRadius="8dp"
            app:layout_constraintEnd_toStartOf="@+id/cv2"
            app:layout_constraintHorizontal_bias="0.5"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <ImageView
                    android:id="@+id/imv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.75"
                    android:background="@color/color_grey_light"></ImageView>

                <ProgressBar
                    android:id="@+id/pb1"
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="2dp"
                    android:progress="50" />

                <TextView
                    android:id="@+id/tv1"
                    android:layout_width="match_parent"
                    android:layout_height="0dp"
                    android:layout_weight="0.18"
                    android:padding="3dp"
                    android:text="Upload"
                    android:textAlignment="center" />

            </LinearLayout>

        </androidx.cardview.widget.CardView>


    </LinearLayout>


 */