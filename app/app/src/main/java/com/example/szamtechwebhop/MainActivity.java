package com.example.szamtechwebhop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = activity_register.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 10;



    EditText email;
    EditText password;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword); //Jelszó lekérése a main activityből

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();




        Log.i(LOG_TAG, "onCreate");


    }


    public void login(View view) {

        String emailSTR = email.getText().toString();
        String passwordSTR = password.getText().toString();


        if(emailSTR.length() > 0 && passwordSTR.length() > 0) { //reszponzivitás

            mAuth.signInWithEmailAndPassword(emailSTR, passwordSTR).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Sikeres bejelentkezés!");

                        Toast.makeText(MainActivity.this, "Sikeres bejelentkezés!", Toast.LENGTH_LONG).show();
                        IntoTheShop();


                    } else {
                        Toast.makeText(MainActivity.this, "Nem sikerült a bejelentkezés: " + task.getException(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }



    public void register(View view) {



        Intent reg_intent = new Intent(this, activity_register.class); //Ha a regisztráció gombra kattintok, akkor átdob a regisztrációs oldalra
        reg_intent.putExtra("SECRET_KEY", 10);
        startActivity(reg_intent);

    }

    private void IntoTheShop(){
        Intent gotoshop = new Intent(this,TheShop.class);
        startActivity(gotoshop);


    }


    @Override
    protected void onStart() { // lifecycle hooks
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Email", email.getText().toString());
        editor.apply();

        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }
}