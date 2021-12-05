package com.cria.agora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.type.Date;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import DadosUsuario.UserAchievementes;
import DadosUsuario.UserInfo;

public class Login extends AppCompatActivity {

private MaterialButton loginButton;
private EditText editTextMat;
private EditText editTextPassword;
private ProgressBar progressLogin;
private TextView txtVersion;

private DatabaseReference myRef;
private FirebaseDatabase database;

/*

sharedPreferences.getString("utbCRIA", "0")
sharedPreferences.getString("firstName", "unknown")
sharedPreferences.getLong("tipoUser", 1) ==0
sharedPreferences.getString("matricula", "0")
SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("tipoUser", 9);
                editor.apply();

                */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tirar action bar
        //getActionBar().hide();

        setContentView(R.layout.activity_login);
        // Inicializar e Instanciar o Firebase
        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        // Declarar UI
        loginButton = findViewById(R.id.loginButton);
        editTextMat = findViewById(R.id.editTextmatricula);
        editTextPassword = findViewById(R.id.editTextPass);
        progressLogin = findViewById(R.id.progressBarLogin);
        txtVersion = findViewById(R.id.textVersion);

        if (sharedPreferences.getBoolean("LoggedIn", false)){
            logeIn(sharedPreferences.getLong("tipoUser", 1));
        }else{
            loginButton.setVisibility(View.VISIBLE);
            editTextMat.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);
            txtVersion.setVisibility(View.VISIBLE);
            progressLogin.setVisibility(View.GONE);

