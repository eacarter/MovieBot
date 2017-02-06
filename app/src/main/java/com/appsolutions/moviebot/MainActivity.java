package com.appsolutions.moviebot;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity implements AIListener {

    private AIService aiService;
    private TextView helloworld;
    String url = "http://www.omdbapi.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},1);

        helloworld = (TextView)findViewById(R.id.helloworld);

        final AIConfiguration config = new AIConfiguration("958658b0ebfb48bc9bb93107c4bc4900",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Now Listening...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //aiService.startListening();
//                JsonRequest("Skins", 2007);

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(url).build();

                MovieInterface movieInterface = restAdapter.create(MovieInterface.class);

                movieInterface.getFeed("Skins", "2007", new Callback<MovieModel>() {

                    @Override
                    public void success(MovieModel movieModel, retrofit.client.Response response) {
                        helloworld.setText(movieModel.getTitle());
                        Log.d("RetroFit", "success");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        helloworld.setText(error.toString());
                        Log.d("RetroFit", "failed");
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";

            }
        }

        // Show results in TextView.
        helloworld.setText("Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString);
    }

//    public void JsonRequest(String name, @Nullable int year){
//
//        String url = "http://www.omdbapi.com/?t="+name+"&y="+year+"&plot=short&r=json";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>(){
//
//            @Override
//            public void onResponse(JSONObject response) {
//                helloworld.setText(response.toString());
//                Log.d("blah", response.toString());
//            }
//        }, new Response.ErrorListener(){
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                helloworld.setText(volleyError.toString());
//            }
//        });
//
//        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }

    @Override
    public void onError(AIError error) {
        helloworld.setText(error.getMessage());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        
    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
