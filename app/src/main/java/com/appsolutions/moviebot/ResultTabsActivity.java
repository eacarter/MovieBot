package com.appsolutions.moviebot;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ResultTabsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    JSONObject jOb;
    String info1;
    String info2;
    String movieposter;
    ArrayList<String> filmNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tabs);

        Intent intent = getIntent();
        String jString = intent.getStringExtra("jsonObject");

        try {
            jOb = new JSONObject(jString);
           info1 =  "Title: " +jOb.getString("Title") + "\n" +
                    "Year: " + jOb.getString("Year") + "\n" +
                    "Rated: " + jOb.getString("Rated") + "\n" +
                    "Released: " + jOb.getString("Released") + "\n" +
                    "Runtime: " + jOb.getString("Runtime") + "\n" +
                    "Genre: " + jOb.getString("Genre") + "\n" +
                    "Director(s): " + jOb.getString("Director") + "\n" +
                    "Actor(s): " + jOb.getString("Actors");
           info2 = jOb.getString("Plot");

            movieposter = jOb.getString("Poster");
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

                filmNames.add(info1);

                Snackbar.make(view, "Film added to list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
                    return MovieInfoFragment.newInstance(info1, info2);
                case 1:
                    return MoviePosterFragment.newInstance(movieposter);
                case 2:
                    return MovieListFragment.newInstance(filmNames);
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
