package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by vaidhyanathannarayanan on 17/02/17.
 */
public class GlobDict {
    private static GlobDict ourInstance;
    private static Context mContext;
    private final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    AssetManager am;
    private ArrayList<String> wordlist;

    public static GlobDict getInstance(Context context) {
        mContext = context;
        if(ourInstance == null){
            ourInstance = new GlobDict();
        }
        return ourInstance;
    }

    private GlobDict() {
        am = mContext.getAssets();
    }

    public boolean search (String str){

        if(str.length() < 2) return false;

        str = str.toLowerCase();

        if(str.length() == 3) {
            try {
                InputStream is = am.open("words/"+str+".txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                wordlist = new ArrayList<>();

                String s;
                while((s = br.readLine())!=null){
                    wordlist.add(s);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(str.length() >= 3){
            if(wordlist==null || wordlist.isEmpty()) return false;
            return wordlist.contains(str);
        }

        return false;
    }
}
