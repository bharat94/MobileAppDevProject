package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.R;

public class TestDictionaryActivity extends AppCompatActivity {

    private EditText editText1;
    private ArrayList<String> matchesStringArray;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dictionary);

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
                matchesStringArray.add(s.toString());
                arrayAdapter.notifyDataSetChanged();
                System.out.println(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
