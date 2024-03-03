package com.example.hw2.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hw2.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class SharedPreferencesManager { //singleton
    private static volatile SharedPreferencesManager instance;
    private final int MAX_RECORDS = 10;
    private final String KEY_RECORDS = "Records";
    private static final String DB_FILE = "DB_FILE";
    private ArrayList<User> records;
    private transient SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        records = new ArrayList<>();
    }

    public static void init(Context context) {
        synchronized (SharedPreferencesManager.class) {
            if (instance == null)
                instance = new SharedPreferencesManager(context);
        }
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public SharedPreferencesManager setRecords(ArrayList<User> records) {
        this.records = records;
        return  instance;
    }


    public ArrayList<User> getRecords(){
        return this.records;
    }

    public String getString(final String key, final String value) {
        return sharedPreferences.getString(key, value);
    }

    public void putString(final String key, final String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }
    public ArrayList<User> getRecordsFromDB(){
        String json = getInstance().getString(KEY_RECORDS,"");
        if(json.isEmpty())
            return new ArrayList<User>();
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        setRecords(new Gson().fromJson(json,type));
        return getRecords();
    }

    public void saveRecordToDB(User record){
        ArrayList<User> records = getRecordsFromDB();
        records.add(record);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(records);
        instance.putString(KEY_RECORDS,jsonStr);
    }
    public ArrayList<User> getTopTen(){
        ArrayList<User> records = getRecordsFromDB();
        Collections.sort(records);
        ArrayList<User> topTen = new ArrayList<>();
        for (int i = 0; i < MAX_RECORDS && i<records.size(); i++)
            topTen.add(records.get(i));
        return topTen;
    }
}
