package com.example.quizapp;

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

public class QuizScreen extends AppCompatActivity {
    private Bundle extras;
    private TextView tvTitle,tvQuestion;
    private Button btnNext;

    private String quizTitle;
    private String correctAnswer;
    private int score = 0;
    private int numQuestions;
    private HashMap<String,String> answerKey = new HashMap<>();
    private ArrayList<String> questions = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();
    private ArrayList<Button> answerButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_screen);
        extras = getIntent().getExtras();
        Button btnA1 = findViewById(R.id.btnA1);
        Button btnA2 = findViewById(R.id.btnA2);
        Button btnA3 = findViewById(R.id.btnA3);
        Button btnA4 = findViewById(R.id.btnA4);
        btnNext = findViewById(R.id.btnNext);
        tvTitle = findViewById(R.id.tvTitle);
        tvQuestion = findViewById(R.id.tvQuestion);

        //Add buttons to button array
        answerButtons.add(btnA1);
        answerButtons.add(btnA2);
        answerButtons.add(btnA3);
        answerButtons.add(btnA4);

        //Load the quiz and set the title
        loadQuiz();
        tvTitle.setText(quizTitle);
        numQuestions = questions.size();
        setupQuestion();

        btnA1.setOnClickListener(answerListener);
        btnA2.setOnClickListener(answerListener);
        btnA3.setOnClickListener(answerListener);
        btnA4.setOnClickListener(answerListener);
        btnNext.setOnClickListener(nextListener);
    }

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
    }//end loadQuiz

    //Method that sets up questions
    private void setupQuestion() {
        ArrayList<String> possibleAnswers = new ArrayList<>();
        //Shuffle questions list and remove/save the first element
        //as the current question and set the question text
        Collections.shuffle(questions);
        String currentQuestion = questions.remove(0);
        tvQuestion.setText(currentQuestion);
        //Save the correct answer and
        correctAnswer = answerKey.get(currentQuestion);
        Collections.shuffle(answers);
        //Add the correct answer to the possible answers list
        possibleAnswers.add(correctAnswer);
        //Add three more answers to the possible answers list (no dupes)
        for (int i = 0; i < answers.size(); i++) {
            if (!possibleAnswers.contains(answers.get(i))) {
                possibleAnswers.add(answers.get(i));
            }
            System.out.println("Possible answers: " + possibleAnswers);
            if (possibleAnswers.size() == 4) {break;}
        }
        //Shuffle possible answers so that btnA1 is not always answer
        //and then set the button colour
        Collections.shuffle(possibleAnswers);
        for (int i = 0; i < possibleAnswers.size(); i++) {
            answerButtons.get(i).setText(possibleAnswers.get(i));
            answerButtons.get(i).setBackgroundColor(Color.argb(255,66,133,244));
        }
        btnNext.setVisibility(View.INVISIBLE);
    }//end setupQuestion

    //Listener for answer buttons
    View.OnClickListener answerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button click = findViewById(v.getId());
            //If no answer has been selected
            if (btnNext.getVisibility() == View.INVISIBLE) {
                //If the answer is correct, change font colour to green
                //if (answerIsCorrect(click.getText().toString())) {
                if (click.getText().equals(correctAnswer)) {
                    click.setBackgroundColor(Color.argb(255,52,168,83));
                    score++;
                }
                //If the answer is incorrect, change the font colour to red
                else {
                    click.setBackgroundColor(Color.argb(255,234, 67, 53));
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
                extras.putInt("NUM", numQuestions);
                extras.putInt("SCORE", score);
                extras.putString("TITLE", quizTitle);
                i.putExtras(extras);
                startActivity(i);
            }
        }
    };//end nextListener

}