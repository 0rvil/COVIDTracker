package com.project.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView confirmedCases, deathCases, recoveredCases, mysavedCountry, mySavedCountryCases;
    EditText searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fetchData(View view) {
        searchbar = findViewById(R.id.search_bar);
        confirmedCases = findViewById(R.id.confirmed_cases);
        deathCases = findViewById(R.id.death_cases);
        recoveredCases = findViewById(R.id.recovered_cases);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.covid19api.com/summary";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Create a new JSON Object out of the response
                            JSONObject responseObject = new JSONObject(response);

                            // Get the countries array from responseObject
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");

                            // Iterate through countries array and find the country indicated by the user and display total cases, total recovered, and total deaths accordingly

                            for (int i = 0; i < countriesArray.length(); i++) {
                                JSONObject currentCountry = countriesArray.getJSONObject(i);
                                String currentCountryString = currentCountry.getString("Country").toLowerCase();
                                String currentCountryAbriviation = currentCountry.getString("CountryCode").toLowerCase();

                                String countrySearched = searchbar.getText().toString().toLowerCase();

                                if (currentCountryString.equals(countrySearched) || currentCountryAbriviation.equals(countrySearched)) {
                                    confirmedCases.setText(currentCountry.getString("TotalConfirmed"));
                                    recoveredCases.setText(currentCountry.getString("TotalRecovered"));
                                    deathCases.setText(currentCountry.getString("TotalDeaths"));
                                }

                            }
                        } catch (Exception e) {
                            confirmedCases.setText("Country not found");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                confirmedCases.setText("Error 404");
            }
        });

        queue.add(stringRequest);

    }

    public void saveData(View view) {
        EditText searchbar = findViewById(R.id.search_bar);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.project.covidtracker", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Countries", searchbar.getText().toString().toLowerCase());
        editor.apply();
        Toast.makeText(this, "Country Saved", Toast.LENGTH_SHORT).show();

    }

    public void mySavedCountries(View view) {
        mysavedCountry = findViewById(R.id.saved_country_name);
        mySavedCountryCases = findViewById(R.id.saved_country_cases);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.project.covidtracker", MODE_PRIVATE);
        String text = sharedPreferences.getString("Countries", "");
        mysavedCountry.setText(text);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.covid19api.com/summary";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Create a new JSON Object out of the response
                            JSONObject responseObject = new JSONObject(response);

                            // Get the countries array from responseObject
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");

                            // Iterate through countries array and find the country indicated by the user and display total cases, total recovered, and total deaths accordingly

                            for (int i = 0; i < countriesArray.length(); i++) {
                                JSONObject currentCountry = countriesArray.getJSONObject(i);
                                String currentCountryString = currentCountry.getString("Country").toLowerCase();
                                String currentCountryAbriviation = currentCountry.getString("CountryCode").toLowerCase();
                                if (currentCountryString.equals(mysavedCountry.getText()) || currentCountryAbriviation.equals(mysavedCountry.getText())) {
                                    mySavedCountryCases.setText(currentCountry.getString("TotalConfirmed"));
                                }
                            }
                        } catch (Exception e) {
                            mysavedCountry.setText("Country not found");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mysavedCountry.setText("Error 404");
            }
        });
        queue.add(stringRequest);

    }

}

