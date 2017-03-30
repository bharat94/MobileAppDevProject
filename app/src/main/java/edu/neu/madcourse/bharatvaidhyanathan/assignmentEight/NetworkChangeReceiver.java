package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by vaibhavshukla on 3/29/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtility.getConnectivityStatusString(context);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtility.NETWORK_STATUS_NOT_CONNECTED){
                System.out.println("Network disconnected");
                Toast.makeText(context, "Disconnected from the internet, connect to the internet and restart the game", Toast.LENGTH_SHORT).show();
            }else{
                System.out.println("Connect to internet");
                Toast.makeText(context, "Network Connected back", Toast.LENGTH_SHORT).show();
            }

        }
    }
}