package com.example.ekaksha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekaksha.Data.Message;
import com.example.ekaksha.Database.Adapter.ClassroomChatAdapter;
import com.example.ekaksha.Database.ClassroomContract;
import com.example.ekaksha.Database.JoinClassHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ClassroomActivity extends AppCompatActivity {
    JoinClassHelper joinClassHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ClassroomChatAdapter classroomChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        setTitle(name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name + "/messages");
        TextInputEditText messageBox = (TextInputEditText) findViewById(R.id.editTextNumberDecimal);
        FloatingActionButton send = (FloatingActionButton) findViewById(R.id.floating_action_button);
        joinClassHelper = new JoinClassHelper(getBaseContext(), user.getUid());
        db = joinClassHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + ClassroomContract.MessageList.TABLE_NAME + user.getUid(), null);
        classroomChatAdapter = new ClassroomChatAdapter(getBaseContext(), cursor);
        Log.v(" message data", "inserted"+cursor.getCount());
        ListView recyclerView = (ListView) findViewById(R.id.reyclerview_message_list);
        recyclerView.setSelection(recyclerView.getCount());
        recyclerView.setAdapter(classroomChatAdapter);
        // Create and/or open a database to read from it

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageBox.getText().length()>0) {
                    Message msg = new Message();
                    msg.setUserName(user.getEmail());
                    msg.setMessage(messageBox.getText().toString());
                    msg.setTime(new Date().getTime());
                    databaseReference.push().setValue(msg);
                    messageBox.setText("");
                }
            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                String[] projection = {ClassroomContract.MessageList.COLUMN_MESSAGE_ID};
                String selection = ClassroomContract.MessageList.COLUMN_MESSAGE_ID + "=?";
                Log.v("classroom", "time" + msg.getTime());
                String[] selectionArgs = {snapshot.getKey()};
                Cursor cursor_query = db.query(ClassroomContract.MessageList.TABLE_NAME + user.getUid(), projection, selection, selectionArgs, null, null, null);
                if (cursor_query.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put(ClassroomContract.MessageList.COLUMN_CLASSROOM_ID, name);
                    values.put(ClassroomContract.MessageList.COLUMN_MESSAGE_ID, snapshot.getKey());
                    values.put(ClassroomContract.MessageList.COLUMN_NAME, msg.getUserName());
                    values.put(ClassroomContract.MessageList.COLUMN_URL, msg.getFileUrl());
                    values.put(ClassroomContract.MessageList.COLUMN_message, msg.getMessage());
                    values.put(ClassroomContract.MessageList.COLUMN_TIME, msg.getTime());



                    db.insert(ClassroomContract.MessageList.TABLE_NAME + user.getUid(), null, values);
                    cursor_query.close();

                    //Intent intent = getActivity().getIntent();
                    // getActivity().finish();
                    // startActivity(intent);
                    // Cursor temp_cursor   = db.rawQuery("SELECT * FROM " + ClassroomContract.joined.JOINED_TABLE_NAME + firebaseUser.getUid(), null);
                    // cursor.requery();
                    // cursor.close();
                    // cursor=temp_cursor;
                    cursor.requery();
                    Log.v(" message data", "inserted"+cursor.getCount());
                    classroomChatAdapter.swapCursor(cursor);
                    recyclerView.setAdapter(classroomChatAdapter);
                    //  classroomlistAdapter.notifyDataSetChanged();
                    // temp_cursor.close();
                    //Toast.makeText(getBaseContext(), "Classroom Joined sucessfully", Toast.LENGTH_LONG).show();
                } else {
                    Log.v("id ", "id found");
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TextView textView=(TextView)findViewById(R.id.clasroom_activity_name);
        //textView.setText(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.classroom_create_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.create_assignment) {
            Intent intent =new Intent(this,NewAssignment.class);
            startActivity(intent);

        } else if(item.getItemId() == R.id.create_examination) {
            Intent intent =new Intent(this,NewExamination.class);
            startActivity(intent);

        }
        return true;
    }
}