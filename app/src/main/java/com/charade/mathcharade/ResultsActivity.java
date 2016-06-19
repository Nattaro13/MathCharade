package com.charade.mathcharade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    private TextView correctField;
    private TextView wrongField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        ArrayList<String> correctList = intent.getStringArrayListExtra("correct");
        ArrayList<String> wrongList = intent.getStringArrayListExtra("wrong");

        String correctString = "";
        String wrongString = "";

        for(int i=0; i < correctList.size(); i++) {
            correctString += correctList.get(i);
            correctString += "\n";
        }

        for(int i=0; i < wrongList.size(); i++) {
            wrongString += wrongList.get(i);
            wrongString += "\n";
        }

        setContentView(R.layout.activity_results);

        //Text Field is for seeing how the rotation is changing.
        correctField = (TextView) this.findViewById(R.id.correct);
        wrongField = (TextView) this.findViewById(R.id.wrong);

        correctField.setText(correctString);
        wrongField.setText(wrongString);

    }

    /**
     * Called when the back button is pressed.
     */
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
