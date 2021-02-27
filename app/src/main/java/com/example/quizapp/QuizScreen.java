package com.example.quizapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class QuizScreen extends AppCompatActivity {
    private Bundle extras;
    private TextView tvTitle,tvQuestion;
    Button[] answerButtons = new Button[4];
    Button btnNext;
    private String quizTitle;
    private String correctAnswer;
    private int score = 0;
    private HashMap<String,String> answerKey = new HashMap<>();
    private ArrayList<String> questions = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);
        extras = getIntent().getExtras();
        //Set custom action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        //Set up UI
        answerButtons[0] = findViewById(R.id.btnA1);
        answerButtons[1] = findViewById(R.id.btnA2);
        answerButtons[2] = findViewById(R.id.btnA3);
        answerButtons[3] = findViewById(R.id.btnA4);
        btnNext = findViewById(R.id.btnNext);
        tvTitle = findViewById(R.id.tvTitle);
        tvQuestion = findViewById(R.id.tvQuestion);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setOnClickListener(answerListener);
        }
        btnNext.setOnClickListener(nextListener);

        //Load quiz, set title, and set up a question
        loadQuiz();
        tvTitle.setText(quizTitle);
        setupQuestion();
    }//end onCreate

    //Loads quiz based on ID from home screen
    private void loadQuiz() {
        //Use key to get the correct quiz
        int quizID = extras.getInt("QUIZ");
        String line = "";
        BufferedReader reader = null;
        InputStream inputStream;
        try {
            inputStream = getResources().openRawResource(quizID);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            int count = 0;
            while ((line=reader.readLine()) != null) {
                //Store the first line as title
                if (count == 0) {
                    quizTitle = line;
                }
                //Split other lines into questions and answers and
                //add elements to hash map and array lists
                else {
                    String[] lineParts = line.split(">");
                    answerKey.put(lineParts[0], lineParts[1]);
                    questions.add(lineParts[0]);
                    answers.add(lineParts[1]);
                }
                count++;
            }
            reader.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.e("ReadFile", "Unable to read selected file.");
        }
        catch (Exception e) {
            Log.e("ReadFile", "An error occurred trying to read the quiz file.");
        }
        Log.v("ReadFile", "Quiz loaded");
        Collections.shuffle(questions);
    }//end loadQuiz

    //Method that sets up questions
    private void setupQuestion() {
        ArrayList<String> possibleAnswers = new ArrayList<>();
        //Remove and save first element as current question
        String currentQuestion = questions.remove(0);
        tvQuestion.setText(currentQuestion);
        //Save the correct answer and
        correctAnswer = answerKey.get(currentQuestion);
        Collections.shuffle(answers);

        //Add the correct answer to the possible answers list
        possibleAnswers.add(correctAnswer);
        //Add three more answers to the possible answers list (no dupes)
        for (int i = 0; possibleAnswers.size() < 4; i++) {
            if (!possibleAnswers.contains(answers.get(i))) {
                possibleAnswers.add(answers.get(i));
            }
        }
        //Shuffle possible answers so that btnA1 is not always answer
        //and then set the text and button colour
        Collections.shuffle(possibleAnswers);
        for (int i = 0; i < possibleAnswers.size(); i++) {
            answerButtons[i].setText(possibleAnswers.get(i));
            answerButtons[i].setBackgroundColor(Color.argb(255,66,133,244));
        }
        //Hide the next button
        btnNext.setVisibility(View.INVISIBLE);
    }//end setupQuestion

    //Listener for answer buttons
    View.OnClickListener answerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button click = findViewById(v.getId());
            //If no answer has been selected
            if (btnNext.getVisibility() == View.INVISIBLE) {
                //If the answer is correct, change button color to green
                if (click.getText().equals(correctAnswer)) {
                    click.setBackgroundColor(Color.argb(255,52,168,83));
                    score++;
                }
                //If the answer is incorrect, change button colour to red
                //and the correct answer green (so user learns the answer)
                else {
                    click.setBackgroundColor(Color.argb(255,234, 67, 53));
                    for (int i = 0; i < answerButtons.length; i++) {
                        if (answerButtons[i].getText().equals(correctAnswer)) {
                            answerButtons[i].setBackgroundColor(Color.argb(255,52,168,83));
                        }
                    }
                }
                btnNext.setVisibility(View.VISIBLE);
            }
        }
    };//end answerListener

    //Listener for the next button
    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //If there are still questions left, set up a new question
            if (questions.size() > 0) {
                setupQuestion();
            }
            //If there are no questions left, go to results screen with calculated score
            else {
                Intent i = new Intent("ResultScreen");
                extras.putInt("NUM", answers.size());
                extras.putInt("SCORE", score);
                extras.putString("TITLE", quizTitle);
                i.putExtras(extras);
                startActivity(i);
            }
        }
    };//end nextListener

}