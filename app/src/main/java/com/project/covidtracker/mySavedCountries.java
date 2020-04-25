package com.project.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class mySavedCountries extends AppCompatActivity {
    ListView lv;
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.project.covidtracker",MODE_PRIVATE);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(sharedPreferences);
        setContentView(R.layout.activity_my_saved_countries);
        String text = sharedPreferences.getString("Countries","");



    }
}
