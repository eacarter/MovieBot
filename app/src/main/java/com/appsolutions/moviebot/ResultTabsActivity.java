package com.appsolutions.moviebot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ResultTabsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    JSONObject jOb;
    MovieData movie;
    ArrayList<String>MovieInfo;
    ArrayList<String> filmNames;
    SharedPreferences sharedPreferences;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tabs);

        Intent intent = getIntent();
        String jString = intent.getStringExtra("jsonObject");

        movie = new MovieData();
        MovieInfo = new ArrayList<>();

        try {
            jOb = new JSONObject(jString);

            MovieInfo.add(jOb.getString("Title"));
            MovieInfo.add(jOb.getString("Year"));
            MovieInfo.add(jOb.getString("Rated"));
            MovieInfo.add(jOb.getString("Released"));
            MovieInfo.add(jOb.getString("Runtime"));
            MovieInfo.add(jOb.getString("Genre"));
            MovieInfo.add(jOb.getString("Director"));
            MovieInfo.add(jOb.getString("Actors"));
            MovieInfo.add(jOb.getString("Plot"));
            MovieInfo.add(jOb.getString("imdbRating"));

            movie.setTitle(jOb.getString("Title"));
            movie.setYear(jOb.getString("Year"));
            movie.setRating(jOb.getString("Rated"));
            movie.setReleased(jOb.getString("Released"));
            movie.setRuntime(jOb.getString("Runtime"));
            movie.setGenre(jOb.getString("Genre"));
            movie.setDirector(jOb.getString("Director"));
            movie.setActors(jOb.getString("Actors"));
            movie.setPlot(jOb.getString("Plot"));
            movie.setPoster(jOb.getString("Poster"));
            movie.setAwards(jOb.getString("Awards"));
            movie.setMetaScore(jOb.getString("MetaScore"));
            movie.setImdbRating(jOb.getString("imdbRating"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        filmNames = new ArrayList<>();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filmNames.add(movie.getTitle());

                storeList(filmNames);

                Snackbar.make(view, "Film added to list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void storeList (ArrayList<String> title){
        sharedPreferences = this.getSharedPreferences("MOVIELIST", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.contains("LIST")){
            Set<String> set = sharedPreferences.getStringSet("LIST", null);
            set.addAll(title);
            editor.putStringSet("LIST", set);
            editor.commit();
        }
        else {
            Set list = new HashSet();
            list.addAll(title);
            editor.putStringSet("LIST", list);
            editor.commit();
        }
    }

    public ArrayList<String> retrieveList(){
        Set<String> set = sharedPreferences.getStringSet("LIST", null);

        if(set != null) {
             data = new ArrayList<>(set);
        }
        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_tabs, menu);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return MovieInfoFragment.newInstance(MovieInfo);
                case 1:
                    return MoviePosterFragment.newInstance(movie.getPoster());
                case 2:
                    return MovieListFragment.newInstance(retrieveList());
                default:
                    return null;
            }
        }

        /*
http://api-public.guidebox.com/v2/search?api_key=YOUR_API_KEY&type=movie&field=title&query=Terminator


         */

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    try {
                        return jOb.getString("Title");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case 1:
                    try {
                        return jOb.getString("Title") +" Poster";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                case 2:
                    return "Availability";
            }
            return null;
        }
    }
}
