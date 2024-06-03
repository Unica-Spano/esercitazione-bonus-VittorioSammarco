package com.example.esercitazionebonus.ui.login;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.esercitazionebonus.DatePickerFragment;
import com.example.esercitazionebonus.HomeActivity;
import com.example.esercitazionebonus.Person;
import com.example.esercitazionebonus.R;
import com.example.esercitazionebonus.databinding.ActivityLoginBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    public static final String PERSON_PATH = "com.example.esercitazionebonus.person";
    public Person person;
    protected Calendar calendar;
    protected EditText name, surname, birthday, email, password;
    protected SimpleDateFormat simpleDateFormat;
    protected TextView error;
    protected Intent result;
    protected Button loginButton;
    protected int calendar_age;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        birthday = findViewById(R.id.birthday);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        error = findViewById(R.id.error);
        person = new Person();

        birthday.setOnClickListener(v -> showDialog());

        birthday.setOnFocusChangeListener((v, hasFocus) -> {if (hasFocus) showDialog();});

        loginButton.setOnClickListener(view -> {

            updatePerson();

            if (checkInput()) {
                result = new Intent(LoginActivity.this, HomeActivity.class);
                result.putExtra(PERSON_PATH, person);
                updatePerson();
                startActivity(result);
            }
        });
    }

    protected void showDialog(){
        DialogFragment dialogFragment = DatePickerFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick(Calendar calendar){
        birthday.setText(simpleDateFormat.format(calendar.getTime()));
        birthday.setError(null);
    }

    protected int ageCalculator(){
        Calendar today = Calendar.getInstance();
        calendar_age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            calendar_age--;
        } else
        if (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
            calendar_age--;
        }
        return calendar_age;
    }

    protected void updatePerson() {
        person.setName(name.getText().toString());
        person.setSurname(surname.getText().toString());
        try {
            calendar.setTime(simpleDateFormat.parse(birthday.getText().toString()));
        }catch (ParseException e){
            e.printStackTrace();
        }
        person.setBirthday(calendar);
        person.setEmail(email.getText().toString());
    }

    protected boolean checkAll(){
        boolean check = false;

        if (name.getText().length() > 0 && surname.getText().length() > 0 &&
                birthday.getText().length() > 0 && email.getText().length() > 0 &&
                password.getText().length() > 0) {
            check = true;
        }
        return check;
    }

    protected boolean checkInput() {
        int errors = 0;

        if (name.getText() == null || name.getText().length() == 0) {
            errors++;
            name.setError("Inserire il nome");
        } else {
            name.setError(null);
        }

        if (surname.getText() == null || surname.getText().length() == 0) {
            errors++;
            surname.setError("Inserire il cognome");
        } else {
            surname.setError(null);
        }

        if (birthday.getText() == null || birthday.getText().length() == 0) {
            errors++;
            birthday.setError("Inserire la data di nascita");
        } else {
            birthday.setError(null);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            errors++;
            email.setError("Inserisci una email valida");
        } else {
            email.setError(null);
        }

        calendar_age = ageCalculator();

        if ((calendar_age) < 0) {
            errors++;
            birthday.setError("La data di nascita non può essere nel futuro!");
        } else if (Integer.valueOf(calendar_age) < 18) {
            errors++;
            birthday.setError("Devi essere maggiorenne!");
        } else {
            birthday.setError(null);
        }

        if (password.getText() == null || password.getText().length() == 0) {
            errors++;
            password.setError("Inserire la password");
        } else if (password.getText().length() < 8) {
            errors++;
            password.setError("La password deve avere minimo 8 caratteri");
        } else {
            birthday.setError(null);
        }

        if (errors > 0) {
            error.setVisibility(View.VISIBLE);
            error.setText(R.string.error_form_activity);
        } else {
            error.setVisibility(View.GONE);
        }
        return errors == 0;
    }
}