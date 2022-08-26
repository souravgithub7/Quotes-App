package com.google.firebase.udacity.quotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    public static String BASE_URL="https://quotes15.p.rapidapi.com/quotes/random/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=findViewById(R.id.button);
        searchView=(SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this,"Fetching Quotes on "+query,Toast.LENGTH_LONG).show();
                String Url=BASE_URL+"?tags="+query;
                callapi(Url);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        callapi(BASE_URL);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callapi(BASE_URL);
            }
        });

    }

    private void callapi(String BASE_URL) {
        String url=BASE_URL;
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String uri = Uri.parse(url).buildUpon().build().toString();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = (JSONObject) new JSONTokener(response).nextValue();


                    String Fact = (String) json.get("content");
                    TextView view = findViewById(R.id.textview);
                    view.setText(Fact);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("X-RapidAPI-Host", "quotes15.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "902a2b984amsh84a33ff0a94925fp15e8edjsn18892fa69ded");   //changed key
                return params;
            }
        };
        requestQueue.add(stringRequest);
        }
    }