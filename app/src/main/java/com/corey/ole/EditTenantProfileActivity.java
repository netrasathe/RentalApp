package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditTenantProfileActivity extends AppCompatActivity {

    TenantProfile tenant;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tenant_profile);

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
        EditText name = findViewById(R.id.tenant_name_edit_field);
        EditText property = findViewById(R.id.property_name_edit_field);
        EditText room = findViewById(R.id.room_number_edit_field);
        EditText gender = findViewById(R.id.gender_edit_field);
        EditText dob= findViewById(R.id.date_of_birth_edit_field);
        EditText phone = findViewById(R.id.cell_phone_edit_field);
        EditText email = findViewById(R.id.email_edit_field);

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
        if (id == R.id.done) {
            // do something

            updateTenantData();

            Intent intent = new Intent(this, TenantProfileActivity.class);
            intent.putExtra(TenantProfile.EXTRA_TENANT_ID, tenant.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    private void updateTenantData() {
        // TODO: Update tenant data on firebase
    }

}
