package com.appsolutions.moviebot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by eric on 2/20/17.
 */

public class MainPresenter  {

    MovieModel movie;
    View MainPresenterView;
    ImageLoader mImageLoader;


    public MainPresenter (View view){
        this.MainPresenterView = view;
        movie = new MovieModel();
    }


    public void JsonRequest(String name, String year, android.view.View view) throws UnsupportedEncodingException {

        String url = "http://www.omdbapi.com/?t="+ URLEncoder.encode(name, "UTF-8")+"&y="+year+"&plot=short&r=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try {
                    movie.setTitle(response.getString("Title"));
                    movie.setYear(response.getString("Year"));
                    movie.setRated(response.getString("Rated"));
                    movie.setReleased(response.getString("Released"));
                    movie.setRuntime(response.getString("Runtime"));
                    movie.setGenre(response.getString("Genre"));
                    movie.setDirector(response.getString("Director"));
                    movie.setActor(response.getString("Actors"));
                    movie.setPlot(response.getString("Plot"));
                    movie.setPoster(response.getString("Poster"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MainPresenterView.updateViewText(movie.toString());
                MainPresenterView.updateViewImage();
                Log.d("MainPresenter", movie.getPoster());
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("MainPresenter", volleyError.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(view).addToRequestQueue(jsonObjectRequest);
    }

    public void getPoster(NetworkImageView image, Context context ){
        mImageLoader = VolleyImageRequest.getinstance(context.getApplicationContext()).getImageLoader();
            mImageLoader.get(movie.getPoster(), ImageLoader.getImageListener(image,
                    R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            image.setImageUrl(movie.getPoster() , mImageLoader);
    }

    public interface View{
        void updateViewText(String data);
        void updateViewImage();
    }

}
