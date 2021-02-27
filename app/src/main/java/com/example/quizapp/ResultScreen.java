package com.example.quizapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
        TextView tvNameMsg = findViewById(R.id.tvNameMsg);
        ImageView ivResultIcon = findViewById(R.id.ivResultIcon);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("NAME");
        String quizTitle = extras.getString("TITLE");
        int score = extras.getInt("SCORE");
        int numQuestions = extras.getInt("NUM");
        double percentScore = score/numQuestions;


        tvTitle.setText(quizTitle);
        tvResult.setText(name + "'s score: ");
        tvNameMsg.setText(score + "/" + numQuestions);
        if (percentScore < 0.5)
            ivResultIcon.setImageResource(R.drawable.upset_face);
        else if (percentScore < 0.6)
            ivResultIcon.setImageResource(R.drawable.sad_face);
        else if (percentScore < 0.7)
            ivResultIcon.setImageResource(R.drawable.straight_face);
        else if (percentScore < 0.8)
            ivResultIcon.setImageResource(R.drawable.smile_face);
        else if (percentScore < 0.9)
            ivResultIcon.setImageResource(R.drawable.smile_med_face);
        else if (percentScore < 1)
            ivResultIcon.setImageResource(R.drawable.smile_big_face);
        else
            ivResultIcon.setImageResource(R.drawable.sunglasses_face);
    }
}