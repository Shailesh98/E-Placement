package com.example.e_placement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button studentLogin = (Button) findViewById(R.id.studentLogin);
        Button placementCoordinatorLogin = (Button) findViewById(R.id.placementCoordinatorLogin);
        Button placementOfficer = (Button) findViewById(R.id.placementOfficer);

        studentLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText( getApplicationContext(), "Student btn click", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, studentLogInActivity.class);
                startActivity(i);
            }
        });


        placementCoordinatorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getApplicationContext(), "P cO btn click", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), coordinatorLogInActivity.class);
                startActivity(i);
            }
        });
        placementOfficer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "po btn click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), officerLogInActivity.class);
                startActivity(i);
            }
        });
    }

}