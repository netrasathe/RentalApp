package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PropertyDetailsActivity extends AppCompatActivity {
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView image;
    private TextView name;
    private TextView address;
    private String intentName;
    private String intentAddr;
    private int intentImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.property_details_main);
        Toolbar toolbar = findViewById(R.id.property_details_toolbar);
        toolbar.setTitle("Property Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_property_detail);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        image = findViewById(R.id.property_detail_image);
        name = findViewById(R.id.building_name);
        address = findViewById(R.id.building_address);
        setUpIntent();


        RecyclerView policiesRecycler = findViewById(R.id.policies_rv);
        RecyclerView notesRecylcer = findViewById(R.id.notes_rv);
        Button viewTenantButton = findViewById(R.id.view_tenants_button);

        viewTenantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTenantList();

            }
        });

    }
    private void setUpIntent()
    {
        Intent intent = getIntent();
        intentName = intent.getStringExtra("name");
        if (name != null)
            name.setText(intentName);
        intentAddr = intent.getStringExtra("address");
        if (intentAddr != null)
            address.setText(intentAddr);
        intentImage = intent.getIntExtra("image", 0);
        if (intentImage != 0)
            image.setImageResource(intentImage);
    }

    private void startTenantList(){
        Intent intent = new Intent(this, TenantListActivity.class);
        startActivity(intent);
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
            Intent intent = new Intent(this, EditPropertyActivity.class);
            intent.putExtra("name", intentName);
            intent.putExtra("address", intentAddr);
            intent.putExtra("image", intentImage);
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
