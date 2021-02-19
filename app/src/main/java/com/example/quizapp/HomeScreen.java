package com.example.quizapp;

import androidx.appcompat.app.ActionBar;
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
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setIcon(R.mipmap.ic_launcher_foreground);

        ArrayList<String> dropdownList = new ArrayList<>(fields.length);
        //Create a list of file name strings, formatted so each word is
        //capitalized and underscores are replaced with spaces
        for(int i = 0; i < fields.length; i++) {
            String[] temp = fields[i].getName().split("_");
            String quizName = "";
            for (int j = 0; j < temp.length; j++) {
                temp[j] = Character.toUpperCase(temp[j].charAt(0)) + temp[j].substring(1);
                if (j < temp.length-1) {
                    temp[j] += " ";
                }
                quizName += temp[j];
            }
            dropdownList.add(quizName);
        }
        //Add menu title to the front of the list
        dropdownList.add(0, "Select a quiz");

        teEnterName = findViewById(R.id.teEnterName);
        tvInvalidName = findViewById(R.id.tvInvalidName);
        Button btnChooseQuiz = findViewById(R.id.btnChooseQuiz);
        Spinner dropdown = findViewById(R.id.spChooseQuiz);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_base, dropdownList);
        adapter.setDropDownViewResource(R.layout.spinner_custom);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        btnChooseQuiz.setOnClickListener(chooseQuizListener);
    }//end onCreate

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (position > 0) {
            quizID = this.getResources().getIdentifier(fields[position-1].getName(), "raw", this.getPackageName());
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