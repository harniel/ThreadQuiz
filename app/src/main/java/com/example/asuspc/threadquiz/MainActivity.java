package com.example.asuspc.threadquiz;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Switch mSwitch;
    private TextView mTvScore;
    private TextView mTvCountdown;
    private TextView mTvColor;
    private Button mBtnTrue;
    private Button mBtnFalse;

    private int score = 0;
    private String[] colorText={"Red", "Green", "Blue"};
    private int[] colorBackground = {
            Color.rgb(255, 0, 0), //red
            Color.rgb(0, 255, 0), //green
            Color.rgb(0, 0, 255)}; //blue
    private int colorT;
    private int colorB;
    private Random rndT;
    private Random rndB;
    private Thread run;
    private Handler handler;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwitch = (Switch) findViewById(R.id.on_off);
        mTvScore = (TextView) findViewById(R.id.txtScore);
        mTvColor = (TextView) findViewById(R.id.color);
        mBtnTrue = (Button) findViewById(R.id.btmTrue);
        mBtnFalse = (Button) findViewById(R.id.btnFalse);
        mTvCountdown = (TextView) findViewById(R.id.countdown);
        mSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                run = new Thread(new changeTheColor());
                run.start();

                if (mBtnFalse.isEnabled()){
                    mBtnTrue.setEnabled(false);
                    mBtnFalse.setEnabled(false);

                    try {
                        run.join();
                    } catch (InterruptedException e) {
                        return;
                    }
                }else{
                    mBtnTrue.setEnabled(true);
                    mBtnFalse.setEnabled(true);

                    rndT = new Random();
                    rndB = new Random();
                    colorT = rndT.nextInt(3);
                    colorB = rndB.nextInt(3);
                    mTvCountdown.setBackgroundColor(colorBackground[colorB]);
                    mTvColor.setText(colorText[colorT]);
                }
            }
        });

        mBtnTrue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    run.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (colorT == colorB){
                    score++;
                }
                else{
                    score--;
                }
                rndT = new Random();
                rndB = new Random();
                colorT = rndT.nextInt(3);
                colorB = rndB.nextInt(3);
                mTvCountdown.setBackgroundColor(colorBackground[colorB]);
                mTvColor.setText(colorText[colorT]);
                mTvScore.setText(""+score);
                run = new Thread(new changeTheColor());
                run.start();
            }
        });

        mBtnFalse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    run.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (colorT != colorB){
                    score++;
                }else{
                    score--;
                }
                rndT = new Random();
                rndB = new Random();
                colorT = rndT.nextInt(3);
                colorB = rndB.nextInt(3);
                mTvCountdown.setBackgroundColor(colorBackground[colorB]);
                mTvColor.setText(colorText[colorT]);
                mTvScore.setText(""+score);
                run = new Thread(new changeTheColor());
                run.start();
            }
        });



    }
    private class changeTheColor implements Runnable{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CountDownTimer(10100, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    mTvCountdown.setText(""+millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    mTvCountdown.setText("0");
                                    if (score > 0){
                                        score-=1;
                                        mTvScore.setText(""+score);
                                    }else{
                                        mTvScore.setText(""+score);
                                    }
                                }
                            }.start();

                        }
                    });
                }
            });
        }
    }
}
