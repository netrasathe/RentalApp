package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TenantProfileActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    TenantProfile tenant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_profile);


        Toolbar toolbar = findViewById(R.id.property_details_toolbar);
        toolbar.setTitle("Property Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_property_detail);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        Intent tenantIntent = getIntent();
        Bundle intentExtras = tenantIntent.getExtras();

        int tenantID;

        if(intentExtras != null) {
            tenantID = (int) intentExtras.get(TenantProfile.EXTRA_TENANT_ID);
            tenant = TenantProfile.getTenantFromID(tenantID);
            loadData();
        } else {
            // Raise dialog exception
            tenant = null;
        }

        Button messageButton = findViewById(R.id.message_button);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMessageIntent = new Intent(v.getContext(), MessagesActivity.class);
                goToMessageIntent.putExtra("tenantID", tenant.getId());

                v.getContext().startActivity(goToMessageIntent);
            }
        });


    }

    public void loadData(){
        TextView name = findViewById(R.id.tenant_name_field);
        TextView property = findViewById(R.id.property_name_field);
        TextView room = findViewById(R.id.room_number_field);
        TextView gender = findViewById(R.id.gender_field);
        TextView dob= findViewById(R.id.date_of_birth_field);
        TextView phone = findViewById(R.id.cell_phone_field);
        TextView email = findViewById(R.id.email_field);

        name.setText(tenant.getName());
        property.setText(String.valueOf(tenant.getProperty()));
        room.setText(tenant.getRoom());
        gender.setText(tenant.getGender());
        dob.setText(tenant.getBirthdate().toString());
        phone.setText(String.valueOf(tenant.getPhone()));
        email.setText(tenant.getEmail());

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            // do something
            Intent intent = new Intent(this, EditTenantProfileActivity.class);
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


}
