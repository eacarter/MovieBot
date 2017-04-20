package com.appsolutions.moviebot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;

public class MoviePosterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    ImageLoader mImageLoader;

    private String mParam1;

    public MoviePosterFragment() {

    }

    public static MoviePosterFragment newInstance(String param1) {
        MoviePosterFragment fragment = new MoviePosterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_poster, container, false);

        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.fragPoster);

        getPoster(imageView);

        return view;
    }

    private NetworkImageView getPoster(NetworkImageView image){
        mImageLoader = VolleyImageRequest.getinstance(getContext()).getImageLoader();
            mImageLoader.get(mParam1, ImageLoader.getImageListener(image,
                    R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            image.setImageUrl(mParam1, mImageLoader);

        return image;
    }
}
