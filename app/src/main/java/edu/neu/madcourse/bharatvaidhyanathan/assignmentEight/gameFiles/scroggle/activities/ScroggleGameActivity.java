/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.HashSet;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.fragments.ScroggleControlFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.fragments.ScroggleGameFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.fragments.ScroggleTimerFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.fragments.GameFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.GlobDict;

import static edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleMainActivity.MUSIC_ON;

public class ScroggleGameActivity extends Activity {

   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   public static MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();
   private GameFragment mGameFragment;
   private ScroggleTimerFragment mTimerFragment;
   private View mGameView;
   private View mPauseScreen;
   private Button mResumeButton;
   HashSet<String> hs = new HashSet<>();
   private String words;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      words = this.getIntent().getExtras().getBundle("value").getString("words");

      setContentView(R.layout.a8_activity_scroggle_game);
      mResumeButton = (Button) findViewById(R.id.resume);
      mGameView = findViewById(R.id.game_layout);
      mPauseScreen = findViewById(R.id.pause_layout);

      mResumeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            mGameView.setVisibility(View.VISIBLE);
            mPauseScreen.setVisibility(View.GONE);
            mTimerFragment.initializeTimer(ScroggleTimerFragment.remainingTime);
         }
      });

      boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
      Log.d("UT3", "restore = " + restore);
      mTimerFragment = (ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer);
   }


   public void pauseMusic()
   {
      if(MUSIC_ON) {
         {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
         }
      }
   }

   public void startMusic()
   {
      if(!MUSIC_ON) {
         mMediaPlayer = MediaPlayer.create(this, R.raw.happy_song);
         mMediaPlayer.setLooping(true);
         mMediaPlayer.start();
      }
   }

   public void pausetheGame()
   {
      ScroggleTimerFragment.ct.cancel();
      mGameView.setVisibility(View.GONE);
      mPauseScreen.setVisibility(View.VISIBLE);
   }

   @Override
   protected void onResume() {
      super.onResume();
      if(MUSIC_ON) {
         mMediaPlayer = MediaPlayer.create(this, R.raw.happy_song);
         mMediaPlayer.setLooping(true);
         mMediaPlayer.start();
      }
   }


   public void displayword(String word)
   {
      mTimerFragment.displayWord(word);
   }

   public void setScore(int score)
   {
      mTimerFragment.setScore(score);
   }

   @Override
   protected void onPause() {
      super.onPause();
      mHandler.removeCallbacks(null);
      if(MUSIC_ON) {
         mMediaPlayer.stop();
         mMediaPlayer.reset();
         mMediaPlayer.release();
      }
   }



   public void proceedToPhase2()
   {
      ScroggleTimerFragment.ct.cancel();
      mTimerFragment.removeTimerText();
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Please select one cell from each grid to proceed");
      builder1.setCancelable(false);

      builder1.setPositiveButton(
              "Ok",
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    ((ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).displayWord("");
                    ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).updateUIForTransitionPhase();
                    ((ScroggleControlFragment) getFragmentManager().findFragmentById(R.id.fragment_game_controls)).setDonePhase(1);
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
                     mTimerFragment.initializeTimer(90000);
                    ((GridLayout)findViewById(R.id.phase1)).setVisibility(View.GONE);
                    ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).updateUIForPhaseTwo();
                    ((ScroggleControlFragment) getFragmentManager().findFragmentById(R.id.fragment_game_controls)).setDonePhase(2);
                    ((ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).displayWord("");
                    ((GridLayout)findViewById(R.id.phase2)).setVisibility(View.VISIBLE);

                 }
              });
      builder1.show();
   }

   public void computeBoggleScore(){

      ((ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).recomputeScore();
      int cur_score = ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore();
      String word = ((ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).getWord();

      if(word.length()>2 && !hs.contains(word) && GlobDict.getInstance(this).search(word)) {
         hs.add(word);
         int new_score = cur_score + (word.length() * 10);

         ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).setScore(new_score);
         ((ScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).setScore(new_score);
      }

      //System.out.println("Reset");
      resetBoard();
   }

   public void resetBoard(){
      ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).resetBoard();
   }


   public void goToThanksPhase(){
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Thank you for playing the game \n Your Score is : "+ ((ScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore());
      builder1.setCancelable(false);

      builder1.setPositiveButton(
              "OK",
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    mTimerFragment.initializeTimer(90000);
                    finish();

                 }
              });
      builder1.show();
   }


    public String getWords(){
        return this.words;
    }

}
