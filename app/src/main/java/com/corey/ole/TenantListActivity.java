package com.corey.ole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

public class TenantListActivity extends AppCompatActivity {

    Toolbar toolbar;
    String propertyName = "Property";
    RecyclerView mRecyclerView;
    TenantAdapter mAdapter;
    int propertyID;

    ArrayList<TenantProfile> mTenants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_tenant_list);


        //mToolbar = findViewById(R.id.toolbar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(propertyName + " Tenants");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        makeTestTenants();
        setAdapterAndUpdateData();

    }

    private void makeTestTenants() {
        TenantProfile tenant = new TenantProfile("John Smith", "Male",
                new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000)),
                12345678,
                "email@email.com",
                null,
                propertyID,
                "212");

        mTenants.add(tenant);
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new TenantAdapter(this, mTenants);
        mRecyclerView.setAdapter(mAdapter);

    }

}
