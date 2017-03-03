package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.neu.madcourse.bharatvaidhyanathan.MainActivity;
import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentThree.DictObj;

public class CommunicationActivity extends AppCompatActivity {

    private static final String TAG = CommunicationActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAah3rOSw:APA91bHgST1osJaZru9lXAy8xGT5mclUeLJkm35qKuiYzUw7KAmihGh16MhNmuLg7DXdqlBpavGimZ0KmTF2dWbMBwQBmC5n2dugmgXoD09tuawr2NCyRJuoS-SzwClj7-RtWWq481Oc";

    // This is the client registration token
    private static final String CLIENT_REGISTRATION_TOKEN = "fyolgZuInII:APA91bGq-lZsGJ8YxwSdWItATKSbVwYCHdSW01MAJMkzADnkYWMRLoe6k9uBsty8nFRP-sCVrWYu8EDVd_147lUESZ1fqH7dpBeXEdCPyDt-8WM2GccR2qnTPFGVpVI-7yU_Awop-km6";

    private List<String> listData;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        logToken();

        final EditText editText_username = (EditText) findViewById(R.id.editTextUsername);
        final EditText editText_emailId = (EditText) findViewById(R.id.editTextEmailID);



        Button b = (Button) findViewById(R.id.buttonRegister);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User current_user;
                current_user = new User();
                current_user.setEmailID(editText_emailId.getText().toString());
                current_user.setName(editText_username.getText().toString());
                current_user.setRegID(FirebaseInstanceId.getInstance().getToken());

                if(current_user == null){
                    Toast.makeText(CommunicationActivity.this,"Please enter a username and password",Toast.LENGTH_SHORT);
                }
                else {
                    NetworkThread n = new NetworkThread(current_user);
                    n.execute();
                }
            }
        });

        listData = new ArrayList<String>();
        //listData.add("hello");

        final ListView listView = (ListView) findViewById(R.id.listview_users);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listData);

        listView.setAdapter(adapter);

        //listData.add("world");
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

            }

        });

    }



    public void pushNotification(View type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(CLIENT_REGISTRATION_TOKEN);
            }
        }).start();
    }

    private void pushNotification(String client_token) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "Firebase Cloud Messaging (App)");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

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


    private void logToken(){
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        //String msg = getString(R.string.msg_token_fmt, token);
        //Log.d(TAG, msg);
        //Toast.makeText(CommunicationActivity.this, msg, Toast.LENGTH_SHORT).show();
    }




    private class NetworkThread extends AsyncTask {

        //private Intent intent;
        private ProgressDialog progressDialog;
        private User user;
        private long users_count;

        public NetworkThread(User user) {
            //this.intent = intent;
            this.progressDialog = new ProgressDialog(CommunicationActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Connecting to the Database...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            this.user = user;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //Network calls

            //DictObj.getInstance();
            //Thread.sleep(4000);
            String token = FirebaseInstanceId.getInstance().getToken();
            final DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();
            System.out.println("FireBase Token : "+ token);
            System.out.println("FireBase Reference : " + dRef);

            dRef.child("users_count").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users_count = (Long) dataSnapshot.getValue();
                    System.out.println("User Count : " + users_count);

                    users_count ++;

                    Map<String, Object> update = new HashMap<String, Object>();
                    update.put("user"+users_count, user);
                    dRef.child("users").updateChildren(update);

                    update = new HashMap<String, Object>();
                    update.put("users_count", users_count);
                    dRef.updateChildren(update);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*dRef.child("users").addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    System.out.println("Value : " + dataSnapshot.getValue());

                    listData.add(dataSnapshot.child("name").getValue(String.class));

                    System.out.println(listData);
                    //adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    System.out.println("Value : " + dataSnapshot.getValue());

                    listData.add(dataSnapshot.child("name").getValue(String.class));
                    //adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    System.out.println("Value : " + dataSnapshot.getValue());
                    listData.add(dataSnapshot.child("name").getValue(String.class));
                    //adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/


            dRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        listData.add(ds.child("name").getValue(String.class));
                    }

                    adapter.notifyDataSetChanged();
                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            //UiThread
            switch_phase();
            // dialog kill
            progressDialog.dismiss();
            adapter.notifyDataSetChanged();
            super.onPostExecute(o);
        }

        @Override
        protected void onPreExecute() {
            // dialog display
            super.onPreExecute();
            progressDialog.show();
        }
    }


    public void switch_phase(){
        LinearLayout ll1 = (LinearLayout) findViewById(R.id.linear_layout_register);
        LinearLayout ll2 = (LinearLayout) findViewById(R.id.linear_layout_registered);
        if(ll1.getVisibility() == View.VISIBLE)
            ll1.setVisibility(View.GONE);
        else
            ll1.setVisibility(View.VISIBLE);
        if(ll2.getVisibility() == View.VISIBLE)
            ll2.setVisibility(View.GONE);
        else
            ll2.setVisibility(View.VISIBLE);
    }


}
