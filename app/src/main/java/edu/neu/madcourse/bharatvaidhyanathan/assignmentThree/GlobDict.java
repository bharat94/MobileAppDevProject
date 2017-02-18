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

        if(str.length() < 3) return false;

        str = str.toLowerCase();
        String sm = str.substring(0,3);

        if(str.length() >= 3){try {
            InputStream is = am.open("words/"+sm+".txt");
            if(is == null){
                is.close();
                return false;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            wordlist = new ArrayList<>();

            String s;
            try {
                while((s = br.readLine())!=null){
                    wordlist.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                is.close();
                br.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


            if(wordlist==null || wordlist.isEmpty()) return false;
            return wordlist.contains(str);
        }

        return false;
    }
}
