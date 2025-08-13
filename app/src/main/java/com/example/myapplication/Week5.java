package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Week5 extends AppCompatActivity {

    private TextView goalTitle1, goalTitle2;
    private TextView timerText1, timerText2;
    private ProgressBar circularProgressBar1, circularProgressBar2;
    private Button startButton1, startButton2;
    private Button gotoWeek4, backButton, nextDayButton;
    private TextView dayLabel;
    private ImageView checkmark;  // Declare checkmark as field

    private CountDownTimer countDownTimer1, countDownTimer2;
    private boolean isRunning1 = false, isRunning2 = false;

    private int totalTime1, totalTime2;
    private int currentDay = 28; // Week 5 starts at day 28
    private final int TOTAL_DAYS = 34; // Week 5 goes to day 34

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week5);

        checkmark = findViewById(R.id.checkmark); // Initialize checkmark

        goalTitle1 = findViewById(R.id.goalTitle1);
        goalTitle2 = findViewById(R.id.goalTitle2);

        ArrayList<String> selectedGoals = getIntent().getStringArrayListExtra("selectedGoals");
        if (selectedGoals != null && !selectedGoals.isEmpty()) {
            if (selectedGoals.size() > 0) goalTitle1.setText(selectedGoals.get(0));
            if (selectedGoals.size() > 1) goalTitle2.setText(selectedGoals.get(1));
        }

        ArrayList<Integer> goalTimes = getIntent().getIntegerArrayListExtra("goalTimes");
        if (goalTimes != null && goalTimes.size() >= 2) {
            totalTime1 = Math.max(60, (goalTimes.get(0)) * 60);
            totalTime2 = Math.max(60, (goalTimes.get(1)) * 60);
        } else {
            totalTime1 = 300;
            totalTime2 = 300;
        }

        timerText1 = findViewById(R.id.timerText1);
        circularProgressBar1 = findViewById(R.id.circularProgressBar1);
        startButton1 = findViewById(R.id.startButton1);
        circularProgressBar1.setMax(totalTime1);
        circularProgressBar1.setProgress(totalTime1);
        timerText1.setText(formatTime(totalTime1));

        timerText2 = findViewById(R.id.timerText2);
        circularProgressBar2 = findViewById(R.id.circularProgressBar2);
        startButton2 = findViewById(R.id.startButton2);
        circularProgressBar2.setMax(totalTime2);
        circularProgressBar2.setProgress(totalTime2);
        timerText2.setText(formatTime(totalTime2));

        backButton = findViewById(R.id.backButton);
        nextDayButton = findViewById(R.id.nextDayButton);
        dayLabel = findViewById(R.id.dayLabel);
        dayLabel.setText("Day " + currentDay);

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

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Week5.this, Screen4Activity.class);
            ArrayList<String> goalsToSend = getIntent().getStringArrayListExtra("selectedGoals");
            if (goalsToSend != null) intent.putStringArrayListExtra("selectedGoals", goalsToSend);

            ArrayList<String> reminders = getIntent().getStringArrayListExtra("reminders");
            if (reminders != null) intent.putStringArrayListExtra("reminders", reminders);

            startActivity(intent);
            finish();
        });

        nextDayButton.setOnClickListener(v -> {
            if (currentDay < TOTAL_DAYS) {
                currentDay++;
                dayLabel.setText("Day " + currentDay);
                resetTimers();
            }
        });

        Button lockInHabitBtn = findViewById(R.id.lockInHabitBtn);
        lockInHabitBtn.setOnClickListener(v -> {

            showCheckmarkAnimation();
        });
    }

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

    private void showCheckmarkAnimation() {
        checkmark.setVisibility(View.VISIBLE);
        checkmark.setScaleX(0f);
        checkmark.setScaleY(0f);
        checkmark.setAlpha(0f);

        checkmark.animate()
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(300)
                .withEndAction(() ->
                        checkmark.animate().alpha(0f).setDuration(300).withEndAction(() ->
                                checkmark.setVisibility(View.GONE)
                        ).start()
                ).start();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
}
