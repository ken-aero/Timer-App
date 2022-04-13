package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText taskName;
    TextView timeSpent;
    TextView timeSpentPrev;
    Timer timer;
    TimerTask timerTask;
    Double time = 00.00;
    Boolean timerStarted = false;
    String prevTaskMsg;

    // KEY CONSTANTS
    String TIME_SPENT_PREV = "TIME_SPENT_PREV";
    String CURRENT_TIME = "CURRENT TIME";
    String CURRENT_TIME_TEXT = "CURRENT_TIME_TEXT";
    String TASK_NAME = "TASK NAME";
    String TIMER_STARTED = "TIMER_STARTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskName = findViewById(R.id.editTextTask);
        timeSpent = findViewById(R.id.timeSpent);
        timeSpentPrev = findViewById(R.id.timeSpentPrev);
        timer = new Timer();

        if (savedInstanceState != null) {
            timeSpentPrev.setText(savedInstanceState.getString(TIME_SPENT_PREV));
            time = savedInstanceState.getDouble(CURRENT_TIME);
            timeSpent.setText(savedInstanceState.getString(CURRENT_TIME_TEXT));
            taskName.setText(savedInstanceState.getString(TASK_NAME));
            timerStarted = savedInstanceState.getBoolean(TIMER_STARTED);
            taskName.setEnabled(false);
            if (timerStarted) {
                // resume the timer
                timerStart();
            }
            if (!timerStarted && time == 0) {
                taskName.setEnabled(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TIME_SPENT_PREV, prevTaskMsg);
        outState.putDouble(CURRENT_TIME, time);
        outState.putString(CURRENT_TIME_TEXT, timeSpent.getText().toString());
        outState.putString(TASK_NAME, taskName.getText().toString());
        outState.putBoolean(TIMER_STARTED, timerStarted);
    }

    public void timerStartStopOnClick(View view) {
        String task = taskName.getText().toString();

        if (timerStarted == false && !task.isEmpty() && view.getId() == R.id.startBtn) {
            taskName.setEnabled(false);
            timerStarted = true;
            timerStart();
        }
        if (timerStarted == false && task.isEmpty() && view.getId() == R.id.startBtn) {
            taskName.setEnabled(true);
            Toast.makeText(MainActivity.this, "Please enter a task name to time", Toast.LENGTH_SHORT ).show();
        }
        if (view.getId() == R.id.pauseBtn && timerStarted == true) {
            timerStarted = false;
            timerTask.cancel();
        }
        if (view.getId() == R.id.stopBtn && !task.isEmpty() && timerTask != null) {
            prevTaskMsg = "You spent " + timeSpent.getText().toString() + " on " + task;
            timerStarted = false;
            timerTask.cancel();
            time = 00.00;
            timeSpent.setText("00:00:00");
            taskName.setText("");
            taskName.setEnabled(true);
            timeSpentPrev.setText(prevTaskMsg);
            Toast.makeText(MainActivity.this, "Task event saved", Toast.LENGTH_SHORT ).show();
        }
    }

    public void timerStart() {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        int round = (int) Math.round(time);
                        // 86400 == total seconds in a day 60 * 24 * 60
                        int seconds = ((round % 86400) % 3600) % 60;
                        int minutes = ((round % 86400) % 3600) / 60;
                        int hours = ((round % 86400) / 3600);
                        timeSpent.setText(String.format("%02d", hours)
                                + ":" + String.format("%02d", minutes)
                                + ":" + String.format("%02d", seconds));
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }
}