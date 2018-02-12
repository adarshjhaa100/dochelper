package com.example.ayush.afinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class DoctorDashboard extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button startPatBtn;
    TextView noPatTv;
    public int count =0;
    public final String TAG1 = "HI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        mAuth = FirebaseAuth.getInstance();
        noPatTv = findViewById(R.id.no_pat_tv);

        startPatBtn = findViewById(R.id.start_pat_btn);
        startPatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDashboard.this,PatientStart.class));
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user)
    {
        if(user==null) {
            startActivity(new Intent(this, DocLogin.class));
        }
        else
        {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("DoctorSchedule");

            Log.d(TAG1,myRef.toString());

            String docid = user.getEmail();
            docid = docid.split("@")[0];

            DatabaseReference doctorref = myRef.child(docid);
            Log.d(TAG1,doctorref.toString());
            count = 0;
            doctorref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG1,dataSnapshot.toString());
                    for(DataSnapshot d : dataSnapshot.getChildren())
                    {   DoctorScheduleEntry dentry = d.getValue(DoctorScheduleEntry.class);

                        Log.d(TAG1,dentry.Date);

                        if(dentry.Date.equals("10/02/2018"))
                        {Log.d(TAG1, Integer.toString(count) + "A");
                            count++;}
                    }
                    Log.d(TAG1, Integer.toString(count)+ "B");
                    noPatTv.setText("No OF PATIENTS EPECTED TODAY: " + Integer.toString(count));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Log.d(TAG1, Integer.toString(count) +"C");

        }
    }
}

