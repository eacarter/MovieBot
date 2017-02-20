package com.appsolutions.moviebot;

import android.content.Context;

import java.io.UnsupportedEncodingException;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by eric on 2/20/17.
 */

public class AiApi implements AIListener, MainPresenter.View{

    MainPresenter mainPresenter;
    AIService aiService;

    public AiApi (Context context, MainPresenter.View view){
        final AIConfiguration config = new AIConfiguration("958658b0ebfb48bc9bb93107c4bc4900",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(context, config);
        aiService.setListener(this);
        mainPresenter = new MainPresenter(view);
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        if (result.getParameters() != null && !result.getParameters().isEmpty()) {

            if (result.getParameters().containsKey("number")) {

                try {
                    mainPresenter.JsonRequest(result.getParameters().get("any").getAsString(),
                            result.getParameters().get("number").getAsString(), null);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    @Override
    public void updateViewText(String data) {

    }

    @Override
    public void updateViewImage() {

    }
}
