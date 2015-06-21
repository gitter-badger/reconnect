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
import android.util.Log;
import android.view.Display;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Peter on 20/06/15.
 */
public class ReconnectService extends Service {
    private static final String TAG = "ReconnectService";
    private ScheduledThreadPoolExecutor poolExecutor;

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

        poolExecutor = new ScheduledThreadPoolExecutor(1);
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

            } catch (Exception e) {
                Log.e(getPackageName(), e.getMessage(), e);
            }
        }

        private void criarNotificacao(String usuario) {

            int icone = R.drawable.ic_launcher;
            String aviso = getString(R.string.titulo);
            long data = System.currentTimeMillis();
            String titulo = usuario + " " + getString(R.string.aviso);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = new Notification(icone, aviso, data);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND;


            // This pending intent will open after notification click
            PendingIntent i = PendingIntent.getActivity(ReconnectService.this, 0,
                    new Intent(ReconnectService.this, MainActivity.class),
                    0);
            notification.setLatestEventInfo(ReconnectService.this, aviso,
                    titulo, i);
            mNotificationManager.notify(NOTIFY_ME_ID, notification);
        }
    }
}
