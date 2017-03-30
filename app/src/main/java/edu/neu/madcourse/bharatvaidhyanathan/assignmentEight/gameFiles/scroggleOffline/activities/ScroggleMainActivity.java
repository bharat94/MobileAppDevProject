/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.neu.madcourse.bharatvaidhyanathan.R;

import static edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleGameActivity.mMediaPlayer;

public class ScroggleMainActivity extends AppCompatActivity {
   // ...
   public static boolean MUSIC_ON = true;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_scroggle_main);
   }


   public void pauseMusic()
   {
      if(ScroggleMainActivity.MUSIC_ON) {
         {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
         }
      }
   }

   public void startMusic()
   {
      if(!ScroggleMainActivity.MUSIC_ON) {
         mMediaPlayer = MediaPlayer.create(this, R.raw.happy_song);
         mMediaPlayer.setLooping(true);
         mMediaPlayer.start();
      }
   }

   @Override
   protected void onResume() {
      super.onResume();
         if(MUSIC_ON)
      {
         mMediaPlayer = MediaPlayer.create(this, R.raw.happy_song);
         mMediaPlayer.setVolume(0.5f, 0.5f);
         mMediaPlayer.setLooping(true);
         mMediaPlayer.start();
      }
   }

   @Override
   protected void onPause() {
      super.onPause();
      if(MUSIC_ON) {
         mMediaPlayer.stop();
         mMediaPlayer.reset();
         mMediaPlayer.release();
      }
   }
}
