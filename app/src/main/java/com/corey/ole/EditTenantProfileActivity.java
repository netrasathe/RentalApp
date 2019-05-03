package com.corey.ole;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditTenantProfileActivity extends NavDrawerActivity {
    protected TenantProfile tenant;

    public void loadData(){
        EditText name = findViewById(R.id.tenant_name_edit_field);
        EditText property = findViewById(R.id.property_name_edit_field);
        EditText room = findViewById(R.id.room_number_edit_field);
        EditText gender = findViewById(R.id.gender_edit_field);
        EditText dob= findViewById(R.id.date_of_birth_edit_field);
        EditText phone = findViewById(R.id.cell_phone_edit_field);
        TextView email = findViewById(R.id.email_edit_field);

        name.setText(tenant.getName());
        property.setText(String.valueOf(tenant.getPropertyID()));
        room.setText(tenant.getRoom());
        gender.setText(tenant.getGender());
        dob.setText(new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(tenant.getBirthdate()));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    protected void updateTenantData(String tenantID) {
        EditText name = findViewById(R.id.tenant_name_edit_field);
        //EditText property = findViewById(R.id.property_name_edit_field);
        //EditText room = findViewById(R.id.room_number_edit_field);
        EditText gender = findViewById(R.id.gender_edit_field);
        EditText dob= findViewById(R.id.date_of_birth_edit_field);
        EditText phone = findViewById(R.id.cell_phone_edit_field);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("users/" + tenantID);

        String[] n = name.getText().toString().split(" ");
        if (n.length == 0) {
            // Do nothing
        } else if (n.length == 1) {
            userRef.child("First Name").setValue(n[0]);
            userRef.child("Last Name").setValue("");
        } else {
            userRef.child("First Name").setValue(n[0]);
            userRef.child("Last Name").setValue(n[1]);
        }

        if (!TextUtils.isEmpty(gender.getText().toString())) {
            userRef.child("Gender").setValue(gender.getText().toString());
        }

        if (!TextUtils.isEmpty(dob.getText().toString())) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
            try {
                Date date = format.parse(dob.getText().toString());
                userRef.child("Birthday").setValue(date);
            } catch (ParseException e) {
                // Do nothing
            }
        }

        String num = phone.getText().toString();
        num = num.replaceAll("[^\\d]", "" );
        if (!TextUtils.isEmpty(num)) {
            userRef.child("Phone").setValue(Integer.parseInt(num));
        }
    }
}
