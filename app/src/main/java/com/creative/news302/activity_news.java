package com.creative.news302;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.creative.news302.adapters.news_adapter;
import com.creative.news302.firebase.Firebase_retrievSearchedfeed;
import com.creative.news302.firebase.Firebase_retrievcategorisedfeed;
import com.creative.news302.firebase.Firebase_retrievmixfeed;
import com.creative.news302.models.model_news;
import com.creative.news302.utility.touchlstener;
import com.creative.news302.viewpagers.verticalviewpager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class activity_news extends AppCompatActivity{

    ArrayList<model_news> model_newsx = new ArrayList<>();

    private verticalviewpager news_viewPager;
    private DatabaseReference db;
    private LinearLayout linearLayout;
    private com.creative.news302.adapters.news_adapter news_adapter;
    private Firebase_retrievcategorisedfeed firebase_retrievcategorisedfeed;
    private Firebase_retrievSearchedfeed firebase_retrievSearchedfeed;
    int i = 0;
    int counter = 0;
    int lastsize = 0;
    String category,search;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        category = getIntent().getExtras().getString("category");
        search = getIntent().getExtras().getString("search");
        db = FirebaseDatabase.getInstance().getReference();
        //Toast.makeText(this, search+" "+category, Toast.LENGTH_SHORT).show();




        news_viewPager = findViewById(R.id.viewPagerNewscat);

        if(Objects.equals(search, "no")) {
           // Toast.makeText(this, "noope", Toast.LENGTH_SHORT).show();
            firebase_retrievcategorisedfeed = new Firebase_retrievcategorisedfeed(db, this, news_adapter, news_viewPager, model_newsx,category);
        }else{
           // Toast.makeText(this, "yessssss", Toast.LENGTH_SHORT).show();
            firebase_retrievSearchedfeed = new Firebase_retrievSearchedfeed(db, this, news_adapter, news_viewPager, model_newsx,search);
        }
        news_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Toast.makeText(activity_news.this, position + " " + model_newsx.size() + " " + lastsize, Toast.LENGTH_SHORT).show();
                if (model_newsx.size() - position == 1) {
                    if (model_newsx.size() - 1 == lastsize) {
                        Toast.makeText(activity_news.this, "end of list", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    lastsize = model_newsx.size();
                    //loadmore
                    if(Objects.equals(search, "no")) {
                        firebase_retrievcategorisedfeed.loadmore();
                    }else{
                        firebase_retrievSearchedfeed.loadmore();
                    }

                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        news_viewPager.setOnTouchListener(new touchlstener(this) {
            public void onSwipeTop() {
                if (i < model_newsx.size() - 1) {
                    news_viewPager.setCurrentItem(++i);
                    //Toast.makeText(activity_news.this, "top " + i, Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(activity_news.this, "end of list", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() {
                if (i != 0) {
                    news_viewPager.setCurrentItem(--i);
                    //Toast.makeText(activity_news.this, "bottom " + i, Toast.LENGTH_SHORT).show();
                }
            }


        });


    }


}
