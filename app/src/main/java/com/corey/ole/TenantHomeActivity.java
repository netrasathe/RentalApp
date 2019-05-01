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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TenantHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mAnnouceRecycler;
    private RecyclerView mUpcomingRecycler;
    private TextView mUsername;
    private String mUid;

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
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("property").orderByChild("tenants").addListenerForSingleValueEvent(new ValueEventListener() {
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
        //data.add("Building Maintenance is on 04/20/19");
        //data.add("The backdoor lock is broken");
    }

    private void setUpcoming() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Rent due on next Tuesday 04/30/19");
        data.add("Repair Request on 04/22/19");
        AnnouncementAdapter adapter = new AnnouncementAdapter(data);
        mUpcomingRecycler.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
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
}