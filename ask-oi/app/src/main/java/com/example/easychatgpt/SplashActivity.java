package com.example.easychatgpt;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2 saniye
    private static final int TOTAL_PROGRESS = 100;
    private static final int PROGRESS_DURATION = 2000; // �lerleme s�resi, milisaniye cinsinden

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        // �lerleme �ubu�unu ba�lat
        startProgressBar();

        // Belirli bir s�re sonra LoginActivity'e ge�i� yap
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }

    // �lerleme �ubu�unu ba�latan metot
    private void startProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setMax(TOTAL_PROGRESS); // Toplam ilerleme miktar�n� belirle

        // Animasyonlu ilerleme �ubu�u i�in ValueAnimator kullanma
        ValueAnimator animator = ValueAnimator.ofInt(0, TOTAL_PROGRESS);
        animator.setDuration(PROGRESS_DURATION); // �lerleme s�resini ayarla
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            progressBar.setProgress(progress); // �lerleme miktar�n� g�ncelle
        });
        animator.start(); // Animasyonu ba�lat
    }
}
