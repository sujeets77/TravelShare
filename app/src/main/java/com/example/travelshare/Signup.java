package com.example.travelshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    Button loginBtn,signupBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextInputLayout fullnameLayout,usernameLayout,emailLayout,phonenoLayout,passwordLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("datauser");

        loginBtn=findViewById(R.id.loginBtn);
        signupBtn=findViewById(R.id.registerBtn);

        fullnameLayout=findViewById(R.id.fullname_text_field_design);
        usernameLayout=findViewById(R.id.username_text_field_design);
        emailLayout=findViewById(R.id.email_text_field_design);
        phonenoLayout=findViewById(R.id.phone_text_field_design);
        passwordLayout=findViewById(R.id.password_text_field_design);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname=fullnameLayout.getEditText().getText().toString();
                String username=usernameLayout.getEditText().getText().toString();
                String email=emailLayout.getEditText().getText().toString();
                String phoneno=phonenoLayout.getEditText().getText().toString();
                String password=passwordLayout.getEditText().getText().toString();

                if(!fullname.isEmpty())
                {
                    fullnameLayout.setError(null);
                    fullnameLayout.setErrorEnabled(false);
                    if(!username.isEmpty())
                    {
                        usernameLayout.setError(null);
                        usernameLayout.setErrorEnabled(false);
                        if(!email.isEmpty())
                        {
                            emailLayout.setError(null);
                            emailLayout.setErrorEnabled(false);
                            if(!phoneno.isEmpty())
                            {
                                phonenoLayout.setError(null);
                                phonenoLayout.setErrorEnabled(false);
                                if(!password.isEmpty())
                                {
                                    passwordLayout.setError(null);
                                    passwordLayout.setErrorEnabled(false);

                                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
                                    {
                                        emailLayout.setError(null);
                                        emailLayout.setErrorEnabled(false);
                                        if(phoneno.length()==10)
                                        {
                                            User user=new User(fullname,username,email,phoneno,password);

                                            databaseReference.child(username).setValue(user);

                                            Toast.makeText(getApplicationContext(),"User Signup Success",Toast.LENGTH_SHORT).show();

                                            Intent intent=new Intent(getApplicationContext(), Dashboard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            phonenoLayout.setError("Please enter full phone no");
                                        }


                                    }
                                    else
                                    {
                                        emailLayout.setError("Please Enter Valid Email");
                                    }

                                }else {
                                    passwordLayout.setError("Please Enter Password");

                                }

                            }else {
                                phonenoLayout.setError("Please Enter Phoneno");

                            }

                        }else {
                            emailLayout.setError("Please Enter Email");

                        }


                    }else {
                        usernameLayout.setError("Please Enter Username");

                    }

                }else {
                    fullnameLayout.setError("Please Enter Full Name");

                }


            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });



    }
}