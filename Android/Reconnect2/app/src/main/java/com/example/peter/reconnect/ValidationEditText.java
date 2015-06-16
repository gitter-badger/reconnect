package com.example.peter.reconnect;

import android.widget.CheckBox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Nesta clases serao implementadas todas as validacoes que forem itulizadas ou necesarias no app
 * Created by Peter on 15/06/15.
 */
public class ValidationEditText {

    // validating email id
    public boolean isValidEmail(String email) {

        //utlizamos uma expresao regular para verfiicar o email
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    public boolean lentghIsValidPassword(String pass) {

        if (pass != null && pass.length() > 0)
            return true;

        return false;
    }

    public boolean isValidCheck(CheckBox agree) {

        if (agree.isChecked())
            return true;

        return false;
    }
}
