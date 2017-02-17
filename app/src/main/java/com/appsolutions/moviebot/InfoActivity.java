package com.appsolutions.moviebot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class InfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, JsonInterface {

    TextView title;
    TextView plot;
    TextView navTitle;
    TextView navYear;
    TextView trailer;

    View headerView;

    String url;

    JSONObject jOb;

    private NetworkImageView poster;
    private NetworkImageView navPoster;
    private ImageLoader mImageLoader;

    private static String TAG = "InfoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (TextView)findViewById(R.id.title);
        poster = (NetworkImageView)findViewById(R.id.poster);
        plot = (TextView)findViewById(R.id.plot);
        trailer = (TextView)findViewById(R.id.trailerText);

        Intent intent = getIntent();
        String jString = intent.getStringExtra("jsonObject");

        try {
            jOb = new JSONObject(jString);
            setTitle(jOb.getString("Title"));
            title.setText("Title: " +jOb.getString("Title") + "\n" +
                    "Year: " + jOb.getString("Year") + "\n" +
                    "Rated: " + jOb.getString("Rated") + "\n" +
                    "Released: " + jOb.getString("Released") + "\n" +
                    "Runtime: " + jOb.getString("Runtime") + "\n" +
                    "Genre: " + jOb.getString("Genre") + "\n" +
                    "Director(s): " + jOb.getString("Director") + "\n" +
                    "Actor(s): " + jOb.getString("Actors"));
            plot.setText(jOb.getString("Plot"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        trailer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)  {
                try {
                    JsonRequest(jOb.getString("Title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saving to watch list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navTitle = (TextView)headerView.findViewById(R.id.navtitle);
        navYear = (TextView)headerView.findViewById(R.id.navyear);
        navPoster = (NetworkImageView)headerView.findViewById(R.id.Navposter);

        try {
            navTitle.setText(jOb.getString("Title"));
            navYear.setText(jOb.getString("Year"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        getPoster(poster);
        getPoster(navPoster);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPoster(NetworkImageView image){
        mImageLoader = VolleyImageRequest.getinstance(this.getApplicationContext()).getImageLoader();
        try {
            mImageLoader.get(jOb.getString("Poster"), ImageLoader.getImageListener(image,
                    R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            image.setImageUrl(jOb.getString("Poster"), mImageLoader);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void JsonRequest(String name, String year) throws UnsupportedEncodingException {

    }

    @Override
    public void JsonRequest(String name) throws UnsupportedEncodingException {
        url = "http://trailersapi.com/trailers.json?movie="+ URLEncoder.encode(name, "UTF-8");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, url.toString());
            }

        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
               // helloworld.setText(volleyError.toString());
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

}
