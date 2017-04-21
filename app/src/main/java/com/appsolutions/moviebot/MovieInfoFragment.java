package com.appsolutions.moviebot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieInfoFragment extends Fragment {

    private static final String ARG_ARRAY_PARAM = "filmArray";

    private ArrayList<String> mParam1;

    public MovieInfoFragment() {
        // Required empty public constructor
    }

    public static MovieInfoFragment newInstance(ArrayList<String> param1) {
        MovieInfoFragment fragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_ARRAY_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_ARRAY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_info, container, false);

        TextView textView1 = (TextView) view.findViewById(R.id.movieTitle);
        TextView textView2 = (TextView) view.findViewById(R.id.movieYear);
        TextView textView3 = (TextView) view.findViewById(R.id.movieRating);
        TextView textView4 = (TextView) view.findViewById(R.id.movieReleased);
        TextView textView5 = (TextView) view.findViewById(R.id.movieRuntime);
        TextView textView6 = (TextView) view.findViewById(R.id.movieGenre);
        TextView textView7 = (TextView) view.findViewById(R.id.movieDirector);
        TextView textView8 = (TextView) view.findViewById(R.id.movieActors);
        TextView textView9 = (TextView) view.findViewById(R.id.moviePlot);
        TextView textView10 = (TextView) view.findViewById(R.id.movieImdbRating);

        textView1.setText("Title: " +mParam1.get(0));
        textView2.setText("Year: " +mParam1.get(1));
        textView3.setText("Rated: " +mParam1.get(2));
        textView4.setText("Release Date: : " +mParam1.get(3));
        textView5.setText("Runtime: " +mParam1.get(4));
        textView6.setText("Genre: " +mParam1.get(5));
        textView7.setText("Director: " +mParam1.get(6));
        textView8.setText("Actor(s): " +mParam1.get(7));
        textView9.setText("Plot: " +mParam1.get(8));
        textView10.setText("Imdb Rating: " +mParam1.get(9));

        return view;
    }


}
