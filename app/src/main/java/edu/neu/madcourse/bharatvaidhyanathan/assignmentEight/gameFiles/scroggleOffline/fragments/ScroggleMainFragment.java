/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities.OfflineScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities.ScroggleMainActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.NineLetterDict;

import static edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleMainActivity.MUSIC_ON;

public class ScroggleMainFragment extends Fragment {

   private AlertDialog mDialog;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView =
            inflater.inflate(R.layout.fragment_scroggle_main, container, false);
      // Handle buttons here...
      View newButton = rootView.findViewById(R.id.new_button);
      View continueButton = rootView.findViewById(R.id.continue_button);
      View ackButton = rootView.findViewById(R.id.ack_btn);
      View quitButton = rootView.findViewById(R.id.quit_btn);
      final View musicButton = rootView.findViewById(R.id.music_btn);
      View instruction = rootView.findViewById(R.id.instruction_btn);

      instruction.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.inst_text)
                    .setTitle("Instructions");
            // 3. Get the AlertDialog from create()
            final android.support.v7.app.AlertDialog dialog = builder.create();

            //setting the title
            builder.setTitle("Scroggle");
            dialog.show();
         }
      });
      musicButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if(MUSIC_ON) {

               ((Button)musicButton).setText("Music Off");
               ((ScroggleMainActivity) getActivity()).pauseMusic();
               MUSIC_ON =false;

            }
               else
            {

               ((Button)musicButton).setText("Music On");
               ((ScroggleMainActivity)getActivity()).startMusic();
               MUSIC_ON =true;
            }
         }
      });


      newButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            NineLetterDict.getInstance(getActivity()).resetWords();
            Intent intent = new Intent(getActivity(), OfflineScroggleGameActivity.class);
            getActivity().startActivity(intent);
         }
      });
      continueButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(getActivity(), OfflineScroggleGameActivity.class);
            intent.putExtra(OfflineScroggleGameActivity.KEY_RESTORE, true);
            getActivity().startActivity(intent);
         }
      });
      ackButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.ack_text)
                    .setTitle("Acknowledgements");
            // 3. Get the AlertDialog from create()
            final android.support.v7.app.AlertDialog dialog = builder.create();

            //setting the title
            builder.setTitle("Scroggle");
            dialog.show();

         }
      });

      quitButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            getActivity().finish();
         }
      });

      return rootView;
   }

   @Override
   public void onPause() {
      super.onPause();

      // Get rid of the about dialog if it's still up
      if (mDialog != null)
         mDialog.dismiss();
   }
}
