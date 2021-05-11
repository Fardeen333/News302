package com.creative.news302;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.news302.utility.sharedData;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;


public class fragmain extends Fragment  {

    private YouTubePlayer YPlayer;
Button play;
CountDownTimer timer;
long milliLeft = 60000;
TextView t;
int i = 0;
String x = "c3teJwKIEOo";
boolean played = false;
    com.creative.news302.utility.sharedData sharedData;
    Context ctx;

Button b;
YouTubePlayer.OnInitializedListener onInitializedListener;
YouTubePlayer.PlaybackEventListener playbackEventListener;

private static final String TAG = "xMainActivity";
    public fragmain(Context baseContext) {
        // Required empty public constructor
        ctx = baseContext;
        sharedData = new sharedData(ctx);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmain, container, false);
        Log.d("aaaaa","staf");

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_fragment,youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(getString(R.string.apppi), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("aaaaa","s");

                if (!b) {
                    Log.d("aaaaa","sf");

                    YPlayer = youTubePlayer;
                    YPlayer.setPlaybackEventListener(playbackEventListener);
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    //YPlayer.setFullscreen(true);
//                    YPlayer.loadVideo("c3teJwKIEOo");
//                    YPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                  Log.d("aaaaa","f");

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //youTubePlayerView = view.findViewById(R.id.view);
        play = view.findViewById(R.id.button);
        b = view.findViewById(R.id.buttonaddcoins);
        t = view.findViewById(R.id.cc);
        YouTubePlayerFragment youtubePlayerFragment = new YouTubePlayerFragment();
        youtubePlayerFragment.initialize(getString(R.string.apppi), onInitializedListener);

       play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String c = sharedData.retrieveList();
               if(c!=null) x = c;
               //x = sharedData.retrieveList();
               Toast.makeText(ctx, "playing: "+x, Toast.LENGTH_SHORT).show();
               plaay(x);
           }
       });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==10){
                    Toast.makeText(getActivity(), "added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "please watch more to add", Toast.LENGTH_SHORT).show();

                }
            }
        });
        playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                Log.d("xxx","playing");
                if(!played){
                timerStart(milliLeft);
                played= true;
                }
                else
                    timerResume(milliLeft);
            }

            @Override
            public void onPaused() {
                Log.d("xxx","pause");
                timerPause();

            }

            @Override
            public void onStopped() {
                Log.d("xxx","stop");
                timerPause();

            }

            @Override
            public void onBuffering(boolean b) {
                Log.d("xxx","buffer");
                timerPause();

            }

            @Override
            public void onSeekTo(int i) {
                Log.d("xxx","seek");
                timerPause();

            }
        };









    }

    public void timerStart(long timeLengthMilli) {
            timer = new CountDownTimer(timeLengthMilli,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    milliLeft = millisUntilFinished;
                    i++;
                    if(i<=10){
                        t.setText("Coins Collected: "+String.valueOf(i));
                        played = false;

                    }
                    if(i==10){
                        Toast.makeText(getActivity(),"Video watched successfully",Toast.LENGTH_LONG).show();

                    }
                    Log.i("Secccc", milliLeft+" "+i);


                }

                @Override
                public void onFinish() {
                    played = false;
                    Toast.makeText(getActivity(),"Video watched successfully",Toast.LENGTH_LONG).show();
                }
            };

timer.start();
    }

    public void timerPause(){
        if(timer!=null)
        timer.cancel();
    }

    private void timerResume(long milliLeft) {
        Log.i("Sec", milliLeft+" ");

        timerStart(milliLeft);
    }

    public void plaay(String x){
        if(YPlayer==null)
            return;


        i=0;
        milliLeft=60000;
        played=false;

        YPlayer.loadVideo(x);
      //  YPlayer.play();YPlayer.loadVideo("c3teJwKIEOo");
        YPlayer.play();
    }

}
