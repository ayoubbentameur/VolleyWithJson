package com.example.myapplication;

import static com.android.volley.Request.Method.*;

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
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
     /*  JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(GET, url, null, new Response.Listener<JSONArray>() {

           @Override
           public void onResponse(JSONArray response) {

                    for (int i = 0; i < response.length(); i++) {
                        // Get the JSONObject at the current index
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response.getJSONObject(i);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Get the value of the "name" key
                        String name = null;
                        try {
                            name = jsonObject.getString("name");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Do something with the name value
                        Log.d(TAG, "Name: " + name);
                    }



           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e(TAG, "Error: " + error.getMessage());

           }
       });
        queue.add(jsonArrayRequest);*/
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
                                name = obj.getString("name");
                                Log.d("JSON", "Name: " + name);
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
                String selectedState = wilayaSpinner.getSelectedItem().toString();
                Toast.makeText(MainActivity.this, selectedState, Toast.LENGTH_SHORT).show();

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

        queue.add(stringRequest);
    }
}

// Request a string response from the provided URL.
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);*/


       /* JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parsing json object response
                            // response will be a JSONObject instance
                            String name = response.getString("name");
                            int age = response.getInt("userId");
                            JSONArray hobbies = response.getJSONArray("communes");

                            // Do something with the parsed data
                            Log.d(TAG, "Name: " + name);
                            Log.d(TAG, "Age: " + age);
                            for (int i = 0; i < hobbies.length(); i++) {
                                textView.setText(hobbies.getString(i));

                                Log.d(TAG, "Hobby " + i + ": " + hobbies.getString(i));
                            }
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurs
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });
            queue.add(jsonObjectRequest);*/

