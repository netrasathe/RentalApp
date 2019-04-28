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

public class TenantListActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    String propertyName = "Property";
    RecyclerView mRecyclerView;
    TenantListAdapter mAdapter;
    int propertyID;

    ArrayList<TenantProfile> mTenants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_tenant_list);

        Intent intent = getIntent();
        propertyName = intent.getStringExtra("name");
        propertyID = intent.getIntExtra("PropertyID", 0);



        Toolbar toolbar = findViewById(R.id.tenant_list_toolbar);
        toolbar.setTitle(propertyName + " Tenants");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.tenant_list_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);




        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTenants.add(TenantProfile.makeTestTenants(propertyID));
        setAdapterAndUpdateData();

    }



    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new TenantListAdapter(this, mTenants);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


}
