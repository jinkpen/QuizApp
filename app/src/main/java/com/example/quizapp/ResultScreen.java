package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);

        TextView tvTitle = findViewById(R.id.tvTitle2);
        TextView tvResult = findViewById(R.id.tvResult);
        TextView tvNameMsg = findViewById(R.id.tvNameMsg);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("NAME");
        String quizTitle = extras.getString("TITLE");
        int score = extras.getInt("SCORE");
        int numQuestions = extras.getInt("NUM");

        tvTitle.setText(quizTitle);
        tvResult.setText("Your score: " + score + "/" + numQuestions);
        tvNameMsg.setText("Thank you, " + name + "!\nPlease play again.");
    }
}