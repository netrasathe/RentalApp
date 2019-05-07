package com.corey.ole;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TenantProfileActivity extends NavDrawerActivity {
    protected TenantProfile tenant;

    protected void loadData(){
        TextView name = findViewById(R.id.tenant_name_field);
        TextView room = findViewById(R.id.room_number_field);
        TextView gender = findViewById(R.id.gender_field);
        TextView dob= findViewById(R.id.date_of_birth_field);
        TextView phone = findViewById(R.id.cell_phone_field);
        TextView email = findViewById(R.id.email_field);

        name.setText(tenant.getFirstName() + " " + tenant.getLastName());
        room.setText("Room " + tenant.getRoom());
        gender.setText(tenant.getGender());
        dob.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(tenant.getBirthdate()));
        phone.setText(String.valueOf(tenant.getPhone()).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
        email.setText(tenant.getEmail());

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Fetch the property name from the database since it might change!
        database.getReference("property").child(tenant.getPropertyId() + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                TextView property = findViewById(R.id.property_name_field);
                property.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /* Fetch the image from Firebase Storage and sets it to imageButton */
        try {
            final File localFile = File.createTempFile("images", "jpg");
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            if (tenant.getImagePath() != null && tenant.getImagePath().length() != 0) {
                StorageReference imageStorage = storageRef.child(tenant.getImagePath());
                imageStorage.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri uri = Uri.fromFile(localFile);
                                ImageView image = findViewById(R.id.profile_picture_field);
                                image.setImageURI(uri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTenantFromID(String tenantID){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("users/" + tenantID);
        userRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tenant = dataSnapshot.getValue(TenantProfile.class);
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
