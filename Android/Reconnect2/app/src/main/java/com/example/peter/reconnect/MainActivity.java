package com.example.peter.reconnect;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity {
    // private Button  button_configuration;
    private TextView internet_message;
    private ToggleButton buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internet_message = (TextView) findViewById(R.id.text_is_connected);
        buttonStart = (ToggleButton) findViewById(R.id.buttonStart);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        isConnected(activeNetwork != null && activeNetwork.isConnectedOrConnecting());


        ToggleButton toggle = (ToggleButton) findViewById(R.id.buttonStart);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Toast.makeText(MainActivity.this, "" + isChecked, Toast.LENGTH_LONG).show();
                if (isChecked) {
                    teste("Reconnect", "login automatico ligado");
                    startService(new Intent(getBaseContext(), MyService.class));
                }
                if (!isChecked) {
                    teste("Reconnect", "Autologin desactivado");
                    stopService(new Intent(getBaseContext(), MyService.class));
                }
            }
        });

    }

    private void teste(String title, String text) {

        //Set default notification sound

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                        .setContentTitle(title)
                        .setContentText(text).setDefaults(Notification.DEFAULT_VIBRATE).setDefaults(Notification.DEFAULT_SOUND);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

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
    protected void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

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

}
