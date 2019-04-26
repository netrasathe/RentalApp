package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TenantProfileActivity extends AppCompatActivity {

    TenantProfile tenant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_profile);

        Intent tenantIntent = getIntent();
        Bundle intentExtras = tenantIntent.getExtras();

        int tenantID;

        if(intentExtras != null) {
            tenantID = (int) intentExtras.get(TenantViewHolder.EXTRA_TENANT_ID);
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
        property.setText(tenant.getProperty());
        room.setText(tenant.getRoom());
        gender.setText(tenant.getGender());
        dob.setText(tenant.getBirthdate().toString());
        phone.setText(tenant.getPhone());
        email.setText(tenant.getEmail());

    }


}
