package com.example.esercitazionebonus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.esercitazionebonus.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    protected TextView welcome;
    protected Person person;
    protected Intent intent, addIntent;
    protected Serializable object;
    protected ImageButton add_vote_button, first_vote_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        intent = getIntent();
        object = intent.getSerializableExtra(LoginActivity.PERSON_PATH);

        person = object instanceof Person ? (Person) object : new Person();

        welcome = findViewById(R.id.welcome);
        welcome.setText("Benvenuto " + person.getName() + "!");

        add_vote_button = findViewById(R.id.add_vote_button);
        first_vote_button = findViewById(R.id.first_vote_button);

        first_vote_button.setOnClickListener(view -> {

            addIntent = new Intent(HomeActivity.this, NewVoteActivity.class);
            addIntent.putExtra("name", person.getName());
            startActivity(addIntent);
        });

        add_vote_button.setOnClickListener(view -> {

            addIntent = new Intent(HomeActivity.this, NewVoteActivity.class);
            addIntent.putExtra("name", person.getName());
            startActivity(addIntent);
        });
    }
}