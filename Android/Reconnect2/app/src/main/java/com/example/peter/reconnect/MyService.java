package com.example.peter.reconnect;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

/**
 * Created by Peter on 20/06/15.
 */
public class MyService extends Service {
    private static final String TAG = "HelloService";

    private boolean isRunning = false;

    @Override
    public Context createDisplayContext(Display display) {
        return super.createDisplayContext(display);
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "service create", Toast.LENGTH_SHORT).show();

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {


                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);

                    } catch (Exception e) {
                    }

                    if (isRunning) {
                        Log.i(TAG, "Service running");
                    }
                }

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Toast.makeText(this, "service onBind", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Toast.makeText(this, "service onDestroy", Toast.LENGTH_SHORT).show();
    }
}
