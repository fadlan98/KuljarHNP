package com.example.kuljarhanip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public EditText userTxt;
    public EditText passTxt;
    public Button loginBtn;
    public Button registBtn;
    private static final String TAG = "EmailPassword";
    private AVLoadingIndicatorView avi;
    private ImageView avatarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        avi= findViewById(R.id.avi);
        avi.setIndicator("PacmanIndicator");
        mAuth = FirebaseAuth.getInstance();
        userTxt = findViewById(R.id.userText);
        passTxt = findViewById(R.id.passText);
        loginBtn = findViewById(R.id.btnLogin);
        //registBtn = findViewById(R.id.registerBtn);
        avatarImg = findViewById(R.id.imageViewLogin);
        avatarImg.setVisibility(View.VISIBLE);
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(loginActivity.this, mainMenuActivity.class));
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avi.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signIn(userTxt.getText().toString(),passTxt.getText().toString());
                    }
                },2000);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        /*registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avi.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createAccount(userTxt.getText().toString(),passTxt.getText().toString());
                    }
                },2000);
            }
        });*/
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    /*private void createAccount(String username, String password) {
        //validate
        Log.d(TAG, "createAccount:" + username);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(loginActivity.this, "Registration Success",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            avi.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        // [END create_user_with_email]
    }*/

    private void signIn(String username, String password) {

        Log.d(TAG, "signIn:" + username);
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            avi.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(loginActivity.this, mainMenuActivity.class);
                            startActivity(i);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            avi.setVisibility(View.INVISIBLE);
                            //updateUI(null);
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    //Make Sure User and Password doesn't Empty
    private boolean validateForm() {
        boolean valid = true;

        String email = userTxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userTxt.setError("Required.");
            valid = false;
        } else {
            userTxt.setError(null);
        }

        String password = passTxt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passTxt.setError("Required.");
            valid = false;
        } else {
            passTxt.setError(null);
        }

        return valid;
    }

}
