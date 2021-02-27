package com.example.quizapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.view.*;
import java.lang.reflect.Field;
import java.util.*;

/*
    This application was completed in accordance with
    specifications outlined in assignment two for
    MOBI 3002 at NSCC, Winter 2021.

    Author: Jess Inkpen

 */

public class HomeScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText teEnterName;
    private TextView tvInvalidName;
    private int quizID;
    private Field[] fields = R.raw.class.getFields(); //gets quizzes in folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        //Set custom action bar that includes the app icon
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        ArrayList<String> dropdownList = new ArrayList<>(fields.length);
        //Create a list of file name strings, formatted so each word is
        //capitalized and underscores are replaced with spaces
        for(int i = 0; i < fields.length; i++) {
            String[] temp = fields[i].getName().split("_");
            String quizName = "";
            for (int j = 0; j < temp.length; j++) {
                temp[j] = Character.toUpperCase(temp[j].charAt(0)) + temp[j].substring(1);
                if (j < temp.length-1)
                    temp[j] += " ";
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_base, dropdownList);
        adapter.setDropDownViewResource(R.layout.spinner_custom);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        btnChooseQuiz.setOnClickListener(chooseQuizListener);
    }//end onCreate

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (position > 0)
            quizID = this.getResources().getIdentifier(fields[position-1].getName(), "raw", this.getPackageName());
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

    //Method to detect when area outside the edit text is touched so keyboard hides
    //I can't believe how ridiculously hard this was to achieve!!!
    //The input method manager seems so extra
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        if (view != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE)
                && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            //Get absolute coordinates of view
            view.getLocationOnScreen(sourceCoordinates);
            //getLeft() returns the x value of view relative to parent
            //getRawX() + getRawY() are coordinates of where screen was touched
            float x = event.getRawX() + view.getLeft() - sourceCoordinates[0];
            float y = event.getRawY() + view.getTop() - sourceCoordinates[1];
            //If screen touch is outside the edit text, hide the keyboard
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(event);
    }

    //Method to hide the keyboard
    private void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
/*
    References

    https://stackoverflow.com/questions/6539715/android-how-do-can-i-get-a-list-of-all-files-in-a-folder
    https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
    https://stackoverflow.com/questions/64662485/solution-for-hiding-soft-keyboard-when-touching-outside-of-any-part-of-screen-in
    The keyboard solution made me remember (and appreciate) an intro to OOP assignment where I had to make an
    object move around a cartesian plane based on input directions.

    Questions for the Java keyword quiz from here https://www.w3schools.com/java/java_ref_keywords.asp

    All emoji PNGs are derived from a screen shot I had

 */