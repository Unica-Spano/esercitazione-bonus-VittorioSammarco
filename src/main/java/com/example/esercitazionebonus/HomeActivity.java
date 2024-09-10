package com.example.esercitazionebonus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    public static final String VOTE_PATH = "com.example.esercitazionebonus.vote";
    protected TextView welcome, libret_title, aritmetica, ponderata, laurea;
    protected Person person;
    protected Vote votePerson;
    protected Intent intent, addIntent, voteIntent, rowsIntent, logoutIntent;
    protected Serializable object, voteObject;
    protected ImageButton add_vote_button, remove_vote_button, first_vote_button, logout;
    protected TableLayout titleLayout, tableLayout;
    protected LinearLayout titles, buttons, result_titles, results, first_vote;
    protected ScrollView scrollView;
    protected LayoutInflater layoutInflater;
    protected String name;
    protected boolean reset;
    protected int rows;
    protected int totCfu = 0;
    protected int totA = 0;
    protected int totP = 0;
    protected float mediaA, mediaP, mediaL;
    protected List<String> voteNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Abilita Edge-to-Edge
        setContentView(R.layout.activity_home); // Imposta il layout

        // Associa i widget alle variabili
        add_vote_button = findViewById(R.id.add_vote_button);
        remove_vote_button = findViewById(R.id.remove_vote_button);
        first_vote_button = findViewById(R.id.first_vote_button);
        first_vote = findViewById(R.id.first_vote);
        tableLayout = findViewById(R.id.tableLayout);
        layoutInflater = LayoutInflater.from(this);
        titles = findViewById(R.id.add_remove_titles);
        buttons = findViewById(R.id.add_remove_buttons);
        result_titles = findViewById(R.id.result_titles);
        results = findViewById(R.id.results);
        logout = findViewById(R.id.logout);
        aritmetica = findViewById(R.id.aritmetica);
        ponderata = findViewById(R.id.ponderata);
        laurea = findViewById(R.id.laurea);
        titleLayout = findViewById(R.id.titleLayout);
        libret_title = findViewById(R.id.libret_title);
        scrollView = findViewById(R.id.scrollView);

        welcome(); // Chiamata al metodo welcome

        firstVote(); // Chiamata al metodo firstVote

        logout.setOnClickListener(v -> {
            logoutIntent = new Intent(HomeActivity.this, MainActivity.class); // Creazione intent per logout
            startActivity(logoutIntent); // Avvio della nuova attività
            reset(); // Chiamata al metodo reset
        });

        first_vote_button.setOnClickListener(view -> {
            addIntent = new Intent(HomeActivity.this, NewVoteActivity.class); // Creazione intent per aggiungere un voto
            addIntent.putExtra("name", person.getName());
            addIntent.putExtra("rows", 0);
            startActivity(addIntent); // Avvio della nuova attività
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Imposta il nuovo intent

        welcome(); // Chiamata al metodo welcome

        rowsIntent = intent;
        rows = rowsIntent.getIntExtra("rows", 0);
        reset = rowsIntent.getBooleanExtra("reset", false);

        if (reset) reset();

        if (rows == 0) {
            firstVote(); // Chiamata al metodo firstVote se non ci sono righe
        } else {
            addVote(rows); // Chiamata al metodo addVote se i crediti sono validi
            colorMedia(); // Chiamata al metodo colorMedia
        }

        add_vote_button.setOnClickListener(view -> {
            addIntent = new Intent(HomeActivity.this, NewVoteActivity.class); // Creazione intent per aggiungere un voto
            addIntent.putExtra("name", name);
            addIntent.putExtra("rows", rows);
            addIntent.putExtra("totCfu", totCfu);
            addIntent.putStringArrayListExtra("voteList", new ArrayList<>(voteNames));
            startActivity(addIntent); // Avvio della nuova attività
        });

        remove_vote_button.setOnClickListener(v -> {
            showDeleteVoteDialog(); // Chiamata al metodo showDeleteVoteDialog per rimuovere un voto
        });
    }

    protected void welcome(){
        intent = getIntent();
        object = intent.getSerializableExtra(LoginActivity.PERSON_PATH);

        person = object instanceof Person ? (Person) object : new Person();

        welcome = findViewById(R.id.welcome);
        if (person.getName() != null) {
            welcome.setText("Benvenuto " + person.getName() + "!"); // Imposta il messaggio di benvenuto
            name = person.getName();
        }
    }

    public void firstVote(){
        // Imposta la visibilità dei vari elementi
        add_vote_button.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        tableLayout.setVisibility(View.GONE);
        titleLayout.setVisibility(View.GONE);
        libret_title.setVisibility(View.GONE);
        titles.setVisibility(View.GONE);
        buttons.setVisibility(View.GONE);
        result_titles.setVisibility(View.GONE);
        results.setVisibility(View.GONE);
        first_vote.setVisibility(View.VISIBLE);
    }

    public void addVote(int rows) {
        TableRow newRow = (TableRow) layoutInflater.inflate(R.layout.table_row, tableLayout, false);

        // Imposta i dati delle celle
        TextView cell1 = newRow.findViewById(R.id.subject);
        TextView cell2 = newRow.findViewById(R.id.vote);
        TextView cell3 = newRow.findViewById(R.id.cfu);

        voteIntent = getIntent();
        voteObject = voteIntent.getSerializableExtra(VOTE_PATH);

        votePerson = voteObject instanceof Vote ? (Vote) voteObject : new Vote();

        cell1.setText(votePerson.getExam());
        cell2.setText(String.valueOf(votePerson.getVote()));
        cell3.setText(String.valueOf(votePerson.getCredits()));

        // Aggiungi la nuova riga alla TableLayout
        tableLayout.addView(newRow);

        voteNames.add(votePerson.getExam());

        addMedia(rows); // Chiamata al metodo addMedia

        // Imposta la visibilità delle viste
        add_vote_button.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.VISIBLE);
        libret_title.setVisibility(View.VISIBLE);
        tableLayout.setVisibility(View.VISIBLE);
        titles.setVisibility(View.VISIBLE);
        buttons.setVisibility(View.VISIBLE);
        result_titles.setVisibility(View.VISIBLE);
        results.setVisibility(View.VISIBLE);
        first_vote.setVisibility(View.GONE);
    }

    protected void addMedia(int rows){
        // Calcola la media dei voti

        voteIntent = getIntent();
        voteObject = voteIntent.getSerializableExtra(VOTE_PATH);

        votePerson = voteObject instanceof Vote ? (Vote) voteObject : new Vote();

        totCfu = totCfu + (votePerson.getCredits());
        totA = totA + (votePerson.getVote());
        totP = totP + (votePerson.getCredits() * votePerson.getVote());

        mediaA = (float) totA / rows;
        mediaP = (float) totP / totCfu;
        mediaL = (mediaP) * 110 / 30;

        if (mediaL > 110) mediaL = 110;

        aritmetica.setText(String.format("%.1f", mediaA));
        ponderata.setText(String.format("%.1f", mediaP));
        laurea.setText(String.valueOf((int)mediaL));
    }

    protected void deleteMedia(TextView vote, TextView cfu){
        // Elimina i voti e aggiorna le medie

        int votetoDelete = Integer.parseInt(vote.getText().toString());
        int cfutoDelete = Integer.parseInt(cfu.getText().toString());

        totCfu = totCfu - cfutoDelete;
        totA = totA - votetoDelete;
        totP = totP - (votetoDelete * cfutoDelete);

        mediaA = (float) totA / rows;
        mediaP = (float) totP / totCfu;
        mediaL = (mediaP) * 110 / 30;

        if (mediaL > 110) mediaL = 110;

        aritmetica.setText(String.format("%.1f", mediaA));
        ponderata.setText(String.format("%.1f", mediaP));
        laurea.setText(String.valueOf((int)mediaL));
    }

    protected void colorMedia(){
        // Colora le medie in base ai valori

        voteIntent = getIntent();
        voteObject = voteIntent.getSerializableExtra(VOTE_PATH);

        votePerson = voteObject instanceof Vote ? (Vote) voteObject : new Vote();

        if (Float.parseFloat(aritmetica.getText().toString().replace(",", ".")) < 24){
            aritmetica.setTextColor(getResources().getColor(R.color.strong_red));
        } else if (Float.parseFloat(aritmetica.getText().toString().replace(",", ".")) < 27){
            aritmetica.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            aritmetica.setTextColor(getResources().getColor(R.color.green));
        }

        if (Float.parseFloat(ponderata.getText().toString().replace(",", ".")) < 24){
            ponderata.setTextColor(getResources().getColor(R.color.strong_red));
        } else if (Float.parseFloat(ponderata.getText().toString().replace(",", ".")) < 27){
            ponderata.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            ponderata.setTextColor(getResources().getColor(R.color.green));
        }

        if (Integer.parseInt(laurea.getText().toString()) < 80){
            laurea.setTextColor(getResources().getColor(R.color.strong_red));
        } else if (Integer.parseInt(laurea.getText().toString()) < 95){
            laurea.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            laurea.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private void showDeleteVoteDialog() {
        // Mostra il dialogo per selezionare il voto da eliminare

        String[] voteNamesArray = voteNames.toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("Seleziona il voto da eliminare")
                .setItems(voteNamesArray, (dialog, which) -> {
                    String selectedVote = voteNamesArray[which];
                    showConfirmationDialog(selectedVote); // Mostra il dialogo di conferma
                })
                .setNegativeButton("Annulla", null)
                .show();
    }

    private void removeVoteByName(String voteName) {
        // Rimuove il voto dalla tabella

        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView cell1 = row.findViewById(R.id.subject);
                TextView cell2 = row.findViewById(R.id.vote);
                TextView cell3 = row.findViewById(R.id.cfu);
                if (cell1.getText().toString().equals(voteName)) {
                    tableLayout.removeView(row);
                    voteNames.remove(voteName);
                    deleteMedia(cell2, cell3); // Aggiorna le medie
                    break;
                }
            }
        }
    }

    private void showConfirmationDialog(String voteName) {
        // Mostra il dialogo di conferma per l'eliminazione

        new AlertDialog.Builder(this)
                .setTitle("Conferma eliminazione")
                .setMessage("Sei sicuro di voler eliminare l'esame di " + voteName + "?")
                .setPositiveButton("Conferma", (dialog, which) -> {
                    rows--;
                    removeVoteByName(voteName); // Rimuove il voto
                    if (rows == 0) {
                        firstVote(); // Se non ci sono più righe, chiama il metodo firstVote
                    }
                })
                .setNegativeButton("Annulla", null)
                .show();
    }

    public void reset(){
        // Reset delle variabili e delle righe della tabella

        totA = 0;
        totP = 0;
        totCfu = 0;

        for (int i = tableLayout.getChildCount() - 1; i >= 0; i--) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                TextView cell1 = row.findViewById(R.id.subject);
                tableLayout.removeView(row);
                voteNames.remove(cell1.getText().toString());
            }
        }
    }
}
