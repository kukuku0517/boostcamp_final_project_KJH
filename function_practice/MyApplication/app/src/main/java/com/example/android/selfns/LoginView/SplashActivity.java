package com.example.android.selfns.LoginView;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.example.android.selfns.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AwesomeSplash {

    @BindView(R.id.splash_layout)
    View view;
    @BindView(R.id.splash_s)
    TextView tvS;
    @BindView(R.id.splash_elf)
    View tvElf;
    @BindView(R.id.splash_ns)
    View tvNS;


    @Override
    public void initSplash(ConfigSplash configSplash) {

			/* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logo2); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
//        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
//        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
//        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
//        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
//        configSplash.setAnimPathStrokeDrawingDuration(3000);
//        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
//        configSplash.setPathSplashStrokeColor(R.color.accent); //any color you want form colors.xml
//        configSplash.setAnimPathFillingDuration(3000);
//        configSplash.setPathSplashFillColor(R.color.Wheat); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("나의 일상을 기록하다");
        configSplash.setTitleTextColor(R.color.com_facebook_share_button_text_color);
        configSplash.setTitleTextSize(14f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
//        configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        //transit to another activity here
        //or do whatever you want
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        ButterKnife.bind(this);
//
//        Animation ani = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//        view.startAnimation(ani);
//
//        ani.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            //애니메이션이 끝나고 화면 이동
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Animation ani2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_elf);
//                tvElf.setVisibility(View.VISIBLE);
//                tvElf.startAnimation(ani2);
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                }, 500);
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

}
