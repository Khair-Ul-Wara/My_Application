package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnGetStarted = findViewById(R.id.button);

        // Initialize MediaPlayer with sound from res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.clickable);

        btnGetStarted.setOnClickListener(v -> {
            // Play click sound
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }

            // Original navigation logic
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
