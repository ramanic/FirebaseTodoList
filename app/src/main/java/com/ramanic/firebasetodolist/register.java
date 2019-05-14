package com.ramanic.firebasetodolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    EditText name;
    EditText usernname;
    EditText password;
    DatabaseReference dataRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        usernname =findViewById(R.id.username);
        password = findViewById(R.id.setPass);
        dataRef = FirebaseDatabase.getInstance().getReference();


    }

    public void register_clk(View v){
        try {
            dataRef.child("users").child(usernname.getText().toString()).child("name").setValue(name.getText().toString());
            dataRef.child("users").child(usernname.getText().toString()).child("pass").setValue(password.getText().toString());
            Toast.makeText(getApplicationContext(), "Success!!.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_LONG).show();
        }







    }

}
