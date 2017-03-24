package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggle.activities.ScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;

public class JoinFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ListView mGamesView;
    private ArrayList<Game> mGames = new ArrayList<Game>();
    private ArrayList<String> mGameNames = new ArrayList<String>();
    private ArrayAdapter mGamaesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a8_join_game_fragment, container, false);
        mGamesView = (ListView) view.findViewById(R.id.live_game_list);

        mGamesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) mGamesView.getItemAtPosition(position);
                onGameJoined(mDatabase,mGames.get(itemPosition).getGameID());
                // Show Alert
                Toast.makeText(getActivity(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
            }

        });

        populateGames();
        return  view;
    }
    private void onGameJoined(DatabaseReference postRef, final String gameId) {
        postRef
                .child("games")
                .child(gameId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Game g = mutableData.getValue(Game.class);
                        if (g == null) {
                            return Transaction.success(mutableData);
                        }
                        Constants.GameWords=g.getBoard();
                        g.setJoined(true);
                        g.setPlayer2(((MultiplayerScroggleActivity)getActivity()).userName);
                        mutableData.setValue(g);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                        Constants.GameID=gameId;
                        ((MultiplayerScroggleActivity)getActivity()).finish();
                        Intent i = new Intent(getActivity(), ScroggleGameActivity.class);
                        //calling FCMActivity from Joined game, so include that info in the bundle
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isHosted", false);
                        bundle.putString("words", Constants.GameWords);
                        i.putExtra("value", bundle);
                        startActivity(i);
                    }
                });
    }

    public void populateGames()
    {
        mGames.clear();
        mDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot key : dataSnapshot.getChildren())
                {
                    if(key.getValue(Game.class).isHosted()){
                        mGames.add(key.getValue(Game.class));
                        mGameNames.add(key.getValue(Game.class).getPlayer1() + "'s game");
                    }
                }

                mGamaesAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, mGameNames);

                mGamesView.setAdapter(mGamaesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
