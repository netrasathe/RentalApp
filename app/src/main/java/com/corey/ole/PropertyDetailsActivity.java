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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PropertyDetailsActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView image;
    private TextView name;
    private TextView address;
    private FirebaseDatabase database;
    private String id;
    private RecyclerView policiesRecycler;
    private RecyclerView notesRecylcer;
    private Button viewTenantButton;



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

        image = findViewById(R.id.property_details_image);
        name = findViewById(R.id.property_details_name);
        address = findViewById(R.id.property_details_address);

        policiesRecycler = findViewById(R.id.property_details_policies_recycler_view);
        notesRecylcer = findViewById(R.id.property_details_notes_recycler_view);
        Button viewTenantButton = findViewById(R.id.property_details_view_tenants_button);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("property");
        DatabaseReference property = ref.child(id);

        property.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Property property = dataSnapshot.getValue(Property.class);
                name.setText(property.getName());
                address.setText(property.getStreet() + property.getCityStateZip());
                image.setImageResource(property.getImage());

                ArrayList<String> notes = property.getNotes();

                AnnouncementAdapter policyAdapter = new AnnouncementAdapter(notes);
                policiesRecycler.setAdapter(policyAdapter);

//                AnnouncementAdapter noteAdapter = new AnnouncementAdapter(property.getNotes());
//                notesRecylcer.setAdapter(noteAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        viewTenantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTenantList();

            }
        });

    }


    private void startTenantList(){
        Intent intent = new Intent(this, TenantListActivity.class);
        //intent.putExtra("name", intentName);
        intent.putExtra("PropertyID", id);

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
            Intent intent = new Intent(this, EditAddPropertyActivity.class);
            intent.putExtra("id", id);
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
