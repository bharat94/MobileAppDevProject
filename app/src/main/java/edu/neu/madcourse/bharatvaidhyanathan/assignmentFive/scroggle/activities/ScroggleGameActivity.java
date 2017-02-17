/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.fragments.ScroggleControlFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.fragments.ScroggleGameFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.fragments.ScroggleTimerFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.Tile;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.fragments.GameFragment;

public class ScroggleGameActivity extends Activity {
   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   private MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();
   private GameFragment mGameFragment;
   private ScroggleTimerFragment mTimerFragment;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_scroggle_game);
      boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
      Log.d("UT3", "restore = " + restore);
      mTimerFragment = (ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer);
   }

   @Override
   protected void onResume() {
      super.onResume();
      mMediaPlayer = MediaPlayer.create(this, R.raw.frankum_loop001e);
      mMediaPlayer.setLooping(true);
      mMediaPlayer.start();
   }

   public void displayword(String word)
   {
      mTimerFragment.displayWord(word);
   }

   @Override
   protected void onPause() {
      super.onPause();
      mHandler.removeCallbacks(null);
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
   }


   public void proceedToPhase2()
   {
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Please select one cell from each grid to proceed");
      builder1.setCancelable(false);

      builder1.setPositiveButton(
              "Ok",
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).updateUIForTransitionPhase();
                    ((ScroggleControlFragment) getFragmentManager().findFragmentById(R.id.fragment_game_controls)).changeDoneButton();
                    ((ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).setTimerDone();
                 }
              });
      builder1.show();

   }

   public void initializePhase2(){
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Yay! Solve the Boggle now!");
      builder1.setCancelable(false);

      builder1.setPositiveButton(
              "Proceed",
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    ((GridLayout)findViewById(R.id.phase1)).setVisibility(View.GONE);
                    ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).updateUIForPhaseTwo();
                    ((GridLayout)findViewById(R.id.phase2)).setVisibility(View.VISIBLE);

                 }
              });
      builder1.show();
   }

}
