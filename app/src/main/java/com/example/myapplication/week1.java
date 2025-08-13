package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class week1 extends AppCompatActivity {

    private TextView goalTitle1, goalTitle2;

    // Goal 1 UI
    private TextView timerText1;
    private ProgressBar circularProgressBar1;
    private Button startButton1;
    private CountDownTimer countDownTimer1;
    private boolean isRunning1 = false;

    // Goal 2 UI
    private TextView timerText2;
    private ProgressBar circularProgressBar2;
    private Button startButton2;
    private CountDownTimer countDownTimer2;
    private boolean isRunning2 = false;

    // Other buttons
    private Button backButton, nextDayButton;
    private TextView dayLabel;

    private Button goToWeek2;

    private int totalTime = 60; // seconds
    private int currentDay = 1;
    private final int TOTAL_DAYS = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week1);

        // Goal title text views
        goalTitle1 = findViewById(R.id.goalTitle1);
        goalTitle2 = findViewById(R.id.goalTitle2);

        // Retrieve the selected goals from intent
        ArrayList<String> selectedGoals = getIntent().getStringArrayListExtra("selectedGoals");

        if (selectedGoals != null && !selectedGoals.isEmpty()) {
            // Assign first goal if exists
            if (selectedGoals.size() > 0) {
                goalTitle1.setText(selectedGoals.get(0));
            }
            // Assign second goal if exists
            if (selectedGoals.size() > 1) {
                goalTitle2.setText(selectedGoals.get(1));
            }
        }
        // Goal 1
        timerText1 = findViewById(R.id.timerText1);
        circularProgressBar1 = findViewById(R.id.circularProgressBar1);
        startButton1 = findViewById(R.id.startButton1);
        circularProgressBar1.setMax(totalTime);
        circularProgressBar1.setProgress(totalTime);

        // Goal 2
        timerText2 = findViewById(R.id.timerText2);
        circularProgressBar2 = findViewById(R.id.circularProgressBar2);
        startButton2 = findViewById(R.id.startButton2);
        circularProgressBar2.setMax(totalTime);
        circularProgressBar2.setProgress(totalTime);

        // Navigation
        backButton = findViewById(R.id.backButton);
        nextDayButton = findViewById(R.id.nextDayButton);
        dayLabel = findViewById(R.id.dayLabel);

        // Start buttons
        startButton1.setOnClickListener(v -> {
            if (!isRunning1) {
                startTimer1();
                startButton1.setText("Pause");
            } else {
                pauseTimer1();
                startButton1.setText("Start");
            }
        });

        startButton2.setOnClickListener(v -> {
            if (!isRunning2) {
                startTimer2();
                startButton2.setText("Pause");
            } else {
                pauseTimer2();
                startButton2.setText("Start");
            }
        });

        // Back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(week1.this, Screen4Activity.class);

            // Pass selectedGoals from week1's intent to Screen4Activity
            ArrayList<String> goalsToSend = getIntent().getStringArrayListExtra("selectedGoals");
            if (goalsToSend != null) {
                intent.putStringArrayListExtra("selectedGoals", goalsToSend);
            }
//
//            // Pass reminders
            ArrayList<String> reminders = getIntent().getStringArrayListExtra("reminders");
            if (reminders != null) {
                intent.putStringArrayListExtra("reminders", reminders);
            }

            startActivity(intent);  // start the new activity

            finish();
        });


        // Next Day button
        nextDayButton.setOnClickListener(v -> {
            if (currentDay < TOTAL_DAYS) {
                currentDay++;
                dayLabel.setText("Day " + currentDay);
                resetTimers();
            }
        });

        //Go to week 2
        goToWeek2 = findViewById(R.id.goToWeek2);
        goToWeek2.setOnClickListener(v -> {
            Intent intent = new Intent(week1.this, Week2.class);

            // Pass goals
            ArrayList<String> goalsToSend = getIntent().getStringArrayListExtra("selectedGoals");
            if (goalsToSend != null) {
                intent.putStringArrayListExtra("selectedGoals", goalsToSend);
            }

            // Pass reminders
            ArrayList<String> reminders = getIntent().getStringArrayListExtra("reminders");
            if (reminders != null) {
                intent.putStringArrayListExtra("reminders", reminders);
            }


            ArrayList<Integer> goalTimes = getIntent().getIntegerArrayListExtra("goalTimes");
            if (goalTimes != null) {
                intent.putIntegerArrayListExtra("goalTimes", goalTimes);
            }

            startActivity(intent);
        });
    }

    // Goal 1 timer
    private void startTimer1() {
        isRunning1 = true;
        countDownTimer1 = new CountDownTimer(totalTime * 1000, 1000) {
            int timeLeft = totalTime;

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft--;
                circularProgressBar1.setProgress(timeLeft);
                timerText1.setText(formatTime(timeLeft));
            }

            @Override
            public void onFinish() {
                isRunning1 = false;
                timerText1.setText("Done!");
                startButton1.setText("Restart");
            }
        }.start();
    }

    private void pauseTimer1() {
        isRunning1 = false;
        if (countDownTimer1 != null) countDownTimer1.cancel();
    }

    // Goal 2 timer
    private void startTimer2() {
        isRunning2 = true;
        countDownTimer2 = new CountDownTimer(totalTime * 1000, 1000) {
            int timeLeft = totalTime;

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft--;
                circularProgressBar2.setProgress(timeLeft);
                timerText2.setText(formatTime(timeLeft));
            }

            @Override
            public void onFinish() {
                isRunning2 = false;
                timerText2.setText("Done!");
                startButton2.setText("Restart");
            }
        }.start();
    }

    private void pauseTimer2() {
        isRunning2 = false;
        if (countDownTimer2 != null) countDownTimer2.cancel();
    }

    private void resetTimers() {
        pauseTimer1();
        pauseTimer2();
        circularProgressBar1.setProgress(totalTime);
        timerText1.setText(formatTime(totalTime));
        startButton1.setText("Start");

        circularProgressBar2.setProgress(totalTime);
        timerText2.setText(formatTime(totalTime));
        startButton2.setText("Start");
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}