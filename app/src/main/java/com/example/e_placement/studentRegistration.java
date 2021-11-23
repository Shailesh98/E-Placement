package com.example.e_placement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class studentRegistration extends AppCompatActivity {

    private String TAG = "studentRegistration";
    private EditText firstName;
    private EditText middleName;
    private EditText lastName;
    private EditText email;
    private EditText contact;
    private EditText faculty;
    private EditText semester;
    private EditText password;
    private EditText re_password;

    private Button Register;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        firstName = (EditText) findViewById(R.id.f_name);
        middleName = (EditText) findViewById(R.id.m_name);
        lastName = (EditText) findViewById(R.id.l_name);

        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.contact_no);
        faculty = (EditText) findViewById(R.id.faculty);
        semester = (EditText) findViewById(R.id.semester);
        password = (EditText) findViewById(R.id.password);
        re_password = (EditText) findViewById(R.id.Repassword);

        Register = (Button) findViewById(R.id.Register);




        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_Name = firstName.getText().toString();
                String middle_Name = middleName.getText().toString();
                String last_Name = lastName.getText().toString();
                String emailtxt = email.getText().toString();
                String contact_no = contact.getText().toString();
                String facultytxt = faculty.getText().toString();
                String sem = semester.getText().toString();
                String pass = password.getText().toString();
                String re_pass = re_password.getText().toString();


                if(TextUtils.isEmpty(first_Name)){
                    Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
                    firstName.setError("Field can't be empty.");
                    firstName.requestFocus();
                } else if(TextUtils.isEmpty(middle_Name)){
                    Toast.makeText(getApplicationContext(), "Please enter middle name", Toast.LENGTH_SHORT).show();
                    middleName.setError("Field can't be empty.");
                    middleName.requestFocus();
                } else if(TextUtils.isEmpty(last_Name)){
                    Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_SHORT).show();
                    lastName.setError("Field can't be empty.");
                    lastName.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches()){
                    Toast.makeText(getApplicationContext(), "Please enter valid eemail", Toast.LENGTH_SHORT).show();
                    email.setError("Field can't be empty.");
                    email.requestFocus();
                } else if(TextUtils.isEmpty(contact_no)){
                    Toast.makeText(getApplicationContext(), "Please enter your mobile no", Toast.LENGTH_SHORT).show();
                    contact.setError("Field can't be empty.");
                    contact.requestFocus();
                } else if(contact_no.length() != 10){
                    Toast.makeText(getApplicationContext(), "Please enter your mobile no", Toast.LENGTH_SHORT).show();
                    contact.setError("Field can't be empty.");
                    contact.requestFocus();
                } else if(TextUtils.isEmpty(facultytxt)){
                    Toast.makeText(getApplicationContext(), "Please enter faculty name", Toast.LENGTH_SHORT).show();
                    faculty.setError("Field can't be empty.");
                    faculty.requestFocus();
                } else if(TextUtils.isEmpty(sem)){
                    Toast.makeText(getApplicationContext(), "Please enter your semister", Toast.LENGTH_SHORT).show();
                    semester.setError("Field can't be empty.");
                    semester.requestFocus();
                } else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    password.setError("Field can't be empty.");
                    password.requestFocus();
                } else if(pass.length() < 6 ){
                    Toast.makeText(getApplicationContext(), "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    password.setError("Password too weak");
                    password.requestFocus();
                } else if(TextUtils.isEmpty(re_pass)){
                    Toast.makeText(getApplicationContext(), "Please enter your password again", Toast.LENGTH_SHORT).show();
                    re_password.setError("Field can't be empty.");
                    re_password.requestFocus();
                } else if(!pass.equals(re_pass)){
                    Toast.makeText(getApplicationContext(), "Password and re_password does no match", Toast.LENGTH_SHORT).show();
                    re_password.setError("password confirmation is required");
                    re_password.requestFocus();

                    password.clearComposingText();
                    re_password.clearComposingText();
                } else {

                    registerUser(first_Name, middle_Name, last_Name, emailtxt, contact_no, facultytxt, sem, pass);


                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //register user
    private void registerUser(String first_Name, String middle_Name, String last_Name, String emailtxt, String contact_no, String facultytxt, String sem, String pass){
        FirebaseAuth auth = FirebaseAuth.getInstance();


        auth.createUserWithEmailAndPassword(emailtxt,pass).addOnCompleteListener(studentRegistration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(first_Name, middle_Name, last_Name, emailtxt, contact_no, facultytxt, sem);
                        DatabaseReference refProfile = FirebaseDatabase.getInstance().getReference("Students");



                        refProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(studentRegistration.this, "User registered successfully", Toast.LENGTH_LONG).show();

                                }else{

                                    Toast.makeText(studentRegistration.this, "User registered failed. Try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                else{
                    try{
                        throw Objects.requireNonNull(task.getException());

                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(studentRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


}
