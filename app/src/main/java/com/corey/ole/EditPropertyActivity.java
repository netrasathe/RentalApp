package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class EditPropertyActivity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageButton image;
    TextView name;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_property_details);
        Toolbar toolbar = findViewById(R.id.edit_property_toolbar);
        toolbar.setTitle("Edit Property");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_property_detail);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                R.string.drawer_open, R.string.drawer_close);
//        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        image = findViewById(R.id.edit_property_image);
        name = findViewById(R.id.edit_property_name);
        address = findViewById(R.id.edit_property_address);
        setUpIntent();

        RecyclerView policiesRecycler = findViewById(R.id.policies_rv);
        RecyclerView notesRecylcer = findViewById(R.id.notes_rv);
        setUpIntent();

    }
    private void setUpIntent()
    {
        Intent intent = getIntent();
        String n = intent.getStringExtra("name");
        if (name != null)
            name.setText(n);
        String a = intent.getStringExtra("address");
        if (a != null)
            address.setText(a);
        int i = intent.getIntExtra("image", 0);
        if (i != 0)
            image.setImageResource(i);
    }

}
