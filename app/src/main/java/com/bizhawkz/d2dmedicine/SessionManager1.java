package com.bizhawkz.d2dmedicine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Heena on 2/21/2017.
 */
public class SessionManager1 {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_RATING = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_IMAGE="image";

    public static final String KEY_CHEMEMAIL="mail";

    public static final String KEY_RATING="rating";

    // Constructor
    public SessionManager1(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String email, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_EMAIL, email);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);


        // commit changes
        editor.commit();
    }

    public void createChemistLogin(String mail) {
        editor.putString(KEY_CHEMEMAIL, mail);
        editor.commit();
    }



    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Mem.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user email id
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        user.put(KEY_CHEMEMAIL, pref.getString(KEY_CHEMEMAIL, null));

        user.put(KEY_IMAGE,pref.getString(KEY_IMAGE,null));


        return user;
    }


    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State


    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public  String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public void createprescription(String s) {
        editor.putString(KEY_IMAGE,s);
    }

    public void createRatingSession(String abcCheck) {

        editor.putBoolean(IS_RATING, true);

        // Storing name in pref
        editor.putString(KEY_CHEMEMAIL, abcCheck);


        editor.commit();
    }

    public void checkRegister() {
        if(!this.isRated()){
            // user is not logged in redirect him to Login Activity
        }

    }
    public boolean isRated(){
        return pref.getBoolean(IS_RATING, false);
    }
}
