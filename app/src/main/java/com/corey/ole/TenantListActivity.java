package com.corey.ole;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class TenantListActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    String propertyName = "Property";
    RecyclerView mRecyclerView;
    TenantListAdapter mAdapter;
    int propertyID;

    ArrayList<TenantProfile> mTenants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_property_tenant_list);

        Toolbar toolbar = findViewById(R.id.property_details_toolbar);
        toolbar.setTitle("Property Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_property_detail);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        //mToolbar = findViewById(R.id.toolbar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(propertyName + " Tenants");

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

}
