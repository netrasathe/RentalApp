package com.corey.ole;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TenantProfileActivity extends NavDrawerActivity {
    protected TenantProfile tenant;

    protected void loadData(){
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
        dob.setText(new SimpleDateFormat("MM/dd/yy").format(tenant.getBirthdate()));
        phone.setText(String.valueOf(tenant.getPhone()).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
        email.setText(tenant.getEmail());

    }

    public void getTenantFromID(String tenantID){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("users/" + tenantID);
        userRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();
                String name = dataSnapshot.child("First Name").getValue(String.class) + " " +
                        dataSnapshot.child("Last Name").getValue(String.class);
                String gender = dataSnapshot.child("Gender").getValue(String.class);
                Date birthday = dataSnapshot.child("Birthday").getValue(Date.class);
                int phone = dataSnapshot.child("Phone").getValue(Integer.class);
                String email = dataSnapshot.child("Email").getValue(String.class);

                tenant = new TenantProfile(id, name, gender, birthday, phone, email, null,
                        0, "212");
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
