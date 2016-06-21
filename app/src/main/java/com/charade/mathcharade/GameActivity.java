package com.charade.mathcharade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends Activity implements SensorEventListener {
    private ArrayList<String> correctList = new ArrayList<>();
    private ArrayList<String> wrongList = new ArrayList<>();
    private ArrayList<String> wordList = new ArrayList<>();


    CountDownTimer myTimer;

    private TextView mWord;
    private TextView mTextField;
    private TextView mVectorField;

    private SensorManager sm;

    private float gravity[] = {
            0.0F, 0.0F, 0.0F
    };
    private float linear_acceleration[] = {
            0.0F, 0.0F, 0.0F
    };
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private float sensorX;
    private float sensorY;
    private float sensorZ;
    private long curTime;
    private long currentBuffer;

    private boolean isReady = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //fillList();

        Intent intent = getIntent();
        String topic = intent.getStringExtra("topic"); //use for files later

        String filename = topic + ".txt";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(filename)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                wordList.add(mLine);
            }
            Collections.shuffle(wordList);
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }


        sensorZ = 0.0F;
        sensorY = 0.0F;
        sensorX = 0.0F;

        setContentView(R.layout.activity_game);

        //Text Field is for seeing how the rotation is changing.
        mVectorField = (TextView) this.findViewById(R.id.gyroscope_values);

        //text field is for loading the game words.
        mWord = (TextView) this.findViewById(R.id.game_word);
        //text field is for the timer.
        mTextField = (TextView) this.findViewById(R.id.game_timer);

        //params for countdown timer: total time, interval
        myTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("Time Left: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                setEndText();
                unregister();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //get the results screen activity
                        getResults();
                        onDestroy();
                    }
                }, 2000);
            }
        }.start();
    }

    protected void onStart() {
        super.onStart();
        if (!wordList.isEmpty()) {
            mWord.setText(wordList.remove(0));
        }
    }

    private void fillList() {
        wordList.add("Tarjan's Algorithm");
        wordList.add("Kolmogorov Complexity");
        wordList.add("Euler");
        wordList.add("Sieve of Eratosthenes");
        wordList.add("AVL Tree");
        wordList.add("tautology");
        wordList.add("Binomial Heap");
        wordList.add("modus ponens");
        wordList.add("Power Set");
        wordList.add("Cantor's Diagonalisation");
        wordList.add("Kruskal's Algorithm");
        wordList.add("Bezout's Identity");
        wordList.add("well ordered");
        wordList.add("Git Commit");
        wordList.add("fork");
        wordList.add("pigeonhole principle");

        Collections.shuffle(wordList);

    }

    public void setEndText() {
        mWord.setText("Time's up!");
        mTextField.setText("");
    }

    /**
     * Start the results screen.
     */
    public void getResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("correct", correctList);
        intent.putExtra("wrong", wrongList);
        startActivity(intent);
    }


    /**
     * Called when the back button is pressed.
     */
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        onDestroy();
    }

    protected void unregister() {
        sm.unregisterListener(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
        Log.w("TIMER", "Timer stopped");
        Log.w("DESTROY", "Destroying Game.java");
        sm.unregisterListener(this);
    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
        myTimer.cancel();
    }

    protected void onStop() {
        super.onStop();
        myTimer.cancel();
        sm.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        Log.w("Resume", "Resume");
        //initTimer(timeRemaining);
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sm.getSensorList(1).size() != 0) {
            Sensor sensor = sm.getSensorList(1).get(0);
            sm.registerListener(this, sensor, 2);
            //Toast.makeText(this, "Started Rotation Sensor", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Rotation Sensor", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorX = sensorEvent.values[0];
        sensorY = sensorEvent.values[1];
        sensorZ = sensorEvent.values[2];

        /*
        gravity[0] = gravity[0] * 0.8F + sensorEvent.values[0] * 0.2F;
        gravity[1] = gravity[1] * 0.8F + sensorEvent.values[1] * 0.2F;
        gravity[2] = gravity[2] * 0.8F + sensorEvent.values[2] * 0.2F;
        linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
        linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
        linear_acceleration[2] = sensorEvent.values[2] - gravity[2];
        curTime = System.currentTimeMillis();
        currentBuffer = System.currentTimeMillis();
        */
        if (isReady) {
            if (sensorZ > 8) {
                isReady = false;
                String word = mWord.getText().toString();
                if (word.equals("Time's up!")) {
                    return;
                }
                if (correctList.contains(word)) {
                    return;
                }
                correctList.add(word);
                mVectorField.setText("Correct!");
                mVectorField.setTextColor(Color.parseColor("#00a652"));

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //Load the new Game Word
                        mVectorField.setText("");
                        if (!wordList.isEmpty()) {
                            mWord.setText(wordList.remove(0));
                        }
                        isReady = true;
                    }
                }, 1000);
            } else if (sensorZ < -8) {
                isReady = false;
                String word = mWord.getText().toString();
                if (word.equals("Time's up!")) {
                    return;
                }
                if (wrongList.contains(word)) {
                    return;
                }
                wrongList.add(word);
                mVectorField.setText("Pass");
                mVectorField.setTextColor(Color.parseColor("#ffcc00"));

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //load the new Game Word
                        mVectorField.setText("");
                        if (!wordList.isEmpty()) {
                            mWord.setText(wordList.remove(0));
                            isReady = true;
                        }
                    }
                }, 1000);
            }
        }

        /*
        if ((double)Math.abs(sensorZ) < 1.7D && !isReady && currentBuffer - lastBuffer > bufferVariable) {
            isReady = true;
            lastReadyBuffer = System.currentTimeMillis();
            Log.w("Set True", "True");
        }
        if (isFinished || !isReady || (double)Math.abs(linear_acceleration[2]) <= 4.5D || currentBuffer - lastReadyBuffer <= readyBuffer) {
            return;
        } else {
            lastUpdate = curTime;
            if (sensorZ <= 6F || !isReady || (double) linear_acceleration[2] <= 4.5D) {
                _L4:
                if (sensorX > 10F && (double) sensorZ < 0.0D && sensorZ > -8F && isReady && (double) linear_acceleration[2] < -3D) {
                    Log.w("X-Special", (new StringBuilder(String.valueOf(sensorX))).toString());
                    Log.w("Y-Special", (new StringBuilder(String.valueOf(sensorY))).toString());
                    Log.w("Z-Special", (new StringBuilder(String.valueOf(sensorZ))).toString());
                    Log.w("Z-Special", (new StringBuilder(String.valueOf(sensorZ))).toString());
                    Log.w("More than 5", (new StringBuilder(String.valueOf(linear_acceleration[2]))).toString());
                    Log.w("Block 2", "Block 2");
                    lastBuffer = System.currentTimeMillis();
                    if (block == 1) {
                        bufferVariable = 800L;
                    } else {
                        bufferVariable = 600L;
                    }
                    block = 2;
                    isReady = false;
                    gameText.setText("Pass");
                    timerText.setVisibility(8);
                    gameText.setTextColor(Color.parseColor("#ffc21e"));
                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#ff0000"));
                    wordList.add(currentWord);
                    resultList.add("wrong");
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            gameText.setTextColor(Color.parseColor("#793141"));
                            getWindow().getDecorView().setBackgroundColor(Color.parseColor("#ffcc00"));
                            int i = (new Random()).nextInt(selectedList.size());
                            currentWord = (String) selectedList.get(i);
                            gameText.setText(currentWord);
                            timerText.setVisibility(0);
                            selectedList.remove(i);
                        }
                    }, 500L);
                } else if ((double) sensorZ < -4.7000000000000002D && (double) sensorX < 9.5D && isReady && (double) linear_acceleration[2] < -4.5D) {
                    lastBuffer = System.currentTimeMillis();
                    bufferVariable = 600L;
                    block = 3;
                    Log.w("Block 3", "Block 3");
                    Log.w("X-Correct", (new StringBuilder(String.valueOf(sensorX))).toString());
                    Log.w("Y-Correct", (new StringBuilder(String.valueOf(sensorY))).toString());
                    Log.w("Z-Correct", (new StringBuilder(String.valueOf(sensorZ))).toString());
                    Log.w("More than 5", (new StringBuilder(String.valueOf(linear_acceleration[2]))).toString());
                    isReady = false;
                    wordList.add(currentWord);
                    resultList.add("correct");
                    gameText.setText("Correct");
                    timerText.setVisibility(8);
                    gameText.setTextColor(Color.parseColor("#ffc21e"));
                    getWindow().getDecorView().setBackgroundColor(Color.parseColor("#00a652"));
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            gameText.setTextColor(Color.parseColor("#793141"));
                            getWindow().getDecorView().setBackgroundColor(Color.parseColor("#ffcc00"));
                            int i = (new Random()).nextInt(selectedList.size());
                            currentWord = (String) selectedList.get(i);
                            gameText.setText(currentWord);
                            timerText.setVisibility(0);
                            selectedList.remove(i);
                        }
                    }, 500L);
                }
            } else {

                Log.w("X-Incorrect", (new StringBuilder(String.valueOf(sensorX))).toString());
                Log.w("Y-Incorrect", (new StringBuilder(String.valueOf(sensorY))).toString());
                Log.w("Z-Incorrect", (new StringBuilder(String.valueOf(sensorZ))).toString());
                Log.w("More than 5", (new StringBuilder(String.valueOf(linear_acceleration[2]))).toString());
                Log.w("Block 1", "Block 1");
                lastBuffer = System.currentTimeMillis();
                bufferVariable = 600L;
                block = 1;
                isReady = false;

                gameText.setText("Pass");
                timerText.setVisibility(8);
                gameText.setTextColor(Color.parseColor("#ffc21e"));
                getWindow().getDecorView().setBackgroundColor(Color.parseColor("#ff0000"));
                wordList.add(currentWord);
                resultList.add("wrong");
                (new Handler()).postDelayed(new Runnable() {

                    final GameActivity this;

                    public void run() {
                        gameText.setTextColor(Color.parseColor("#793141"));
                        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#ffcc00"));
                        int i = (new Random()).nextInt(selectedList.size());
                        currentWord = (String) selectedList.get(i);
                        gameText.setText(currentWord);
                        timerText.setVisibility(0);
                        selectedList.remove(i);
                    }


                    {
                        this = GameActivity.this;
                        super();
                    }
                }, 500L);
            }
        }

        if (true) {
            if (selectedList.size() == 0) {
                finish();
            }
        } else {
            if (isFinished) {
                finish();
            }
        } */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


