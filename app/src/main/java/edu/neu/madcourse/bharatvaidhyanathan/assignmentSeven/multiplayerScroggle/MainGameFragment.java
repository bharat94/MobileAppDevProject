package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.bharatvaidhyanathan.R;


public class MainGameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_game_fragment,container,false);

        TextView name_text_view = (TextView) view.findViewById(R.id.a7_player_name);

        name_text_view.setText("Hello, "+ ((CommunicationActivity)getActivity()).userName);

        Button b_live_game = (Button) view.findViewById(R.id.a7_live_game);
        b_live_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CommunicationActivity)getActivity()).replaceFragment(new OnlineGameFragment(), Constants.ONLINE_LIST_FRAGMENT_TAG);
            }
        });

        Button b_offline_game = (Button) view.findViewById(R.id.a7_offline_game);
        b_offline_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CommunicationActivity)getActivity()).replaceFragment(new OfflineGameFragment(), Constants.OFFLINE_LIST_FRAGMENT_TAG);
            }
        });


        return view;
    }


}
