package com.project.covidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class SavedCountriesAdapter extends ArrayAdapter<String> {
    public SavedCountriesAdapter(Context context, String[] savedCountriesArray) {
        super(context, 0, savedCountriesArray);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        final String currentCountryName = getItem(position);
        TextView savedCountryNameTextView = convertView.findViewById(R.id.saved_country_name);
        savedCountryNameTextView.setText(currentCountryName);
        final TextView savedCountryConfirmedTextView = convertView.findViewById(R.id.saved_country_name);

        /*Make an API call to get live data for each saved country*/

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://api.covid19api.com/summary";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");
                            for(int i = 0; i < countriesArray.length(); i++) {
                                JSONObject currentCountry = countriesArray.getJSONObject(i);
                                String currentCountryString = currentCountry.getString("Country").toLowerCase();


                                //check if the current country in the loop is the same as this list item
                                if(currentCountryString.equals(currentCountryName)) {
                                    //set the text of the TextView to be the # of confirmed cases
                                    savedCountryConfirmedTextView.setText(currentCountry.getString("TotalConfirmed"));

                                    //You can set text on other TextViews below here if you have more Views and data you want to show
                                    //e.g. savedCountryRecoveredTextView.setText(currentCountry.getString("TotalRecovered"));

                                }
                            }
                        } catch (Exception e) {
                            savedCountryConfirmedTextView.setText("Country not found");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                savedCountryConfirmedTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        return convertView;
    }

}


