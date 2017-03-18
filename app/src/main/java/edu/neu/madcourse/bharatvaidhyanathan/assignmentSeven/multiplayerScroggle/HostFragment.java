package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;

import static com.google.android.gms.internal.zzs.TAG;

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
        View view = inflater.inflate(R.layout.host_game_fragment, container, false);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Waiting for other player to join...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        game = new Game();
        game.setBoard("0");
        game.setPlayer1(((CommunicationActivity) getActivity()).userName);
        game.setScore1("0");
        game.setScore2("0");
        game.setJoined(false);
        game.setHosted(true);
        game.setGameID(mDatabase.child("games").push().getKey());
        mDatabase.child("games").child(game.getGameID()).setValue(game);
        waitForOtherPlayer();
        return view;
    }


    public void waitForOtherPlayer() {
        mDatabase.child("games").child(game.getGameID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game g = dataSnapshot.getValue(Game.class);
                if (g.isJoined()) {
                    //Toast.makeText(getActivity(), "The other player has joined", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    onGameJoined(mDatabase,g.getGameID());
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

    private void onGameJoined(DatabaseReference dbRef, final String gameId) {
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
                    Intent i = new Intent(getActivity(), FCMActivity.class);
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
