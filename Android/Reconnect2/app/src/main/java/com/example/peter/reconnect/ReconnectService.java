package com.example.peter.reconnect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Display;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Intent.*;

/**
 * Created by Peter on 20/06/15.
 */
public class ReconnectService extends Service {
    private static final String TAG = "LogininjaService";
    private ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);

    @Override
    public Context createDisplayContext(Display display) {
        return super.createDisplayContext(display);

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service Create");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Starting...");


        long delayInicial = 0;
        long periodo = 3;
        TimeUnit unit = TimeUnit.MINUTES;

        poolExecutor.scheduleAtFixedRate(new NotificationTask(), delayInicial, periodo, unit);


        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy");
        poolExecutor.shutdown();
        poolExecutor.shutdownNow();
        stopSelf();
    }

    private boolean estaConectado() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        return info.isConnected();
    }

    private class NotificationTask implements Runnable {

        private static final int NOTIFY_ME_ID = 1337;
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String emailUser = sharedPref.getString(getString(R.string.key_user_name), "");
        String passwordUser = sharedPref.getString(getString(R.string.key_user_password), "");
        boolean agreeUser = sharedPref.getBoolean(getString(R.string.key_user_agree), false);

        @Override
        public void run() {
            if (!estaConectado()) {
                return;
            }
            try {
                RequestHttp http = new RequestHttp();
                http.clientHTTP(emailUser, passwordUser, agreeUser);

                buildNotification("Logininja", "Logado com sucesso: " + emailUser);

            } catch (Exception e) {
                Log.e(getPackageName(), e.getMessage(), e);
            }
        }

        private void buildNotification(String title, String text) {

            boolean sound;
            boolean vibrate;
            boolean whiteLight;
            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            sound = sharedPreferences.getBoolean("sons", true);
            vibrate = sharedPreferences.getBoolean("vibra", true);
            whiteLight = sharedPreferences.getBoolean("whiteLight", true);

            //Set default notification sound
            Context context = getApplicationContext();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher).setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true);

            if (sound) {
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            }
            if (vibrate) {
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            }
            if (whiteLight) {
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
            }


            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MainActivity.class);


            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ME_ID, resultIntent,
                    FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(NOTIFY_ME_ID, mBuilder.build());
        }

    }
}