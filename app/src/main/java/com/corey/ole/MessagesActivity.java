package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MessagesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mMessagesRecycler;
    private TextView mUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setDrawerData(navigationView);

        mMessagesRecycler = findViewById(R.id.rv_messages);
        mMessagesRecycler.setHasFixedSize(true);
        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        setMessages();
    }

    private void setMessages() {
        ArrayList<Message> data = new ArrayList<>();
        data.add(new Message("Lorem ipsum dolor sit amet, consectetur adipisicing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna wirl aliqua. Up " +
                "exlaborum incididunt quis nostrud exercitatn.", new Date(),
                "ZYXILsSYC9POaErJhpRUAEMNi8T2", true));
        data.add(new Message("Lorem ipsum dolor sit amet, consectetur adipisicing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna wirl aliqua. Up " +
                "exlaborum incididunt quis nostrud exercitatn.", new Date(),
                "DKYk5BGJZaWlkB9MpyMDr15O9VF2", false));
        data.add(new Message("Lorem ipsum dolor sit amet, consectetur adipisicing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna wirl aliqua. Up " +
                "exlaborum incididunt quis nostrud exercitatn.", new Date(),
                "ZYXILsSYC9POaErJhpRUAEMNi8T2", false));
        data.add(new Message("Lorem ipsum dolor sit amet, consectetur adipisicing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna wirl aliqua. Up " +
                "exlaborum incididunt quis nostrud exercitatn.", new Date(),
                "DKYk5BGJZaWlkB9MpyMDr15O9VF2", true));
        MessageAdapter adapter = new MessageAdapter(data, this);
        mMessagesRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            // Do nothing
        } else if (id == R.id.nav_rent) {
            Intent intent = new Intent(this, RentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lease) {

        } else if (id == R.id.nav_documents) {

        } else if (id == R.id.nav_repair) {

        } else if (id == R.id.nav_profile) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getConversation() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    private void setDrawerData(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        mUsername = header.findViewById(R.id.txt_name);
        TextView txt_email = header.findViewById(R.id.txt_email);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference user = usersRef.child(uid);
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