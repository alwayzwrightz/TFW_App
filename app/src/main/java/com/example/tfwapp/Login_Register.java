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
import com.google.firebase.auth.FirebaseUser;

public class Login_Register extends AppCompatActivity {

    private FirebaseAuth fba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fba = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__register);

        String userRegistration;

        //creates the button objects
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        //input text objects
        EditText email = (EditText) findViewById(R.id.EmailAddress);
        EditText password = (EditText) findViewById(R.id.Password);
        //text for password/email
        TextView email_text = (TextView)findViewById(R.id.textEmail);
        TextView password_text = (TextView)findViewById(R.id.textPassword);

        //checks if user is already logged in
        FirebaseUser user = fba.getCurrentUser();
        if(user != null){
           // finish();
            //here is where the user can go to the next activity
        }
        //login button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(email.getText().toString(), password.getText().toString());
            }
        });
        //register button
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Login_Register.this,RegistrationActivity.class));
            }
        });
    }
    private void check(String userName, String userPassword){
        fba.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login_Register.this,"Logged In!", Toast.LENGTH_SHORT).show();
                    //here the user moves on to the next activity
                }else{
                    Toast.makeText(Login_Register.this,"Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
