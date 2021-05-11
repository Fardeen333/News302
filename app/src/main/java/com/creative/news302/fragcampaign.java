package com.creative.news302;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creative.news302.utility.sharedData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class fragcampaign extends Fragment {

EditText ed;
Button add;
    com.creative.news302.utility.sharedData sharedData;
    Context ctx;
    public fragcampaign(Context baseContext) {
        // Required empty public constructor
        ctx = baseContext;
        sharedData = new sharedData(ctx);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragcampaign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed = view.findViewById(R.id.editText);
        add = view.findViewById(R.id.buttonadd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ed.getText().toString())){
                    Toast.makeText(getActivity(), "Please paste link", Toast.LENGTH_SHORT).show();
                return;
                }

                String n = ed.getText().toString();
               n =  getYoutubeVideoId(n);
                ed.setText(getYoutubeVideoId(n));
                sharedData.updateList(n);
            }
        });
    }

    public static String getYoutubeVideoId(String youtubeUrl)
    {
        String video_id="";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
        {

            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }
}
