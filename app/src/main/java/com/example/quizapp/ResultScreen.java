package com.example.quizapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class ResultScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        TextView tvTitle = findViewById(R.id.tvTitle2);
        TextView tvResult = findViewById(R.id.tvResult);
        ImageView ivResultIcon = findViewById(R.id.ivResultIcon);
        Button btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(restartListener);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("NAME");
        String quizTitle = extras.getString("TITLE");
        int score = extras.getInt("SCORE");
        int numQuestions = extras.getInt("NUM");
        double percentScore = (double)score/(double)numQuestions;
        System.out.println("Percent score: " + percentScore);

        tvTitle.setText(quizTitle);
        tvResult.setText(String.format("%s's score: %d/%d", name, score, numQuestions));
        if (percentScore == 1)
            ivResultIcon.setImageResource(R.drawable.sunglasses_face);
        else if (percentScore >= 0.85)
            ivResultIcon.setImageResource(R.drawable.smile_big_face);
        else if (percentScore >= 0.7)
            ivResultIcon.setImageResource(R.drawable.smile_face);
        else if (percentScore >= 0.5)
            ivResultIcon.setImageResource(R.drawable.straight_face);
        else
            ivResultIcon.setImageResource(R.drawable.upset_face);
    }

    View.OnClickListener restartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(ResultScreen.this, HomeScreen.class));
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeScreen.class));
    }
}