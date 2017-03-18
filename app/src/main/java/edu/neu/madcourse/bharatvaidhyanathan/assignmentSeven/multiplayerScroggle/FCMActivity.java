package edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.multiplayerScroggle;

import android.content.Intent;
import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.MutableData;
        import com.google.firebase.database.Transaction;
        import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import edu.neu.madcourse.bharatvaidhyanathan.R;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.Game;
import edu.neu.madcourse.bharatvaidhyanathan.assignmentSeven.User;


public class FCMActivity extends AppCompatActivity {

    private static final String TAG = FCMActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private TextView score;
    private Button add5;
    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

        getSupportActionBar().setTitle("Communication");


        gameId=Constants.GameID;
        score = (TextView) findViewById(R.id.score);
        add5 = (Button) findViewById(R.id.add);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        waitForOtherPlayer();
        add5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGame1Joined(mDatabase,gameId);
            }
        });


    }



    public void waitForOtherPlayer() {
        mDatabase.child("games").child(gameId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game g = dataSnapshot.getValue(Game.class);

                    score.setText(g.getBoard());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    private void onGame1Joined(DatabaseReference postRef, String gameId) {
        postRef
                .child("games")
                .child(gameId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Game g = mutableData.getValue(Game.class);
                        if (g == null) {
                            return Transaction.success(mutableData);
                        }

                        g.setBoard(String.valueOf(Integer.parseInt(score.getText().toString())+10));

                        mutableData.setValue(g);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                    }
                });
    }

}