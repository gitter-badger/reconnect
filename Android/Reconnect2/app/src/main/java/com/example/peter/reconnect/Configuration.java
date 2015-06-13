package com.example.peter.reconnect;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Peter on 13/06/15.
 */
public class Configuration extends ActionBarActivity {
Button backHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
         backHome = (Button) findViewById(R.id.button_back);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();


            }
        });
    }
}
