package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

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
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://gist.githubusercontent.com/ayoubbentameur/06e623397d2dd224c9add9cd92ad0ffd/raw/169975b451ce6f43a4e23190b01b866e0d6c5cb3/testJson.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Parse the JSON response

                         jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray jsonArrayFr = jsonObject.getJSONArray("fr");

                        for (int i = 0; i < jsonArrayFr.length(); i++) {
                            obj = jsonArrayFr.getJSONObject(i);
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





                }, error -> {
                    // Handle error

                });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the user's selection here

                 selectedState = wilayaSpinner.getSelectedItem().toString();
                Toast.makeText(MainActivity.this, selectedState, Toast.LENGTH_SHORT).show();
               // JSONArray family = (JSONArray) jsonObject.get("family");
                JsonArrayRequest request;

                 request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
                     try {
                         // Parse the JSON data and extract the required information

                         if (communeAdapter!=null){
                             communeAdapter.clear();
                         }
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
                             String commune = communesArray.getString(j);
                             // String result = response.toString();
                             communesList.add(commune);

                             Log.d("Commune List", commune);
                           //  Toast.makeText(MainActivity.this, commune, Toast.LENGTH_SHORT).show();
                         }


                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     communeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, communesList);
                     communeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                     communeSpinner.setAdapter(communeAdapter);
                     communeAdapter.notifyDataSetChanged();
                 }, Throwable::printStackTrace);

                queue.add(request);





            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });





    }
}

/* communeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, communesList);
                            communeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            communeSpinner.setAdapter(communeAdapter);
  // Update the adapter of the commune spinner
                    communeAdapter.notifyDataSetChanged();

 */