package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PropertyDetailsActivity extends AppCompatActivity {
    ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView image;
    private TextView name;
    private TextView address;

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

    }
    private void setUpIntent()
    {
        Intent intent = getIntent();
        String n = intent.getStringExtra("name");
        if (name != null)
            name.setText("Benvenue Apartments");
        String a = intent.getStringExtra("address");
        if (a != null)
            address.setText("540 Shattuck Avenue, Berkeley, CA");
        int i = intent.getIntExtra("image", 0);
        if (i != 0)
            image.setImageResource(i);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
