package com.example.travelshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button forgotBtn,loginBtn,signupBtn,googleBtn;
    TextInputLayout usernameLayout,passwordLayout;

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth mAuth;
    public final int REQ_ONE_TAP=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();




        forgotBtn=findViewById(R.id.forgotBtn);
        loginBtn=findViewById(R.id.loginBtn);
        signupBtn=findViewById(R.id.registerBtn);
        googleBtn=findViewById(R.id.googleBtn);

        usernameLayout=findViewById(R.id.username_text_field_design);
        passwordLayout=findViewById(R.id.password_text_field_design);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username=usernameLayout.getEditText().getText().toString();
                String password=passwordLayout.getEditText().getText().toString();

                if(!username.isEmpty())
                {
                    usernameLayout.setError(null);
                    usernameLayout.setErrorEnabled(false);
                    if(!password.isEmpty())
                    {
                        passwordLayout.setError(null);
                        passwordLayout.setErrorEnabled(false);

                        //login authentication using firebase
                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference=firebaseDatabase.getReference("datauser");

                        Query checkUsername=databaseReference.orderByChild("username").equalTo(username);

                        checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists())
                                {
                                    usernameLayout.setError(null);
                                    usernameLayout.setErrorEnabled(false);
                                    if(password.equals(snapshot.child(username).child("password").getValue(String.class)))
                                    {
                                        passwordLayout.setError(null);
                                        passwordLayout.setErrorEnabled(false);

                                        Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(getApplicationContext(), Dashboard.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else
                                    {
                                        passwordLayout.setError("Wrong Password");
                                    }

                                }
                                else
                                {
                                    usernameLayout.setError("User Does not exist");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else
                    {
                        passwordLayout.setError("Please Enter Password");
                    }

                }
                else
                {
                    usernameLayout.setError("Please Enter Username");
                }


            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
                finish();
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize sign in options the client-id is copied form google-services.json file
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                // Initialize sign in client
                googleSignInClient = GoogleSignIn.getClient(Login.this, googleSignInOptions);

                // Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, REQ_ONE_TAP);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                // When request code is equal to 100 initialize task
                Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                // check condition
                if (signInAccountTask.isSuccessful()) {
                    // When google sign in successful initialize string
                    String s = "Google sign in successful";
                    // Display Toast
                    displayToast(s);
                    // Initialize sign in account
                    try {
                        // Initialize sign in account
                        GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                        // Check condition
                        if (googleSignInAccount != null) {
                            // When sign in account is not equal to null initialize auth credential
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                            // Check credential
                            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Check condition
                                    if (task.isSuccessful()) {
                                        // When task is successful redirect to profile activity display Toast
                                        startActivity(new Intent(Login.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        displayToast("Firebase authentication successful");
                                    } else {
                                        // When task is unsuccessful display Toast
                                        displayToast("Authentication Failed :" + task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }


    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // Initialize firebase user
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(new Intent(Login.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }
}