package com.example.peter.reconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Peter on 13/06/15.
 */
public class Configuration extends ActionBarActivity {
    private Button saveProfile;
     private EditText editUserName,editUserPassword;
    private CheckBox checkBoxAgree;
    private SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loaderPreferences();

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                finish();

            }
        });

    }

    private void savePreferences() {
        //Salva as novas preferencias  que o usuario inserio na configuracao do Perfil
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String nameInput = editUserName.getText().toString();
        String passwordInput = editUserPassword.getText().toString();
        boolean checkBoxInput = checkBoxAgree.isChecked();

        editor.putString(getString(R.string.key_user_name), nameInput);
        editor.putString(getString(R.string.key_user_password),passwordInput);
        editor.putBoolean(getString(R.string.key_user_termo),checkBoxInput);
        editor.commit();
    }

    private void loaderPreferences() {
        //Carrega as preferencias gravadas
        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        saveProfile = (Button) findViewById(R.id.button_back);
        editUserName = (EditText) findViewById(R.id.editUser);
        editUserPassword =(EditText) findViewById(R.id.editPassword);
        checkBoxAgree = (CheckBox) findViewById(R.id.checkBoxTermo);


        editUserName.setText(sharedPreferences.getString(getString(R.string.key_user_name), ""));
        editUserPassword.setText(sharedPreferences.getString(getString(R.string.key_user_password),""));
        checkBoxAgree.setChecked(sharedPreferences.getBoolean(getString(R.string.key_user_termo),false));
    }


}
