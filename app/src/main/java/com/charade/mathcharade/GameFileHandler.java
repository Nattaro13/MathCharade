package com.charade.mathcharade;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nat on 19/6/2016.
 */
public class GameFileHandler {
    private String filename;


    public GameFileHandler(String filename) {
        this.filename = filename;
    }


    public void readFileContents() {
        ArrayList<String> words = new ArrayList<>();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(filename));
            try {
                String x;
                while ( (x = br.readLine()) != null ) {
                    // printing out each line in the file
                    Log.w("word",x);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
