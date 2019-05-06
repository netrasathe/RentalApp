package com.corey.ole;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TenantEditTenantProfileActivity extends EditTenantProfileActivity {

    private String uid;
    private Button cameraButton;
    private Button galleryButton;
    private AlertDialog imageButtonDialog;
    private FirebaseDatabase database;
    private DatabaseReference propertyRef;
    private Uri photoUri;
    private ImageButton imageButton;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tenant_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent tenantIntent = getIntent();
        Bundle intentExtras = tenantIntent.getExtras();
//
//        if(intentExtras != null) {
//            uid = intentExtras.getString(TenantProfile.EXTRA_TENANT_ID);
//            getTenantFromID(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        } else {
//            // TODO: Raise dialog exception
//            tenant = null;
//        }

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getTenantFromID(uid);

        imageButton = findViewById(R.id.profile_picture_edit_field);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            saveImageToFirebaseStorage();
            updateTenantData(uid);
            Intent intent = new Intent(this, TenantTenantProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.corey.ole",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFileName, ".jpg", storageDir
        );
        return image;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                imageButton.setImageURI(photoUri);
                imageButtonDialog.cancel();
            } else if (requestCode == PICK_IMAGE) {
                photoUri = data.getData();
                imageButton.setImageURI(photoUri);
                imageButtonDialog.cancel();
            }
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_photo, null);
        cameraButton = view.findViewById(R.id.add_photo_camera);
        galleryButton = view.findViewById(R.id.add_button_gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }

        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }

        });

        builder.setTitle("Add a photo");
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
            }
        });

        imageButtonDialog = builder.create();
        imageButtonDialog.getWindow().setLayout(300, 150);
        imageButtonDialog.show();
    }

    private void saveImageToFirebaseStorage() {
        if (photoUri == null) {
            tenant.setImagePath("");
            return;
        }
        String path = uid + "/images/profile.jpg";
        tenant.setImagePath(path);

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(path);
        imageRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }

}
