package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EditTenantProfileActivity extends AppCompatActivity {



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
}
