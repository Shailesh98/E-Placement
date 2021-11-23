package com.example.e_placement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private String firstName, middleName, lastName, email, contact, faculty, sem;
    private EditText firstNameTxt, middleNameTxt, lastNameTxt, emailTxt, contactTxt, facultyTxt, semTxt;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


                firstNameTxt = findViewById(R.id.ed_fname);
                middleNameTxt = findViewById(R.id.ed_mname);
                lastNameTxt = findViewById(R.id.ed_lname);
                emailTxt  = findViewById(R.id.ed_email);
                contactTxt = findViewById(R.id.ed_contact);
                facultyTxt  = findViewById(R.id.ed_faculty);
                semTxt = findViewById(R.id.ed_sem);
                updateBtn = findViewById(R.id.ed_updateBtn);

                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();

                showProfileData(firebaseUser);
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateProfile(firebaseUser);
                    }
                });
    }

    private void updateProfile(FirebaseUser firebaseUser) {


        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
            firstNameTxt.setError("Field can't be empty.");
            firstNameTxt.requestFocus();
        } else if(TextUtils.isEmpty(middleName)){
            Toast.makeText(getApplicationContext(), "Please enter middle name", Toast.LENGTH_SHORT).show();
            middleNameTxt.setError("Field can't be empty.");
            middleNameTxt.requestFocus();
        } else if(TextUtils.isEmpty(lastName)){
            Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_SHORT).show();
            lastNameTxt.setError("Field can't be empty.");
            lastNameTxt.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), "Please enter valid eemail", Toast.LENGTH_SHORT).show();
            emailTxt.setError("Field can't be empty.");
            emailTxt.requestFocus();
        } else if(TextUtils.isEmpty(contact)){
            Toast.makeText(getApplicationContext(), "Please enter your mobile no", Toast.LENGTH_SHORT).show();
            contactTxt.setError("Field can't be empty.");
            contactTxt.requestFocus();
        } else if(contact.length() != 10){
            Toast.makeText(getApplicationContext(), "Please enter your mobile no", Toast.LENGTH_SHORT).show();
            contactTxt.setError("Field can't be empty.");
            contactTxt.requestFocus();
        } else if(TextUtils.isEmpty(faculty)){
            Toast.makeText(getApplicationContext(), "Please enter faculty name", Toast.LENGTH_SHORT).show();
            facultyTxt.setError("Field can't be empty.");
            facultyTxt.requestFocus();
        } else if(TextUtils.isEmpty(sem)){
            Toast.makeText(getApplicationContext(), "Please enter your semester", Toast.LENGTH_SHORT).show();
            semTxt.setError("Field can't be empty.");
            semTxt.requestFocus();
        } else {

            firstName = firstNameTxt.getText().toString();
            middleName = middleNameTxt.getText().toString();
            lastName = lastNameTxt.getText().toString();
            email = emailTxt.getText().toString();
            contact = contactTxt.getText().toString();
            faculty = facultyTxt.getText().toString();
            sem = semTxt.getText().toString();

            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(firstName, middleName, lastName, email, contact, faculty, sem);

            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Students");

            String userID = firebaseUser.getUid();

            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + " " +lastName).build();
                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(getApplicationContext(), "Update Successful.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UpdateProfileActivity.this, studentProfile.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }else{

                        try{
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

        }

    }

    private void showProfileData(FirebaseUser firebaseUser) {
        String userIDofRegistered = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Students");

        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    firstName = readUserDetails.first_Name;
                    middleName = readUserDetails.middle_Name;
                    lastName = readUserDetails.last_Name;
                    email = readUserDetails.emailtxt;
                    contact = readUserDetails.contact_no;
                    faculty = readUserDetails.facultytxt;
                    sem = readUserDetails.sem;

                    firstNameTxt.setText(firstName);
                    middleNameTxt.setText(middleName);
                    lastNameTxt.setText(lastName);
                    emailTxt.setText(email);
                    contactTxt.setText(contact);
                    facultyTxt.setText(faculty);
                    semTxt.setText(sem);

                }else{
                    Toast.makeText(getApplicationContext(), "Something is wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}