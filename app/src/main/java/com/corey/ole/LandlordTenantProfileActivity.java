package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LandlordTenantProfileActivity extends TenantProfileActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_tenant_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tenant Profile");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setDrawerData(navigationView);


        Intent tenantIntent = getIntent();
        Bundle intentExtras = tenantIntent.getExtras();

        String tenantID;

        if(intentExtras != null) {
            tenantID = intentExtras.getString(TenantProfile.EXTRA_TENANT_ID);
            getTenantFromID("DKYk5BGJZaWlkB9MpyMDr15O9VF2");
        } else {
            // Raise dialog exception
            tenant = null;
        }

        Button messageButton = findViewById(R.id.message_button);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMessageIntent = new Intent(v.getContext(), TenantMessagesActivity.class);
                goToMessageIntent.putExtra("tenantID", tenant.getId());

                v.getContext().startActivity(goToMessageIntent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            // do something
            Intent intent = new Intent(this, LandlordEditTenantProfileActivity.class);
            intent.putExtra(TenantProfile.EXTRA_TENANT_ID, tenant.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, LandlordHomeActivity.class);
            intent.putExtra("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(intent);
        } else if (id == R.id.nav_messages) {
            Intent intent = new Intent(this, LandlordMessagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
