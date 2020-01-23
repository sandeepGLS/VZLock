package com.gls.vz_lock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Santosh on 12-Jan-18.
 */

public class SP {
    public final String SP_NAME = "LOCK";
    public final String PASSWORD = "PASSWORD";
    public final String DEF_PASS = "00000";
    SharedPreferences sp;
    public SP(Context context){
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setPassword(String password){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PASSWORD,password);
        editor.commit();
    }

    public String getPassword(){
        return sp.getString(PASSWORD,DEF_PASS);
    }

    public String getMasterPassword(){
        Date date = new Date();
        SimpleDateFormat textFormat = new SimpleDateFormat("yyMMMEddD");
        String text = textFormat.format(date).toUpperCase();
        System.out.println(text);
        byte [] input = text.getBytes();
        byte [] result = new byte [input.length];
        for (int i = 0; i<input.length; i++){
            result[i] = (byte) (input[input.length-i-1]+1);
        }
        text = new String(result);
        System.out.println(text);
        text = text.charAt(text.length() - 1) + text.substring(0, text.length() - 1);
        Log.e("DATE", text);
        return text;
    }
}
