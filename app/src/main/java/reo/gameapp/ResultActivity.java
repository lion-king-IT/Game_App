package reo.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("PYON!",0);
        scoreLabel.setText(score + "");

        SharedPreferences share = getSharedPreferences("GAME_DATA",MODE_PRIVATE);
        int highScore = share.getInt("HIGH",0);

        if (score > highScore) {
            highScoreLabel.setText("HighScore:" + score);

            SharedPreferences.Editor edityan = share.edit();
            edityan.putInt("HIGH",score);
            edityan.apply();

        }else{
            highScoreLabel.setText("High Score :" + highScore);
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void again(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}