package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RESULT:";
    //Declaring an Spinner
    String name;
    List<String> wilayaNames = new ArrayList<>();
    ArrayList<String> communesList = new ArrayList<>();
    ArrayAdapter<String> communeAdapter;
    JSONArray jsonArray;
    JSONObject obj;
    String selectedState;
    //TextViews to display details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Initialize the spinners and their adapters
        Spinner wilayaSpinner=findViewById(R.id.spinner);
        Spinner communeSpinner=findViewById(R.id.spinner2);
        final TextView textView = (TextView) findViewById(R.id.textView);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://gist.githubusercontent.com/ayoubbentameur/06e623397d2dd224c9add9cd92ad0ffd/raw/169975b451ce6f43a4e23190b01b866e0d6c5cb3/testJson.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response

                             jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            JSONArray jsonArrayFr = jsonObject.getJSONArray("fr");

                            for (int i = 0; i < jsonArrayFr.length(); i++) {
                                obj = jsonArrayFr.getJSONObject(i);
                                int id=obj.getInt("id");
                                name = obj.getString("name");
                                Log.d("JSON", "Name:" + name);
                                wilayaNames.add(name);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error",e.getMessage());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, wilayaNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Finally, set the adapter to the spinner
                        wilayaSpinner.setAdapter(adapter);



// Define the adapter for the commune spinner





                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error

            }
        });

// Add the request to the RequestQueue.


        wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the user's selection here
                 selectedState = wilayaSpinner.getSelectedItem().toString();
                Toast.makeText(MainActivity.this, selectedState, Toast.LENGTH_SHORT).show();
               // JSONArray family = (JSONArray) jsonObject.get("family");

                JSONObject selectedStateObject = null;
               for (int k = 0; k < jsonArray.length(); k++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(k);
                        String stateName = jsonObject.getString("name");
                        if (stateName.equals(selectedState)) {
                            selectedStateObject = jsonObject;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Retrieve the list of communes for the selected state

                try {

                        JSONArray communesArray = obj.getJSONArray("communes");
                        Toast.makeText(MainActivity.this, communesArray.toString(), Toast.LENGTH_SHORT).show();
                        for (int j = 0;j < communesArray.length(); j++) {
                            String communeName = communesArray.getString(j);
                            communesList.add(communeName);
                            Log.d("JSON", "commune: " + communeName);

                            communeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, communesList);
                            communeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            communeSpinner.setAdapter(communeAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Update the adapter of the commune spinner
                    communeAdapter.notifyDataSetChanged();

            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Parse the JSON data and extract the required information


                    JSONArray jsonArray = response.getJSONObject(0).getJSONArray("fr");
                    JSONObject jsonObject = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("name").equals(selectedState)) {
                            break;
                        }
                    }
                    JSONArray communesArray = jsonObject.getJSONArray("communes");
                    for (int j = 0; j < communesArray.length(); j++) {
                        String commune = communesArray.getString(j).toString();
                       // String result = response.toString();
                        Log.d("Commune", commune);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

// Add the request to the RequestQueue.
        queue.add(request);

        queue.add(stringRequest);



    }
}


