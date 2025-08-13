package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Screen2Activity extends AppCompatActivity {

    CheckBox cbMeditation, cbJournaling, cbExercise, cbSkill, cbReading;
    Button submitButton;
    TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        cbMeditation = findViewById(R.id.checkbox_meditation);
        cbJournaling = findViewById(R.id.checkbox_journaling);
        cbExercise = findViewById(R.id.checkbox_exercise);
        cbSkill = findViewById(R.id.checkbox_skill);
        cbReading = findViewById(R.id.checkbox_reading);
        submitButton = findViewById(R.id.submitGoalsButton);
        warningText = findViewById(R.id.warningText);

        submitButton.setOnClickListener(v -> {
            List<String> selectedGoals = new ArrayList<>();
            if (cbMeditation.isChecked()) selectedGoals.add("Meditation");
            if (cbJournaling.isChecked()) selectedGoals.add("Journaling");
            if (cbExercise.isChecked()) selectedGoals.add("Exercise");
            if (cbSkill.isChecked()) selectedGoals.add("Practicing a Skill");
            if (cbReading.isChecked()) selectedGoals.add("Reading 10 Pages");

            if (selectedGoals.size() != 2) {
                warningText.setVisibility(View.VISIBLE);
            } else {
                warningText.setVisibility(View.GONE);

                // Save selected goals using SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("goal1", selectedGoals.get(0));
                editor.putString("goal2", selectedGoals.get(1));
                editor.apply();

                // Move to next screen (replace Screen3Activity with your actual one)
                Intent intent = new Intent(Screen2Activity.this, Screen3Activity.class);
                intent.putStringArrayListExtra("selectedGoals", new ArrayList<>(selectedGoals));  // pass selected goals
                startActivity(intent);
            }
        });
    }
}