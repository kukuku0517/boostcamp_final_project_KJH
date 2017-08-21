package com.example.android.selfns.LoginView;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.android.selfns.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_layout)
    View view;
    @BindView(R.id.splash_s)
    TextView tvS;
    @BindView(R.id.splash_elf)
    View tvElf;
    @BindView(R.id.splash_ns)
    View tvNS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation ani = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        view.startAnimation(ani);

        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //애니메이션이 끝나고 화면 이동
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation ani2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_elf);
                tvElf.setVisibility(View.VISIBLE);
                tvElf.startAnimation(ani2);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }, 1000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
