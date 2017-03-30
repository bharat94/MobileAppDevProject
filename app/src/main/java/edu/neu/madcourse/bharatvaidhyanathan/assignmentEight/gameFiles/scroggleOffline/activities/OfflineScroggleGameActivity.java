/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.NineLetterDict;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.fragments.OfflineScroggleTimerFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.fragments.OfflineScroggleControlFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.fragments.OfflineScroggleGameFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle.Constants;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.fragments.GameFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.GlobDict;

import static edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleMainActivity.MUSIC_ON;

public class OfflineScroggleGameActivity extends Activity {

   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   public static MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();
   private GameFragment mGameFragment;
   private OfflineScroggleTimerFragment mTimerFragment;
   private View mGameView;
   private View mPauseScreen;
   private Button mResumeButton;
   private String words;
   private boolean isHost;
   HashSet<String> hs = new HashSet<>();
   private String otherPlayerScore;
   private boolean otherPlayerDone;
   private int myScore;
   private int otherScore;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      words = this.getIntent().getExtras().getString(Constants.GAME_BOARD);
      Constants.GameID = getIntent().getExtras().getString(Constants.GAME_ID);

      System.out.println("OFFLINE ACTIVITY WORDS: "+words);
      System.out.println("OA Game ID: "+Constants.GameID);

      isHost = this.getIntent().getExtras().getBoolean("isHosted",false);

      setContentView(R.layout.a8_activity_scroggle_game_offline);
      mResumeButton = (Button) findViewById(R.id.resume);
      mGameView = findViewById(R.id.game_layout);
      mPauseScreen = findViewById(R.id.pause_layout);

      mResumeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            mGameView.setVisibility(View.VISIBLE);
            mPauseScreen.setVisibility(View.GONE);
            mTimerFragment.initializeTimer(OfflineScroggleTimerFragment.remainingTime);
         }
      });

      boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);

      Log.d("UT3", "restore = " + restore);
      mTimerFragment = (OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer);
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
      OfflineScroggleTimerFragment.ct.cancel();
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
      OfflineScroggleTimerFragment.ct.cancel();
      mTimerFragment.removeTimerText();
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Please select one cell from each grid to proceed");
      builder1.setCancelable(false);

      builder1.setPositiveButton(
              "Ok",
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    ((OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).displayWord("");
                    ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).updateUIForTransitionPhase();
                    ((OfflineScroggleControlFragment) getFragmentManager().findFragmentById(R.id.fragment_game_controls)).setDonePhase(1);
                    ((OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).setTimerDone();

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
                    ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).updateUIForPhaseTwo();
                    ((OfflineScroggleControlFragment) getFragmentManager().findFragmentById(R.id.fragment_game_controls)).setDonePhase(2);
                    ((OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).displayWord("");
                    ((GridLayout)findViewById(R.id.phase2)).setVisibility(View.VISIBLE);

                 }
              });
      builder1.show();
   }

   public void computeBoggleScore(){

      ((OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).recomputeScore();
      int cur_score = ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore();
      String word = ((OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).getWord();

      if(word.length()>2 && !hs.contains(word) && GlobDict.getInstance(this).search(word)) {
         hs.add(word);
         int new_score = cur_score + (word.length() * 10);

         ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).setScore(new_score);
         ((OfflineScroggleTimerFragment) getFragmentManager().findFragmentById(R.id.fragment_timer)).setScore(new_score);
      }


      //System.out.println("Reset");
      resetBoard();
   }

   public void resetBoard(){
      ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).resetBoard();
   }


   public String getWhichPlayerScore(boolean b){
      if(b) return "score1";
      else return "score2";
   }

   private void updatePlayerScore(DatabaseReference postRef, final String gameId, final String score, final String whichscore) {
      postRef
              .child("games")
              .child(gameId)
              .child(whichscore)
              .runTransaction(new Transaction.Handler() {
                 @Override
                 public Transaction.Result doTransaction(MutableData mutableData) {
                    mutableData.setValue(score);
                    return Transaction.success(mutableData);
                 }

                 @Override
                 public void onComplete(DatabaseError databaseError, boolean b,
                                        DataSnapshot dataSnapshot) {
                    // Transaction completed
                    getOtherPlayerScore(FirebaseDatabase.getInstance().getReference(), Constants.GameID, getWhichPlayerScore(!isHost));
                 }
              });
   }


   private void getOtherPlayerScore(DatabaseReference postRef, final String gameId, final String whichscore) {
      postRef
              .child("games")
              .child(gameId)
              .child(whichscore)
              .runTransaction(new Transaction.Handler() {
                 @Override
                 public Transaction.Result doTransaction(MutableData mutableData) {
                    otherPlayerScore = String.valueOf(mutableData.getValue());
                    return Transaction.success(mutableData);
                 }

                 @Override
                 public void onComplete(DatabaseError databaseError, boolean b,
                                        DataSnapshot dataSnapshot) {
                    // Transaction completed
                    otherScore = scorify(otherPlayerScore);

                    getOtherPlayerDone(FirebaseDatabase.getInstance().getReference(), Constants.GameID);
                 }
              });
   }




   private void getOtherPlayerDone1(DatabaseReference postRef, final String gameId) {
      postRef
              .child("games")
              .child(gameId)
              .child("playerDone")
              .runTransaction(new Transaction.Handler() {
                 @Override
                 public Transaction.Result doTransaction(MutableData mutableData) {
                    otherPlayerDone = mutableData.getValue(Boolean.class);

                    return Transaction.success(mutableData);
                 }

                 @Override
                 public void onComplete(DatabaseError databaseError, boolean b,
                                        DataSnapshot dataSnapshot) {
                    // Transaction completed
                    System.out.println("OTHER PLAYER DONE READ : "+otherPlayerDone);
                    System.out.println("SUSUSUSUSUSUUS");
                    System.out.println(myScore);
                    System.out.println(otherScore);
                    sendGreets();
                 }
              });
   }


   public void getOtherPlayerDone(DatabaseReference databaseReference, String gameId) {
      databaseReference.child("games").child(gameId).child("playerDone").addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
            otherPlayerDone = dataSnapshot.getValue(Boolean.class);
            System.out.println("OTHER PLAYER DONE READ : "+otherPlayerDone);
            System.out.println("SUSUSUSUSUSUUS");
            System.out.println(myScore);
            System.out.println(otherScore);
            sendGreets();
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
      });
   }


   private void setOtherPlayerDone(DatabaseReference postRef, final String gameId) {
      postRef
              .child("games")
              .child(gameId)
              .child("playerDone")
              .runTransaction(new Transaction.Handler() {
                 @Override
                 public Transaction.Result doTransaction(MutableData mutableData) {
                    mutableData.setValue(true);
                    return Transaction.success(mutableData);
                 }

                 @Override
                 public void onComplete(DatabaseError databaseError, boolean b,
                                        DataSnapshot dataSnapshot) {
                    // Transaction completed
                    waitForOtherPlayerToFinishGame();
                 }
              });
   }





   public void goToThanksPhase(){
      //updating player 1 score
      myScore = ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore();
      updatePlayerScore(FirebaseDatabase.getInstance().getReference(), Constants.GameID, ""+((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore(), getWhichPlayerScore(isHost));

   }


   public void sendGreets(){
      System.out.println("OTHERPLAYERDONE : "+otherPlayerDone);
      if(otherPlayerDone){
         saythanks(myScore, otherScore);
         notifyOtherPlayer();
      }
      else{
         setOtherPlayerDone(FirebaseDatabase.getInstance().getReference(), Constants.GameID);

      }
   }

   public void goToThanksPhase1(){
      //update your score
      //check if others score has already been updated
         // if no then just update your score and say thanks for playing and you would be informed shortly
         // if yes then update your score, say won loss and update other player
      System.out.println("THanks Phase game id: "+Constants.GameID);
      System.out.println("isHost: "+isHost);
      final int myScore = ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore();
      final String gameID = Constants.GameID;
      System.out.println("My score: "+myScore);


      FirebaseDatabase.getInstance().getReference().child("games").runTransaction(new Transaction.Handler() {
         @Override
         public Transaction.Result doTransaction(MutableData mutableData) {
            System.out.println(mutableData);
            return Transaction.success(mutableData);
         }

         @Override
         public void onComplete(DatabaseError databaseError, boolean b,
                                DataSnapshot dataSnapshot) {
         }
      });;



      FirebaseDatabase.getInstance().getReference()
              .child("games")
              .child(gameID)
              .runTransaction(new Transaction.Handler() {
                 @Override
                 public Transaction.Result doTransaction(MutableData mutableData) {
                    System.out.println("Inside doTransaction");
                    System.out.println(mutableData);
                    Game g = mutableData.getValue(Game.class);
                    System.out.println(g);
                    System.out.println("game ID : "+g.getGameID());
                    if (g == null) {
                       return Transaction.success(mutableData);
                    }

                    if(isHost){
                       g.setScore1(""+((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore());
                       if(g.getScore2()==null || g.getScore2().equals("") || g.getScore2().equals("0")){
                          waitForOtherPlayerToFinishGame();
                       }
                       else{
                           int a = ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore();
                           int b = Integer.parseInt(g.getScore2());
                           saythanks(a,b);
                          //add code here to send thanks to the other player as well.
                       }
                    }
                    else{
                       g.setScore2(""+((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore());
                       if(g.getScore1()==null || g.getScore1().equals("") || g.getScore1().equals("0")){
                          waitForOtherPlayerToFinishGame();
                       }
                       else{
                          int a = ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore();
                          int b = Integer.parseInt(g.getScore1());
                          saythanks(a,b);
                          //add code here to send thanks to the other player as well.
                       }
                    }
                    g.setPlayer2(Constants.PLAYER_NAME);
                    mutableData.setValue(g);
                    return Transaction.success(mutableData);
                 }

                 @Override
                 public void onComplete(DatabaseError databaseError, boolean b,
                                        DataSnapshot dataSnapshot) {
                    // Transaction completed
                    //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                 }
              });


      //previous code here
      /*
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Thank you for playing the game \n Your Score is : "+ ((OfflineScroggleGameFragment) getFragmentManager().findFragmentById(R.id.fragment_game)).getScore());
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
      */
   }


   public void saythanks(int a, int b){
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage(greet(a,b)+"\nThank you for playing the game \n Your Score is : "+ a+"\n Your Opponents score is : "+b);
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

   public String greet(int a, int b){
      if(a>b) return "You Win!";
      else if (a<b) return "You Lose!";
      else return "Its a Tie!";
   }

   public void waitForOtherPlayerToFinishGame(){
      AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
      builder1.setMessage("Thank you for playing the game \n You will receive your score once the other player plays");
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


   public void notifyOtherPlayer(){

   }


   public String getWords(){
      return this.words;
   }

   public char[][] getWordsasMatrix(){
      // arr has the words, now you want to populate a new array
      char[][] brr = new char[9][9];
      String[] str_arr = this.words.split(",");

      for(int i =0; i<9; i++){
         brr[i] = str_arr[i].toCharArray();
      }
      return brr;
   }


   public int scorify(String s){
      if(s == null) return 0;
      try{
         return Integer.parseInt(s);
      }
      catch (Exception e){
         return 0;
      }
   }


}
