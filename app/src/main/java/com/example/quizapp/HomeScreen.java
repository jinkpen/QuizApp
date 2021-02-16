package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.*;
import android.view.*;
import java.lang.reflect.Field;
import java.util.*;

public class HomeScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText teEnterName;
    private TextView tvInvalidName;
    private int quizID;
    private Field[] fields = R.raw.class.getFields(); //gets quizzes in folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        ArrayList<String> dropdownList = new ArrayList<>(fields.length);
        //Create a list of file name strings
        for(int i = 0; i < fields.length; i++) {
            dropdownList.add(fields[i].getName());
        }
        //Add menu title to the front of the list
        dropdownList.add(0, "Select a quiz");

        teEnterName = findViewById(R.id.teEnterName);
        tvInvalidName = findViewById(R.id.tvInvalidName);
        Button btnChooseQuiz = findViewById(R.id.btnChooseQuiz);
        Spinner dropdown = findViewById(R.id.spChooseQuiz);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownList);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        btnChooseQuiz.setOnClickListener(chooseQuizListener);
    }//end onCreate

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (position > 0) {
            quizID = this.getResources().getIdentifier(fields[position-1].getName(), "raw", this.getPackageName());
            System.out.println("Quiz ID: " + quizID);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    View.OnClickListener chooseQuizListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nameIsValid(teEnterName.getText().toString())) {
                if (quizID > 0) {
                    Intent intent = new Intent("QuizScreen");
                    Bundle extras = new Bundle();
                    extras.putInt("QUIZ", quizID);
                    extras.putString("NAME", teEnterName.getText().toString());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
            else {
                tvInvalidName.setVisibility(View.VISIBLE);
            }
        }
    };

    //Method to validate user name input
    private boolean nameIsValid(String input) {
        return input.matches("[a-zA-Z]+");
    }

}
/*
    References
    https://stackoverflow.com/questions/6539715/android-how-do-can-i-get-a-list-of-all-files-in-a-folder
    https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list

 */