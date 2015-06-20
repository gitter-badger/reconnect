package com.example.peter.reconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Peter on 16/06/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver  {

  private  String teste ;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);


        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

     }


 }
