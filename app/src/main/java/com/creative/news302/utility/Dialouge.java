package com.creative.news302.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.R;
import com.squareup.picasso.Picasso;

public class Dialouge extends Dialog {

    Dialouge m_dialouge;
    Context m_context;
    Button b_retry,b_update;
    ImageView imageView;
    String imageLink;
    TextView tv;
    prerequisites prerequisites;


    public Dialouge(Context ctx) {
        super(ctx);
        this.m_context = ctx;
    }


    public void showPopUp(final Context ctx) {
        prerequisites = new prerequisites();
        m_dialouge = new Dialouge(ctx);
        m_dialouge.setContentView(R.layout.dialouge_no_network);
        b_retry = m_dialouge.findViewById(R.id.b_retrynetwork);
        b_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerequisites.checknetwork(ctx)) {
                    m_dialouge.dismiss();

                } else {
                    Toast.makeText(ctx, "No Network!", Toast.LENGTH_SHORT).show();
                    m_dialouge.dismiss();
                }

            }
        });
        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_dialouge.setCanceledOnTouchOutside(true);
        m_dialouge.setCancelable(true);
        m_dialouge.show();

    }

    public void showUpdate(final Context ctx) {
        prerequisites = new prerequisites();
        m_dialouge = new Dialouge(ctx);
        m_dialouge.setContentView(R.layout.dialouge_update);
        b_update = m_dialouge.findViewById(R.id.b_update);
        b_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prerequisites.checknetwork(ctx)) {
                   // m_dialouge.dismiss();


                } else {
                    //ZToast.makeText(ctx, "No Network!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_dialouge.setCanceledOnTouchOutside(true);
        m_dialouge.setCancelable(true);
        m_dialouge.show();

    }


    public void showImage(final Context ctx, String imageLink) {
        m_dialouge = new Dialouge(ctx);
        m_dialouge.setContentView(R.layout.dialouge_image);
        imageView = m_dialouge.findViewById(R.id.imvdialouge);
        Picasso.get().load(imageLink).into(imageView);
        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_dialouge.show();

    }

    public void showFullArticle(final Context ctx, String news) {
        m_dialouge = new Dialouge(ctx);
        m_dialouge.setContentView(R.layout.dialouge_full_article);
        tv = m_dialouge.findViewById(R.id.tvArticle);
        tv.setText(news);
        m_dialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_dialouge.show();

    }
}
