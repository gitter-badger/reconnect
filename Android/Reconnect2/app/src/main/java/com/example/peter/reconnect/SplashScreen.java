package com.example.peter.reconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by Peter on 13/06/15.
 */
public class SplashScreen extends Activity {

    private static final int SPLASH_SHOW_TIME = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new BackgroundSplashTask().execute();
    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    public class BackgroundSplashTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {

            // I have just given a sleep for this thread
            // if you want to load database, make
            // network calls, load images
            // you can do them here and remove the following
            // sleep

            // do not worry about this Thread.sleep
            // this is an async task, it will not disrupt the UI
            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Intent i = new Intent(SplashScreen.this,
                    MainActivity.class);
            // any info loaded can during splash_show
            // can be passed to main activity using
            // below
            i.putExtra("loaded_info", " ");
            startActivity(i);
            finish();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
