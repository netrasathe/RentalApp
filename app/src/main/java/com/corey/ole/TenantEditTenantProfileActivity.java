package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class TenantEditTenantProfileActivity extends EditTenantProfileActivity {

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tenant_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent tenantIntent = getIntent();
        Bundle intentExtras = tenantIntent.getExtras();

        if(intentExtras != null) {
            uid = intentExtras.getString(TenantProfile.EXTRA_TENANT_ID);
            getTenantFromID(uid);
        } else {
            // TODO: Raise dialog exception
            tenant = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            updateTenantData(uid);

            Intent intent = new Intent(this, TenantTenantProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
