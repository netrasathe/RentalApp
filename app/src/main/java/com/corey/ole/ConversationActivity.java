package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
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
import java.util.List;

public class ConversationActivity extends NavDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mConversationRecycler;
    private ArrayList<Message> data = new ArrayList<>();
    private EditText commentInputBox;
    private EditText toInputBox;
    private RelativeLayout layout;
    private FloatingActionButton sendButton;
    private String mConvId;
    private FirebaseDatabase mDb;
    private String mTitle = "";
    private Boolean newMessage;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.comment_layout);
        commentInputBox = layout.findViewById(R.id.comment_input_edit_text);
        sendButton = layout.findViewById(R.id.send_button);
        toInputBox = layout.findViewById(R.id.to_edit_text);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setDrawerData(navigationView);

        Intent intent = getIntent();
        newMessage = intent.getBooleanExtra("New Message", false);
        if (newMessage) {
            findViewById(R.id.llTo).setVisibility(View.VISIBLE);
            this.setTitle("");
        } else {
            findViewById(R.id.llTo).setVisibility(View.GONE);
            mConvId = intent.getStringExtra("Conversation Id");
            getConversation();
        }
        setOnClickForSendButton();
    }

    private void getConversation() {
        setmTitle();

        mConversationRecycler = findViewById(R.id.rv_conversation);
        mConversationRecycler.setHasFixedSize(true);
        mConversationRecycler.setLayoutManager(new LinearLayoutManager(this));

        getMessages();
    }

    private void setmTitle() {
        DatabaseReference particRef = mDb.getReference("messages/" + mConvId + "/participants");

        Query query = particRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> participants = (ArrayList<String>) dataSnapshot.getValue();
                for (String participant : participants) {
                    if (!participant.equals(mUid)) {
                        DatabaseReference nameRef = mDb.getReference("users/" + participant);
                        Query query = nameRef.orderByKey();
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String firstName = dataSnapshot.child("First Name").getValue(String.class);
                                String lastName = dataSnapshot.child("Last Name").getValue(String.class);
                                mTitle = firstName + " " + lastName;
                                setTitle();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setTitle() {
        this.setTitle(mTitle);
    }

    private void getMessages() {
        DatabaseReference comsRef = mDb.getReference("messages/" + mConvId + "/messages");

        Query query = comsRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String message = child.child("Message").getValue(String.class);
                    String sender = child.child("Sender").getValue(String.class);
                    Date date = child.child("Date").getValue(Date.class);
                    Boolean read = child.child("Read").getValue(Boolean.class);
                    Message newMessage = new Message(message, date, sender, read, mConvId);
                    if (sender != mUid && !read) {
                        mDb.getReference("messages/" + mConvId + "/messages/" + child.getKey() + "/Read").setValue(true);
                    }
                    data.add(newMessage);
                }

                setConversation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setConversation() {
        // Save state
        Parcelable recyclerViewState;
        recyclerViewState = mConversationRecycler.getLayoutManager().onSaveInstanceState();

        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        Collections.sort(data, new Comparator<Message>() {
            @Override
            public int compare(Message comment, Message t1) {
                return comment.getDate().compareTo(t1.getDate());
            }
        });
        ConversationAdapter adapter = new ConversationAdapter(data);
        mConversationRecycler.setAdapter(adapter);

        // Restore state
        mConversationRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        try {
            mConversationRecycler.smoothScrollToPosition(data.size() - 1);
        } catch (IllegalArgumentException e) {
            // Do nothing
        }
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
        getMenuInflater().inflate(R.menu.conversation, menu);
        return true;
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
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rent) {
            Intent intent = new Intent(this, RentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lease) {

        } else if (id == R.id.nav_documents) {

        } else if (id == R.id.nav_repair) {
            Intent intent = new Intent(this, RepairsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setOnClickForSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = "";
                if (newMessage) {
                    to = toInputBox.getText().toString();
                    if (TextUtils.isEmpty(to)) {
                        toInputBox.requestFocus();
                        toInputBox.setError("Please enter an email.");
                        return;
                    } else {
                        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(to)
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();
                                    if (signInMethods.size() > 0) {
                                        // User exists
                                    } else {
                                        toInputBox.setError("Not a valid user.");
                                    }
                                } else {
                                    Log.e("Conversation", "Error getting sign in methods for user", task.getException());
                                }
                            }
                        });
                    }
                }
                if (!TextUtils.isEmpty(toInputBox.getError())) {
                    return;
                }
                comment = commentInputBox.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    // don't do anything if nothing was added
                    commentInputBox.requestFocus();
                } else {
                    // clear edit text, post comment
                    commentInputBox.setText("");
                    toInputBox.setText("");
                    if (newMessage) {
                        newMessage = false;
                        findViewById(R.id.llTo).setVisibility(View.GONE);
                        mDb.getReference("users").orderByChild("Email").equalTo(to).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DatabaseReference conv = mDb.getReference().child("messages").push();
                                mConvId = conv.getKey();
                                ArrayList<String> part = new ArrayList<>();
                                part.add(mUid);
                                String toUid = "";
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    toUid = child.getKey();
                                }
                                part.add(toUid);
                                conv.child("participants").setValue(part);
                                getConversation();
                                postNewComment(comment);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        return;
                    }
                    postNewComment(comment);
                }
            }
        });
    }

    private void postNewComment(String messageText) {
        DatabaseReference comsRef = mDb.getReference("messages/" + mConvId + "/messages");
        DatabaseReference comm = comsRef.push();
        comm.child("Sender").setValue(mUid);
        comm.child("Date").setValue(new Date());
        comm.child("Message").setValue(messageText);
        comm.child("Read").setValue(false);
    }
}
