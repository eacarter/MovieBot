package com.appsolutions.moviebot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class MovieListFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private static final String ARG_PARAM1 = "param1";

    private ArrayList<String> mParam1;

    ListView listView;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(ArrayList<String> title) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, title );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);

        ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, mParam1);

        listView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
