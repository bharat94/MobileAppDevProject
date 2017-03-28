package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.MultiplayerScroggleActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle.CommunicationActivity;


public class MainGameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a8_choose_game_fragment,container,false);

        TextView name_text_view = (TextView) view.findViewById(R.id.a7_player_name);

        name_text_view.setText("Hello, "+ ((MultiplayerScroggleActivity)getActivity()).userName);

        Button b_live_game = (Button) view.findViewById(R.id.a7_live_game);
        b_live_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MultiplayerScroggleActivity)getActivity()).replaceFragment(new OnlineGameFragment(), Constants.ONLINE_LIST_FRAGMENT_TAG);
            }
        });

        Button b_offline_game = (Button) view.findViewById(R.id.a7_offline_game);
        b_offline_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MultiplayerScroggleActivity)getActivity()).replaceFragment(new OfflineGameFragment(), Constants.OFFLINE_LIST_FRAGMENT_TAG);
            }
        });

        Button b_leaderboard = (Button) view.findViewById(R.id.a7_bt_lead);
        b_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MultiplayerScroggleActivity)getActivity()).replaceFragment(new LeaderBoardFragment(), Constants.LEADERBOARD_FRAGMENT_TAG);
            }
        });

        Button b_ack = (Button) view.findViewById(R.id.a7_bt_ack);
        b_ack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.a7_ack_text)
                        .setTitle("Acknowledgements");
                // 3. Get the AlertDialog from create()
                final android.support.v7.app.AlertDialog dialog = builder.create();

                //setting the title
                builder.setTitle("Multiplayer Scroggle");
                dialog.show();
            }
        });

        ((MultiplayerScroggleActivity) getActivity()).getLocation();

        return view;
    }


}
