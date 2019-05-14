package com.ramanic.firebasetodolist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        dataRef = FirebaseDatabase.getInstance().getReference();


    }

    public  void register(View v){

        Intent intent = new Intent(getApplicationContext(), register.class);
        startActivity(intent);



    }

    public  void login(View v){
        final String uname = username.getText().toString();
        final String upass = password.getText().toString();


        dataRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(uname)) {
                    if (snapshot.child(uname).child("pass").getValue().equals(upass)) {
                        Toast.makeText(getApplicationContext(), "Loging In..", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), todo_list.class);
                        intent.putExtra("USER", uname);
                        startActivity(intent);
                    } else {
                        //Log.d("RAMANIX", snapshot.child(uname).getValue(String.class));
                        Toast.makeText(getApplicationContext(), "Incorrect Password !!", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }





}
