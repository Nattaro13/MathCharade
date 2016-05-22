package com.charade.mathcharade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends Activity implements SensorEventListener {
    private TextView mWord;
    private TextView mTextField;
    private TextView mVectorField;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float[] mRotationMatrix = new float[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String topic = intent.getStringExtra("topic");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        setContentView(R.layout.activity_game);

        //Text Field is for seeing how the rotation is changing.
        mVectorField = (TextView) this.findViewById(R.id.gyroscope_values);

        //text field is for loading the game words.
        //dummy: for now, we load the topic title.
        mWord = (TextView) this.findViewById(R.id.game_word);
        mWord.setText(topic);
        //text field is for the timer.
        mTextField = (TextView) this.findViewById(R.id.game_timer);

        //params for countdown timer: total time, interval
        CountDownTimer myTimer = new CountDownTimer(12000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("Time Left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("Time's up!");
                //get the results screen activity
                getResults();
            }
        }.start();
    }

    /**
     * Start the results screen.
     */
    public void getResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            mVectorField.setText(getVectorValues());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //nth to do
    }

    public String getVectorValues() {
        String stringRep = "";

        for(int i =0; i < mRotationMatrix.length; i++) {
            stringRep += mRotationMatrix[i];
            stringRep += ",";
        }
        return stringRep;
    }


    /**
     * Called when the back button is pressed.
     */
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


