package edu.neu.madcourse.bharatvaidhyanathan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.AboutMeActivity;

public class MainActivity extends AppCompatActivity {



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

        final Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });

    }
}