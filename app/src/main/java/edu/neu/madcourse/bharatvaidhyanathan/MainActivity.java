package edu.neu.madcourse.bharatvaidhyanathan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.AboutMeActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.DictObj;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private Intent dictionaryIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Setting the app title
        setTitle("Bharat Vaidhyanathan");

        // Here, thisActivity is the current activity


        // About me button
        final Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(i);
            }
        });

        // Generate error button
        final Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] arr = new int[5];
                arr[-1]++;
            }
        });

        // Ultimate Tic tac Button
        final Button b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.activities.MainActivity.class);
                startActivity(i);
            }
        });

        // Dictionary Button
        final Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize the dictionary intent
                dictionaryIntent = new Intent(MainActivity.this, edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.TestDictionaryActivity.class);
                //setup and start the progress dialog
                progress = ProgressDialog.show(MainActivity.this, "Loading", "Loading the Dictionary...", true, false);
                SearchThread searchThread = new SearchThread(4000);
                searchThread.start();


            }
        });


        // Word game - Scrobble Button
        final Button b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scrobble.activities.ScrobbleMainActivity.class);
                startActivity(i);
            }
        });


        //Quit button
        final Button bq = (Button) findViewById(R.id.buttonQuit);
        bq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });

    }



    private class SearchThread extends Thread {

        private int time;

        public SearchThread(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            DictObj.getInstance();
            //Thread.sleep(time);
            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //displaySearchResults(search);
                startActivity(dictionaryIntent);
                progress.dismiss();
            }
        };
    }

}