package com.example.peter.reconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    // private Button  button_configuration;
    private TextView internet_message;

    @Override
    protected void onStart() {
        super.onStart();
        internet_message = (TextView) findViewById(R.id.text_is_connected);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

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
        internet_message = (TextView) findViewById(R.id.text_is_connected);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            internet_message.setText(getString(R.string.internet_online));
        }
        if (!isConnected) {
            internet_message.setText(getString(R.string.internet_offline));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internet_message = (TextView) findViewById(R.id.text_is_connected);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            internet_message.setText(getString(R.string.internet_online));
        }
        if (!isConnected) {
            internet_message.setText(getString(R.string.internet_offline));
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent configIntent = new Intent(MainActivity.this, Configuration.class);
            startActivity(configIntent);
            return true;
        }
        if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(MainActivity.this, AboutApp.class);
            startActivity(aboutIntent);
            return true;
        }
        if (id == R.id.action_feedback) {

            Intent sendFeeback = new Intent(MainActivity.this, SendMail.class);
            startActivity(sendFeeback);
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
}
