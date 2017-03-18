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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle.CommunicationActivity;

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
        View view = inflater.inflate(R.layout.offline_player_list_fragment,container,false);

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((CommunicationActivity) getActivity()).pushNotification(usersList.get(position).getRegID());
                    }
                }).start();

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
                    usersList.add(key.getValue(User.class));
                    userNamesList.add(key.getValue(User.class).getName());
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