package com.appsolutions.moviebot;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;

/**
 * Created by eric on 2/7/17.
 */
public interface JsonInterface {

    void JsonRequest(String name, String year) throws UnsupportedEncodingException;

    void JsonRequest(String name) throws UnsupportedEncodingException;


}