            setarUILogin();
        }




    }

    private void setarUILogin() {
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //ação de login
                if (editTextMat.getText().toString().equals("") || editTextPassword.getText().toString().equals("") ){

                    //mensagem caso campos estejam vazios
                    Toast.makeText(getApplicationContext(),"Preencha todos os campos.", Toast.LENGTH_LONG).show();
                }else {
                    // ajustar visibilidade para a barra de progresso
                    loginButton.setVisibility(View.GONE);
                    editTextMat.setVisibility(View.GONE);
                    editTextPassword.setVisibility(View.GONE);
                    txtVersion.setVisibility(View.GONE);
                    progressLogin.setVisibility(View.VISIBLE);

                    //referenciar e Ler do database
                    myRef = database.getReference("users").child(editTextMat.getText().toString()).child("senha");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String senha = dataSnapshot.getValue(String.class);
                            if (editTextPassword.getText().toString().equals(senha)) {

                                //popular objeto java com dados do usuário
                                myRef = database.getReference("users").child(editTextMat.getText().toString());

                                Map map = new HashMap();
                                map.put("lastLogin", ServerValue.TIMESTAMP);
                                myRef.updateChildren(map);
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        //popular objeto java com achieves de usuário
                                        Boolean eco = true;
                                        Boolean forest = true;
                                        Boolean halloween = true;
                                        Boolean number = true;
                                        Boolean labor = true;
                                        Boolean saturn = true;
                                        Boolean summer = true;
                                        Boolean thanksgiving = true;
                                        Boolean travel = true;
                                        Boolean unicorn = true;

                                        if (dataSnapshot.child("achieves").child("badgeEco").getValue()==null){ eco = false; }
                                        if (dataSnapshot.child("achieves").child("badgeForest").getValue()==null){ forest = false; }
                                        if (dataSnapshot.child("achieves").child("badgeHalloween").getValue()==null){ halloween = false; }
                                        if (dataSnapshot.child("achieves").child("badgeLabor").getValue()==null){ labor = false; }
                                        if (dataSnapshot.child("achieves").child("badgeNumber").getValue()==null){ number = false; }
                                        if (dataSnapshot.child("achieves").child("badgeSaturn").getValue()==null){ saturn = false; }
                                        if (dataSnapshot.child("achieves").child("badgeSummer").getValue()==null){ summer = false; }
                                        if (dataSnapshot.child("achieves").child("badgeThanksgiving").getValue()==null){ thanksgiving = false; }
                                        if (dataSnapshot.child("achieves").child("badgeTravel").getValue()==null){ travel = false; }
                                        if (dataSnapshot.child("achieves").child("badgeUnicorn").getValue()==null){ unicorn = false; }


                                        //Setar o shared Preference

                                        editor.putBoolean("LoggedIn", true);
                                        editor.putString("matricula", Objects.requireNonNull(dataSnapshot.child("matricula").getValue()).toString());
                                        editor.putString("firstName", Objects.requireNonNull(dataSnapshot.child("firstName").getValue()).toString());
                                        editor.putString("surName", Objects.requireNonNull(dataSnapshot.child("surName").getValue()).toString());
                                        editor.putString("email", Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                                        editor.putString("phone", Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString());
                                        editor.putString("escola", Objects.requireNonNull(dataSnapshot.child("escola").getValue()).toString());
                                        editor.putString("func", Objects.requireNonNull(dataSnapshot.child("func").getValue()).toString());
                                        editor.putString("dia", Objects.requireNonNull(dataSnapshot.child("dia").getValue()).toString());
                                        editor.putString("mes", Objects.requireNonNull(dataSnapshot.child("mes").getValue()).toString());
                                        editor.putString("ano", Objects.requireNonNull(dataSnapshot.child("ano").getValue()).toString());
                                        editor.putString("sexo", Objects.requireNonNull(dataSnapshot.child("sexo").getValue()).toString());
                                        editor.putLong("tipoUser", Long.parseLong(Objects.requireNonNull(dataSnapshot.child("tipoUser").getValue()).toString()));
                                        editor.putLong("utbCRIA", Long.parseLong(Objects.requireNonNull(dataSnapshot.child("utbCRIA").getValue()).toString()));

                                        editor.putBoolean("Achieve_eco", eco);
                                        editor.putBoolean("Achieve_forest", forest);
                                        editor.putBoolean("Achieve_halloween", halloween);
                                        editor.putBoolean("Achieve_labor", labor);
                                        editor.putBoolean("Achieve_number", number);
                                        editor.putBoolean("Achieve_saturn", saturn);
                                        editor.putBoolean("Achieve_summer", summer);
                                        editor.putBoolean("Achieve_thanksgiving", thanksgiving);
                                        editor.putBoolean("Achieve_travel", travel);
                                        editor.putBoolean("Achieve_unicorn", unicorn);
                                        editor.apply();



                                        //ir para nova activity
                                        logeIn(sharedPreferences.getLong("tipoUser", 1));




                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });







                            } else {
                                Toast.makeText(getApplicationContext(),"Matrícula ou senha incorreta.", Toast.LENGTH_LONG).show();
                                loginButton.setVisibility(View.VISIBLE);
                                editTextMat.setVisibility(View.VISIBLE);
                                editTextPassword.setVisibility(View.VISIBLE);
                                progressLogin.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"Usuário não encontrado", Toast.LENGTH_LONG).show();
                            loginButton.setVisibility(View.VISIBLE);
                            editTextMat.setVisibility(View.VISIBLE);
                            editTextPassword.setVisibility(View.VISIBLE);
                            progressLogin.setVisibility(View.GONE);

                        }
                    });

                }
            }
        });

    }

    private void logeIn(long tipoUser) {

        if (tipoUser ==0){

            Intent intent = new Intent(getApplicationContext(), MainAdm.class);
            startActivity(intent);

            finish();
            //Toast.makeText(getApplicationContext(), String.valueOf(itipoUser), Toast.LENGTH_SHORT).show();
        }else if(tipoUser ==9) {
            Toast.makeText(getApplicationContext(), "Usuário Inativo", Toast.LENGTH_SHORT).show();
            setarUILogin();

        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            //Toast.makeText(getApplicationContext(), String.valueOf(itipoUser), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //teste e ajeitar visual após login
        //Toast.makeText(getApplicationContext(),"Sucesso", Toast.LENGTH_LONG).show();
        loginButton.setVisibility(View.VISIBLE);
        editTextMat.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        progressLogin.setVisibility(View.GONE);
    }
}

/*
     // Write a message to the database
       FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message").child("azul");
                myRef.setValue("Hello, World!");

                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        Log.d("sucesso", "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Failed to read value
                        Log.w("falha", "Failed to read value.", databaseError.toException());
                    }
                });


                  usuario = new UserInfo(database.getReference("users").child(editTextMat.getText().toString()).child("matricula").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("firstName").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("surName").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("email").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("phone").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("escola").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("func").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("dia").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("mes").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("ano").toString(),
                                        database.getReference("users").child(editTextMat.getText().toString()).child("sexo").toString());

                                            usuario = new UserInfo((String) dataSnapshot.child("matricula").getValue(),
                                                (String) dataSnapshot.child("firstName").getValue(),
                                                (String) dataSnapshot.child("surName").getValue(),
                                                (String) dataSnapshot.child("email").getValue(),
                                                (String) dataSnapshot.child("phone").getValue(),
                                                (String) dataSnapshot.child("escola").getValue(),
                                                (String) dataSnapshot.child("func").getValue(),
                                                (String) dataSnapshot.child("dia").getValue(),
                                                (String) dataSnapshot.child("mes").getValue(),
                                                (String) dataSnapshot.child("ano").getValue(),
                                                (String) dataSnapshot.child("sexo").getValue());
 */