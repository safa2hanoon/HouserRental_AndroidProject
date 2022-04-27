package edu.berziet.houserental.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "HouseRentalSharedPrefs";
    private static final int SHARED_PREF_PRIVATE = Context.MODE_PRIVATE;
    private static SharedPrefManager ourInstance = null;
    private static SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private final String COUNTRY_ID_PREF_KEY = "countryId";
    private final String COUNTRY_NAME_PREF_KEY = "countryName";
    private final String CITY_ID_PREF_KEY = "cityId";
    private final String CITY_NAME_PREF_KEY = "cityName";
    private final String USER_ROLE_PREF_KEY = "role";
    private final String USER_ID_PREF_KEY = "userId";
    private final String USER_EMAIL_PREF_KEY = "email";
    private final String NAME_PREF_KEY = "name";
    private final String REMEMBER_EMAIL_KEY = "remember_email";


    public static SharedPrefManager getInstance(Context context) {
        if (ourInstance != null) {
            return ourInstance;
        }
        ourInstance=new SharedPrefManager(context);
        return ourInstance;
    }

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,
                SHARED_PREF_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean setRememberEmail(Boolean remember) {
        editor.putBoolean(REMEMBER_EMAIL_KEY,remember );
        return editor.commit();
    }
    public Boolean getRememberEmail() {
        return sharedPreferences.getBoolean(REMEMBER_EMAIL_KEY, false);
    }

    public boolean setName(String name) {
        editor.putString(NAME_PREF_KEY, name);
        return editor.commit();
    }
    public String getName() {
        return sharedPreferences.getString(NAME_PREF_KEY, "");
    }


    public boolean setEmail(String email) {
        editor.putString(USER_EMAIL_PREF_KEY, email);
        return editor.commit();
    }
    public String getEmail() {
        return sharedPreferences.getString(USER_EMAIL_PREF_KEY, null);
    }

    public boolean setRole(String role) {
        editor.putString(USER_ROLE_PREF_KEY, role);
        return editor.commit();
    }
    public String getRole() {
        return sharedPreferences.getString(USER_ROLE_PREF_KEY, null);
    }

    public boolean setCityName(String cityName) {
        editor.putString(CITY_NAME_PREF_KEY, cityName);
        return editor.commit();
    }
    public String getCityName() {
        return sharedPreferences.getString(CITY_NAME_PREF_KEY, null);
    }

    public boolean setCountryName(String countryName) {
        editor.putString(COUNTRY_NAME_PREF_KEY, countryName);
        return editor.commit();
    }
    public String getCountryName() {
        return sharedPreferences.getString(COUNTRY_NAME_PREF_KEY, null);
    }


    public boolean setCityId(int cityId){
        editor.putInt(CITY_ID_PREF_KEY, cityId);
        return editor.commit();
    }
    public int getCityId(){
        return sharedPreferences.getInt(CITY_ID_PREF_KEY, 0);
    }

    public boolean setUserId(String userId) {
        editor.putString(USER_ID_PREF_KEY, userId);
        return editor.commit();
    }
    public String getUserId() {
        return sharedPreferences.getString(USER_ID_PREF_KEY, null);
    }
    public boolean setCountryId(int countryId){
        editor.putInt(COUNTRY_ID_PREF_KEY, countryId);
        return editor.commit();
    }
    public int getCountryId(){
        return sharedPreferences.getInt(COUNTRY_ID_PREF_KEY, 0);
    }
}
