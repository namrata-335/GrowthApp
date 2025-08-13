package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GoalTime extends AppCompatActivity {

    private LinearLayout goalTimeContainer;
    private ArrayList<String> selectedGoals;
    private ArrayList<Integer> goalTimes; // store minutes per goal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_goals);

        goalTimeContainer = findViewById(R.id.goalTimeContainer);
        selectedGoals = getIntent().getStringArrayListExtra("selectedGoals");
        goalTimes = new ArrayList<>();

        if (selectedGoals != null) {
            for (String goal : selectedGoals) {
                addGoalWithSlider(goal);
            }
        }

        Button startButton = findViewById(R.id.startWeek1Button);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(GoalTime.this, week1.class);
            intent.putStringArrayListExtra("selectedGoals", selectedGoals);
            intent.putIntegerArrayListExtra("goalTimes", goalTimes); // send chosen times
            startActivity(intent);
        });
    }

    private void addGoalWithSlider(String goal) {
        TextView goalText = new TextView(this);
        goalText.setText(goal);
        goalText.setTextSize(18);
        goalText.setPadding(0, 20, 0, 10);

        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(120);
        seekBar.setProgress(30);

        TextView progressText = new TextView(this);
        progressText.setText("30 minutes");

        // Add default value to goalTimes list
        goalTimes.add(30);

        int index = goalTimes.size() - 1;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText(progress + " minutes");
                goalTimes.set(index, progress); // store updated value
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        goalTimeContainer.addView(goalText);
        goalTimeContainer.addView(seekBar);
        goalTimeContainer.addView(progressText);
    }
}