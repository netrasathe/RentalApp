package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TenantHomeActivity extends NavDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mAnnouceRecycler;
    private RecyclerView mUpcomingRecycler;
    private String mUid;
    private FirebaseDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_home);
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

        mDb = FirebaseDatabase.getInstance();

        mAnnouceRecycler = findViewById(R.id.rv_announcements);
        mAnnouceRecycler.setHasFixedSize(true);
        mAnnouceRecycler.setLayoutManager(new LinearLayoutManager(this));

        mUpcomingRecycler = findViewById(R.id.rv_upcoming);
        mUpcomingRecycler.setHasFixedSize(true);
        mUpcomingRecycler.setLayoutManager(new LinearLayoutManager(this));

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setAnnouncements();
        setUpcoming();
    }

    private void setAnnouncements() {
        mDb.getReference("property").orderByChild("tenants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArrayList tenants = (ArrayList<String>) child.child("tenants").getValue();
                    if (tenants != null && tenants.contains(mUid)) {
                        ArrayList<String> data = new ArrayList<>();
                        Iterable<DataSnapshot> announcements = child.child("announcements").getChildren();
                        for (DataSnapshot ds : announcements) {
                            data.add(ds.getValue(String.class));
                        }
                        AnnouncementAdapter adapter = new AnnouncementAdapter(data);
                        mAnnouceRecycler.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpcoming() {
        mDb.getReference("users/" + mUid + "/repairs").orderByChild("Date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> data = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Date d = child.child("Date").getValue(Date.class);
                    String date = new SimpleDateFormat("MM/dd/yy").format(d);
                    String request = child.child("Request").getValue(String.class);
                    if (request.length() > 15) {
                        data.add("Repair Request submitted " + date + ": " + request.substring(0, 15) + "...");
                    } else {
                        data.add("Repair Request submitted " + date + ": " + request);
                    }
                }
                Collections.reverse(data);
                AnnouncementAdapter adapter = new AnnouncementAdapter(data);
                mUpcomingRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        getMenuInflater().inflate(R.menu.tenant_home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Do nothing
        } else if (id == R.id.nav_messages) {
            Intent intent = new Intent(this, TenantMessagesActivity.class);
            startActivity(intent);
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
}