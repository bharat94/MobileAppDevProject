package edu.neu.madcourse.bharatvaidhyanathan.assignmentFive;

import android.content.Context;
import android.graphics.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class NineLetterDict {
    private static NineLetterDict ourInstance;
    private static Context mContext;
    private static ArrayList<String> arr;
    private static char[][][] charr;
    private static Random randomGenerator;

    public static NineLetterDict getInstance(Context context)
    {
        mContext = context;
        if (ourInstance == null)
        {
            ourInstance = new NineLetterDict();
            return  ourInstance;

        }
        else
            return ourInstance;
    }

    private NineLetterDict() {
        arr = new ArrayList<String>();
        fetchWords();
    }


    private static void fetchWords(){
        try {
            System.out.println("Context : "+mContext);
            System.out.println("Vaidbhansdalkdj");
            InputStream reader = mContext.getResources().openRawResource(mContext.getResources().getIdentifier("nineletterwords", "raw", mContext.getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(reader));
            System.out.println("bharat");
            String str = "";
            int count = 0;
            int total_count = 60121;

            ArrayList<Integer> al = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                int a = (int) Math.floor(Math.random() * total_count);
                if (!al.contains(a)) {
                    al.add(a);
                } else
                    i--;
            }

            Collections.sort(al);

            int words_i = 0;

            while ((str = br.readLine()) != null) {
                // application logic here
                if (words_i >= 9) break;
                if (count == al.get(words_i)) {
                    System.out.println(str);
                    arr.add(str);
                    words_i++;
                }
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetWords(){
        arr = new ArrayList<String>();
        fetchWords();
    }

    public char[][] getMatrix(){
        // arr has the words, now you want to populate a new array
        char[][] brr = new char[9][9];

        for(int i=0; i<9; i++){
            char [][] crr = new char[3][3];
            initArr(crr);
            fitGrid(crr, arr.get(i));
            int index = 0;
            for (int n = 0; n < 3; n++) {
                brr[i][index++] = crr[n][0];
                brr[i][index++] = crr[n][1];
                brr[i][index++] = crr[n][2];
            }
        }

        return brr;
    }

    private static void initArr(char[][] arr){
        for(int i=0; i<arr.length; i++){
            for(int j=0; j<arr[0].length; j++){
                arr[i][j] = '1';
            }
        }
    }


    public static void fitGrid(char[][] arr, String word){
        Random randomGenerator1 = new Random();
        int rand_i = randomGenerator1.nextInt(3);

        Random randomGenerator2 = new Random();
        int rand_j = randomGenerator2.nextInt(3);


        fitGridRand(arr, word, rand_i, rand_j);
    }

    private static boolean fitGridRand(char[][] arr, String word, int i, int j){
        if(word == "") return true;
        if(i<0 || j<0 || i>=arr.length || j>=arr[0].length )return false;

        if(arr[i][j]!='1'){
            //arr[i][j] = '1';
            return false;
        }

        arr[i][j] = word.charAt(0);
        if(word.length()!=1)
            word = word.substring(1);
        else
            word = "";


        ArrayList<Point> valids = new ArrayList<Point>();
        valids.add(new Point(i+1,j));
        valids.add(new Point(i-1,j));
        valids.add(new Point(i,j+1));
        valids.add(new Point(i,j-1));
        valids.add(new Point(i+1,j+1));
        valids.add(new Point(i+1,j-1));
        valids.add(new Point(i-1,j+1));
        valids.add(new Point(i-1,j-1));

        while(!valids.isEmpty()){
            Point p = randPop(valids);
            if(fitGridRand(arr, word, p.x, p.y)) return true;
        }

        arr[i][j] = '1';
        return false;

    }


    public static Point randPop(ArrayList<Point> arr){
        randomGenerator = new Random();
        int index = randomGenerator.nextInt(arr.size());
        Point item = arr.remove(index);
        return item;
    }


}
