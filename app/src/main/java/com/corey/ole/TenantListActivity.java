package com.corey.ole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class TenantListActivity extends AppCompatActivity {

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
