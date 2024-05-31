package com.example.esercitazionebonus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    protected TextView welcome;
    protected Person person;
    protected Serializable object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        person = object instanceof Person ? (Person) object : new Person();

        welcome = findViewById(R.id.welcome);
        welcome.setText("Benvenuto " + person.getName() + "!");
    }
}