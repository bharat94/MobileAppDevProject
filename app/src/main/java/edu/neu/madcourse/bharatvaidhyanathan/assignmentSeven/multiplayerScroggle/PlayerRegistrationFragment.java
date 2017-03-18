package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vaidhyanathannarayanan on 03/03/17.
 */

public class PlayerRegistrationFragment extends Fragment {

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_fragment,container,false);

        final EditText email = (EditText) view.findViewById(R.id.a7_email_id);
        final EditText userName = (EditText) view.findViewById(R.id.a7_user_name);
        Button register = (Button) view.findViewById(R.id.a7_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName!=null || !userName.getText().toString().equals("") || !userName.getText().toString().equals(" ") || email!=null || !email.getText().toString().equals("") || !email.getText().toString().equals(" "))

                {
                    User user = new User();
                    user.setActive(true);
                    user.setEmailID(email.getText().toString());
                    user.setGameID(0);
                    user.setName(userName.getText().toString());
                    user.setRegID(FirebaseInstanceId.getInstance().getToken());
                    mDatabase.child("users").child(mDatabase.child("users").push().getKey()).setValue(user);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit();
                    editor.putString(Constants.PLAYER_NAME, user.getName());
                    editor.putString(Constants.PLAYER_EMAIL, user.getEmailID());
                    editor.commit();

                    ((CommunicationActivity) getActivity()).userName = userName.getText().toString();

                    ((CommunicationActivity) getActivity()).replaceFragment(new MainGameFragment(), Constants.GAME_SELECTION_FRAGMENT_TAG);
                }
                else
                {
                    Toast.makeText(getActivity(),"Email or user name cannot be blank",Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;
    }


}
