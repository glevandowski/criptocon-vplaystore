package levandowski.primao.criptocon;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import levandowski.primao.criptocon.util.CheckInternet;

import static levandowski.primao.criptocon.MainActivity.coinArrayList;
import static levandowski.primao.criptocon.MainActivity.coinSpinner;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 3000;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //progress = findViewById(R.id.splash_progressbar);
        //progress.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!CheckInternet.isConnected(SplashActivity.this)) {
                    new CheckInternet(SplashActivity.this).dialogIfOffline();
                }else {
                    Intent it = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(it);
                    //          progress.setVisibility(View.INVISIBLE);
                    finish();}
            }
        },SPLASH_SCREEN_TIME_OUT);

    }
}