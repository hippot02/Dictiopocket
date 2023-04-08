package com.example.dictiopocket;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizzActivity extends AppCompatActivity {

    TextView tvQuestion;
    Button btnRep1;
    Button btnRep2;
    int idquestion = 0;
    DBHandler db;
    private QuizzActivity activity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().hide();
        gestionToolbar();
        tvQuestion = findViewById(R.id.questionQuizz);
        btnRep1 = findViewById(R.id.reponseQuizz1);
        btnRep2 = findViewById(R.id.reponseQuizz2);
        this.activity = this;
        db = new DBHandler(this);
        db.deleteDatabase();
        fillDB();
        randomiserQuestion();
    }

    public void check(View v) {
        String rep = db.getRepById(idquestion);
        int temp;
        if (v.getId() == R.id.reponseQuizz1) {
            temp = 1;
        } else {
            temp = 2;
        }

        if (temp == Integer.valueOf(rep)) {
            CustomPopup popup = new CustomPopup(activity);
            popup.setTitle("C'est une bonne réponse !");
            popup.setSubtitle("Vous avez trouvé la bonne réponse. Voulez vous rejouer ?");
            popup.build();
            popup.getYesButton().setOnClickListener(view -> {
                popup.dismiss();
                randomiserQuestion();

            });

            popup.getNoButton().setOnClickListener(view -> {
                popup.dismiss();
                Intent u = new Intent(activity, HomeActivity.class);
                startActivity(u);


            });
        } else {
            Toast.makeText(this, "Vous n'avez pas trouvé la réponse.", Toast.LENGTH_SHORT).show();
        }


    }


    public void randomiserQuestion() {
        idquestion = db.selectRandomQuestionId();
        //System.out.println("-----------------------------"+idquestion);
        String question = db.getQuestionById(idquestion);
        tvQuestion.setText(question);
        String reponse1 = db.getRep1ById(idquestion);
        String reponse2 = db.getRep2ById(idquestion);
        btnRep1.setText(reponse1);
        btnRep2.setText(reponse2);
    }


    private void gestionToolbar() {
        ImageView home_icon = findViewById(R.id.home_icon);
        Button tbDevinPaysButton = findViewById(R.id.tbDevinPaysButton);
        Button tbQuizzButton = findViewById(R.id.tbQuizzButton);
        home_icon.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);

        });
        tbDevinPaysButton.setOnClickListener(view -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        });

        tbQuizzButton.setOnClickListener(view -> {
            Intent i = new Intent(this, QuizzActivity.class);
            startActivity(i);

        });

    }

    public void fillDB() {
        db.insertQuestion("Quel pays est le plus grand du monde ?", "Russie", "Canada", "1");
        db.insertQuestion("Quel est le pays avec le moins d'habitants ?", "Suède", "Norvège", "2");
        db.insertQuestion("Avec quel pays la France partage sa plus grosse frontière ?", "Brésil", "Allemagne", "1");
        db.insertQuestion("Quel est la capitale de Brunei ?", "Bandar Seri Begawan", "Khartoum", "1");
        db.insertQuestion("Quel pays possède le plus grand nombre de pyramides ?", "Egypte", "Soudan", "2");
        db.insertQuestion("Ou se situe la Porte de l'Enfer ?", "Nouvelle Guinnée", "Turkménistan", "2");
        db.insertQuestion("Quel est le pays le plus plat du monde ?", "Bolivie", "Maroc", "1");
        db.insertQuestion("Quel est le plus vieil État du monde ?", "Saint-Marin", "Malte", "1");

        randomiserQuestion();

    }
}
