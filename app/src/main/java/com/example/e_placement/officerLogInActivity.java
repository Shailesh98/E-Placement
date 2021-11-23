package com.example.e_placement;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class officerLogInActivity extends AppCompatActivity {
    private Button LoginBtn;
    private Button RegisterBtn;
    private FirebaseAuth userProfile;
    private EditText Email, Password;
    private static final String TAG = "officerLogInActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officerlogin_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LoginBtn = (Button) findViewById(R.id.logIn_officer);
        RegisterBtn = (Button) findViewById(R.id.Register_officer);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);

        userProfile = FirebaseAuth.getInstance();


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();

                String EmailTxt = Email.getText().toString();
                String Pass = Password.getText().toString();

                if(TextUtils.isEmpty(EmailTxt)){
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    Email.setError("Please enter your email");
                    Email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(EmailTxt).matches()){
                    Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                    Email.setError("Please enter valid email");
                    Email.requestFocus();
                }else if(TextUtils.isEmpty(Pass)){
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    Password.setError("Please enter password");
                    Password.requestFocus();
                }else{
                    logIn(EmailTxt, Pass);
                }
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Register", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), officerRegistration.class);
                startActivity(i);
            }
        });


    }


    private void logIn(String emailTxt, String pass) {
        userProfile.signInWithEmailAndPassword(emailTxt, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "you are logged in.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), officerProfileActivity.class); //create activity
                    startActivity(i);
                }else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        Email.setError("User does not exists or no longer valid");
                        Email.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        Email.setError("Invalid credentials. Try again.");
                        Email.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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
}
