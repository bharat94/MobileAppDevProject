package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import android.app.Application;
import android.content.Context;

/**
 * Created by vaidhyanathannarayanan on 31/01/17.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
