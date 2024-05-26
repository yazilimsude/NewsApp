package com.example.newsnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    TextView email, password, signUp;
    Button signInBtn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth=FirebaseAuth.getInstance();

        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        signUp = findViewById(R.id.signUpTextView);
        signInBtn = findViewById(R.id.signInButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser curuser =FirebaseAuth.getInstance().getCurrentUser();
        if(curuser != null){
            Intent tomain = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(tomain);
            finish();
        }
    }

    private void loginUser(){
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this,"Mail boş bırakılamaz!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this,"Şifre boş bırakılamaz!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length()<6){
            Toast.makeText(this,"Şifre 6 karakter veya daha fazla olmalıdır.",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("Rana-Login", auth.getUid());
                            Toast.makeText(SignInActivity.this,"Giriş yapıldı",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));

                        }
                        else{
                            Toast.makeText(SignInActivity.this,"Error"+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}