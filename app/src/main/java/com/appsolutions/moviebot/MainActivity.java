package com.appsolutions.moviebot;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements MainPresenter.View{

    MainPresenter mainPresenter;
    AiApi aiApi;

    private TextView helloworld;
    private NetworkImageView poster;
    private TextView movie;
    private TextView year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainPresenter = new MainPresenter(this);
        aiApi = new AiApi(this,this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);

        helloworld = (TextView)findViewById(R.id.helloworld);
        poster = (NetworkImageView)findViewById(R.id.poster);
        movie = (TextView)findViewById(R.id.helloedit);
        year = (TextView)findViewById(R.id.helloedit2);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Now Listening...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                try {
                    mainPresenter.JsonRequest(movie.getText().toString(),year.getText().toString(),view);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

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
    public void updateViewText(String data) {
        helloworld.setText(data);
    }

    @Override
    public void updateViewImage() {
        mainPresenter.getPoster(poster,getApplicationContext());
    }
}
