package com.example.szamtechwebhop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_register extends AppCompatActivity {

    private static final String LOG_TAG = activity_register.class.getName();
    private static final String PREF_KEY = com.example.szamtechwebhop.MainActivity.class.getPackage().toString();

    EditText username;
    EditText email;
    EditText password;
    EditText passwordAGAIN;
    EditText place;
    RadioGroup nem;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY"); // Secret key elkérése


        if(secret_key != 10){
            finish();
        }

        username = findViewById(R.id.regusernameTEXT);
        email = findViewById(R.id.regemailTEXT);
        password = findViewById(R.id.regpwdTEXT);
        passwordAGAIN = findViewById(R.id.regpwdAGAIN_TEXT);
        place = findViewById(R.id.placeTEXT);
        nem = findViewById(R.id.gender);


        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        String Email = preferences.getString("Email","");
        email.setText(Email); //Ha beírom a bejelentkezésnél az emailt, akkor a regisztrációnál automatikusan kitölti azt a részt


        mAuth = FirebaseAuth.getInstance(); //Firebase kommunikáció


        Log.i(LOG_TAG, "onCreate");

    }




    public void register(View view) {

        String usernameSTR = username.getText().toString();
        String emailSTR = email.getText().toString();
        String passwordSTR = password.getText().toString();
        String passwordAGAIN_STR = passwordAGAIN.getText().toString();
        String place_STR = place.getText().toString();

        if(!passwordSTR.equals(passwordAGAIN_STR)){
            Log.e(LOG_TAG, "Nem egyeznek a jelszavak!");
            return;
        }

        if(usernameSTR.length() > 0 && emailSTR.length() > 0 && passwordSTR.length() > 0 && passwordAGAIN_STR.length() > 0 && place_STR.length() > 0) { //reszponzivitás


            mAuth.createUserWithEmailAndPassword(emailSTR, passwordSTR).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {  //User létrehozása a regisztráció után

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Felhasználó létrehozása sikeresen megtörtént!"); //konzolra kiírja a sikeres reget
                        Toast.makeText(activity_register.this, "Sikeres regisztráció!", Toast.LENGTH_LONG).show(); //Illetve toast formájában is közli a userrel
                        IntoTheShop();
                    } else {
                        Log.d(LOG_TAG, "Felhasználó létrehozása nem sikerült!");
                        Toast.makeText(activity_register.this, "Nem sikerült létrehozni a fiókot: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        //Hiba esetén kidobja a hibaüzenetet
                    }


                }

            });


        }


    }



    public void cancel(View view) {
        finish(); //Visszatér az őt hívó activityhez

    }


    private void IntoTheShop(){
        Intent gotoshop = new Intent(this, com.example.szamtechwebhop.TheShop.class);
        startActivity(gotoshop);


    }

    @Override
    protected void onStart() {
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