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
import com.example.esercitazionebonus.NewVoteActivity;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NewVoteActivity extends AppCompatActivity {

    protected TextView welcome;
    protected String name;
    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_vote);

        intent = getIntent();
        name = intent.getStringExtra("name");

        welcome = findViewById(R.id.welcome);
        welcome.setText("Benvenuto " + name + "!");
    }
}