package edu.neu.madcourse.bharatvaidhyanathan;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle.Constants;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleMainActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.AboutMeActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle.CommunicationActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.DictObj;

public class MainActivity extends AppCompatActivity {

    //private ProgressDialog progress;
    //private Intent dictionaryIntent;


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
                //dictionaryIntent = new Intent(MainActivity.this, edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.TestDictionaryActivity.class);
                //setup and start the progress dialog
                //progress = ProgressDialog.show(MainActivity.this, "Loading", "Loading the Dictionary...", true, false);
                SearchThread searchThread = new SearchThread(new Intent(MainActivity.this, edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.TestDictionaryActivity.class), ProgressDialog.show(MainActivity.this, "Loading", "Loading the Dictionary...", true, false));
                searchThread.start();


            }
        });


        // Word game - Scroggle Button
        final Button b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this, ScroggleMainActivity.class);
                //progress = ProgressDialog.show(MainActivity.this, "Loading", "Loading the Word Game...", true, false);
                SearchThread searchThread = new SearchThread(new Intent(MainActivity.this, ScroggleMainActivity.class), ProgressDialog.show(MainActivity.this, "Loading", "Loading the Word Game...", true, false));
                searchThread.start();
            }
        });


        // Communication Button
        final Button b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this, edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.CommunicationActivity.class);
                //startActivity(i);
                if(isNetworkAvailable()) {
                    Intent i = new Intent(MainActivity.this, CommunicationActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Unable to connect to the internet", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Multiplayer Scroggle Button
        final Button b7 = (Button) findViewById(R.id.button7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    Intent i = new Intent(MainActivity.this, MultiplayerScroggleActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Unable to connect to the internet", Toast.LENGTH_LONG).show();
                }
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

        private Intent intent;
        private ProgressDialog progressDialog;

        public SearchThread(Intent intent, ProgressDialog progressDialog) {
            this.intent = intent;
            this.progressDialog = progressDialog;
        }

        @Override
        public void run() {
            DictObj.getInstance();
            //Thread.sleep(4000);
            handler.sendEmptyMessage(0);
        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                //displaySearchResults(search);
                startActivity(intent);
                progressDialog.dismiss();
            }
        };
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}