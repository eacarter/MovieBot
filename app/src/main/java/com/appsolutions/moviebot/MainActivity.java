package com.appsolutions.moviebot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener, JsonInterface {

    private AIService aiService;
    private AIApplication AiApp;
    private String TAG = "Main";
    private TextView helloworld;
    private TextView edittext;
    private TextView edittext2;
    private String title;
    private String year;

    private final Handler handler = new Handler();
    private Runnable pauseCallback = new Runnable() {
        @Override
        public void run() {
            AiApp.onActivityPaused();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AiApp = (AIApplication)getApplication();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);

        helloworld = (TextView)findViewById(R.id.helloworld);
        edittext = (TextView)findViewById(R.id.helloedit);
        edittext2 = (TextView)findViewById(R.id.helloedit2);

        final AIConfiguration config = new AIConfiguration("9ae22d6dd3c54144b162b195045bd263",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Now Listening...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
               // aiService.startListening();

                title = edittext.getText().toString();
                year = edittext2.getText().toString();

                try {
                    JsonRequest(title,year);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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

//            try {
//                JsonRequest(title);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onError(AIError error) {
        helloworld.setText(error.getMessage());
    }

    @Override
    public void JsonRequest(String name, String year) throws UnsupportedEncodingException{

        String url = "http://www.omdbapi.com/?apikey=5df1c91c&t="+ URLEncoder.encode(name, "UTF-8")+"&y="+year+"&plot=short&r=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
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
        String url = "http://www.omdbapi.com/?apikey=5df1c91c&t="+URLEncoder.encode(name, "UTF-8")+"&y=&plot=short&r=json";

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
    protected void onResume(){
        super.onResume();
        AiApp.onActivityResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        handler.postDelayed(pauseCallback, 500);
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        Log.d(TAG, "Now listening....");
    }

    @Override
    public void onListeningCanceled() {
        Log.d(TAG, "Listening Cancelled");
    }

    @Override
    public void onListeningFinished() {
        Log.d(TAG, "Stopped Listening");
    }
}
