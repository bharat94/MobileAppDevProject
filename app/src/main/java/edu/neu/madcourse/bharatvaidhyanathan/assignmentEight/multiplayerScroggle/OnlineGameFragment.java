package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;

public class OnlineGameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a8_online_game_fragment,container,false);

        Button hostGame = (Button) view.findViewById(R.id.a7_bt_host_game);
        hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MultiplayerScroggleActivity) getActivity()).replaceFragment(new HostFragment(), Constants.HOST_GAME_FRAGMENT_TAG);
            }
        });

        Button joinGame = (Button) view.findViewById(R.id.a7_bt_join_game);
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MultiplayerScroggleActivity) getActivity()).replaceFragment(new JoinFragment(), Constants.JOIN_GAME_FRAGMENT_TAG);
            }
        });

        return view;
    }
}
