package com.creative.news302.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creative.news302.R;
import com.creative.news302.models.model_news;
import com.creative.news302.utility.Dialouge;
import com.creative.news302.utility.sharedData;
import com.creative.news302.utility.touchlstener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class news_adapter extends PagerAdapter {

    sharedData sharedData;
    Context ctx;
    LayoutInflater layoutInflater;
    ImageView imv;
    TextView tvTitle, tvDescription, tvBrief, tvTimestamp;
    ArrayList<model_news> array_model_newsx;
    ProgressBar pbimg;
    Dialouge dialouge;

    public news_adapter(Context ctx, ArrayList<model_news> array_model_newsx) {
        this.ctx = ctx;
        this.array_model_newsx = array_model_newsx;
        sharedData = new sharedData(ctx);
    }


    @Override
    public int getCount() {
        return array_model_newsx.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.model_news, container, false);


        imv = view.findViewById(R.id.imvNews);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvBrief = view.findViewById(R.id.tvBrief);
        tvTimestamp = view.findViewById(R.id.tvTimestamp);


        if (Objects.equals(sharedData.retrieveLanguage(), "Eng")) {
            tvTitle.setText(array_model_newsx.get(position).getTitle().trim());
            tvDescription.setText(array_model_newsx.get(position).getDescription().trim());
            tvBrief.setText(array_model_newsx.get(position).getBrief().trim());
        }
        else if (Objects.equals(sharedData.retrieveLanguage(), "Hin")) {
            tvTitle.setText(array_model_newsx.get(position).getTitleH().trim());
            tvDescription.setText(array_model_newsx.get(position).getDescriptionH().trim());
            tvBrief.setText(array_model_newsx.get(position).getBriefH().trim());
        }
        Long timestamp = (Long) array_model_newsx.get(position).getTimestampCreatedLong();


        Date d = new Date(timestamp);
        //Time t = new Time(timestamp);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        //System.out.println(simpleDateFormat);
        CharSequence c = android.text.format.DateFormat.format("E " +
                "-dd/MM/yyyy hh.mm aa", d);
        tvTimestamp.setText(c);
        Picasso.get().load(array_model_newsx.get(position).getImgurl()).into(imv, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("picasso","done");
            }

            @Override
            public void onError(Exception e) {

                Log.d("picasso","not done");

            }
        });

//tvBrief.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        if (Objects.equals(sharedData.retrieveLanguage(), "Eng")) {
//            //dialouge = new Dialouge(ctx);
//            //dialouge.showFullArticle(ctx,array_model_newsx.get(position).getDescription());
//        }
//        else if (Objects.equals(sharedData.retrieveLanguage(), "Hin")) {
//          //  dialouge = new Dialouge(ctx);
//            //dialouge.showFullArticle(ctx,array_model_newsx.get(position).getDescriptionH());
//        }
//    }
//});
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
