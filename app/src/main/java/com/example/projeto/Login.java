package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BootstrapEditText edtEmail, edtSenha;
    private BootstrapButton btnLogin, btnCancela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtSenha = (BootstrapEditText) findViewById(R.id.edtSenha);
        BootstrapButton btnLogin = findViewById(R.id.btnLogin);
        BootstrapButton btnCadastro = findViewById(R.id.btnCadastro);

        cadastro(btnCadastro);
        login(btnLogin);
    }

    private void login(BootstrapButton btnLogin) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fazerLogin(edtEmail.getText().toString(), edtSenha.getText().toString());

            }
        });
    }

    private void cadastro(BootstrapButton btnCadastro) {
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastro = new Intent(Login.this, Escolha.class);
                startActivity(cadastro);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    public void Logado() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Login: yan@gmail.com  Senha: yan1234
    private void fazerLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            Logado();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // ...
                        }

                        // ...
                    }
                });
    }
}