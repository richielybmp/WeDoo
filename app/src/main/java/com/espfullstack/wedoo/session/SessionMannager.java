package com.espfullstack.wedoo.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionMannager {
    public static final String FOLDER = "SESSION";
    private static final String PROPERTY_TOKEN = "TOKEN";

    public static void saveToken(String inputToken, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                FOLDER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROPERTY_TOKEN, inputToken);
        editor.apply();
    }


    public static String getToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                FOLDER, Context.MODE_PRIVATE);
        String token = preferences.getString(PROPERTY_TOKEN,"");
        return token;
    }

    public static void clearToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                FOLDER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PROPERTY_TOKEN);
        editor.clear();
        editor.commit();
    }
}
