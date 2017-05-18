package com.appsolutions.moviebot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener, JsonInterface {

    EditText edittext, edittext2;
    TextView helloworld;
    Button button;
    AIService aiService;
    String title, year,TAG = "MainMovieBot" ;
    ArrayList<String> data;

    SharedPreferences sharedPreferences;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    sharedPreferences = getSharedPreferences("MOVIELIST", Context.MODE_PRIVATE);
                    Set<String> set = sharedPreferences.getStringSet("LIST", null);
                    if(set != null) {
                        data = new ArrayList<>(set);
                    }
                    MovieListFragment.newInstance(data);
                    return true;
                case R.id.navigation_notifications:
                    aiService.startListening();
                   return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);

        helloworld = (TextView)findViewById(R.id.error);
        edittext = (EditText) findViewById(R.id.helloedit);
        edittext2 = (EditText) findViewById(R.id.helloedit2);
        button = (Button) findViewById(R.id.hellobutton);

        final AIConfiguration config = new AIConfiguration("958658b0ebfb48bc9bb93107c4bc4900",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edittext.getText().toString();
                year = edittext2.getText().toString();

                try {
                    JsonRequest(title,year);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        if (result.getParameters() != null && !result.getParameters().isEmpty()) {

            title = result.getParameters().get("any").getAsString();

            if (result.getParameters().containsKey("number")) {
                year = result.getParameters().get("number").getAsString();

                try {
                    JsonRequest(title, year);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onError(AIError error) {
        helloworld.setText(error.getMessage().toString());
    }

    @Override
    public void JsonRequest(String name, String year) throws UnsupportedEncodingException{

        String url = "http://www.omdbapi.com/?t="+ URLEncoder.encode(name, "UTF-8")+"&y="+year+"&plot=short&r=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                Intent intent = new Intent(MainActivity.this, ResultTabsActivity.class);
                intent.putExtra("jsonObject", response.toString());
                startActivity(intent);

                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
                helloworld.setText(volleyError.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void JsonRequest(String name) throws UnsupportedEncodingException {
        String url = "http://www.omdbapi.com/?t="+URLEncoder.encode(name, "UTF-8")+"&y=&plot=short&r=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
                helloworld.setText(volleyError.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        Toast.makeText(this, "Now Listening", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListeningCanceled() {
        Toast.makeText(this, "Cancelled Listening", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListeningFinished() {
        Toast.makeText(this, "Stopped Listening", Toast.LENGTH_SHORT).show();
    }

}
