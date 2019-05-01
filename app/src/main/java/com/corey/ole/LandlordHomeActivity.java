package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/* Activity for Landlord's Home UI */

public class LandlordHomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    PropertyAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<Property> mProperties = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landlord_home);
        toolbar = findViewById(R.id.landlord_home_toolbar);
        toolbar.setTitle("My Properties");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.landlord_home_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        mRecyclerView = findViewById(R.id.landlord_home_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addProperty();
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            // do something
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
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

    private void addProperty() {
        ArrayList<String> policies = new ArrayList<>();
        policies.add("Thou shall not kill!");
        policies.add("Thou shall not eat roommate's food!");
        policies.add("Enjoy!");
        ArrayList<String> notes = new ArrayList<>();
        notes.add("No dogs allowed! Only cats.");
        notes.add("No smoking cigarettes! Only weed");

        Property p1 = new Property("Benvenue Apartments", "234 Shattuck Avenue", "Berkley, CA 95054", R.drawable.apartment,
                100, new ArrayList<String>(), policies, notes, new ArrayList<String>(), new ArrayList<String>());
        mProperties.add(p1);
        Property p2 = new Property("Martinez Commons", "21 Bowditch Avenue", "Berkley, CA 94076", R.drawable.martinez_commons, 12,
                new ArrayList<String>(), policies, new ArrayList<String>(), notes, new ArrayList<String>());
        mProperties.add(p2);
        Property p3  = new Property("Telegraph Apartments", "945 Telegraph Avenue", "Berkley, CA 94704", R.drawable.telegraph, 68,
                new ArrayList<String>(), policies, notes, new ArrayList<String>(), new ArrayList<String>());
        mProperties.add(p3);

        Property p4  = new Property("Unit 1", "532 Peidmont Avenue", "Berkley, CA 94708", R.drawable.unit, 210,
                policies, policies, notes, new ArrayList<String>(), new ArrayList<String>());
        mProperties.add(p4);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference property = database.getReference("property");
        DatabaseReference d1 = property.child(p1.getId());
        d1.setValue(p1);
        DatabaseReference d2 = property.child(p2.getId());
        d2.setValue(p2);
        DatabaseReference d3 = property.child(p3.getId());
        d3.setValue(p3);
        DatabaseReference d4 = property.child(p4.getId());
        d4.setValue(p4);

        setAdapterAndUpdateData();
    }
}
