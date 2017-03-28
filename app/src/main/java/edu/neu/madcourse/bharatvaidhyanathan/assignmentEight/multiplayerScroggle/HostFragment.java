package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.NineLetterDict;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;

public class HostFragment extends Fragment {

    private DatabaseReference mDatabase;

    ProgressDialog mProgressDialog;

    Game game;

    boolean isCancelled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a8_host_game_fragment, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Waiting for other player to join...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        String words = NineLetterDict.getInstance(getActivity()).getWordsAsString();

        Integer arr[] = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        ArrayList<Integer> gridNos= new ArrayList<Integer>(Arrays.asList(arr));
        Collections.shuffle(gridNos);

        int a = gridNos.remove(0);
        Constants.CurrentGrid = a;
        int b = gridNos.remove(0);
        String s = "";
        for(int c : gridNos){
            s+=c+",";
        }
        s.substring(0,s.length());

        game = new Game();
        game.setBoard(words);
        game.setSelection(Constants.DefaultSelection);
        game.setPlayer1(((MultiplayerScroggleActivity) getActivity()).userName);
        game.setScore1("0");
        game.setScore2("0");
        game.setJoined(false);
        game.setHosted(true);

        game.setPlayer1gridnumber(a);
        game.setPlayer2gridnumber(b);
        game.setGridNumbers(s);

        game.setGameID(mDatabase.child("games").push().getKey());
        mDatabase.child("games").child(game.getGameID()).setValue(game);

        waitForOtherPlayer(words);
        return view;
    }


    public void waitForOtherPlayer(final String words) {
        mDatabase.child("games").child(game.getGameID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game g = dataSnapshot.getValue(Game.class);
                if (g.isJoined()) {
                    //Toast.makeText(getActivity(), "The other player has joined", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    onGameJoined(mDatabase,g.getGameID(),words);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                isCancelled = true;
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        onGameLeft(mDatabase, game.getGameID());;
    }

    private void onGameJoined(DatabaseReference dbRef, final String gameId, final String words) {
        dbRef.child("games").child(gameId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Game game = mutableData.getValue(Game.class);
                if (game != null) {
                    game.setHosted(false);
                    Constants.GameID=game.getGameID();
                    mutableData.setValue(game);
                }
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Constants.GameID=gameId;
                if(getActivity()!=null) {
                    getActivity().finish();
                    Intent i = new Intent(getActivity(), ScroggleGameActivity.class);
                    //calling FCMActivity from Hosted game, so include that info in the bundle
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isHosted", true);
                    bundle.putString("words", words);
                    bundle.putInt("currentGrid", Constants.CurrentGrid);
                    i.putExtra("value", bundle);
                    startActivity(i);
                }
            }
        });
    }


    private void onGameLeft(DatabaseReference dbRef, String gameId) {
        dbRef.child("games").child(gameId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Game game = mutableData.getValue(Game.class);
                if (game != null) {
                    game.setHosted(false);
                    Constants.GameID=game.getGameID();
                    mutableData.setValue(game);
                }
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {


                //((CommunicationActivity)getActivity()).replaceFragment(new MainGameFragment(), Constants.GAME_SELECTION_FRAGMENT_TAG);
            }
        });
    }
}
