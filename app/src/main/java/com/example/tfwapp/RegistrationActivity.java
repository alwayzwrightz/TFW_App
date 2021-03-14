package com.example.tfwapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText username, userPassword, userEmail;
    String email, name, password;
    Button button1;
    private TextView back;
    private FirebaseAuth fba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //create objects
        setupUI();
        //Initializes firebase authentication
        fba = FirebaseAuth.getInstance();

        //Register button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if string is empty or not
                if(check()){
                    //grabs input
                    String u_email = userEmail.getText().toString().trim();
                    String u_password = userPassword.getText().toString().trim();

                    fba.createUserWithEmailAndPassword(u_email, u_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                //creates user object
                                User user = new User(name,email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(RegistrationActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                //Toast.makeText(RegistrationActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                                //Goes back to main login activity
                                startActivity(new Intent(RegistrationActivity.this,Login_Register.class));
                            }else{
                                Toast.makeText(RegistrationActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        //back button to go back to Login_Register.xml
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,Login_Register.class));
            }
        });
    }
    //creates all the objects
    private void setupUI(){
        username = (EditText) findViewById(R.id.User_Name);
        userEmail = (EditText) findViewById(R.id.EmailAddress);
        userPassword = (EditText) findViewById(R.id.Password);
        button1 = (Button) findViewById(R.id.reg_button);
        back = (Button) findViewById(R.id.back);
    }
    //checks for empty input returns true if no empty lines, false otherwise
    private Boolean check(){
        Boolean result = false;

        name = username.getText().toString();
        password = userPassword.getText().toString();
        email = userEmail.getText().toString();


        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }
}