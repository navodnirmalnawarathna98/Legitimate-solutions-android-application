package com.example.milestone06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    TextView loginRegisterText;
    EditText rEmail,rPassword;
    Button registerBtn;

   ProgressDialog progressDialog;

//    private Spinner spinnerUserType;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        spinnerUserType = findViewById(R.id.spi01);

//        String[] textSize = getResources().getStringArray(R.array.user_type);
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,textSize);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerUserType.setAdapter(adapter);

        rEmail = findViewById(R.id.txt_mail);
        rPassword = findViewById(R.id.txt_password);
        registerBtn = findViewById(R.id.reg_btn);
        loginRegisterText = findViewById(R.id.regLog_txt);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User......");
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    rEmail.setError("Invalid Email");
                    rEmail.setFocusable(true);
                }else if(password.length()<6){
                    rPassword.setError("Password length at least 6 characters");
                    rPassword.setFocusable(true);
                }else{
                    registerUser(email,password);
                }
            }
        });

        loginRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }


    private void registerUser(String email, String password) {

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "Registerd...."+user.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,DashboardActivity.class));
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}