package com.creative.news302;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.creative.news302.utility.sharedData;

import java.util.Objects;

public class settings extends AppCompatActivity {

    Switch LSwitch, TSwitch;
    sharedData sharedData;
    String theme;
    String language;
    Button bApply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = new sharedData(this);

        if (Objects.equals(sharedData.retrieveTheme(), "Day")) {
            theme = "Day";
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (Objects.equals(sharedData.retrieveTheme(), "Night")) {
            theme = "Night";
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if (Objects.equals(sharedData.retrieveLanguage(), "Eng")) {
            language = "Eng";
        } else if (Objects.equals(sharedData.retrieveTheme(), "Hin")) {
            language = "Hin";

        }

        setContentView(R.layout.activity_settings);

        bApply = findViewById(R.id.bapply);
        LSwitch = findViewById(R.id.switchLanguage);
        TSwitch = findViewById(R.id.switchTheme);



        if (Objects.equals(theme, "Night")) {
            TSwitch.setChecked(true);
            TSwitch.setText("On");
        }
        else{
            TSwitch.setChecked(false);
            TSwitch.setText("Off");
        }
        if (Objects.equals(language, "Eng")) {
            LSwitch.setChecked(false);
            LSwitch.setText("Off");
        }
        else{
            LSwitch.setChecked(true);
            LSwitch.setText("On");
        }
        TSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    recreate();
//                    setContentView(R.layout.activity_settings);

                    sharedData.updateTheme("Night");
                    Intent a = new Intent(settings.this, settings.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                    finish();

                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    setContentView(R.layout.activity_settings);
                    sharedData.updateTheme("Day");
                    Intent a = new Intent(settings.this, settings.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(a);
                    finish();


                }
            }
        });

        LSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    sharedData.updateLanguage("Hin");
                    LSwitch.setText("On");

                } else {
                    sharedData.updateLanguage("Eng");
                    LSwitch.setText("Off");


                }
            }
        });

        bApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ax = new Intent(settings.this, home.class);
                ax.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ax);
                finish();
            }
        });
    }
}
