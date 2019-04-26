package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class RentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
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
        getMenuInflater().inflate(R.menu.rent, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rent) {
            // Do nothing
        } else if (id == R.id.nav_lease) {

        } else if (id == R.id.nav_documents) {

        } else if (id == R.id.nav_repair) {

        } else if (id == R.id.nav_profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
