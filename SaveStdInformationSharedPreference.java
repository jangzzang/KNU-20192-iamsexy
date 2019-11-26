package com.example.continuousliving;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveStdInformationSharedPreference {
    static final String PREF_USER_NAME = "username";
    static final String PREF_STD_ID = "StdID";
    static final String PREF_Std_NAME = "StdName";
    static final String PREF_Std_NUMBER = "StdNumber";
    static final String PREF_Std_STATUS = "StdStatus";
    static final String PREF_Std_PASSWORD = "StdPassword";
    static final String PREF_PUSH_STATUS = "PushStatus";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장 (로그인에서만 활용)
    public static void setUserName(Context ctx, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, id);
        editor.commit();
    }
    // 저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    // 학생정보 저장 (메인에서 활용)
    public static void setStd(Context ctx, String id, String password, String stdNum, String stdName, int status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STD_ID, id);
        editor.putString(PREF_Std_PASSWORD, password);
        editor.putString(PREF_Std_NAME, stdName);
        editor.putString(PREF_Std_NUMBER, stdNum);
        editor.putInt(PREF_Std_STATUS, status);
        editor.commit();
    }

    public static void setPrefPushStatus(Context ctx, boolean push_status){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_PUSH_STATUS, push_status);
        editor.commit();
    }

    public static boolean getPushStatus(Context ctx){
        return getSharedPreferences(ctx).getBoolean(PREF_PUSH_STATUS,true);
    }

    //id 불러오기
    public static String getStdID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_STD_ID, "");
    }
    //학번 불러오기
    public static String getStdNum(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_Std_NUMBER, "");
    }//학생명 불러오기

    public static String getStdName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_Std_NAME, "");
    }//상태(권한) 불러오기

    public static int getStdStatus(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_Std_STATUS, 1);
    }

    public static String getStdPassword(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_Std_PASSWORD, "");
    }

    // 로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
