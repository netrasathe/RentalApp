package com.corey.ole;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class NavDrawerActivity extends AppCompatActivity {
    protected TextView mUsername;
    protected String mUid;
    protected ImageView imageView;

    protected void setDrawerData(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        mUsername = header.findViewById(R.id.txt_name);
        TextView txt_email = header.findViewById(R.id.txt_email);
        imageView = header.findViewById(R.id.imageView_nav_header);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        txt_email.setText(email);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        mUid = auth.getCurrentUser().getUid();
        DatabaseReference user = usersRef.child(mUid);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot data) {
                String firstName = data.child("firstName").getValue(String.class);
                String lastName = data.child("lastName").getValue(String.class);
                mUsername.setText(firstName + " " + lastName);

                String imagePath = data.child("imagePath").getValue(String.class);
                if (imagePath != null && !imagePath.isEmpty())
                {
                    /* Fetch the image from Firebase Storage and sets it to imageButton */
                    try {
                        final File localFile = File.createTempFile("images", "jpg");
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference imageStorage = storageRef.child(imagePath);
                        imageStorage.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Uri uri = Uri.fromFile(localFile);
                                        imageView.setImageURI(uri);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("App Bar", String.valueOf(databaseError));
            }
        });

    }
}