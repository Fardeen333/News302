package com.creative.news302.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.creative.news302.adapters.news_adapter;
import com.creative.news302.admin.addData;
import com.creative.news302.models.model_news;
import com.creative.news302.utility.touchlstener;
import com.creative.news302.viewpagers.verticalviewpager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class Firebase_retrievmixfeed {

    DatabaseReference db;
    model_news model_newsx;
    ArrayList<model_news> arrayList_news;
    Context c;
    news_adapter news_adapterx;
    verticalviewpager news_viewPager;
    Long timestamp;
    long lastkey;

    public Firebase_retrievmixfeed(DatabaseReference db, Context context, news_adapter news_adapterx,verticalviewpager news_viewPager,ArrayList<model_news> model_newsx) {
        this.db = db;
        this.c = context;
        this.news_adapterx = news_adapterx;
        this.news_viewPager = news_viewPager;
        this.arrayList_news = model_newsx;
        this.retrieveNews();
    }


    public void retrieveNews()
    {

        Log.d("FirebaseHelper","start retrieved");
        final Query q=db.child("news");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i =1;
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    // if(dataSnapshot.getChildrenCount()<5)return;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_newsx = ds.getValue(model_news.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));
                        arrayList_news.add(model_newsx);


                        lastkey =  (model_newsx.getCount());
                        timestamp = (Long) model_newsx.getTimestampCreatedLong();
                        Log.d("Timestamp", String.valueOf(model_newsx.getTimestampCreatedLong()));

                        Date d = new Date(timestamp);
                        Time t = new Time(timestamp);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                        System.out.println(simpleDateFormat);
                        CharSequence c = android.text.format.DateFormat.format("E", d);
                        Log.d("day"+ ++i, String.valueOf(c));
                    }
                    news_adapterx = new news_adapter(c,arrayList_news);

                    // news_viewPager.setPageTransformer(true,new DepthPageTransformer());
                    news_viewPager.setOffscreenPageLimit(2);
                    news_viewPager.setDurationScroll(250);
                    news_viewPager.setAdapter(news_adapterx);
                    q.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                q.removeEventListener(this);

            }
        };
        q.orderByChild("count").limitToFirst(5).addValueEventListener(eventListener);

    }


    public void loadmore() {
        final Query q=db.child("news");
        ValueEventListener mvalueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i =1;
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    arrayList_news.remove(arrayList_news.size()-1);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        model_newsx = ds.getValue(model_news.class);
                        Log.d("FirebaseHelper", String.valueOf(dataSnapshot.getChildrenCount()));

                        arrayList_news.add(model_newsx);
                        lastkey =  (model_newsx.getCount());

                        Long timestamp = (Long) model_newsx.getTimestampCreatedLong();
                        Log.d("mCount", String.valueOf(model_newsx.getCount()));

                        Date d = new Date(timestamp);
                        Time t = new Time(timestamp);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                        System.out.println(simpleDateFormat);
                        CharSequence c = android.text.format.DateFormat.format("E", d);
                        Log.d("dayMore"+ ++i, String.valueOf(c));
                    }
                    news_adapterx .notifyDataSetChanged();
                }
                q.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                q.removeEventListener(this);
            }
        };

        q.orderByChild("count").startAt(lastkey).limitToFirst(5).addValueEventListener(mvalueEventListener);
    }
}