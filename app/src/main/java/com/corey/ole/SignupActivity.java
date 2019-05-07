package com.corey.ole;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {


    private String firstName;
    private String lastName;
    private String gender;
    private Date birthday;

    private String email;
    private String password;
    private String phone;
    private int accountType;
    private String propertyCode;
    private String roomCode = "100A";

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbUsersRef;

    private static final String TAG = "SignupEmailPassword";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_signup_personal_info);

        mAuth = FirebaseAuth.getInstance();

        dbUsersRef = database.getReference("users");

    }

    public void onNextClick(View view) {
        boolean savedDetailsSuccess = savePersonalDetails();
        if (savedDetailsSuccess) {
            setContentView(R.layout.app_bar_signup_account_info);
        } else {
            // TODO: raise dialog
        }

    }

    private boolean savePersonalDetails() {

        EditText firstNameField = findViewById(R.id.firstNameField);
        EditText lastNameField = findViewById(R.id.lastNameField);
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        EditText birthMonthField = findViewById(R.id.birthMonthField);
        EditText birthDayField = findViewById(R.id.birthDayField);
        EditText birthYearField = findViewById(R.id.birthYearField);

        firstName = firstNameField.getText().toString();
        lastName = lastNameField.getText().toString();
        gender = genderSpinner.getSelectedItem().toString();

        String birthdayString = birthMonthField.getText().toString() + "/" +
                birthDayField.getText().toString() + "/" +
                birthYearField.getText().toString();

        try {
            birthday = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(birthdayString);
        } catch (ParseException e) {
            e.printStackTrace();
            // TODO: improper date formatting
            return false;
        }

        return true;
    }


    public void onFinishClick(View view) {
        boolean saveAccountDetailsSuccess = saveAccountDetails();

        if (saveAccountDetailsSuccess) {
            createAccount();
        } else {

        }
    }


    private boolean saveAccountDetails() {

        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        EditText phoneField = findViewById(R.id.phoneField);
        RadioGroup accountTypeField = findViewById(R.id.accountTypeField);
        EditText propertyCodeField = findViewById(R.id.propertyCodeField);

        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        phone = phoneField.getText().toString();

        if (accountTypeField.getCheckedRadioButtonId() == R.id.tenantRadioButton) {
            accountType = 1;
            propertyCode = propertyCodeField.getText().toString();
        } else if (accountTypeField.getCheckedRadioButtonId() == R.id.landlordRadioButton) {
            accountType = 2;
            propertyCode = null;
        }

        return true;
    }

    private void createAccount() {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            addUser(user, accountType);

                            if (accountType == 1) {
                                tenantLogin(user.getUid());
                            } else if (accountType == 2) {
                                landlordLogin(user.getUid());
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // TODO: launch signup error
                        }

                        // ...
                    }
                });
    }

    private void addUser(FirebaseUser user, int accountType) {
        if (accountType == 1) {
            TenantProfile tenant = new TenantProfile(user.getUid(), firstName, lastName,
                    gender, birthday, phone, email, null, propertyCode, roomCode, new ArrayList<Repair>());
            dbUsersRef.child(user.getUid()).setValue(tenant);


        } else if (accountType == 2) {
            LandlordProfile landlord = new LandlordProfile(firstName, lastName, email, gender, birthday, phone, null, new ArrayList<String>());
            dbUsersRef.child(user.getUid()).setValue(landlord);
        }
    }

    public void tenantLogin(String uid) {
        Intent intent = new Intent(this, TenantHomeActivity.class);
        intent.putExtra("tenantId", uid);
        startActivity(intent);
    }

    public void landlordLogin(String uid) {
        Intent intent = new Intent(this, LandlordHomeActivity.class);
        intent.putExtra("landlordId", uid);
        startActivity(intent);
    }


}
