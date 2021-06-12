package com.example.samir_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView circleImageView;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleImageView=findViewById(R.id.chat);

        textView=findViewById(R.id.chatName);

        Animation animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.removal_design);

        textView.setAnimation(animation);

        CountDownTimer countDownTimer=new CountDownTimer(8000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                Intent intent=new Intent(MainActivity.this,PhoneOrEmailChooserActivity.class);

                startActivity(intent);

            }
        };

        countDownTimer.start();

    }
}