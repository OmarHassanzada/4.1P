package com.example.workout_timer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.EditText;
import android.os.CountDownTimer;
import android.media.MediaPlayer;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {

    private boolean IsWorkoutTimerRunning = false;
    private boolean IsRestTimerRunning = false;
    private EditText WorkoutDurationInput;
    private EditText RestDurationInput;
    private CountDownTimer WorkoutTimer;
    private CountDownTimer RestTimer;
    private Button StartStopBtn;
    private ProgressBar WorkoutProgressBar;
    private TextView WorkoutTimerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkoutDurationInput = findViewById(R.id.WorkoutInput);
        RestDurationInput = findViewById(R.id.RestingInput);
        StartStopBtn = findViewById(R.id.StartButton);
        WorkoutProgressBar = findViewById(R.id.WorkoutProgress);
        WorkoutTimerView = findViewById(R.id.RemainingWorkoutText);

        StartStopBtn.setBackgroundColor(Color.GREEN);

        // Set onClickListener for Start/Stop button
        StartStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputCheck();
            }
        });
    }

    private void InputCheck(){
        String CheckWorkoutDurationInput = WorkoutDurationInput.getText().toString().trim();
        String CheckRestDurationInput = RestDurationInput.getText().toString().trim();

        //checks if both values dont have input and throws warning
        if (CheckWorkoutDurationInput.isEmpty() || CheckRestDurationInput.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Workout and/Or Rest Phases", Toast.LENGTH_SHORT).show();
            return;
        }

        ClockCheck();
    }

    private void ClockCheck(){
        if (!IsWorkoutTimerRunning && !IsRestTimerRunning) {
            startWorkoutTimer();
        }
        else if (IsWorkoutTimerRunning) {
            IsWorkoutTimerRunning = false;
            SetWorkoutButton(WorkoutTimer, StartStopBtn);
        }
        else if (IsRestTimerRunning) {
            IsRestTimerRunning = false;
            SetWorkoutButton(RestTimer,StartStopBtn);
        }
    }

    private void SetWorkoutButton(CountDownTimer Timer, Button Butt){
        Timer.cancel();
        Butt.setText("Start");
        Butt.setBackgroundColor(Color.GREEN);
    }

    private void startWorkoutTimer() {
        int WorkoutDuration = Integer.parseInt(WorkoutDurationInput.getText().toString());
        WorkoutTimer = new CountDownTimer(WorkoutDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String WorkOut = "Workout Remaining: ";
                OnTick(millisUntilFinished, WorkOut);
            }

            @Override
            public void onFinish() {
                IsWorkoutTimerRunning = false;
                OnFinish(WorkoutTimerView, StartStopBtn,WorkoutProgressBar);
                startRestTimer();
            }
        }.start();
        IsWorkoutTimerRunning = true;
        StartStopBtn.setText("Stop");
        StartStopBtn.setBackgroundColor(Color.RED);
        WorkoutProgressBar.setMax(WorkoutDuration);
    }

    private void startRestTimer() {
        int RestDuration = Integer.parseInt(RestDurationInput.getText().toString());
        RestTimer = new CountDownTimer(RestDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String Rest = "Rest Remaining: ";
                OnTick(millisUntilFinished, Rest);
            }

            @Override
            public void onFinish() {
                IsRestTimerRunning = false;
                OnFinish(WorkoutTimerView, StartStopBtn,WorkoutProgressBar);
                startWorkoutTimer();
            }
        }.start();
        IsRestTimerRunning = true;
        StartStopBtn.setText("Stop");
        StartStopBtn.setBackgroundColor(Color.RED);
    }


    private void OnTick(long millisUntilFinished, String RestOrWorkout){
        long Seconds = millisUntilFinished / 1000;
        WorkoutTimerView.setText(RestOrWorkout + String.valueOf(Seconds));
        WorkoutProgressBar.setProgress((int) (millisUntilFinished / 1000));
    }

    private void OnFinish(TextView WorkoutView,Button button, ProgressBar progress){
        WorkoutView.setText("0");
        button.setText("Start");
        button.setBackgroundColor(Color.GREEN);
        progress.setProgress(0);
        // play sound to alert user
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ring);
        mediaPlayer.start();
    }
}
