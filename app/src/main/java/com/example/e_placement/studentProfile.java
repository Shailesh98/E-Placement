package com.example.e_placement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class studentProfile extends AppCompatActivity {



    private TextView fullName;
    private TextView firstName;
    private TextView middleName;
    private TextView lastName;
    private TextView email;
    private TextView contact;
    private TextView faculty;
    private TextView sem;
    private String  fullNameTxt, firstNameTxt, middleNameTxt, lastNameTxt, emailTxt, contactTxt, facultyTxt, semTxt;
    private ImageView imgView;
    private Button updateBtn, deleteBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_profile);

        fullName = (TextView) findViewById(R.id.fullName);
        firstName = (TextView) findViewById(R.id.firstName_pf);
        middleName = (TextView) findViewById(R.id.middleName_pf);
        lastName = (TextView) findViewById(R.id.lastName_pf);
        email = (TextView) findViewById(R.id.email);
        contact = (TextView) findViewById(R.id.contact_pf);
        faculty = (TextView) findViewById(R.id.faculty_pf);
        sem = (TextView) findViewById(R.id.sem_pf);

        updateBtn = (Button) findViewById(R.id.update_btn);
        deleteBtn = (Button) findViewById(R.id.delete_btn);

        imgView = (ImageView) findViewById(R.id.profilePic);







        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UploadProfilePicActivity.class);
                startActivity(i);
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();


        if(firebaseUser == null)
        {
            Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showUserProfile(firebaseUser);
        }


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Update User", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Delete user", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
        ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    fullNameTxt = firebaseUser.getDisplayName();
                    firstNameTxt = readUserDetails.first_Name;
                    middleNameTxt = readUserDetails.middle_Name;
                    lastNameTxt = readUserDetails.last_Name;
                    emailTxt = firebaseUser.getEmail();
                    contactTxt = readUserDetails.contact_no;
                    facultyTxt = readUserDetails.facultytxt;
                    semTxt = readUserDetails.sem;


                    fullName.setText("Full name: " +firstNameTxt+ " "+middleNameTxt+ " " +lastNameTxt+ "." );
                    firstName.setText(firstNameTxt);
                    middleName.setText(middleNameTxt);
                    lastName.setText(lastNameTxt);
                    email.setText(emailTxt);
                    contact.setText(contactTxt);
                    faculty.setText(facultyTxt);
                    sem.setText(semTxt);

                    //set user profile pic
                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(studentProfile.this).load(uri).into(imgView);

                }else{
                    Toast.makeText(getApplicationContext(), "Something is wrong. Try again.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if(id == R.id.menu_logout){
            auth.signOut();
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(studentProfile.this, studentLogInActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Something is wrong 2", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
