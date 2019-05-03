package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MessagesActivity extends NavDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mMessagesRecycler;
    private ArrayList<Message> mMessages;

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
        mMessages = new ArrayList<>();
        setMessages();
    }

    private void setMessages() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference messRef = db.getReference("messages");

        Query query = messRef.orderByChild("participants");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMessages.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArrayList participants = (ArrayList<String>) child.child("participants").getValue();
                    String convId = child.getKey();
                    if (participants.contains(mUid)) {
                        Iterable<DataSnapshot> allMessages = child.child("messages").getChildren();
                        Date d = new Date();
                        d.setTime(0);
                        Message newMessage = new Message("", d, "", true, convId);
                        for (DataSnapshot mess : allMessages) {
                            if (mess.child("Date").getValue(Date.class) == null) {
                                newMessage = new Message(mess.child("Message").getValue(String.class),
                                        new Date(),
                                        mess.child("Sender").getValue(String.class),
                                        mess.child("Read").getValue(Boolean.class),
                                        convId);
                            } else if (mess.child("Date").getValue(Date.class).getTime() > newMessage.getDate().getTime()) {
                                newMessage = new Message(mess.child("Message").getValue(String.class),
                                        mess.child("Date").getValue(Date.class),
                                        mess.child("Sender").getValue(String.class),
                                        mess.child("Read").getValue(Boolean.class),
                                        convId);
                            }
                        }
                        mMessages.add(newMessage);
                    }
                }

                setAdapterAndUpdateData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // Save state
        Parcelable recyclerViewState;
        recyclerViewState = mMessagesRecycler.getLayoutManager().onSaveInstanceState();

        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        Collections.sort(mMessages, new Comparator<Message>() {
            @Override
            public int compare(Message comment, Message t1) {
                return -Long.compare(comment.getDate().getTime(), t1.getDate().getTime());
            }
        });
        MessageAdapter adapter = new MessageAdapter(mMessages, this);
        mMessagesRecycler.setAdapter(adapter);

        // Restore state
        mMessagesRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);
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
        getMenuInflater().inflate(R.menu.add, menu);
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
            Intent intent = new Intent(this, ConversationActivity.class);
            intent.putExtra("New Message", true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, TenantHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_messages) {
            // Do nothing
        } else if (id == R.id.nav_rent) {
            Intent intent = new Intent(this, RentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lease) {

        } else if (id == R.id.nav_repair) {
            Intent intent = new Intent(this, RepairsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, TenantTenantProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getConversation(String convId) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("Conversation Id", convId);
        startActivity(intent);
    }
}
