package com.example.esercitazionebonus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewVoteActivity extends AppCompatActivity {

    protected EditText exam, vote, cfu;
    protected TextView welcome, error;
    protected String name;
    protected Intent intent, result, rowsIntent, logoutIntent;
    protected Button add;
    protected ImageButton logout;
    protected Vote votePerson;
    protected int rows, totCfu;
    protected boolean reset;
    protected ArrayList<String> voteNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_vote);

        votePerson = new Vote();

        exam = findViewById(R.id.exam_name);
        vote = findViewById(R.id.vote);
        cfu = findViewById(R.id.cfu);
        add = findViewById(R.id.add_vote_button);
        logout = findViewById(R.id.logout);
        error = findViewById(R.id.error_add);
        welcome = findViewById(R.id.welcome);

        logout.setOnClickListener(v -> {
            logoutIntent = new Intent(NewVoteActivity.this, MainActivity.class); // Creazione intent per logout
            startActivity(logoutIntent); // Avvio della nuova attività
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        name = intent.getStringExtra("name");

        rowsIntent = getIntent();
        rows = rowsIntent.getIntExtra("rows", 0);
        totCfu = rowsIntent.getIntExtra("totCfu", 0);
        voteNames = rowsIntent.getStringArrayListExtra("voteList");

        if (name != null) {
            welcome.setText("Benvenuto " + name + "!");
        }

        add.setOnClickListener(v -> {

            if (checkInput()) {

                rows++;
                updateVote();
                result = new Intent(NewVoteActivity.this, HomeActivity.class);
                result.putExtra(HomeActivity.VOTE_PATH, votePerson);
                result.putExtra("rows", rows);
                result.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(result);

            }
        });
    }

    protected void updateVote() {
        votePerson.setExam(exam.getText().toString());
        votePerson.setVote(Integer.parseInt(vote.getText().toString()));
        votePerson.setCredits(Integer.parseInt(cfu.getText().toString()));
    }

    protected boolean checkInput() {
        int errors = 0;

        // Controllo del nome dell'esame
        if (exam.getText() == null || exam.getText().length() == 0) {
            errors++;
            exam.setError("Inserire il nome dell'esame");
        } else if (exam.getText().length() > 10) {
            errors++;
            exam.setError("Inserisci un nome di max 10 caratteri (cerca di abbreviare)");
        } else if (checkDuplicateName(exam.getText().toString(), voteNames)) {
            errors++;
            exam.setError("Hai già inserito un esame con quel nome");
        } else {
            exam.setError(null);
        }

        // Controllo dei CFU
        try {
            int cfuValue = Integer.parseInt(cfu.getText().toString());
            if (cfuValue < 2) {
                errors++;
                cfu.setError("La materia non può valere meno di 2 cfu");
            } else if (cfuValue > 20) {
                errors++;
                cfu.setError("La materia non può valere più di 20 cfu");
            } else if ((totCfu + cfuValue) > 180) {
                errors++;
                cfu.setError("Il tuo libretto può avere al massimo 180 cfu");
            } else {
                cfu.setError(null);
            }
        } catch (NumberFormatException e) {
            errors++;
            cfu.setError("Inserisci un numero valido per i CFU");
        }

        // Controllo del voto
        try {
            int voteValue = Integer.parseInt(vote.getText().toString());
            if (voteValue < 18) {
                errors++;
                vote.setError("Non puoi inserire nel libretto un esame non superato");
            } else if (voteValue > 31) {
                errors++;
                vote.setError("Il massimo voto inseribile è 31 (corrispondente a 30L)");
            } else {
                vote.setError(null);
            }
        } catch (NumberFormatException e) {
            errors++;
            vote.setError("Inserisci un numero valido per il voto");
        }

        // Mostra errore generico se ci sono errori
        if (errors > 0) {
            error.setVisibility(View.VISIBLE);
            error.setText(R.string.error_form_activity);
        } else {
            error.setVisibility(View.GONE);
        }

        return errors == 0;
    }

    protected boolean checkDuplicateName(String newSubject, ArrayList<String> voteNames) {
        if (voteNames != null) {
            for (String name : voteNames) {
                if (name.equalsIgnoreCase(newSubject)) {
                    return true; // Nome duplicato trovato
                }
            }
        }
        return false; // Nessun duplicato trovato
    }
}
