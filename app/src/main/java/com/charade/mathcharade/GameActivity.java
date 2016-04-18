package com.charade.mathcharade;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends Activity {
    private TextView mTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTextField = (TextView) this.findViewById(R.id.game_timer);


        CountDownTimer myTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("Time Left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("Time's up!");
                //get the results screen activity
            }
        }.start();
    }
}
