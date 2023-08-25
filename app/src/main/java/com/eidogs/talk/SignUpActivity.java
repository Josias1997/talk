package com.eidogs.talk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eidogs.talk.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameBox, emailBox, passwordBox;
    private Button loginBtn, signupBtn;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore database;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameBox = findViewById(R.id.name);
        emailBox = findViewById(R.id.email);
        passwordBox = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        progressBar = findViewById(R.id.progress_circular);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, name;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();
                name = nameBox.getText().toString();
                if(!name.equals("") && !email.equals("") && !password.equals("")) {
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setName(name);
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                                assert mUser != null;
                                database.collection("users")
                                                .document(mUser.getUid())
                                                        .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                            }
                                        });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else  {
                    Toast.makeText(SignUpActivity.this, "Fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });
    }
}