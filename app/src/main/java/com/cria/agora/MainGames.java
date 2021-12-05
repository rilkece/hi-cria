package com.cria.agora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class MainGames extends AppCompatActivity {

    private ImageButton  btnGameMatching, btnGameWords, btnGameHangman;
    private ImageButton btnBack;
    private DatabaseReference myRef;
    private MediaPlayer myMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_games);

        final  SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        //setar elementos
        btnBack = findViewById(R.id.imgBtnBackMainGames);
        btnGameMatching = findViewById(R.id.btn_game_memory);
        btnGameWords = findViewById(R.id.btn_game_words);
        btnGameHangman = findViewById(R.id.btn_game_hangman);
        myRef = FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("games");


        //setar função dos botões
        btnGameMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setarLastLogin("matchingGame");
                Intent intent = new Intent(getApplicationContext(), GameMatching.class);
                startActivity(intent);
                finish();
            }
        });

        btnGameWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setarLastLogin("BuildWordGame");
                Intent intent = new Intent(getApplicationContext(), GameBuildWords.class);
                startActivity(intent);
                finish();
            }
        });
        btnGameHangman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setarLastLogin("hangmanGame");
                Intent intent = new Intent(getApplicationContext(), GameHangman.class);
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getLong("tipoUser", 1) ==0){
                    Intent intent = new Intent(getApplicationContext(), MainAdm.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        //setar música de fundo
        myMP = MediaPlayer.create(getApplicationContext(), R.raw.back_good_morning_doctor_weird);
        myMP.setLooping(true);
        myMP.start();


    }

    private void setarLastLogin(String stringGame) {
        myRef = myRef.child(stringGame);

        Map map = new HashMap();
        map.put("lastGameLogin", ServerValue.TIMESTAMP);
        myRef.updateChildren(map);

    }

    @Override
    protected void onStop() {
        super.onStop();
        myMP.stop();
        myMP.release();
    }
}
