package com.corey.ole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class LandlordEditTenantProfileActivity extends EditTenantProfileActivity {

    private String uid;
    private String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tenant_profile);

        Intent tenantIntent = getIntent();
        Bundle intentExtras = tenantIntent.getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

            Intent intent = new Intent(this, LandlordTenantProfileActivity.class);
            intent.putExtra(TenantProfile.EXTRA_TENANT_ID, tenant.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
