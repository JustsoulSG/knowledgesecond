package com.knowledgesecond.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zsg95 on 2016/11/15.
 */

public class PreUtils {

    public static void putStringToDefault(Context context,String key,String value){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(key,value).commit();
    }

    public static String getStringFromDefault(Context context,String key,String defValue){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key,defValue);
    }


}
