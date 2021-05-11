package com.creative.news302;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.adapters.news_adapter;
import com.creative.news302.firebase.Firebase_retrievmixfeed;
import com.creative.news302.models.model_news;
import com.creative.news302.utility.touchlstener;
import com.creative.news302.viewpagers.verticalviewpager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class fragment_home extends Fragment {

    ArrayList<model_news> model_newsx = new ArrayList<>();

    private verticalviewpager news_viewPager;
    private DatabaseReference db;
    private LinearLayout linearLayout;
    private news_adapter news_adapter;
    private Firebase_retrievmixfeed firebase_retrievmixfeed;
    int i = 0;
    int counter = 0;
    ViewPager viewPager;
    int lastsize = 0;

    public fragment_home(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public fragment_home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseDatabase.getInstance().getReference();

        news_viewPager = view.findViewById(R.id.viewPagerNews);
        firebase_retrievmixfeed = new Firebase_retrievmixfeed(db, getContext(), news_adapter, news_viewPager, model_newsx);
        // news_viewPager.setAdapter(news_adapter);
        news_viewPager.addOnPageChangeListener(mviewpagelitsener);
        // news_viewPager.setPageTransformer(true,new DepthPageTransformer());

        news_viewPager.setOnTouchListener(new touchlstener(getContext()) {
            public void onSwipeTop() {
                if(i<model_newsx.size()-1) {
                    news_viewPager.setCurrentItem(++i);
                   // Toast.makeText(getActivity(), "top "+i, Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getContext(), "end of list", Toast.LENGTH_SHORT).show();

                }
            }


            public void onSwipeBottom() {
                if(i!=0){
                news_viewPager.setCurrentItem(--i);
                //Toast.makeText(getActivity(), "bottom "+i, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeRight() {
            viewPager.setCurrentItem(0,true);
            }
        });


    }


    ViewPager.OnPageChangeListener mviewpagelitsener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
           // Toast.makeText(getContext(), position + " " + model_newsx.size() + " " + lastsize, Toast.LENGTH_SHORT).show();
            if (model_newsx.size() - position == 1) {
                if (model_newsx.size() - 1 == lastsize) {
                    Toast.makeText(getContext(), "end of list", Toast.LENGTH_SHORT).show();
                    return;
                }
                lastsize = model_newsx.size();
                //loadmore
                firebase_retrievmixfeed.loadmore();
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

}
