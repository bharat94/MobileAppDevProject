package edu.neu.madcourse.bharatvaidhyanathan.assignmentEight;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.Manifest;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;

public class LocationManagerActivity extends AppCompatActivity {
    private Button b;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;

    private ArrayList<User> usersList = new ArrayList<User>();

    private ArrayList<String> userNamesList = new ArrayList<String>();

    private ArrayAdapter usersAdapter;

    private ListView usersListView;

    private DatabaseReference dRef;
    private String userName;
    private Button mRefreshButton;
    private TextView tvCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a8_leaderboard_fragment);

        dRef = FirebaseDatabase.getInstance().getReference();
        userName = getIntent().getExtras().getString("USN");
        usersListView = (ListView) findViewById(R.id.leaderboard_list);

        mRefreshButton = (Button) findViewById(R.id.btn_refresh_location);
        tvCurrentLocation = (TextView) findViewById(R.id.current_location_text);


        addUsersFromServerToList();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String add;
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address obj = addresses.get(0);
                    add = obj.getCountryName();
                    tvCurrentLocation.setText("My Current Location : "+add);
                    Toast.makeText(LocationManagerActivity.this, "Location updated", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    System.out.println("EXCEPTION IN GEO CODE");
                    e.printStackTrace();

                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 100, 0, listener);
            }
        });
    }



    public void addUsersFromServerToList()
    {
        System.out.println("Populating users");
        System.out.println(dRef);
        usersList.clear();
        dRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Entering user ds code");
                for (DataSnapshot key : dataSnapshot.getChildren())
                {
                    User loopUser = key.getValue(User.class);
                    usersList.add(loopUser);
                }
                usersAdapter = new ArrayAdapter<String>(LocationManagerActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, userNamesList);
                usersListView.setAdapter(usersAdapter);
                Collections.sort(usersList, new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        int a =0;
                        int b = 0;
                        if(user.getScore()!=null){
                            a = Integer.parseInt(user.getScore());
                        }
                        if(t1.getScore()!=null){
                            b = Integer.parseInt(t1.getScore());
                        }
                        return a-b;
                    }
                });

                Collections.reverse(usersList);

                int i = 1;
                for(User u : usersList){
                    if(!u.getName().equals(userName)) {
                        userNamesList.add(i++ + ") "+u.getName() + "  [Score:" + scorify(u.getScore()) + "]   [from "+u.getCountry()+" ]");
                    }
                    else
                    {
                        userNamesList.add(i++ + ") "+u.getName() + "  [Score:" + scorify(u.getScore()) + "]   [YOU]");
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("DB ERROR at addUsersFromServerToList");
            }

            public int scorify(String s){
                if(s == null) return 0;
                else return Integer.parseInt(s);
            }

        });
    }


}