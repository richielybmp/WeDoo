package com.espfullstack.wedoo;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.espfullstack.wedoo.session.SessionMannager;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String token = SessionMannager.getToken(getApplicationContext());
                Intent i;

                if (TextUtils.isEmpty(token)){
                    i = new Intent(SplashScreen.this, LoginActivity.class);
                }
                else{
                    i = new Intent(SplashScreen.this, MainActivity.class);
                }

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
