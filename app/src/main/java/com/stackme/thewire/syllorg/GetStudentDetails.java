package com.stackme.thewire.syllorg;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.TestResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetStudentDetails extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = "GetStudentsDetails";
    AutoCompleteTextView university_autoCompleteView;
    NumberPicker sem_numberPicker;
    Button done_button;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    College college;
    College selectedItem;
    ArrayList<College> universities = new ArrayList<College>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_student_details);

        university_autoCompleteView = findViewById(R.id.university_autoCompleteTextView);
        sem_numberPicker = findViewById(R.id.sem_numberPicker);
        done_button = findViewById(R.id.done_button);
        done_button.setOnClickListener(this);

        sem_numberPicker.setMinValue(1);
        sem_numberPicker.setMaxValue(8);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("colleges")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Document : " + document.getId());
                                college = document.toObject(College.class);
                                college.setCollege_id(document.getId());

                                universities.add(college);
                                Log.d(TAG, "Universities:" + universities);
                            }

                            ArrayAdapter<College> adapter = new ArrayAdapter<College>(GetStudentDetails.this, android.R.layout.simple_dropdown_item_1line, universities);
                            university_autoCompleteView.setAdapter(adapter);

                            university_autoCompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    selectedItem = (College )adapterView.getItemAtPosition(i);
                                    if(selectedItem != null){
                                        Log.d(TAG, "College selection:OK");
                                    }
                                    else{
                                        Log.d(TAG, "College selection:FAILURE");
                                    }
                                }
                            });

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.done_button:
                submitData(view);
                break;
        }
    }

    private void submitData(final View view) {

        if(selectedItem != null && mAuth.getCurrentUser() != null && mAuth.getUid() != null){

            Map<String, Object> data = new HashMap<>();
            data.put("name", mAuth.getCurrentUser().getDisplayName());
            data.put("sem", sem_numberPicker.getValue());
            data.put("college", selectedItem.getCollege_id());

            selectedItem.setUid(mAuth.getUid());

            db.collection("users").document(mAuth.getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(view, "Details saved successfully", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(GetStudentDetails.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Snackbar.make(view, "Operation Failed", Snackbar.LENGTH_SHORT).show();
                }
            });

        }
        else if(selectedItem == null){
            Snackbar.make(view, "Please select college", Snackbar.LENGTH_SHORT).show();
        }
        else{
            Snackbar.make(view, "User authentication failed", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.finishAffinity();
        super.onBackPressed();
    }
}
