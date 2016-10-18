package tieorange.com.leanappenginetodoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getCanonicalName();
  public static String FIREBASE_DB_URL = "https://todofirebaseproject-9e3a9.firebaseio.com/";
  private ListView mListView;
  private FirebaseDatabase mDatabase;
  private DatabaseReference mMyRef;
  private ArrayAdapter<String> mAdapter;
  private EditText mText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    initViews();

    mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

    mListView.setAdapter(mAdapter);

    mDatabase = FirebaseDatabase.getInstance();
    mMyRef = mDatabase.getReference("todoItems");
    mMyRef.addChildEventListener(getChildEventListener());

    initAppEngine();
  }

  private void initAppEngine() {
    new EndpointAsyncTask().execute(new Pair<Context, String>(this, "Andriiiiiiiiiii"));
  }

  @NonNull private ChildEventListener getChildEventListener() {
    return new ChildEventListener() {
      @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String taskName = dataSnapshot.getValue(String.class);
        mAdapter.add(taskName);
      }

      @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
        String taskName = dataSnapshot.getValue(String.class);
        mAdapter.remove(taskName);
      }

      @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String value = dataSnapshot.getValue(String.class);
      }

      @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {
      }

      @Override public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError.toException().getMessage() + "]");
      }
    };
  }

  private void initViews() {
    mListView = (ListView) findViewById(R.id.listView);
    mText = (EditText) findViewById(R.id.todoText);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        DatabaseReference childRef = mMyRef.push();
        childRef.setValue(mText.getText().toString());

        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
      }
    });

    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemAtPosition = (String) mListView.getItemAtPosition(position);
        Query query = mMyRef.orderByValue().equalTo(itemAtPosition);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChildren()) {
              DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
              firstChild.getRef().removeValue();
            }
          }

          @Override public void onCancelled(DatabaseError databaseError) {

          }
        });
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
