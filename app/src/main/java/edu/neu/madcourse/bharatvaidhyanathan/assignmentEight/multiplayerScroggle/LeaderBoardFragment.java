package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle;

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
import java.util.Collections;
import java.util.Comparator;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;

public class LeaderBoardFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.a8_leaderboard_fragment,container,false);

        usersListView = (ListView) view.findViewById(R.id.leaderboard_list);

        addUsersFromServerToList();
        return view;
    }

    public void addUsersFromServerToList()
    {
        System.out.println("Populating users");
        System.out.println(dRef);
        usersList.clear();
        dRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Entering user ds code");
                for (DataSnapshot key : dataSnapshot.getChildren())
                {
                    User loopUser = key.getValue(User.class);
                    usersList.add(loopUser);
                }
                usersAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, userNamesList);
                usersListView.setAdapter(usersAdapter);
                Collections.sort(usersList, new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        int a =0;
                        int b = 0;
                        if(user.getScore()!=null){
                            a = Integer.parseInt(user.getScore());
                        }
                        if(t1.getScore()!=null){
                            b = Integer.parseInt(t1.getScore());
                        }
                        return a-b;
                    }
                });

                Collections.reverse(usersList);

                int i = 1;
                for(User u : usersList){
                    if(!u.getName().equals(((MultiplayerScroggleActivity) getActivity()).userName)) {
                        userNamesList.add(i++ + ") "+u.getName() + " [" + scorify(u.getScore()) + "]");
                    }
                    else
                    {
                        userNamesList.add(i++ + ") "+u.getName() + " [" + scorify(u.getScore()) + "] [YOU]");
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DB ERROR at addUsersFromServerToList");
            }

            public int scorify(String s){
                if(s == null) return 0;
                else return Integer.parseInt(s);
            }

        });
    }
}