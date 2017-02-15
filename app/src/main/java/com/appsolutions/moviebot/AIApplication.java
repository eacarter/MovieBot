package com.appsolutions.moviebot;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import ai.api.util.BluetoothController;


public class AIApplication extends Application {

    private int activeCount;
    private BluetoothControllerImplementation bluetoothController;

    @Override
    public void onCreate(){
        super.onCreate();
        bluetoothController = new BluetoothControllerImplementation(this);
    }

    public BluetoothController getBluetoothController(){
        return bluetoothController;
    }

    protected void onActivityResume(){
        if(activeCount++ == 0){
            bluetoothController.start();
        }
    }

    protected void onActivityPaused(){
        if(--activeCount == 0){
            bluetoothController.stop();
        }
    }

    private boolean isInForeground(){
        return activeCount > 0;
    }


    private class BluetoothControllerImplementation  extends BluetoothController {


        private String TAG = "Bluetooth";

        public BluetoothControllerImplementation(Context context) {
            super(context);
        }

        @Override
        public void onHeadsetDisconnected() {
            Log.d(TAG, "Bluetooth Disconnected");
        }

        @Override
        public void onHeadsetConnected() {
            Log.d(TAG, "Bluetooth Connected");

            if (isInForeground() && !bluetoothController.isOnHeadsetSco()) {
                bluetoothController.start();
            }
        }

        @Override
        public void onScoAudioDisconnected() {
            Log.d(TAG, "Bluetooth sco audio disconnected");

            bluetoothController.stop();

            if (isInForeground()) {
                bluetoothController.start();
            }
        }

        @Override
        public void onScoAudioConnected() {
            Log.d(TAG, "Bluetooth sco audio Connected");
        }
    }
}
