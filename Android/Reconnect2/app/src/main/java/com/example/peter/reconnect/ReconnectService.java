package com.example.peter.reconnect;

import android.app.Activity;
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
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Peter on 20/06/15.
 */
public class ReconnectService extends Service {
    private static final String TAG = "ReconnectService";
    private ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
    ;

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

                criarNotificacao(emailUser);
                // showNotification();

            } catch (Exception e) {
                Log.e(getPackageName(), e.getMessage(), e);
            }
        }

        private void criarNotificacao(String usuario) {

            int icone = R.drawable.ic_launcher;
            String titulo = getString(R.string.titulo);
            long data = System.currentTimeMillis();
            String aviso = usuario + " " + getString(R.string.aviso);

            Context context = getApplicationContext();
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(MainActivity.BOTAO, true);


            Notification notification = new Notification(icone, aviso, data);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND;


            // This pending intent will open after notification click
            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFY_ME_ID, intent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            notification.setLatestEventInfo(context, titulo,
                    aviso, pendingIntent);
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(ns);
            notificationManager.notify(NOTIFY_ME_ID, notification);
        }

        private void showNotification() {
            // In this sample, we'll use the same text for the ticker and the expanded notification
            CharSequence text = getText(R.string.titulo);

            // Set the icon, scrolling text and timestamp
            Notification notification = new Notification(R.drawable.stat_sample, text,
                    System.currentTimeMillis());
            Context context = getApplicationContext();
            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(ReconnectService.this, MainActivity.class), 0);

            // Set the info for the views that show in the notification panel.
            notification.setLatestEventInfo(ReconnectService.this, getText(R.string.remote_service_label),
                    text, contentIntent);


            // Send the notification.
            // We use a string id because it is a unique number.  We use it later to cancel.
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNM =
                    (NotificationManager) getSystemService(ns);
            mNM.notify(NOTIFY_ME_ID, notification);
        }
    }
}