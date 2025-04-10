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
    private static final int PROGRESS_DURATION = 2000; // Ýlerleme süresi, milisaniye cinsinden

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        // Ýlerleme çubuðunu baþlat
        startProgressBar();

        // Belirli bir süre sonra LoginActivity'e geçiþ yap
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }

    // Ýlerleme çubuðunu baþlatan metot
    private void startProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setMax(TOTAL_PROGRESS); // Toplam ilerleme miktarýný belirle

        // Animasyonlu ilerleme çubuðu için ValueAnimator kullanma
        ValueAnimator animator = ValueAnimator.ofInt(0, TOTAL_PROGRESS);
        animator.setDuration(PROGRESS_DURATION); // Ýlerleme süresini ayarla
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            progressBar.setProgress(progress); // Ýlerleme miktarýný güncelle
        });
        animator.start(); // Animasyonu baþlat
    }
}
