package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import edu.neu.madcourse.bharatvaidhyanathan.MainActivity;
import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.NineLetterDict;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.gameFiles.scroggleOffline.activities.OfflineScroggleGameActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle.Constants;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle.MainGameFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentEight.multiplayerScroggle.PlayerRegistrationFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentOne.AboutMeActivity;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;

public class MultiplayerScroggleActivity extends AppCompatActivity {


    private static final String TAG = MultiplayerScroggleActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAah3rOSw:APA91bHgST1osJaZru9lXAy8xGT5mclUeLJkm35qKuiYzUw7KAmihGh16MhNmuLg7DXdqlBpavGimZ0KmTF2dWbMBwQBmC5n2dugmgXoD09tuawr2NCyRJuoS-SzwClj7-RtWWq481Oc";

    public DatabaseReference mDatabase;

    private ArrayList<User> mUsers = new ArrayList<User>();

    private ArrayList<String> mUserNames = new ArrayList<String>();

    private ArrayAdapter mUsersAdapter;

    private ListView userList;

    private int mContainerId = R.id.fragment_container_new;

    private FragmentTransaction fragmentTransaction;

    private FragmentManager fragmentManager = getFragmentManager();

    private String mCurrentFragment;

    public String userName;

    public String email;

    private String provider;
    private Criteria criteria;

    private static final int MY_PERMISSIONS_REQUEST_LOC = 0;
    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 1;

// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.a8_activity_multiplayer_scroggle);


        getSupportActionBar().setTitle("Two Player Word Game");

        checkUser();
        if (userName == null || userName.equals("")) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(mContainerId, new PlayerRegistrationFragment(), Constants.USER_FRAGMENT_TAG);
            mCurrentFragment = Constants.USER_FRAGMENT_TAG;
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(mContainerId, new MainGameFragment(), Constants.GAME_SELECTION_FRAGMENT_TAG);
            mCurrentFragment = Constants.GAME_SELECTION_FRAGMENT_TAG;
            fragmentTransaction.commitAllowingStateLoss();
        }


    }

    public void checkUser()
    {

        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        userName = prefs.getString(Constants.PLAYER_NAME, null);//"No name defined" is the default value.
        email = prefs.getString(Constants.PLAYER_EMAIL, null); //0 is the default value.

    }


    public void replaceFragment(Fragment fragment, String tag) {
        try {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(mContainerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            mCurrentFragment = tag;
            fragmentTransaction.commitAllowingStateLoss();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public void populateUsers()
    {
        System.out.println("POPULATE ACTIVITY Users called");
        mUsers.clear();
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Entering user ds code");
                Iterable<DataSnapshot> dataSnap  = dataSnapshot.getChildren();
                for (DataSnapshot key : dataSnap)
                {
                    mUsers.add(key.getValue(User.class));
                    mUserNames.add(key.getValue(User.class).getName());
                }

                mUsersAdapter = new ArrayAdapter<String>(MultiplayerScroggleActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, mUserNames);
                userList.setAdapter(mUsersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void pushNotification(String client_token, String words) {



        //game has been pushed to db but the reference has not yet been attached to the payload
        // TODO attach payload and set onclick
        Intent intent = new Intent(this, OfflineScroggleGameActivity.class);
        Bundle b = new Bundle();
        b.putBoolean(Constants.IS_HOSTED,false);
        b.putString(Constants.GAME_BOARD,words);
        b.putString(Constants.GAME_ID,Constants.GameID);
        System.out.println("");
        intent.putExtra("value",b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent p = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "NUMAD");
            jNotification.put("body", words+"|"+Constants.GameID);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action","OfflineGame");

            // If sending to a single client
            jPayload.put("to", client_token);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            //jPayload.put("intentValue",p);
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                    Toast.makeText(MultiplayerScroggleActivity.this,resp, Toast.LENGTH_LONG);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

}


