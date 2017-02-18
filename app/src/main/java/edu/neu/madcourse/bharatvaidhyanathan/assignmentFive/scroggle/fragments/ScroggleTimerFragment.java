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
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.activities.GameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.GlobDict;

public class ScroggleTimerFragment extends Fragment {
   boolean b;

    private int score;
   private TextView mWordText;
   private TextView mScoreText;
    private HashSet<String> hs = new HashSet<>();

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      b = false;
      View rootView =
            inflater.inflate(R.layout.fragment_scroggle_timer, container, false);
      final TextView timer = (TextView) rootView.findViewById(R.id.timer_tv);
      mWordText = (TextView) rootView.findViewById(R.id.word);
      mScoreText = (TextView) rootView.findViewById(R.id.text_view_score);
      CountDownTimer ct = new CountDownTimer(40000,1000) {
         @Override
         public void onTick(long l) {
            int a = (int) (l/1000);
            //timer.setText("0"+a/60+" : "+a%60);
            timer.setText(String.format("%02d:%02d",a/60,a%60));
            if(l<10000)
               timer.setTextColor(getResources().getColor(R.color.red_color));
         }

         @Override
         public void onFinish() {
            if(!b) {
               Toast.makeText(getActivity(), "Game's Up baby!!", Toast.LENGTH_LONG).show();
               ((ScroggleGameActivity) getActivity()).proceedToPhase2();
               b = true;
            }
         }
      }.start();

      return rootView;
   }

   public void displayWord(String word)
   {
      mWordText.setText(word.toUpperCase());
   }

   public void updateScore(int score)
   {
      mScoreText.setText(String.valueOf(score));
   }

   public void setTimerDone(){
      b = true;
   }

    public void recomputeScore(){
        String s = mWordText.getText().toString();
        if(!hs.contains(s) && GlobDict.getInstance(getActivity()).search(s)){
            int a = Integer.parseInt(mScoreText.getText().toString());
            a+=s.length();
            mScoreText.setText(a+"");
            score = a;
            hs.add(s);
        }
    }

}
