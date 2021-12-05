package com.cria.agora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMatching extends AppCompatActivity {

    public Switch swtMatch;
    public static int intMatch, matchType;
    private MediaPlayer myMP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_matching);

        //declarar elementos
        swtMatch = findViewById(R.id.switchMatch);
        Button btnNames = findViewById(R.id.btnMatchNames);
        Button btnExpressions = findViewById(R.id.btnMatchExpressions);
        Button btnPhrases = findViewById(R.id.btnMatchPhrases);
        Button btnUpdate = findViewById(R.id.btnUpdateArrays);
        ImageButton btnBack = findViewById(R.id.imgBtnBackMatching);
        ImageButton btnPerfNames = findViewById(R.id.btnPerf_MatchNames);
        ImageButton btnPerfExpressions = findViewById(R.id.btnPerf_MatchExpressions);
        ImageButton btnPerfPhrases = findViewById(R.id.btnPerf_MatchPhrases);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        if (!(sharedPreferences.getLong("tipoUser", 1) ==0)){
            btnUpdate.setVisibility(View.INVISIBLE);
        }

        //marcar se tem legenda
        matchType=1;
        swtMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swtMatch.isChecked()) {
                    matchType = 2;
                    //Toast.makeText(getApplicationContext(), "as  "+matchType, Toast.LENGTH_SHORT).show();

                }else {
                    matchType=1;
                    //Toast.makeText(getApplicationContext(), matchType+"  ggagga", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //função dos botões
        btnNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intMatch=1;
                Intent i = new Intent(getApplicationContext(), PlayMemory.class);
                Atividades.isPerformance=true;
                startActivity(i);
            }
        });

        btnExpressions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intMatch=2;
                Intent i = new Intent(getApplicationContext(), PlayMemory.class);
                Atividades.isPerformance=true;09
                startActivity(i);*/
                String[] myArray = getResources().getStringArray(R.array.english_words_a);
                List<String> words = Arrays.asList(myArray);
                Toast.makeText(getApplicationContext(), words.get(11), Toast.LENGTH_SHORT).show();
            }
        });

        btnPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* intMatch=3;
                Intent i = new Intent(getApplicationContext(), PlayMemory.class);
                Atividades.isPerformance=true;
                startActivity(i);*/
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainGames.class);
                startActivity(i);
                finish();

            }
        });

        btnPerfNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GamePerformance.class);
                startActivity(intent);
                GamePerformance.stgGame = "Memory Game - Names";
                GamePerformance.stgNameBest = "bestScoreNames";
                GamePerformance.stgNameGame = "matchingGame";
            }
        });
        btnPerfExpressions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Soon!", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            }
        });
        btnPerfPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Soon!", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            }
        });


        //setar música de fundo
        myMP = MediaPlayer.create(getApplicationContext(), R.raw.back_whimsical_popsicle);
        myMP.setLooping(true);
        myMP.start();

       // getListFiles();//new File("drawable/"));

    }

    public void getListFiles(View view) {
        Field[] ID_Fields = R.drawable.class.getFields();
        for (Field f : ID_Fields) {
            if (f.getName().contains("thing_")) {
                try {
                    //Toast.makeText(getApplicationContext(), f.getName(), Toast.LENGTH_SHORT).show();
                    final DatabaseReference meRef = FirebaseDatabase.getInstance().getReference("games").child("memory").child("arrayNames");
                    String nameFile = f.getName().replace("thing_", "");
                    Map map = new HashMap();
                    map.put(nameFile, 1);
                    meRef.updateChildren(map);
                    System.out.println(f.getName() + "sample");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        myMP.stop();
        myMP.release();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myMP = MediaPlayer.create(getApplicationContext(), R.raw.back_whimsical_popsicle);
        myMP.setLooping(true);
        myMP.start();
    }
}
