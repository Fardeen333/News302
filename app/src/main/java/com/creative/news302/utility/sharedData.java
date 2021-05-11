package com.creative.news302.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Objects;

public class sharedData {

    public static final String mySharedpref = "com.creative.news302.shared";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public sharedData(){

    }

    public sharedData(Context c){
        pref = c.getSharedPreferences(mySharedpref, 0); // 0 - for private mode
        editor = pref.edit();
    }




    public void updateLanguage(String Language){
        editor.putString("Language", Language);
        editor.apply();

    }

    public void updateTheme(String Theme){
        editor.putString("Theme", Theme);
        editor.apply();
    }

    public void updateList(String list){
        String id2 = pref.getString("list", null);
//        if (id2 == null) {
            editor.putString("list", list);
//        }else{
//            id2=id2+","+list;
//            editor.putString("list", id2);
//
//        }
        editor.apply();
    }

    public String retrieveList(){
        String id2 = pref.getString("list", null);
        if (id2 == null) {
            return "empty";
        }else {
        return id2;
        }
    }


    public String retrieveTheme(){
        String id2 = pref.getString("Theme", null);
        if (id2 == null) {
            return "Day";
        } else if(Objects.equals(id2, "Night")){
            return "Night";

        } else if(Objects.equals(id2,"Day")){
            return "Day";

        }
        else return "Day";

    }
    public String retrieveLanguage(){
        String id2 = pref.getString("Language", null);
        if (id2 == null) {
            return "Eng";
        } else if(Objects.equals(id2, "Eng")){
            return "Eng";

        } else if(Objects.equals(id2,"Hin")){
            return "Hin";
        }
        else return "Eng";
    }
}
