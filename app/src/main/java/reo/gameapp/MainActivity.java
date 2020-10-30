package reo.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel, startLabel;
    private ImageView box, orange, pink, black, droid;

    //サイズ
    private int frameHeight, boxSize,screenWidth,screenHeight;

    //位置
    private float boxY,orangeX,orangeY,pinkX,pinkY,blackX,blackY,droidX,droidY;

    //スピード
    private int boxSpeed,orangeSpeed,pinkSpeed,droidSpeed,blackSpeed;

    //Score
    private int score = 0;

    //Handler & Timer
    private Handler hana = new Handler();
    private Timer tm = new Timer();

    //Status
    private boolean action_flg = false;
    private boolean start_flg = false;

    //Sound
    private SoundPlayer walkMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        walkMan = new SoundPlayer(this);
        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        black = findViewById(R.id.black);
        droid = findViewById(R.id.droid);

        //Screen Size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        boxSpeed = Math.round(screenHeight / 100f);
        orangeSpeed = Math.round(screenWidth / 50f);
        pinkSpeed = Math.round(screenWidth / 80f);
        droidSpeed = Math.round(screenWidth / 100f);
        blackSpeed = Math.round(screenWidth / 20f);

        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        black.setX(-8.0f);
        black.setY(-900.0f);
        droid.setX(-60.0f);
        droid.setY(1.0f);

        scoreLabel.setText("Score : 0");

    }

    public void changePos() {

        hitCheck();

        // Orange
        orangeX -= orangeSpeed;
        if (orangeX < 0) {
            orangeX = screenWidth + 20;
            orangeY = (float) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);


        //Black
        blackX -= blackSpeed;
        if (blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (float)Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);


        //Pink
        pinkX -= pinkSpeed;
        if (pinkX < 0) {
            pinkX = screenWidth + 5000;
            pinkY = (float)Math.floor(Math.random() *(frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //droid
        droidX -= droidSpeed;
        if (droidX < 0) {
            droidX = screenWidth + screenWidth + 100;
            droidY = (float)Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        droid.setX(droidX);
        droid.setY(droidY);


        //Box
        if (action_flg) {
            boxY -= boxSpeed;

        } else {
            boxY += boxSpeed;
        }

        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score :" + score );
    }

    public void hitCheck() {
        //Orange
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        if (hitStatus(orangeCenterX,orangeCenterY)) {
            orangeX = -10.0f;
            score += 10;
            walkMan.playHitSound();
        }


        //Pink
        float pinkCenterX = pinkX + pink.getWidth() / 2;
        float pinkCenterY = pinkY + pink.getHeight() /2;

        if (hitStatus(pinkCenterX,pinkCenterY)) {

            pinkX = -10.0f;
            score += 30;
            walkMan.playHitSound();
        }


        //droid
        float droidCenterX = droidX + droid.getWidth() / 2;
        float droidCenterY = droidY + droid.getHeight() / 2;

        if (hitStatus(droidCenterX,droidCenterY)) {

            droidX = -10.0f;
            score += 200;
            walkMan.playHitSound();
        }


        //Black
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (hitStatus(blackCenterX,blackCenterY)){

            walkMan.playOverSound();

            //Game Over!
            if (tm != null){
                tm.cancel();
                tm = null;
            }

            //結果画面へ
            Intent pyon = new Intent(getApplicationContext(), ResultActivity.class);
            pyon.putExtra("PYON!",score);
            startActivity(pyon);
        }

    }



    public boolean hitStatus(float centerX, float centerY) {
        return (0 <= centerX && centerX <= boxSize &&
                boxY <= centerY && centerY <= boxY + boxSize) ? true : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent momo) {
        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            tm.schedule(new TimerTask() {
                @Override
                public void run() {
                    hana.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        } else {
            if (momo.getAction() == momo.ACTION_DOWN) {
                action_flg = true;

            } else if (momo.getAction() == momo.ACTION_UP) {
                action_flg = false;

            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}