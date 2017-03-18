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

    private DatabaseReference dRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dRef = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_player,container,false);

        final EditText email = (EditText) view.findViewById(R.id.a7_email_id);

        final EditText name = (EditText) view.findViewById(R.id.a7_user_name);

        Button register = (Button) view.findViewById(R.id.a7_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidEntry(name, email))
                {
                    User user = new User();
                    user.setActive(true);
                    user.setEmailID(email.getText().toString());
                    user.setGameID(0);
                    user.setName(name.getText().toString());
                    user.setRegID(FirebaseInstanceId.getInstance().getToken());
                    dRef.child("users").child(dRef.child("users").push().getKey()).setValue(user);

                    //dumping to the shared preferences
                    SharedPreferences.Editor shared = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit();
                    shared.putString(Constants.PLAYER_NAME, user.getName());
                    shared.putString(Constants.PLAYER_EMAIL, user.getEmailID());
                    shared.commit();

                    //setting the current user's name in this context
                    ((CommunicationActivity) getActivity()).userName = name.getText().toString();

                    ((CommunicationActivity) getActivity()).replaceFragment(new MainGameFragment(), Constants.GAME_SELECTION_FRAGMENT_TAG);
                }
                else
                    Toast.makeText(getActivity(),"Please enter a valid email and username",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    public boolean isValidEntry(EditText name, EditText email){
        return name!=null || !name.getText().toString().equals("") || !name.getText().toString().equals(" ") || email!=null || !email.getText().toString().equals("") || !email.getText().toString().equals(" ");
    }


}
