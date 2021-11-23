package com.example.e_placement;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class officerRegistration extends AppCompatActivity {

    private String TAG = "officerRegistration";

    private EditText email;

    private EditText password;


    private Button Register;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_registration);

        email = (EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);


        Register = (Button) findViewById(R.id.Register);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailtxt = email.getText().toString();


                String pass = password.getText().toString();



                if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches()){
                    Toast.makeText(getApplicationContext(), "Please enter valid eemail", Toast.LENGTH_SHORT).show();
                    email.setError("Field can't be empty.");
                    email.requestFocus();
                } else if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    password.setError("Field can't be empty.");
                    password.requestFocus();
                } else if(pass.length() < 6 ){
                    Toast.makeText(getApplicationContext(), "password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    password.setError("Password too weak");
                    password.requestFocus();
                }  else {

                    registerUser(emailtxt, pass);


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
    private void registerUser(String emailtxt, String pass){
        FirebaseAuth auth = FirebaseAuth.getInstance();


        auth.createUserWithEmailAndPassword(emailtxt,pass).addOnCompleteListener(officerRegistration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(emailtxt);
                    DatabaseReference refProfile = FirebaseDatabase.getInstance().getReference("officer");



                    refProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(officerRegistration.this, "User registered successfully", Toast.LENGTH_LONG).show();

                            }else{

                                Toast.makeText(officerRegistration.this, "User registered failed. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else{
                    try{
                        throw Objects.requireNonNull(task.getException());

                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(officerRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
