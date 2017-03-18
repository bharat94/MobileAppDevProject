package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;

import android.app.Fragment;
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
        View view = inflater.inflate(R.layout.join_game_fragment, container, false);
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
    private void onGameJoined(DatabaseReference postRef, String gameId) {
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

                        g.setJoined(true);
                        g.setPlayer2(((CommunicationActivity)getActivity()).email);
                        mutableData.setValue(g);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                        ((CommunicationActivity)getActivity()).replaceFragment(new MainGameFragment(), Constants.GAME_SELECTION_FRAGMENT_TAG);
                    }
                });
    }

    public void populateGames()
    {
        mGames.clear();
        mDatabase.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnap  = dataSnapshot.getChildren();
                for (DataSnapshot key : dataSnap)
                {

                    if(key.getValue(Game.class).isHosted())
                        mGames.add(key.getValue(Game.class));
                        mGameNames.add(key.getValue(Game.class).getPlayer1());
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
