/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities.OfflineScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities.ScroggleMainActivity;

public class OfflineScroggleControlFragment extends Fragment {

   public View rootView;
   public int donePhase;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      donePhase = 0;
      rootView =
            inflater.inflate(R.layout.fragment_scroggle_control, container, false);
      View main = rootView.findViewById(R.id.button_main);
      final View mute = rootView.findViewById(R.id.button_mute);
      View done = rootView.findViewById(R.id.button_done);
      View pause = rootView.findViewById(R.id.button_pause);
       pause.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ((OfflineScroggleGameActivity)getActivity()).pausetheGame();
           }
       });
      main.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             System.out.println("Button Quit clicked");
             ((OfflineScroggleGameActivity)getActivity()).goToThanksPhase();
             OfflineScroggleTimerFragment.ct.cancel();
         }
      });


       mute.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(ScroggleMainActivity.MUSIC_ON) {

                   ((Button)mute).setText("Music Off");
                   ((OfflineScroggleGameActivity) getActivity()).pauseMusic();
                   ScroggleMainActivity.MUSIC_ON =false;

               }
               else
               {

                   ((Button)mute).setText("Music On");
                   ((OfflineScroggleGameActivity)getActivity()).startMusic();
                   ScroggleMainActivity.MUSIC_ON =true;
               }
           }
       });



      done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            switch (donePhase) {
               case 0:
                  ((OfflineScroggleGameActivity) getActivity()).proceedToPhase2();
                  donePhase = 1;
                  break;
               case 1:
                  ((OfflineScroggleGameActivity) getActivity()).initializePhase2();
                  break;
               case 2:
                  ((OfflineScroggleGameActivity) getActivity()).computeBoggleScore();
                  break;
               default:
                  break;
            }
         }
      });

      return rootView;
}


   public void setDonePhase(int a){
      donePhase = a;
   }

}
