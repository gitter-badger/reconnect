package com.example.peter.reconnect;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends ActionBarActivity {
    // private Button  button_configuration;
    private TextView internet_message, nameSSID;
    private ToggleButton buttonStart;
    private String userName, userPass;
    private boolean userAgree;
    private static final int NOTIFY_ME_ID = 1337;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        Context context = getApplicationContext();
        internet_message = (TextView) findViewById(R.id.text_is_connected);
        buttonStart = (ToggleButton) findViewById(R.id.buttonStart);

        nameSSID = (TextView) findViewById(R.id.textNameSSID);
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected(activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        nameSSID.setText(getCurrentSsid(context));
        loaderPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), ReconnectService.class));
        Log.i("Logininja Main Activity", "onDestroy, exit app...");

    }

    @Override
    protected void onStart() {
        super.onStart();

        final boolean stateOff;
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        stateOff = sharedPreferences.getBoolean("state", true);

        buttonStart = (ToggleButton) findViewById(R.id.buttonStart);
        buttonStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("MainActivity", "" + isChecked);
                if (isChecked) {
                    if (userName.length() <= 0 && userPass.length() <= 0) {
                        buildDialog();
                        buttonStart.setChecked(false);
                    } else {
                        buildNotification("Logininja", "Iniciando Login");
                        startService(new Intent(getBaseContext(), ReconnectService.class));
                    }
                } else if (!isChecked) {
                    if (stateOff) {
                        buildNotification("Logininja", "Reconnect desligado");
                    }
                    stopService(new Intent(getBaseContext(), ReconnectService.class));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_account:
                Intent configIntent = new Intent(MainActivity.this, ConfigurationActivity.class);
                startActivity(configIntent);
                return true;
            case R.id.action_about:
                Intent aboutIntent = new Intent(MainActivity.this, AboutAppActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.action_feedback:
                Intent sendFeedback = new Intent(MainActivity.this, SendEmailActivity.class);
                startActivity(sendFeedback);
                return true;
            case R.id.action_notifications:
                Intent notificationsIntent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(notificationsIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //configurando o Back-Key:
    //Funcionalidade do Back Key
    // Do ponto de vista técnico, o botão voltar manipula a pilha de Activities do aplicativo.
    // Pressionando o Back Key você finaliza a Activity atual e a remove da pilha. Se o aplicativo tiver apenas uma Activity
    // ou a Activity atual é a única na pilha (o usuário fechou todos as outras) o botão voltar vai fechar o aplicativo.


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair?");
        builder.setMessage("Deseja realmente sair?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void isConnected(boolean isConnected) {
        if (isConnected) {
            internet_message.setText(getString(R.string.internet_online));
        }
        if (!isConnected) {
            internet_message.setText(getString(R.string.internet_offline));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaderPreferences();
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
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
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
        Intent resultIntent = new Intent(this, MainActivity.class);

        //resultIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent,
                FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFY_ME_ID, mBuilder.build());
    }

    private void loaderPreferences() {
        //Carrega as preferencias gravadas
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(getString(R.string.key_user_name), "");
        userPass = sharedPreferences.getString(getString(R.string.key_user_password), "");
        userAgree = sharedPreferences.getBoolean(getString(R.string.key_user_agree), false);
    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opa!");
        builder.setMessage("Para poder iniciar a aplicação precisa  configurar sua conta de login, quer configurar agora?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                Intent configIntent = new Intent(MainActivity.this, ConfigurationActivity.class);
                startActivity(configIntent);
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
