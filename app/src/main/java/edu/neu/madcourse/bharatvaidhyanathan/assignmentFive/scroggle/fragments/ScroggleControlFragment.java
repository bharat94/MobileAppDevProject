/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.activities.GameActivity;

public class ScroggleControlFragment extends Fragment {

   public View rootView;
   public boolean isOnClickChanged;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      isOnClickChanged = false;
      rootView =
            inflater.inflate(R.layout.fragment_scroggle_control, container, false);
      View main = rootView.findViewById(R.id.button_main);
      View restart = rootView.findViewById(R.id.button_restart);
      View mute = rootView.findViewById(R.id.button_mute);
      View done = rootView.findViewById(R.id.button_done);

      main.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            getActivity().finish();
         }
      });

      restart.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            ((GameActivity) getActivity()).restartGame();
         }
      });

      done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if(!isOnClickChanged) {
               ((ScroggleGameActivity) getActivity()).proceedToPhase2();
               isOnClickChanged = true;
            }
            else{
               Toast.makeText(getActivity(), "Nothing Next", Toast.LENGTH_SHORT).show();
            }
         }
      });

      return rootView;
   }


   public void changeDoneButton(){
      isOnClickChanged = true;
   }

}
