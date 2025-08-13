package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Week4 extends AppCompatActivity {

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

    private Button gotoWeek5;
    private CountDownTimer countDownTimer2;
    private boolean isRunning2 = false;

    // Other buttons
    private Button backButton, nextDayButton;
    private TextView dayLabel;

    // Separate times for each goal
    private int totalTime1;
    private int totalTime2;

    private int currentDay = 22; // Week 4 starts at day 22
    private final int TOTAL_DAYS = 28; // Week 4 goes to day 28
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week4);

        // Goal title text views
        goalTitle1 = findViewById(R.id.goalTitle1);
        goalTitle2 = findViewById(R.id.goalTitle2);

        // Retrieve selected goals
        ArrayList<String> selectedGoals = getIntent().getStringArrayListExtra("selectedGoals");
        if (selectedGoals != null && !selectedGoals.isEmpty()) {
            if (selectedGoals.size() > 0) goalTitle1.setText(selectedGoals.get(0));
            if (selectedGoals.size() > 1) goalTitle2.setText(selectedGoals.get(1));
        }

        // Retrieve the goal times from intent and halve them for Week 3
        ArrayList<Integer> goalTimes = getIntent().getIntegerArrayListExtra("goalTimes");
        if (goalTimes != null && goalTimes.size() >= 2) {
            totalTime1 = Math.max(60, ((goalTimes.get(0) * 3) / 4) * 60); // 3/4 of user time in seconds, at least 60 sec
            totalTime2 = Math.max(60, ((goalTimes.get(1) * 3) / 4) * 60);
        } else {
            totalTime1 = 300; // default 5 min
            totalTime2 = 300;
        }

        // Goal 1
        timerText1 = findViewById(R.id.timerText1);
        circularProgressBar1 = findViewById(R.id.circularProgressBar1);
        startButton1 = findViewById(R.id.startButton1);
        circularProgressBar1.setMax(totalTime1);
        circularProgressBar1.setProgress(totalTime1);
        timerText1.setText(formatTime(totalTime1));

        // Goal 2
        timerText2 = findViewById(R.id.timerText2);
        circularProgressBar2 = findViewById(R.id.circularProgressBar2);
        startButton2 = findViewById(R.id.startButton2);
        circularProgressBar2.setMax(totalTime2);
        circularProgressBar2.setProgress(totalTime2);
        timerText2.setText(formatTime(totalTime2));

        // Navigation
        backButton = findViewById(R.id.backButton);
        nextDayButton = findViewById(R.id.nextDayButton);
        dayLabel = findViewById(R.id.dayLabel);
        dayLabel.setText("Day " + currentDay);

        // Start Goal 1 timer
        startButton1.setOnClickListener(v -> {
            if (!isRunning1) {
                startTimer1();
                startButton1.setText("Pause");
            } else {
                pauseTimer1();
                startButton1.setText("Start");
            }
        });

        // Start Goal 2 timer
        startButton2.setOnClickListener(v -> {
            if (!isRunning2) {
                startTimer2();
                startButton2.setText("Pause");
            } else {
                pauseTimer2();
                startButton2.setText("Start");
            }
        });

        // Back to dashboard
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Week4.this, Screen4Activity.class);
            ArrayList<String> goalsToSend = getIntent().getStringArrayListExtra("selectedGoals");
            if (goalsToSend != null) intent.putStringArrayListExtra("selectedGoals", goalsToSend);

            ArrayList<String> reminders = getIntent().getStringArrayListExtra("reminders");
            if (reminders != null) intent.putStringArrayListExtra("reminders", reminders);

            startActivity(intent);
            finish();
        });

        // Next day
        nextDayButton.setOnClickListener(v -> {
            if (currentDay < TOTAL_DAYS) {
                currentDay++;
                dayLabel.setText("Day " + currentDay);
                resetTimers();
            }
        });

        gotoWeek5 = findViewById(R.id.gotoWeek5);
        gotoWeek5.setOnClickListener(v -> {
                    Intent intent = new Intent(Week4.this, Week5.class);
                    if (selectedGoals != null) {
                        intent.putStringArrayListExtra("selectedGoals", selectedGoals);
                    }
                    if (goalTimes != null) {
                        intent.putIntegerArrayListExtra("goalTimes", goalTimes);
                    }
                    ArrayList<String> reminders = getIntent().getStringArrayListExtra("reminders");
                    if (reminders != null) {
                        intent.putStringArrayListExtra("reminders", reminders);
                    }
            startActivity(intent);
        });
    }

    // Goal 1 timer
    private void startTimer1() {
        isRunning1 = true;
        countDownTimer1 = new CountDownTimer(totalTime1 * 1000, 1000) {
            int timeLeft = totalTime1;

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
        countDownTimer2 = new CountDownTimer(totalTime2 * 1000, 1000) {
            int timeLeft = totalTime2;

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

        circularProgressBar1.setProgress(totalTime1);
        timerText1.setText(formatTime(totalTime1));
        startButton1.setText("Start");

        circularProgressBar2.setProgress(totalTime2);
        timerText2.setText(formatTime(totalTime2));
        startButton2.setText("Start");
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}