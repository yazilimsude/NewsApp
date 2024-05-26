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

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    TextView name, email, password, signIn;
    Button signUp;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    HashMap<String,Object> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://buyukveri-7d8ff-default-rtdb.europe-west1.firebasedatabase.app/");

        name = findViewById(R.id.name_reg);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        signUp = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.signInTextView);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(view);
            }
        });

    }
    private void createUser(View view){
        String userName=name.getText().toString();
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"İsim boş bırakılamaz!",Toast.LENGTH_SHORT).show();
            return;
        }
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
        //Create User
        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            completeSignUpProcess();
                            user=auth.getCurrentUser();
                            hashMap=new HashMap<>();
                            hashMap.put("name",userName);
                            hashMap.put("mail",userEmail);
                            hashMap.put("password",userPassword);
                            reference=database.getReference("Users");

                            String id=task.getResult().getUser().getUid();
                            reference.child(id).setValue(hashMap);

                            Toast.makeText(SignUpActivity.this, "Kaydınız alındı.Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void completeSignUpProcess() {
        // Kullanıcı sign up işlemi tamamlandığında
        // cihaz token'ını al ve Firebase veritabanına kaydet
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        // Kullanıcının token'ını Firebase veritabanına kaydetme
                        saveTokenToFirebase(token);
                    }
                });
    }

    private void saveTokenToFirebase(String token) {
        // Firebase veritabanına token'ı kaydetme
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Kullanıcının ID'si buraya gelmeli
        Log.d("Rana-Rana", userId);
        reference=database.getReference("Users");
        reference.child(userId).child("token").setValue(token)
                .addOnSuccessListener(aVoid -> {
                    // Token başarıyla kaydedildi
                    Toast.makeText(SignUpActivity.this, "Token kaydedildi", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Token kaydı başarısız oldu
                    Toast.makeText(SignUpActivity.this, "Token kaydedilemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}