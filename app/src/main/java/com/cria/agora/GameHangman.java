package com.cria.agora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameHangman extends AppCompatActivity {

    private MaterialButton btnHMA, btnHMB, btnHMC, btnHMD, btnHME, btnHMF, btnHMG, btnHMH, btnHMI, btnHMJ, btnHMK, btnHML, btnHMM, btnHMN, btnHMO, btnHMP, btnHMQ, btnHMR, btnHMS, btnHMT, btnHMU, btnHMV, btnHMW, btnHMX, btnHMY, btnHMZ, btnHMStart, btnHMPerf, btnHMReturn;
    private boolean btnIsClickedHMA, btnIsClickedHMB, btnIsClickedHMC, btnIsClickedHMD, btnIsClickedHME, btnIsClickedHMF, btnIsClickedHMG, btnIsClickedHMH, btnIsClickedHMI, btnIsClickedHMJ, btnIsClickedHMK, btnIsClickedHML, btnIsClickedHMM, btnIsClickedHMN, btnIsClickedHMO, btnIsClickedHMP, btnIsClickedHMQ, btnIsClickedHMR, btnIsClickedHMS, btnIsClickedHMT, btnIsClickedHMU, btnIsClickedHMV, btnIsClickedHMW, btnIsClickedHMX, btnIsClickedHMY, btnIsClickedHMZ;

    private SoundPool spHangman;
    private int  matchSound, failSound, successSound, looseSound;

    private int lgTotalPoints, lgCurrentPoints;
    private TextView txtTotalPoint, txtCurrentPoints;

    private String currentWord, hangWord;
    private int currentWordLenght, countMistakes;

    private DatabaseReference myBestRef;

    private TextView hangAnswer;
    private ImageView imgHM;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hangman);
    // setar elementos
        btnHMA = findViewById(R.id.btnHMLetterA);
        btnHMB = findViewById(R.id.btnHMLetterB);
         btnHMC = findViewById(R.id.btnHMLetterC);
         btnHMD = findViewById(R.id.btnHMLetterD);
         btnHME = findViewById(R.id.btnHMLetterE);
         btnHMF = findViewById(R.id.btnHMLetterF);
         btnHMG = findViewById(R.id.btnHMLetterG);
         btnHMH = findViewById(R.id.btnHMLetterH);
         btnHMI = findViewById(R.id.btnHMLetterI);
         btnHMJ = findViewById(R.id.btnHMLetterJ);
         btnHMK = findViewById(R.id.btnHMLetterK);
         btnHML = findViewById(R.id.btnHMLetterL);
         btnHMM = findViewById(R.id.btnHMLetterM);
         btnHMN = findViewById(R.id.btnHMLetterN);
         btnHMO = findViewById(R.id.btnHMLetterO);
         btnHMP = findViewById(R.id.btnHMLetterP);
         btnHMQ = findViewById(R.id.btnHMLetterQ);
         btnHMR = findViewById(R.id.btnHMLetterR);
         btnHMS = findViewById(R.id.btnHMLetterS);
         btnHMT = findViewById(R.id.btnHMLetterT);
         btnHMU = findViewById(R.id.btnHMLetterU);
         btnHMV = findViewById(R.id.btnHMLetterV);
         btnHMW = findViewById(R.id.btnHMLetterW);
         btnHMX = findViewById(R.id.btnHMLetterX);
         btnHMY = findViewById(R.id.btnHMLetterY);
         btnHMZ = findViewById(R.id.btnHMLetterZ);
         btnHMStart = findViewById(R.id.btnHMPlay);
         btnHMPerf = findViewById(R.id.btnHMPerf);
         btnHMReturn = findViewById(R.id.btnHMBack);

        txtCurrentPoints = findViewById(R.id.HMPoints);
        txtTotalPoint = findViewById(R.id.HMTotalPoints);

        hangAnswer = findViewById(R.id.txtHMAnswer);
        imgHM = findViewById(R.id.imgHM);


        //declarar elementos--setar sons básicos
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        spHangman = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        matchSound = spHangman.load(this, R.raw.right, 1);
        failSound = spHangman.load(this, R.raw.wrong, 1);
        successSound = spHangman.load(this, R.raw.success, 1);
        looseSound = spHangman.load(this, R.raw.loose, 1);

        // setar booleans de botões clicados
        btnIsClickedHMA = false; 
        btnIsClickedHMB = false; 
         btnIsClickedHMC = false; 
         btnIsClickedHMD = false; 
         btnIsClickedHME = false; 
         btnIsClickedHMF = false; 
         btnIsClickedHMG = false; 
         btnIsClickedHMH = false; 
         btnIsClickedHMI = false; 
         btnIsClickedHMJ = false; 
         btnIsClickedHMK = false; 
         btnIsClickedHML = false; 
         btnIsClickedHMM = false; 
         btnIsClickedHMN = false; 
         btnIsClickedHMO = false; 
         btnIsClickedHMP = false; 
         btnIsClickedHMQ = false; 
         btnIsClickedHMR = false; 
         btnIsClickedHMS = false; 
         btnIsClickedHMT = false; 
         btnIsClickedHMU = false; 
         btnIsClickedHMV = false; 
         btnIsClickedHMW = false; 
         btnIsClickedHMX = false; 
         btnIsClickedHMY = false; 
         btnIsClickedHMZ = false;

        //setar cabeçalho - botão voltar
        btnHMReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainGames.class);
                startActivity(i);
                finish();
            }
        });

         btnHMStart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 iniciarHang();
             }
         });
        //setar botão performance
        btnHMPerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GamePerformance.class);
                startActivity(intent);
                GamePerformance.stgGame = "Hangman";
                GamePerformance.stgNameBest = "bestScore";
                GamePerformance.stgNameGame = "hangmanGame";
            }
        });

        
    }



    private void iniciarHang() {
        setarBotoes();
        setarcabecalho();
        setarPalavra();




    }

    private void setarPalavra() {
        //escolher palavra
        String[] arrayWords = getResources().getStringArray(R.array.english_basics_words);
        List<String> wordsList = Arrays.asList(arrayWords);
        Collections.shuffle(wordsList);
        currentWord = wordsList.get(0);
        currentWordLenght = currentWord.length();

        //setar texto com _
        int indiceAtual = 0;
        String tracinhos = "";
        while ((indiceAtual)<currentWordLenght){
           tracinhos = tracinhos.concat("_");
           indiceAtual++;
        }
            hangAnswer.setText(tracinhos);

       // Toast.makeText(getApplicationContext(), currentWord+" + "+String.valueOf(currentWordLenght)+" + "+tracinhos, Toast.LENGTH_SHORT).show();

        funLetras();

    }

    private void funLetras() {

        btnHMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMA){
                    btnIsClickedHMA = true;
                    if (verificaLetra("a")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMA.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMA.setStrokeColorResource(R.color.verde_correto);
                        btnHMA.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMA.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMA.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMA.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });



        btnHMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMB){
                    btnIsClickedHMB = true;
                    if (verificaLetra("b")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMB.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMB.setStrokeColorResource(R.color.verde_correto);
                        btnHMB.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMB.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMB.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMB.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMC){
                    btnIsClickedHMC = true;
                    if (verificaLetra("c")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMC.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMC.setStrokeColorResource(R.color.verde_correto);
                        btnHMC.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMC.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMC.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMC.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMD){
                    btnIsClickedHMD = true;
                    if (verificaLetra("d")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMD.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMD.setStrokeColorResource(R.color.verde_correto);
                        btnHMD.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMD.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMD.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMD.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHME){
                    btnIsClickedHME = true;
                    if (verificaLetra("e")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHME.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHME.setStrokeColorResource(R.color.verde_correto);
                        btnHME.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHME.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHME.setStrokeColorResource(R.color.vermelho_errado);
                        btnHME.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMF){
                    btnIsClickedHMF = true;
                    if (verificaLetra("f")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMF.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMF.setStrokeColorResource(R.color.verde_correto);
                        btnHMF.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMF.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMF.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMF.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMG){
                    btnIsClickedHMG = true;
                    if (verificaLetra("g")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMG.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMG.setStrokeColorResource(R.color.verde_correto);
                        btnHMG.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMG.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMG.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMG.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMH){
                    btnIsClickedHMH = true;
                    if (verificaLetra("h")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMH.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMH.setStrokeColorResource(R.color.verde_correto);
                        btnHMH.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMH.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMH.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMH.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMI){
                    btnIsClickedHMI = true;
                    if (verificaLetra("i")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMI.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMI.setStrokeColorResource(R.color.verde_correto);
                        btnHMI.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMI.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMI.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMI.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMJ){
                    btnIsClickedHMJ = true;
                    if (verificaLetra("j")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMJ.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMJ.setStrokeColorResource(R.color.verde_correto);
                        btnHMJ.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMJ.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMJ.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMJ.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMK){
                    btnIsClickedHMK = true;
                    if (verificaLetra("k")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMK.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMK.setStrokeColorResource(R.color.verde_correto);
                        btnHMK.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMK.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMK.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMK.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHML){
                    btnIsClickedHML = true;
                    if (verificaLetra("l")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHML.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHML.setStrokeColorResource(R.color.verde_correto);
                        btnHML.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHML.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHML.setStrokeColorResource(R.color.vermelho_errado);
                        btnHML.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMM){
                    btnIsClickedHMM = true;
                    if (verificaLetra("m")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMM.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMM.setStrokeColorResource(R.color.verde_correto);
                        btnHMM.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMM.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMM.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMM.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMN){
                    btnIsClickedHMN = true;
                    if (verificaLetra("n")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMN.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMN.setStrokeColorResource(R.color.verde_correto);
                        btnHMN.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMN.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMN.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMN.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMO){
                    btnIsClickedHMO = true;
                    if (verificaLetra("o")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMO.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMO.setStrokeColorResource(R.color.verde_correto);
                        btnHMO.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMO.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMO.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMO.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMP){
                    btnIsClickedHMP = true;
                    if (verificaLetra("p")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMP.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMP.setStrokeColorResource(R.color.verde_correto);
                        btnHMP.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMP.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMP.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMP.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMQ){
                    btnIsClickedHMQ = true;
                    if (verificaLetra("q")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMQ.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMQ.setStrokeColorResource(R.color.verde_correto);
                        btnHMQ.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMQ.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMQ.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMQ.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMR){
                    btnIsClickedHMR = true;
                    if (verificaLetra("r")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMR.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMR.setStrokeColorResource(R.color.verde_correto);
                        btnHMR.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMR.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMR.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMR.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMS){
                    btnIsClickedHMS = true;
                    if (verificaLetra("s")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMS.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMS.setStrokeColorResource(R.color.verde_correto);
                        btnHMS.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMS.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMS.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMS.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMT){
                    btnIsClickedHMT = true;
                    if (verificaLetra("t")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMT.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMT.setStrokeColorResource(R.color.verde_correto);
                        btnHMT.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMT.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMT.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMT.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMU){
                    btnIsClickedHMU = true;
                    if (verificaLetra("u")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMU.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMU.setStrokeColorResource(R.color.verde_correto);
                        btnHMU.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMU.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMU.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMU.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMV){
                    btnIsClickedHMV = true;
                    if (verificaLetra("v")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMV.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMV.setStrokeColorResource(R.color.verde_correto);
                        btnHMV.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMV.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMV.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMV.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMW){
                    btnIsClickedHMW = true;
                    if (verificaLetra("w")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMW.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMW.setStrokeColorResource(R.color.verde_correto);
                        btnHMW.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMW.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMW.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMW.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMX){
                    btnIsClickedHMX = true;
                    if (verificaLetra("x")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMX.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMX.setStrokeColorResource(R.color.verde_correto);
                        btnHMX.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMX.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMX.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMX.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMY){
                    btnIsClickedHMY = true;
                    if (verificaLetra("y")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMY.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMY.setStrokeColorResource(R.color.verde_correto);
                        btnHMY.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMY.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMY.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMY.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnHMZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnIsClickedHMZ){
                    btnIsClickedHMZ = true;
                    if (verificaLetra("z")){
                        spHangman.play(matchSound, 1, 1, 0, 0, 1);
                        btnHMZ.setTextColor(getResources().getColor(R.color.verde_correto));
                        btnHMZ.setStrokeColorResource(R.color.verde_correto);
                        btnHMZ.setStrokeWidth(3);


                    }else {
                        spHangman.play(failSound, 1, 1, 0, 0, 1);
                        btnHMZ.setTextColor(getResources().getColor(R.color.vermelho_errado));
                        btnHMZ.setStrokeColorResource(R.color.vermelho_errado);
                        btnHMZ.setStrokeWidth(3);
                        countMistakes++;
                        erroou();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Already typed.", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void erroou() {

        lgCurrentPoints = lgCurrentPoints - 5;
        txtCurrentPoints.setText(String.valueOf(lgCurrentPoints));
        switch (countMistakes){
            case 1:
                imgHM.setImageResource(R.drawable.hang_1);
                break;
            case 2:
                imgHM.setImageResource(R.drawable.hang_2);
                break;
            case 3:
                imgHM.setImageResource(R.drawable.hang_3);
                break;
            case 4:
                imgHM.setImageResource(R.drawable.hang_4);
                break;
            case 5:
                imgHM.setImageResource(R.drawable.hang_5);
                break;
            case 6:
                imgHM.setImageResource(R.drawable.hang_6);
                hangAnswer.setText(currentWord);
                final SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
                spHangman.play(looseSound, 1, 1, 0, 0, 1);
                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(GameHangman.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("But don't give up. You can always try again!")
                        .setTitle("Sorry, you loose.");
                builder.setCancelable(false);
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        if (sharedPreferences.getLong("tipoUser", 1) == 0){
                            Intent i = new Intent(getApplicationContext(), MainAdm.class);
                            startActivity(i);
                            finish();
                        }else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }
                }).setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        iniciarHang();
                    }
                });
                // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();
                dialog.show();

                break;

        }
    }

    private boolean verificaLetra(String letra) {
        boolean deuCerto = false;
        int indiceLetra=0;
        while (indiceLetra<currentWordLenght){

            //Toast.makeText(getApplicationContext(), currentWord.substring(indiceLetra, indiceLetra+1)+" + "+indiceLetra, Toast.LENGTH_SHORT).show();
            if(letra.equals(currentWord.substring(indiceLetra, indiceLetra+1))){
                hangWord = hangAnswer.getText().toString();
                StringBuilder newWord = new StringBuilder(hangWord);
                newWord.setCharAt(indiceLetra, letra.charAt(0));
                hangAnswer.setText(newWord);
                lgCurrentPoints = lgCurrentPoints + 10;
                txtCurrentPoints.setText(String.valueOf(lgCurrentPoints));
                if (currentWord.equals(hangAnswer.getText().toString())){
                    verifyBest();
                    final SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
                    spHangman.play(successSound, 1, 1, 0, 0, 1);
                    // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameHangman.this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("CONGRATULATIONS!")
                            .setTitle("You won. Your score was "+lgCurrentPoints+" points.");
                    // Add the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            if (sharedPreferences.getLong("tipoUser", 1) == 0){
                                Intent i = new Intent(getApplicationContext(), MainAdm.class);
                                startActivity(i);
                                finish();
                            }else {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        }
                    }).setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            iniciarHang();
                        }
                    });
                    // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                //Toast.makeText(getApplicationContext(), "deu certo"+indiceLetra, Toast.LENGTH_SHORT).show();
                indiceLetra++;

                    deuCerto = true;

            }else {
                //Toast.makeText(getApplicationContext(), "indice "+indiceLetra+": "+letra+" diferente de "+currentWord.substring(indiceLetra, indiceLetra+1), Toast.LENGTH_SHORT).show();
                indiceLetra++;
            }
        }

        return deuCerto;

    }

    private void setarcabecalho() {
        countMistakes = 0;
        imgHM.setImageResource(R.drawable.hang_0);
        //setar cabeçalho-recorde
        lgCurrentPoints = 0;
        txtCurrentPoints.setText("0");

        verifyBest();
    }

    private void setarBotoes() {
        // setar booleans de botões clicados
        btnIsClickedHMA = false;
        btnIsClickedHMB = false;
        btnIsClickedHMC = false;
        btnIsClickedHMD = false;
        btnIsClickedHME = false;
        btnIsClickedHMF = false;
        btnIsClickedHMG = false;
        btnIsClickedHMH = false;
        btnIsClickedHMI = false;
        btnIsClickedHMJ = false;
        btnIsClickedHMK = false;
        btnIsClickedHML = false;
        btnIsClickedHMM = false;
        btnIsClickedHMN = false;
        btnIsClickedHMO = false;
        btnIsClickedHMP = false;
        btnIsClickedHMQ = false;
        btnIsClickedHMR = false;
        btnIsClickedHMS = false;
        btnIsClickedHMT = false;
        btnIsClickedHMU = false;
        btnIsClickedHMV = false;
        btnIsClickedHMW = false;
        btnIsClickedHMX = false;
        btnIsClickedHMY = false;
        btnIsClickedHMZ = false;

        btnHMA.setTextColor(getResources().getColor(R.color.Score100));
        btnHMA.setStrokeColorResource(R.color.Score100);
        btnHMA.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMA.setStrokeWidth(1);

        btnHMB.setTextColor(getResources().getColor(R.color.Score100));
        btnHMB.setStrokeColorResource(R.color.Score100);
        btnHMB.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMB.setStrokeWidth(1);

        btnHMC.setTextColor(getResources().getColor(R.color.Score100));
        btnHMC.setStrokeColorResource(R.color.Score100);
        btnHMC.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMC.setStrokeWidth(1);

        btnHMD.setTextColor(getResources().getColor(R.color.Score100));
        btnHMD.setStrokeColorResource(R.color.Score100);
        btnHMD.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMD.setStrokeWidth(1);

        btnHME.setTextColor(getResources().getColor(R.color.Score100));
        btnHME.setStrokeColorResource(R.color.Score100);
        btnHME.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHME.setStrokeWidth(1);

        btnHMF.setTextColor(getResources().getColor(R.color.Score100));
        btnHMF.setStrokeColorResource(R.color.Score100);
        btnHMF.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMF.setStrokeWidth(1);

        btnHMG.setTextColor(getResources().getColor(R.color.Score100));
        btnHMG.setStrokeColorResource(R.color.Score100);
        btnHMG.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMG.setStrokeWidth(1);

        btnHMH.setTextColor(getResources().getColor(R.color.Score100));
        btnHMH.setStrokeColorResource(R.color.Score100);
        btnHMH.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMH.setStrokeWidth(1);

        btnHMI.setTextColor(getResources().getColor(R.color.Score100));
        btnHMI.setStrokeColorResource(R.color.Score100);
        btnHMI.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMI.setStrokeWidth(1);

        btnHMJ.setTextColor(getResources().getColor(R.color.Score100));
        btnHMJ.setStrokeColorResource(R.color.Score100);
        btnHMJ.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMJ.setStrokeWidth(1);

        btnHMK.setTextColor(getResources().getColor(R.color.Score100));
        btnHMK.setStrokeColorResource(R.color.Score100);
        btnHMK.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMK.setStrokeWidth(1);

        btnHML.setTextColor(getResources().getColor(R.color.Score100));
        btnHML.setStrokeColorResource(R.color.Score100);
        btnHML.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHML.setStrokeWidth(1);

        btnHMM.setTextColor(getResources().getColor(R.color.Score100));
        btnHMM.setStrokeColorResource(R.color.Score100);
        btnHMM.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMM.setStrokeWidth(1);

        btnHMN.setTextColor(getResources().getColor(R.color.Score100));
        btnHMN.setStrokeColorResource(R.color.Score100);
        btnHMN.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMN.setStrokeWidth(1);

        btnHMO.setTextColor(getResources().getColor(R.color.Score100));
        btnHMO.setStrokeColorResource(R.color.Score100);
        btnHMO.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMO.setStrokeWidth(1);

        btnHMP.setTextColor(getResources().getColor(R.color.Score100));
        btnHMP.setStrokeColorResource(R.color.Score100);
        btnHMP.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMP.setStrokeWidth(1);

        btnHMQ.setTextColor(getResources().getColor(R.color.Score100));
        btnHMQ.setStrokeColorResource(R.color.Score100);
        btnHMQ.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMQ.setStrokeWidth(1);

        btnHMR.setTextColor(getResources().getColor(R.color.Score100));
        btnHMR.setStrokeColorResource(R.color.Score100);
        btnHMR.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMR.setStrokeWidth(1);

        btnHMS.setTextColor(getResources().getColor(R.color.Score100));
        btnHMS.setStrokeColorResource(R.color.Score100);
        btnHMS.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMS.setStrokeWidth(1);

        btnHMT.setTextColor(getResources().getColor(R.color.Score100));
        btnHMT.setStrokeColorResource(R.color.Score100);
        btnHMT.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMT.setStrokeWidth(1);

        btnHMU.setTextColor(getResources().getColor(R.color.Score100));
        btnHMU.setStrokeColorResource(R.color.Score100);
        btnHMU.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMU.setStrokeWidth(1);

        btnHMV.setTextColor(getResources().getColor(R.color.Score100));
        btnHMV.setStrokeColorResource(R.color.Score100);
        btnHMV.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMV.setStrokeWidth(1);

        btnHMW.setTextColor(getResources().getColor(R.color.Score100));
        btnHMW.setStrokeColorResource(R.color.Score100);
        btnHMW.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMW.setStrokeWidth(1);

        btnHMX.setTextColor(getResources().getColor(R.color.Score100));
        btnHMX.setStrokeColorResource(R.color.Score100);
        btnHMX.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMX.setStrokeWidth(1);

        btnHMY.setTextColor(getResources().getColor(R.color.Score100));
        btnHMY.setStrokeColorResource(R.color.Score100);
        btnHMY.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMY.setStrokeWidth(1);

        btnHMZ.setTextColor(getResources().getColor(R.color.Score100));
        btnHMZ.setStrokeColorResource(R.color.Score100);
        btnHMZ.setBackgroundColor(getResources().getColor(R.color.position1));
        btnHMZ.setStrokeWidth(1);


    }

    private void verifyBest(){
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        //setar cabeçalho-recorde
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myBestRef = firebaseDatabase.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("games").child("hangmanGame").child("bestScore");
        myBestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    if (lgCurrentPoints>0){
                        lgCurrentPoints = Integer.parseInt(dataSnapshot.getValue().toString()) + lgCurrentPoints;
                            myBestRef.setValue(Long.parseLong(String.valueOf(lgCurrentPoints)));

                    }

                    txtTotalPoint.setText(String.valueOf(dataSnapshot.getValue()));
                }else {
                    //Toast.makeText(getApplicationContext(), (Integer) dataSnapshot.getValue(), Toast.LENGTH_LONG).show();
                    int iBest = 0;
                    myBestRef.setValue(iBest);
                    txtTotalPoint.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    };
}
