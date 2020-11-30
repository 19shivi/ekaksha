package com.example.ekaksha;

import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ekaksha.Database.ClassroomContract;

import com.example.ekaksha.Database.JoinClassHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Examination_fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_examination, container, false);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        JoinClassHelper joinClassHelper=new JoinClassHelper(getActivity().getBaseContext(),firebaseUser.getUid());

        // Create and/or open a database to read from it
        SQLiteDatabase db = joinClassHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + ClassroomContract.ExaminationList.JOINED_TABLE_NAME+firebaseUser.getUid(), null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) view.findViewById(R.id.examination_Fragment);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        return view;
    }
}