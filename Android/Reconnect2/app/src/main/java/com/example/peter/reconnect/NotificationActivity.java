package com.example.peter.reconnect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Peter on 18/06/15.
 */
public class NotificationActivity extends ActionBarActivity {
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    private Switch state, sons, vibrar, login, logout, wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupActionBar();
        settings = getPreferences(MODE_PRIVATE);
        editor = settings.edit();
        state = (Switch) findViewById(R.id.state);
        sons = (Switch) findViewById(R.id.sons);
        vibrar = (Switch) findViewById(R.id.vibra);
        login = (Switch) findViewById(R.id.login);
        logout = (Switch) findViewById(R.id.logout);
        wifi = (Switch) findViewById(R.id.wifi);

        state.setChecked(settings.getBoolean("state", true));
        sons.setChecked(settings.getBoolean("sons", true));
        vibrar.setChecked(settings.getBoolean("vibra", true));
        login.setChecked(settings.getBoolean("login", true));
        logout.setChecked(settings.getBoolean("logout", true));
        wifi.setChecked(settings.getBoolean("wifi", true));

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("state", isChecked);
                editor.apply();
            }
        });
        sons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("sons", isChecked);
                editor.apply();
            }
        });
        vibrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("vibra", isChecked);
                editor.apply();
            }
        });
        login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("login", isChecked);
                editor.apply();
            }
        });
        logout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("logout", isChecked);
                editor.apply();
            }
        });
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("wifi", isChecked);
                editor.apply();
            }
        });
    }

    /**
     * This method will setup the top title bar (Action bar) content and display
     * values. It will also setup the custom background theme for ActionBar. You
     * can override this method to change the behavior of ActionBar for
     * particular Activity
     */
    protected void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
