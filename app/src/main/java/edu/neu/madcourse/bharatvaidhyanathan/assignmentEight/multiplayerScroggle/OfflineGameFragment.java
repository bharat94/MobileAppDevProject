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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.NineLetterDict;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities.OfflineScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;

public class OfflineGameFragment extends Fragment {

    private ArrayList<User> usersList = new ArrayList<User>();

    private ArrayList<String> userNamesList = new ArrayList<String>();

    private ArrayAdapter usersAdapter;

    private ListView usersListView;

    private DatabaseReference dRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dRef = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a8_offline_player_list_fragment,container,false);

        usersListView = (ListView) view.findViewById(R.id.offline_player_list);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) usersListView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getActivity(),
                        "Successfully Sent an Offline Challenge to the player!", Toast.LENGTH_LONG)
                        .show();




                NineLetterDict.getInstance(getActivity()).resetWords();
                final String words = NineLetterDict.getInstance(getActivity()).getWordsAsString();

                Game game = new Game();
                game.setBoard(words);
                game.setSelection(Constants.DefaultSelection);
                game.setPlayer1(((MultiplayerScroggleActivity)getActivity()).userName);
                game.setScore1("0");
                game.setScore2("0");
                game.setJoined(true);
                game.setHosted(false);

                game.setGameID(((MultiplayerScroggleActivity)getActivity()).mDatabase.child("games").push().getKey());
                ((MultiplayerScroggleActivity)getActivity()).mDatabase.child("games").child(game.getGameID()).setValue(game);
                Constants.GameID = game.getGameID();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((MultiplayerScroggleActivity) getActivity()).pushNotification(usersList.get(position).getRegID(), words);
                    }
                }).start();

                Intent i = new Intent(getActivity(),OfflineScroggleGameActivity.class);
                //String words = NineLetterDict.getInstance(mContext).getWordsAsString();
                i.putExtra(Constants.GAME_BOARD,words);
                i.putExtra(Constants.GAME_ID,Constants.GameID);
                i.putExtra("isHosted",true);
                getActivity().startActivity(i);

            }

        });
        addUsersFromServerToList();
        return view;
    }

    public void addUsersFromServerToList()
    {
        System.out.println("POPULATE Users called");
        System.out.println(dRef);
        usersList.clear();
        dRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Entering user ds code");
                for (DataSnapshot key : dataSnapshot.getChildren())
                {
                    User loopUser = key.getValue(User.class);
                    if(!loopUser.getName().equals(((MultiplayerScroggleActivity) getActivity()).userName)) {
                        usersList.add(loopUser);
                        userNamesList.add(loopUser.getName());
                    }
                }
                usersAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, userNamesList);
                usersListView.setAdapter(usersAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DB ERROR at addUsersFromServerToList");
            }

        });
    }
}