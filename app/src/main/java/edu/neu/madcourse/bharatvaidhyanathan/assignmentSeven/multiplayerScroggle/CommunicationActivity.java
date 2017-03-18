package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Scanner;

import edu.neu.madcourse.bharatvaidhyanathan.R;

import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle.fragments.GameSelectionFragment;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle.fragments.UserDetailsFragment;


public class CommunicationActivity extends Activity {

    private static final String TAG = CommunicationActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAah3rOSw:APA91bHgST1osJaZru9lXAy8xGT5mclUeLJkm35qKuiYzUw7KAmihGh16MhNmuLg7DXdqlBpavGimZ0KmTF2dWbMBwQBmC5n2dugmgXoD09tuawr2NCyRJuoS-SzwClj7-RtWWq481Oc";

    private DatabaseReference mDatabase;
    private ArrayList<User> mUsers = new ArrayList<User>();
    private ArrayList<String> mUserNames = new ArrayList<String>();
    private ArrayAdapter mUsersAdapter;
    private ListView userList;
    private int mContainerId = R.id.fragment_container_new;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager =getFragmentManager();
    private String mCurrentFragment;
    public String userName;
    public String email;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.communication_activity_new);

        checkUser();
        if(userName==null || userName.equals("") ) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(mContainerId, new UserDetailsFragment(), Constants.USER_FRAGMENT_TAG);
            mCurrentFragment = Constants.USER_FRAGMENT_TAG;
            fragmentTransaction.commitAllowingStateLoss();
        }
        else
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(mContainerId, new GameSelectionFragment(), Constants.GAME_SELECTION_FRAGMENT_TAG);
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

            mUsersAdapter = new ArrayAdapter<String>(CommunicationActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, mUserNames);
            userList.setAdapter(mUsersAdapter);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }



    public void pushNotification(String client_token) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "Firebase Cloud Messaging (App)");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "");

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
                    Toast.makeText(CommunicationActivity.this,resp,Toast.LENGTH_LONG);
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


