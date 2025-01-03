package com.ayush.spacequiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ayush.spacequiz.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BaseFunctionality extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base_functionality);

        TextView timerText = findViewById(R.id.timer_text);
        long duration = TimeUnit.MINUTES.toMillis(1);  // 1 minute
        long ticksInterval = 1000;  // Update interval set to every second for simplicity

        CountDownTimer timer = new CountDownTimer(duration, ticksInterval) {

            public void onTick(long millisUntilFinished) {
                // Format the time left into minutes, seconds, and milliseconds
                String text = String.format(Locale.getDefault(), "%02d:%02d:%03d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)),
                        millisUntilFinished % 1000  // Correctly calculating milliseconds
                );
                timerText.setText(text);
            }

            public void onFinish() {
                timerText.setText("Finished!");
            }
        };

        timer.start();
    }
}