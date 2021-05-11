package com.creative.news302;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class fragment_Dashboard extends Fragment implements View.OnClickListener {

    Intent HomeActivity2,passwordresetactivity;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    CardView cvinter,cvnational,cvsports,cventertain,cvtech,cvpolitics,cveconomy,cveditorial,cvcurrentaffairs;
    AutoCompleteTextView edSearchBar;
    ImageView imageViewSearch;
    String category;


    public fragment_Dashboard() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewSearch = view.findViewById(R.id.imageViewSearch);
        edSearchBar = view.findViewById(R.id.edSearchBar);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shownews = new Intent(getContext(),activity_news.class);
                shownews.putExtra("category","no");
                shownews.putExtra("search",edSearchBar.getText().toString());
                Log.d("searching: ",edSearchBar.getText().toString());
                startActivity(shownews);
            }
        });



        cvinter = view.findViewById(R.id.cvinternational);
        cvnational = view.findViewById(R.id.cvnational);
        cvsports = view.findViewById(R.id.cvsports);
        cventertain = view.findViewById(R.id.cventertainment);
        cvtech = view.findViewById(R.id.cvtechnology);
        cvpolitics = view.findViewById(R.id.cvpolitics);
        cveconomy = view.findViewById(R.id.cveconomy);
        cveditorial = view.findViewById(R.id.cveditorial);
        cvcurrentaffairs = view.findViewById(R.id.cvcurrentaffairs);

        cvinter.setOnClickListener(this);
        cvnational.setOnClickListener(this);
        cvsports.setOnClickListener(this);
        cventertain.setOnClickListener(this);
        cvtech.setOnClickListener(this);
        cvpolitics.setOnClickListener(this);
        cveconomy.setOnClickListener(this);
        cveditorial.setOnClickListener(this);
        cvcurrentaffairs.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==cvinter.getId()){
            category = "International";
        }else if(v.getId()==cvnational.getId()){
            category = "National";
        }else if(v.getId()==cvsports.getId()){
            category = "Sports";
        }else if(v.getId()==cvpolitics.getId()){
            category = "Politics";
        }else if(v.getId()==cvtech.getId()){
            category = "Technology";
        }else if(v.getId()==cveconomy.getId()){
            category = "Economy";
        }else if(v.getId()==cveditorial.getId()){
            category = "Editorial";
        }else if(v.getId()==cventertain.getId()){
            category = "Entertainment";
        }else if(v.getId()==cvcurrentaffairs.getId()){
            category = "Current Affairs";
        }


        Intent shownews = new Intent(getContext(),activity_news.class);
       // Toast.makeText(getContext(), category, Toast.LENGTH_SHORT).show();
        shownews.putExtra("category",category);
        shownews.putExtra("search","no");
        startActivity(shownews);

    }
}
