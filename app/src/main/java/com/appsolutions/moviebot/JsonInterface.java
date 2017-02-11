package com.appsolutions.moviebot;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;

/**
 * Created by eric on 2/7/17.
 */
public interface JsonInterface {

    void JsonRequest(String name, String year, Context context) throws UnsupportedEncodingException;

    void JsonRequest(String name, Context context) throws UnsupportedEncodingException;


}
