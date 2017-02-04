package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.MainActivity;

/**
 * Created by vaidhyanathannarayanan on 31/01/17.
 */

public class DictObj{
    private static Context context = null;
    private static DictObj ourInstance;
    private static boolean doneParsing = false;
    private static Dictionary d;
    private static ArrayList<String> arr;

    public static DictObj getInstance() {
        if(ourInstance == null || !doneParsing){
            ourInstance = new DictObj();
        }
        return ourInstance;
    }

    private DictObj() {

        System.out.println("constructor called");
        context = App.getContext();
        System.out.println("Context fetched");
        Resources res = context.getResources();

//         d = new Dictionary();
//
//        //Load word list
////        InputStream ins = context.getResources().openRawResource(
////                context.getResources().getIdentifier("wordlist",
////                        "raw", context.getPackageName()));
////
////        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
//
//
//        System.out.println("Before BR");
//        BufferedReader br = new BufferedReader(new InputStreamReader(res.openRawResource(
//                res.getIdentifier("wordlist", "raw", context.getPackageName()))));
//
//        System.out.println("BR set");
//        String strLine;
//
//        //Read File Line By Line
//        try {
//            while ((strLine = br.readLine()) != null)   {
//                //System.out.println (strLine);
//                d.insert(strLine);
//            }
//            //Close the input stream
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Done Parsing");
//        //Make the tree
//        // set doneParsing to true
//        doneParsing = true;
    }

    public static boolean searchTree(String s){
        return (!s.isEmpty() && d.search(s));
    }



}
