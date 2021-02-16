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
    private String currentQuestion;
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

    private void loadQuiz() {
        //Use key to get the correct quiz
        int quizID = this.getResources().getIdentifier(extras.getString("QUIZ"), "raw", this.getPackageName());
        String line = "";
        BufferedReader reader = null;
        InputStream inputStream;
        //Try opening the file (log if there are errors)
        try {
            inputStream = getResources().openRawResource(quizID);
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            Log.println(Log.ERROR, "Error opening file", e.getMessage());
        }
        //Try reading the file line-by-line (log if there are any errors)
        try {
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
        } catch (Exception e) {
            Log.println(Log.ERROR, "Error reading line", e.getMessage());
        }
    }//end loadQuiz

    //Method that shuffles the array passed to it
    private void shuffle(ArrayList<String> list) {
        Collections.shuffle(list);
    }

    //Method that shuffles the questions list and returns
    //the question at the front of the list
    private String chooseQuestion() {
        shuffle(questions);
        return questions.remove(0);
    }

    //Method that sets up the next question
    private void setupQuestion() {
        ArrayList<String> possibleAnswers = new ArrayList<>();
        currentQuestion = chooseQuestion();
        tvQuestion.setText(currentQuestion);
        shuffle(answers);
        //Add the correct answer to the possible answers list
        possibleAnswers.add(answerKey.get(currentQuestion));
        //Add three more answers to the possible answers list (no dupes)
        for (int i = 0; i < answers.size(); i++) {
            if (!possibleAnswers.contains(answers.get(i))) {
                possibleAnswers.add(answers.get(i));
            }
            System.out.println("Possible answers: " + possibleAnswers);
            if (possibleAnswers.size() == 4) {break;}
        }
        //Shuffle possible answers so that btnA1 is not always answer
        //and then set the button text
        shuffle(possibleAnswers);
        for (int i = 0; i < possibleAnswers.size(); i++) {
            answerButtons.get(i).setText(possibleAnswers.get(i));
            answerButtons.get(i).setTextColor(Color.argb(255,80,80,80));
        }
        btnNext.setVisibility(View.INVISIBLE);
    }//end setupQuestion

    //Returns true if the pressed answer is correct
    private boolean answerIsCorrect(String answer) {
        return answer.equals(answerKey.get(currentQuestion));
    }

    //Reset button text colour to dark grey after press
    private void resetButton() {
        for (int i = 0; i < answerButtons.size(); i++) {
            answerButtons.get(i).setTextColor(Color.argb(255,80,80,80));
        }
    }

    //Listener for answer buttons
    View.OnClickListener answerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button click = findViewById(v.getId());
            //If no answer has been selected
            if (btnNext.getVisibility() == View.INVISIBLE) {
                //If the answer is correct, change font colour to green
                if (answerIsCorrect(click.getText().toString())) {
                    click.setTextColor(Color.argb(255,0,128,0));
                    score++;
                }
                //If the answer is incorrect, change the font color to red
                else {
                    click.setTextColor(Color.argb(255,200, 0, 0));
                }
                btnNext.setVisibility(View.VISIBLE);
            }
        }
    };

    //Listener for the next button
    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //If there are still questions left, set up a new question
            if (questions.size() > 0) {
                resetButton();
                btnNext.setVisibility(View.INVISIBLE);
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
    };

}