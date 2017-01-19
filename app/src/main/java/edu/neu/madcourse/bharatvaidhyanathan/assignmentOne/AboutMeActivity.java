package edu.neu.madcourse.bharatvaidhyanathan.assignmentOne;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import edu.neu.madcourse.bharatvaidhyanathan.R;

public class AboutMeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);
        //Setting the app title
        setTitle("Bharat Vaidhyanathan");

        if (ContextCompat.checkSelfPermission(AboutMeActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            String []permissions = {Manifest.permission.READ_PHONE_STATE};
            ActivityCompat.requestPermissions(AboutMeActivity.this, permissions, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        else{
            setIMEIText();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    setIMEIText();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    TextView t =  (TextView) findViewById(R.id.imeiText);
                    String s = t.getText().toString();
                    t.setText(s+="Unable to access Phone's IMEI");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void setIMEIText(){
        TextView t =  (TextView) findViewById(R.id.imeiText);
        String s = t.getText().toString();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String str = telephonyManager.getDeviceId();

        s += str;

        t.setText(s);
    }


}
