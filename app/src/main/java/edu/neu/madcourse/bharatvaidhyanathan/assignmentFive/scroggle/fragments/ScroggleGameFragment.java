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
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.NineLetterDict;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.TileScrobble;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentFive.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.Tile;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.tictactoe.activities.GameActivity;

public class ScroggleGameFragment extends Fragment {

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



    private TileScrobble mPhaseTwoTiles[] = new TileScrobble[9];


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scroggle_game, container, false);

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
        NineLetterDict.getInstance(getActivity());
        charr = NineLetterDict.getInstance(getActivity()).getMatrix();
        setRetainInstance(true);
        initGame();
        // logic variables initialize
        // progress dialog, async task
        // async task - 9 letter array, jumbled
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
        }
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            //phase 2 code start
            View outer_two = rootView.findViewById(phaseTwoIds[large]);
            mPhaseTwoTiles[large].setView(outer_two);
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
                            if(words[fLarge].substring(words[fLarge].length()-1).equals(inner.getText().toString())){
                                smallTile.animate();
                                smallTile.getView().getBackground().setLevel(0);
                                words[fLarge] = words[fLarge].substring(0, words[fLarge].length()-1);
                                ((ScroggleGameActivity)getActivity()).displayword(words[fLarge]);
                            }
                            else{
                                ((ScroggleGameActivity)getActivity()).displayword(words[fLarge]);
                                Toast.makeText(getActivity(), "Cannot delete mid letter", Toast.LENGTH_SHORT);
                            }
                        }
                        else {
                            smallTile.animate();
                            // ...
                            smallTile.getView().getBackground().setLevel(1);
                            words[fLarge] = words[fLarge] + inner.getText().toString();
                            ((ScroggleGameActivity)getActivity()).displayword(words[fLarge]);
                        }
                    }
                });
                // ...
            }
        }
    }


    public void updateUIForTransitionPhase()
    {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {

                if(mSmallTiles[large][small].getView().getBackground().getLevel()==1)
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


}

