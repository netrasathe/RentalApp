package com.corey.ole;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditAddPropertyActivity extends AppCompatActivity {

    private TextView nameView;
    private TextView streetView;
    private TextView cityStateZipView;
    private TextView addPolicyText;
    private TextView addNoteText;
    private Button addPolicyButton;
    private Button addNoteButton;
    private ImageButton imageButton;
    private RecyclerView policiesRecycler;
    private RecyclerView notesRecycler;
    private Button cameraButton;
    private Button galleryButton;
    private AlertDialog imageButtonDialog;
    private Property thisProperty;
    private String propertyId;
    private ArrayList<String> policies;
    private ArrayList<String> notes;
    private boolean isAddProperty;
    private FirebaseDatabase database;
    private DatabaseReference propertyRef;


    private static final int REQUEST_TAKE_PHOTO = 2;
    private String currentPhotoPath;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_property);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        isAddProperty = intent.getBooleanExtra("isAdd", false);
        propertyId = intent.getStringExtra("id");
        if (isAddProperty) {
            toolbar.setTitle("Add Property");
            thisProperty = new Property();
        } else {
            toolbar.setTitle("Edit Property");
        }
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        policies = new ArrayList<>();
        notes = new ArrayList<>();

        imageButton = findViewById(R.id.edit_property_image);
        nameView = findViewById(R.id.edit_property_name);
        streetView = findViewById(R.id.edit_property_street);
        cityStateZipView = findViewById(R.id.edit_property_csz);
        addPolicyButton = findViewById(R.id.edit_property_add_policy);
        addNoteButton = findViewById(R.id.edit_property_add_note);
        addNoteText = findViewById(R.id.edit_property_note);
        addPolicyText = findViewById(R.id.edit_property_policy);
        policiesRecycler = findViewById(R.id.edit_property_details_policies_recycler_view);
        notesRecycler = findViewById(R.id.edit_property_details_notes_recycler_view);
        policiesRecycler.setHasFixedSize(true);
        policiesRecycler.setLayoutManager(new LinearLayoutManager(this));
        notesRecycler.setHasFixedSize(true);
        notesRecycler.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        propertyRef = database.getReference("property");

        if (!isAddProperty) {
            DatabaseReference property = propertyRef.child(propertyId);
            property.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    thisProperty = dataSnapshot.getValue(Property.class);
                    nameView.setText(thisProperty.getName());
                    streetView.setText(thisProperty.getStreet());
                    cityStateZipView.setText(thisProperty.getCityStateZip());
                    imageButton.setImageResource(thisProperty.getImage());

                    notes = thisProperty.getNotes();
                    policies = thisProperty.getPolicies();
                    setNoteAdapter();
                    setPolicyAdapter();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }



        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = addNoteText.getText().toString();
                if (note.length() != 0)
                    notes.add(note);
                setNoteAdapter();
                addNoteText.setText("");
            }

        });

        addPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String policy = addPolicyText.getText().toString();
                if (policy.length() != 0)
                    policies.add(policy);
                setPolicyAdapter();
                addPolicyText.setText("");
            }

        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();

            }

        });

        setNoteAdapter();
        setPolicyAdapter();

    }

    private void setPolicyAdapter() {
        AddPolicyNoteAdapter policyAdapter = new AddPolicyNoteAdapter(policies);
        policiesRecycler.setAdapter(policyAdapter);
        policyAdapter.setOnItemClickListener(new AddPolicyNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                policies.remove(position);
                setPolicyAdapter();

            }
        });
    }

    private void setNoteAdapter() {
        AddPolicyNoteAdapter noteAdapter = new AddPolicyNoteAdapter(notes);
        notesRecycler.setAdapter(noteAdapter);
        noteAdapter.setOnItemClickListener(new AddPolicyNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                notes.remove(position);
                setNoteAdapter();
            }

        });

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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.corey.ole",
                        photoFile);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFileName, ".jpg", storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
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
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageButton.setImageBitmap(imageBitmap);
                imageButtonDialog.cancel();
            } else if (requestCode == PICK_IMAGE) {
                Uri imageUri = data.getData();
                imageButton.setImageURI(imageUri);
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

    private void saveProperty() {
        thisProperty.setName(nameView.getText().toString());
        thisProperty.setStreet(streetView.getText().toString());
        thisProperty.setCityStateZip(cityStateZipView.getText().toString());
        thisProperty.setPolicies(policies);
        thisProperty.setNotes(notes);
        DatabaseReference property = propertyRef.child(thisProperty.getId());
        property.setValue(thisProperty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            saveProperty();
            Intent intent = new Intent(this, PropertyDetailsActivity.class);
            intent.putExtra("id", thisProperty.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
