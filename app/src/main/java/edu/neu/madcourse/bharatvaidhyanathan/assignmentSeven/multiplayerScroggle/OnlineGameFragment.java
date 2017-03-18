package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.bharatvaidhyanathan.R;

public class OnlineGameFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_game_fragment,container,false);
        Button hostGame = (Button) view.findViewById(R.id.a7_live_game);
        Button joinGame = (Button) view.findViewById(R.id.a7_offline_game);
        hostGame.setText("Host Game");
        hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CommunicationActivity) getActivity()).replaceFragment(new HostFragment(), Constants.HOST_GAME_FRAGMENT_TAG);
            }
        });
        joinGame.setText("Join Game");
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CommunicationActivity) getActivity()).replaceFragment(new JoinFragment(), Constants.JOIN_GAME_FRAGMENT_TAG);
            }
        });
        return view;
    }
}
