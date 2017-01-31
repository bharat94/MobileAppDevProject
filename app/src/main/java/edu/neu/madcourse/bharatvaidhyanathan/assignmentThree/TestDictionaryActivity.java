package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.neu.madcourse.bharatvaidhyanathan.R;

public class TestDictionaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dictionary);

        //setting the title
        setTitle("Test Dictionary");

        //initializing the string array
        ArrayList<String> matchesStringArray = new ArrayList<>();

        //setting up the array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, matchesStringArray);

        //Binding the listview to the adapter
        ListView listView = (ListView) findViewById(R.id.dictionaryListView);
        listView.setAdapter(arrayAdapter);
        

    }

}
