package com.kalamcantre.cricket_project;

import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.disklrucache.DiskLruCache;

public class Sharedprefernececonfig {

   private SharedPreferences sharedPreferences;
    private Context context;

    public Sharedprefernececonfig(Context context){

        this.context=context;

        sharedPreferences=context.getSharedPreferences(context.getResources().getString(R.string.login_preference),Context.MODE_PRIVATE);


    }
    public void writeloginstatus(boolean status){


        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putBoolean(context.getResources().getString(R.string.login_status_preference),status);
        editor.commit();


    }

    public boolean readloginstatus(){

        boolean status=false;
        status=sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference),false);
        return status;
    }



}
