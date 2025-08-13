package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class Screen3Activity extends AppCompatActivity {

    private EditText reminderEditText1, reminderEditText2;
    private TimePicker timePicker1, timePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);

        // Bind views by correct IDs matching your XML
        reminderEditText1 = findViewById(R.id.reminder1);
        reminderEditText2 = findViewById(R.id.reminder2);
        timePicker1 = findViewById(R.id.timePicker1);
        timePicker2 = findViewById(R.id.timePicker2);

        ArrayList<String> selectedGoals = getIntent().getStringArrayListExtra("selectedGoals");

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            ArrayList<String> reminders = new ArrayList<>();

            // Reminder 1
            String reminder1 = reminderEditText1.getText().toString().trim();
            int hour1 = timePicker1.getHour();
            int minute1 = timePicker1.getMinute();
            if (!reminder1.isEmpty()) {
                reminders.add(reminder1 + " at " + formatTime(hour1, minute1));
                scheduleReminder(reminder1, hour1, minute1, 1);
            }

            // Reminder 2
            String reminder2 = reminderEditText2.getText().toString().trim();
            int hour2 = timePicker2.getHour();
            int minute2 = timePicker2.getMinute();
            if (!reminder2.isEmpty()) {
                reminders.add(reminder2 + " at " + formatTime(hour2, minute2));
                scheduleReminder(reminder2, hour2, minute2, 2);
            }

            Log.d("Screen3Activity", "Reminders: " + reminders);

            // Pass reminders and goals to next activity
            Intent intent = new Intent(Screen3Activity.this, Screen4Activity.class);
            intent.putStringArrayListExtra("selectedGoals", selectedGoals);
            intent.putStringArrayListExtra("reminders", reminders);
            startActivity(intent);
        });
    }

    private void scheduleReminder(String message, int hour, int minute, int requestCode) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("reminderMessage", message);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            Log.d("Screen3Activity", "Alarm scheduled for: " + message);
        } catch (Exception e) {
            Log.e("Screen3Activity", "Error scheduling reminder: ", e);
        }

    }

    private String formatTime(int hour, int minute) {
        String amPm = (hour >= 12) ? "PM" : "AM";
        int hour12 = (hour == 0 || hour == 12) ? 12 : hour % 12;
        String minuteStr = String.format("%02d", minute);
        return hour12 + ":" + minuteStr + " " + amPm;
    }
}