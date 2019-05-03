package com.corey.ole;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavDrawerActivity extends AppCompatActivity {
    protected TextView mUsername;
    protected String mUid;

    protected void setDrawerData(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        mUsername = header.findViewById(R.id.txt_name);
        TextView txt_email = header.findViewById(R.id.txt_email);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        mUid = auth.getCurrentUser().getUid();
        DatabaseReference user = usersRef.child(mUid);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot data) {
                String firstName = data.child("First Name").getValue(String.class);
                String lastName = data.child("Last Name").getValue(String.class);
                mUsername.setText(firstName + " " + lastName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("App Bar", String.valueOf(databaseError));
            }
        });
        txt_email.setText(email);
    }
}