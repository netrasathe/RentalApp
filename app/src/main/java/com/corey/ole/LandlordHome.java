package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/* Activity for Landlord's Home UI */

public class LandlordHome extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    PropertyAdapter mAdapter;
    RecyclerView mRecyclerView;
    ArrayList<PropertyView> mProperties = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landlord_home);
        toolbar = findViewById(R.id.toolbar_landlord_home);
        toolbar.setTitle("My Properties");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_landlord_home);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        mRecyclerView = findViewById(R.id.rv_landlord_home);

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
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    private void setAdapterAndUpdateData() {
        mAdapter = new PropertyAdapter(this, mProperties);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PropertyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PropertyView p = mProperties.get(position);
                startNewActivity(p);
            }

        });
    }

    public void startNewActivity(PropertyView p) {

        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra("name", p.getTitle());
        intent.putExtra("address", p.getAddress());
        intent.putExtra("image", p.getImage());
        intent.putExtra("PropertyID", p.getId());
        startActivity(intent);

    }

    private void addProperty() {
        PropertyView p1 = new PropertyView("Benvenue Apartments", "234 Shattuck Avenue, Berkley, CA 95054", 100, R.drawable.apartment);
        mProperties.add(p1);
        PropertyView p2 = new PropertyView("Martinez Commons", "21 Bowditch Avenue, Berkley, CA 94076", 12, R.drawable.martinez_commons);
        mProperties.add(p2);
        PropertyView p3  = new PropertyView("Telegraph Apartments", "945 Telegraph Avenue, Berkley, CA 94704", 68, R.drawable.telegraph);
        mProperties.add(p3);
        PropertyView p4  = new PropertyView("Unit 1", "532 Peidmont Avenue, Berkley, CA 94708", 210, R.drawable.unit);
        mProperties.add(p4);


        setAdapterAndUpdateData();
    }
}
