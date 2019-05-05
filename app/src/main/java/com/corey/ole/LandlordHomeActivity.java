package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LandlordHomeActivity extends NavDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    PropertyAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<Property> mProperties = new ArrayList<>();
    ArrayList<String> mPropertyIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setDrawerData(navigationView);

        mRecyclerView = findViewById(R.id.landlord_home_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        DatabaseReference property = ref.child(id);

        property.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPropertyIds = (ArrayList<String>) dataSnapshot.child("properties").getValue();
                DatabaseReference ref = database.getReference("property");

                for (String id: mPropertyIds) {
                    DatabaseReference p = ref.child(id);
                    p.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mProperties.add(dataSnapshot.getValue(Property.class));
                            setAdapterAndUpdateData();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void setAdapterAndUpdateData() {
        mAdapter = new PropertyAdapter(this, mProperties);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PropertyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Property p = mProperties.get(position);
                startNewActivity(p);
            }

        });
    }

    public void startNewActivity(Property p) {
        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra("id", p.getId());
        startActivity(intent);
    }

//    private void addProperty() {
//        ArrayList<String> policies = new ArrayList<>();
//        policies.add("Thou shall not kill!");
//        policies.add("Thou shall not eat roommate's food!");
//        policies.add("Enjoy!");
//        ArrayList<String> notes = new ArrayList<>();
//        notes.add("No dogs allowed! Only cats.");
//        notes.add("No smoking cigarettes! Only weed");
//
//        Property p1 = new Property("Benvenue Apartments", "234 Shattuck Avenue", "Berkley, CA 95054", R.drawable.apartment,
//                new ArrayList<String>(), policies, notes, new ArrayList<String>(), new ArrayList<String>());
//        mProperties.add(p1);
//        Property p2 = new Property("Martinez Commons", "21 Bowditch Avenue", "Berkley, CA 94076", R.drawable.martinez_commons,
//                new ArrayList<String>(), policies, new ArrayList<String>(), notes, new ArrayList<String>());
//        mProperties.add(p2);
//        Property p3  = new Property("Telegraph Apartments", "945 Telegraph Avenue", "Berkley, CA 94704", R.drawable.telegraph,
//                new ArrayList<String>(), policies, notes, new ArrayList<String>(), new ArrayList<String>());
//        mProperties.add(p3);
//
//        Property p4  = new Property("Unit 1", "532 Peidmont Avenue", "Berkley, CA 94708", R.drawable.unit,
//                policies, policies, notes, new ArrayList<String>(), new ArrayList<String>());
//        mProperties.add(p4);
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference property = database.getReference("property");
//        DatabaseReference d1 = property.child(p1.getId());
//        d1.setValue(p1);
//        DatabaseReference d2 = property.child(p2.getId());
//        d2.setValue(p2);
//        DatabaseReference d3 = property.child(p3.getId());
//        d3.setValue(p3);
//        DatabaseReference d4 = property.child(p4.getId());
//        d4.setValue(p4);
//
//        setAdapterAndUpdateData();
//    }

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
            Intent intent = new Intent(this, EditAddPropertyActivity.class);
            intent.putExtra("isAdd", true);
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
            // Do nothing
        } else if (id == R.id.nav_messages) {
            Intent intent = new Intent(this, LandlordMessagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, LandlordTenantProfileActivity.class);
            intent.putExtra(TenantProfile.EXTRA_TENANT_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
            intent.putExtra(TenantProfile.EXTRA_LABEL, "Profile");
            startActivity(intent);
            return true;
        }  else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
