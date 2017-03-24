/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.NineLetterDict;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.TileScrobble;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.Tile;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle.Constants;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.GlobDict;

public class ScroggleGameFragment extends Fragment {

    static int score;
    static int temp = -1;
    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};

    static private int phaseTwoIds[] = {R.id.phase2_small1, R.id.phase2_small2, R.id.phase2_small3,
            R.id.phase2_small4, R.id.phase2_small5, R.id.phase2_small6, R.id.phase2_small7,
            R.id.phase2_small8, R.id.phase2_small9};

    private Handler mHandler = new Handler();
    private TileScrobble mEntireBoard = new TileScrobble(this);
    private TileScrobble mLargeTiles[] = new TileScrobble[9];
    private TileScrobble mSmallTiles[][] = new TileScrobble[9][9];
    private Set<Tile> mAvailable = new HashSet<Tile>();
    private   String  words[]  = new String[9];
    private char[][] charr;
    private ArrayList<Character> transitionCharacters;
    private ArrayList<Integer>[] positionMemory = (ArrayList<Integer>[]) new ArrayList[9];
    private HashSet<String> hs = new HashSet<String>();
    private String phaseTwoString = "";
    private ArrayList<Integer> phaseTwoPositionMemory = new ArrayList<>();
    public static boolean isTransition = false;
    private int mSoundX, mSoundO, mSoundMiss, mSoundRewind;
    private SoundPool mSoundPool;
    private float mVolume = 1f;

    private TileScrobble mPhaseTwoTiles[] = new TileScrobble[9];


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.a8_fragment_scroggle_game, container, false);

        // would initialize views

        initViews(v);
        return v;
    }

    public boolean isAvailable(TileScrobble tile) {
        return mAvailable.contains(tile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NineLetterDict.getInstance(getActivity());
        //charr = NineLetterDict.getInstance(getActivity()).getMatrix();

        charr = getArrayFromString(((ScroggleGameActivity) getActivity()).getWords());

        setRetainInstance(true);
        initGame();
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.sergenious_movex, 1);
        mSoundO = mSoundPool.load(getActivity(), R.raw.sergenious_moveo, 1);
        mSoundMiss = mSoundPool.load(getActivity(), R.raw.erkanozan_miss, 1);
        mSoundRewind = mSoundPool.load(getActivity(), R.raw.joanne_rewind, 1);
        // logic variables initialize
        // progress dialog, async task
        // async task - 9 letter array, jumbled
    }

    public char[][] getArrayFromString(String str){
        char[][] brr = new char[9][9];
        int i = 0;
        for (String s : str.split(",")){
            if(i >8) break;
            int j = 0;
            while(j <= 8) {
                brr[i][j] = s.charAt(j);
                j++;
            }
            i++;
        }

        return brr;
    }



    public void initGame() {
        Log.d("UT3", "init game");
        transitionCharacters = new ArrayList<Character>();
        mEntireBoard = new TileScrobble(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new TileScrobble(this);
            //phase 2 code start
            mPhaseTwoTiles[large] = new TileScrobble(this);
            //phase 2 code end
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new TileScrobble(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        for (int i =0;i<9;i++)
        {
            words[i]="";
            positionMemory[i] = new ArrayList<Integer>();
        }
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            final int fLarge1 = large;
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            //phase 2 code start
            final View outer_two = rootView.findViewById(phaseTwoIds[large]);
            mPhaseTwoTiles[large].setView(outer_two);
            outer_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(outer_two.getBackground().getLevel()==1)
                    {
//                        //remove letter
//                        if(fLarge1 == phaseTwoPositionMemory.get(phaseTwoPositionMemory.size()-1)){
//                            phaseTwoString = phaseTwoString.substring(0,phaseTwoString.length()-1);
//                            ((ScroggleGameActivity) getActivity()).displayword(phaseTwoString);
//                            phaseTwoPositionMemory.remove(phaseTwoPositionMemory.size()-1);
//                            outer_two.getBackground().setLevel(0);
//                        }
                    }
                    else {
                        //add letter

                        //System.out.println("temp : "+temp);
                        if(temp==-1 || isNeighbor(fLarge1, temp) ){
                            phaseTwoString += ((Button) outer_two).getText().toString();
                            //System.out.println(phaseTwoString);
                            if(phaseTwoString.length()>2 && GlobDict.getInstance(getActivity()).search(phaseTwoString) && !hs.contains(phaseTwoString)) {
                                //updateScore1(phaseTwoString.length());
                                //System.out.println(phaseTwoString);
                                hs.add(phaseTwoString);
                            }
                            ((ScroggleGameActivity) getActivity()).displayword(phaseTwoString);
                            outer_two.getBackground().setLevel(1);

                            if(temp!=-1) {
                                mPhaseTwoTiles[temp].getView().getBackground().setLevel(0);
                            }
                            temp = fLarge1;

                        }
                    }
                }
            });

            //phase 2 code end


            for (int small = 0; small < 9; small++) {
                final Button inner = (Button) outer.findViewById
                        (mSmallIds[small]);
                inner.setText(String.valueOf(charr[large][small]));
                final int fLarge = large;
                final int fSmall = small;
                final TileScrobble smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);
                // ...
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(inner.getBackground().getLevel()==1)
                        {
                            if(isTransition) {
                                smallTile.animate();
                                //System.out.println("is transition");
                                smallTile.getView().getBackground().setLevel(0);
                            }
                            else{

                                // remove letter from word
                                if (positionMemory[fLarge].get(positionMemory[fLarge].size() - 1) == fSmall) {
                                    //if(words[fLarge].substring(words[fLarge].length()-1).equals(inner.getText().toString())){
                                    smallTile.animate();
                                    smallTile.getView().getBackground().setLevel(0);
                                    words[fLarge] = words[fLarge].substring(0, words[fLarge].length() - 1);
                                    positionMemory[fLarge].remove(positionMemory[fLarge].size() - 1);
                                    if (words[fLarge].length() > 2 && GlobDict.getInstance(getActivity()).search(words[fLarge])) {
                                        //System.out.println(words[fLarge]);
                                        ((ScroggleGameActivity) getActivity()).displayword(words[fLarge]);
                                    } else {
                                        ((ScroggleGameActivity) getActivity()).displayword("");
                                    }
                                    updateScore(5);
                                } else {
                                    if (words[fLarge].length() > 2 && GlobDict.getInstance(getActivity()).search(words[fLarge]))
                                        ((ScroggleGameActivity) getActivity()).displayword(words[fLarge]);
                                    //Toast.makeText(getActivity(), "Cannot delete mid letter", Toast.LENGTH_SHORT);
                                }
                            }
                        }
                        else {
                            // add letter to word

                            if(isTransition){
                                smallTile.animate();
                                //System.out.println("is transition");
                                smallTile.getView().getBackground().setLevel(1);
                            }
                            else {
                                if (positionMemory[fLarge].isEmpty() || isNeighbor(fSmall, positionMemory[fLarge].get(positionMemory[fLarge].size() - 1))) {
                                    smallTile.animate();
                                    mSoundPool.play(mSoundO, mVolume, mVolume, 1, 0, 1f);
                                    // ...
                                    smallTile.getView().getBackground().setLevel(1);
                                    words[fLarge] = words[fLarge] + inner.getText().toString();
                                    positionMemory[fLarge].add(fSmall);
                                    if (words[fLarge].length() > 2 && GlobDict.getInstance(getActivity()).search(words[fLarge])) {
                                        //System.out.println(words[fLarge]);
                                        ((ScroggleGameActivity) getActivity()).displayword(words[fLarge]);
                                    } else {
                                        ((ScroggleGameActivity) getActivity()).displayword("");
                                    }
                                    updateScore(5);
                                }
                                else
                                {
                                    mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                                }
                            }
                        }

                        //System.out.println(positionMemory[fLarge]);
                    }
                });
                // ...
            }
        }
    }


    public void updateUIForTransitionPhase()
    {
        boolean b = true;
        isTransition = true;
        for (int large = 0; large < 9; large++) {
            b = GlobDict.getInstance(getActivity()).search(words[large]);
            for (int small = 0; small < 9; small++) {

                if(b && mSmallTiles[large][small].getView().getBackground().getLevel()==1)
                {
                    mSmallTiles[large][small].getView().getBackground().setLevel(0);

                }
                else
                {
                    mSmallTiles[large][small].getView().getBackground().setLevel(0);
                    ((Button)mSmallTiles[large][small].getView()).setText("");
                    mSmallTiles[large][small].getView().setEnabled(false);
                }
            }

            //Phase 2 loading
            //transitionCharacters.add('A');
            //((Button)mPhaseTwoTiles[large].getView()).setText(String.valueOf(transitionCharacters.get(large)));
        }

    }

    public void updateUIForPhaseTwo() {
        for (int large = 0; large < 9; large++) {

            char ch = '\0';

            for (int small = 0; small < 9; small++) {
                if (mSmallTiles[large][small].getView().getBackground().getLevel() == 1)
                    ch = ((Button) mSmallTiles[large][small].getView()).getText().charAt(0);
            }

            //Phase 2 loading
            transitionCharacters.add(ch);
            ((Button) mPhaseTwoTiles[large].getView()).setText(String.valueOf(ch));
        }
    }


    public boolean isNeighbor(int i, int j){
        return isNeighborR(i/3, i%3, j/3, j%3);
    }

    public boolean isNeighborR(int r1, int c1, int r2, int c2){
        return (Math.abs(r1 - r2) <= 1 && Math.abs(c1 - c2) <= 1);
    }


    public void updateScore(int factor){
        int newScore = 0;
        for(int i=0; i<9; i++){
            String s = words[i];
            //System.out.print("HEYEYE : "+ s+" , ");

            if(s.length()>2 && GlobDict.getInstance(getActivity()).search(s)) {
                newScore += (s.length() * factor);
                //System.out.println("score factor: "+(s.length() * factor)+", word in dic");
            }
            else{
                //System.out.println();
            }
        }
        score = newScore;

        ((ScroggleGameActivity) getActivity()).setScore(score);

        //System.out.println("new Score : "+score);
    }


    public void updateScore1(int newScore){
        score += newScore;
        ((ScroggleGameActivity) getActivity()).setScore(score);
        //System.out.println("new Score : "+score);
    }

    public void resetBoard(){
        ((ScroggleGameActivity) getActivity()).displayword("");
        phaseTwoString = "";
        if(temp!=-1) {
            mPhaseTwoTiles[temp].getView().getBackground().setLevel(0);
        }
        temp = -1;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int a){
        score = a;
    }

}

