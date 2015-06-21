package com.example.peter.reconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Peter on 13/06/15.
 */
public class ConfigurationActivity extends ActionBarActivity {
    private ImageButton saveProfile;
    private EditText editUserName, editUserPassword;
    private CheckBox checkBoxAgree;
    private SharedPreferences sharedPreferences;
    private ValidationEditText validation;
    boolean passValid = false;
    boolean userValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        saveProfile = (ImageButton) findViewById(R.id.button_back);
        editUserName = (EditText) findViewById(R.id.editUser);
        editUserPassword = (EditText) findViewById(R.id.editPassword);
        checkBoxAgree = (CheckBox) findViewById(R.id.checkBoxTermo);

        //setando configuracao do action bar
        setupActionBar();
        validation = new ValidationEditText();

        //carregando preferencias do usuario
        loaderPreferences();

        //validando dados inseridos
        validationEditText(validation);

    }


    /**
     * This method will setup the top title bar (Action bar) content and display
     * values. It will also setup the custom background theme for ActionBar. You
     * can override this method to change the behavior of ActionBar for
     * particular Activity
     */
    protected void setupActionBar() {

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_STANDARD);
        //setando logotipo do app
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        //    getSupportActionBar().setLogo(R.drawable.icon);
        //  getSupportActionBar().setDisplayUseLogoEnabled(true);

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void buildDialogOneButton(String titulo, String message, String button) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                ConfigurationActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(titulo);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.icon);

        // Setting OK Button
        alertDialog.setButton(button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void validationEditText(final ValidationEditText validation) {
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = editUserName.getText().toString();
                if (!validation.isValidEmail(email)) {
                    editUserName.setError(getString(R.string.invalid_username));

                } else {
                    userValid = true;
                }

                final String pass = editUserPassword.getText().toString();
                if (!validation.lentghIsValidPassword(pass)) {
                    editUserPassword.setError(getString(R.string.invalid_password));

                } else {
                    passValid = true;
                }
                final boolean agreeTerm = checkBoxAgree.isChecked();

                if (!agreeTerm) {
                    checkBoxAgree.setError(getString(R.string.agree_errror));
                    // buildDialogOneButton(getString(R.string.alert_dialog), getString(R.string.message_dialog), getString(R.string.title_button_dialog));
                    return;
                }

                if (userValid && passValid) {
                    //salvando preferencias
                    savePreferences();
                    //saindo da activity
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Atualizando login", Toast.LENGTH_LONG).show();
                }
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
        editor.putString(getString(R.string.key_user_password), passwordInput);
        editor.putBoolean(getString(R.string.key_user_agree), checkBoxInput);
        editor.apply();
    }

    private void loaderPreferences() {
        //Carrega as preferencias gravadas
        sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editUserName.setText(sharedPreferences.getString(getString(R.string.key_user_name), ""));
        editUserPassword.setText(sharedPreferences.getString(getString(R.string.key_user_password), ""));
        checkBoxAgree.setChecked(sharedPreferences.getBoolean(getString(R.string.key_user_agree), false));
    }


}
