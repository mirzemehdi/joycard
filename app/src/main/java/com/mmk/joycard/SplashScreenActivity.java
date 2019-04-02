package com.mmk.joycard;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar loadingProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadingProgress=(ProgressBar)findViewById(R.id.progressBar2);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (currentUser!=null) {
                    Intent intent = new Intent(SplashScreenActivity.this, PlacesActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Intent intent = new Intent(SplashScreenActivity.this, EntranceActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2000);





    }


}
