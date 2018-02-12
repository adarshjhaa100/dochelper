package com.example.ayush.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PatientStart extends AppCompatActivity {
    public final String TAG1 = "HI";
    FirebaseAuth mAuth;
    EditText patID;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_start);

        patID = findViewById(R.id.pat_id);
        submitBtn = findViewById(R.id.pat_search_btn);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("PatientDatabase");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String patientid = patID.getText().toString();

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(patientid)) {
                            Log.d(TAG1,"Making the jump");
                            Intent i = new Intent(PatientStart.this, PatientReport.class);
                            Log.d(TAG1,patientid);
                            i.putExtra("Patid", patientid);
                            startActivity(i);
                        }

                        else
                        {   Log.d(TAG1,"No such patient");
                            Toast.makeText(PatientStart.this,"No User Exists", Toast.LENGTH_LONG);
                            Log.d(TAG1,"Not Making the jump");
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
