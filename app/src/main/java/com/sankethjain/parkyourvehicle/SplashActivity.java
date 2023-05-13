package com.sankethjain.parkyourvehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN= 3000;
    Animation topAnim,bottomAnim,centerAnim,sideAnim;
    ImageView guy,car,pin;
    TextView logoName;
    LinearLayout slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //animation hooks
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        centerAnim= AnimationUtils.loadAnimation(this,R.anim.center_anim);
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);

        //image hooks
        guy = findViewById(R.id.guy);
        car = findViewById(R.id.car);
        pin = findViewById(R.id.pin);

        //text hooks
        logoName = findViewById(R.id.logo_name);

        //layout hook
        slogan = findViewById(R.id.solgan);

        //setting animations
        logoName.setAnimation(topAnim);
        guy.setAnimation(sideAnim);
        car.setAnimation(centerAnim);
        pin.setAnimation(centerAnim);
        slogan.setAnimation(bottomAnim);

        //delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                Pair[] pairs = new Pair[4];
                pairs[0]= new Pair<View,String>(logoName,"logo_name");
                pairs[1]= new Pair<View,String>(guy,"guy");
                pairs[2]= new Pair<View,String>(car,"car");
                pairs[3]= new Pair<View,String>(pin,"pin");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);

    }
}
