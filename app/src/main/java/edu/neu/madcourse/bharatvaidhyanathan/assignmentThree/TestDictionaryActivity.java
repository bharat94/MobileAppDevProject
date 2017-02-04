package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.MainActivity;
import edu.neu.madcourse.bharatvaidhyanathan.R;

public class TestDictionaryActivity extends AppCompatActivity {

    private EditText editText1;
    private ArrayList<String> matchesStringArray;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    AssetManager am;
    private ArrayList<String> wordlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dictionary);

        //getting the assets
        am = this.getAssets();

        //creating the alert dialog

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.ack_text)
                .setTitle("Acknowledgements");

        // 3. Get the AlertDialog from create()
        final AlertDialog dialog = builder.create();


        //setting the title
        setTitle("Test Dictionary");

        //initializing the string array
        matchesStringArray = new ArrayList<>();

        //setting up the array adapter
        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, matchesStringArray);

        //Binding the listview to the adapter
        listView = (ListView) findViewById(R.id.dictionaryListView);
        listView.setAdapter(arrayAdapter);

        //setting up the editText
        editText1 = (EditText) findViewById(R.id.texteditbox1);
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if(before!=count && DictObj.getInstance().searchTree(s.toString())) {
//                    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
//                    matchesStringArray.add(s.toString());
//                    arrayAdapter.notifyDataSetChanged();
//                    System.out.println("#"+s+"#");
//                    System.out.println("start : " +start + ", before : "+before+", count : "+count);
//                }


                if(before!=count && search(s.toString())){
                    String str = s.toString();
                    if(!matchesStringArray.contains(str)) {
                        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        matchesStringArray.add(str);
                        arrayAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button b_ack = (Button) findViewById(R.id.dictionaryAckButton);
        b_ack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        final Button b_back = (Button) findViewById(R.id.dictionaryBackButton);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Going back to the previous activity
                finish();
            }
        });

        final Button b_clear = (Button) findViewById(R.id.dictionaryClearButton);
        b_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear off all the data that has been stored
                editText1.setText("");
                matchesStringArray.clear();
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }


    public boolean search (String str){

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
            return wordlist.contains(str);
        }

        return false;
    }


}
