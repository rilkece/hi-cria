package com.cria.agora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameBuildWords extends AppCompatActivity {

    private MaterialButton btnWBL1, btnWBL2, btnWBL3, btnWBL4, btnWBL5, btnWBL6, btnWBL7, btnWBL8, btnWBL9, btnWBL10, btnWBL11, btnWBL12, btnWBL13, btnWBL14, btnWBL15, btnWBL16, btnWBVerify, btnWBStart, btnWBBackspace, btnWBPerf, btnWBReturn;
    private TextView txtWBCurrentWord, txtWBMyBest, txtWBPoints, txtWBTime, txtAnswers, txtteste;

    private DatabaseReference myBestRef;

    private boolean gameIsRunning, btnIsClicked1, btnIsClicked2, btnIsClicked3, btnIsClicked4, btnIsClicked5, btnIsClicked6, btnIsClicked7, btnIsClicked8, btnIsClicked9, btnIsClicked10, btnIsClicked11, btnIsClicked12, btnIsClicked13, btnIsClicked14, btnIsClicked15, btnIsClicked16;

    private String seqButtons;

    private long currentPoints;
    private int howManyWords;

    private CountDownTimer myCount;
    private List<String> listAnswers;

    private SoundPool spBuildWords;
    private int backSound, matchSound, failSound, backSpaceSound, clickSound, successSound, looseSound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_build_words);

        //declarar elementos
        btnWBL1 = findViewById(R.id.btnWBLetter1);
        btnWBL2 = findViewById(R.id.btnWBLetter2);
        btnWBL3 = findViewById(R.id.btnWBLetter3);
        btnWBL4 = findViewById(R.id.btnWBLetter4);
        btnWBL5 = findViewById(R.id.btnWBLetter5);
        btnWBL6 = findViewById(R.id.btnWBLetter6);
        btnWBL7 = findViewById(R.id.btnWBLetter7);
        btnWBL8 = findViewById(R.id.btnWBLetter8);
        btnWBL9 = findViewById(R.id.btnWBLetter9);
        btnWBL10 = findViewById(R.id.btnWBLetter10);
        btnWBL11 = findViewById(R.id.btnWBLetter11);
        btnWBL12 = findViewById(R.id.btnWBLetter12);
        btnWBL13 = findViewById(R.id.btnWBLetter13);
        btnWBL14 = findViewById(R.id.btnWBLetter14);
        btnWBL15 = findViewById(R.id.btnWBLetter15);
        btnWBL16 = findViewById(R.id.btnWBLetter16);
        btnWBBackspace = findViewById(R.id.btnWBBackspace);
        btnWBStart = findViewById(R.id.btnWBStart);
        btnWBVerify = findViewById(R.id.btnWBVerify);
        btnWBPerf = findViewById(R.id.btnWBPerformance);
        btnWBReturn = findViewById(R.id.btnWBBack);

        txtWBCurrentWord = findViewById(R.id.txtWordBuilt);txtWBCurrentWord.setText("");
        txtWBMyBest = findViewById(R.id.txtWBMyBest);
        txtWBPoints = findViewById(R.id.txtWBPoints);
        txtWBTime = findViewById(R.id.txtWBTime);
        txtAnswers = findViewById(R.id.txtWBanswers);
        txtAnswers.setText("");
        //Toast.makeText(getApplicationContext(), txtAnswers.getText().toString(), Toast.LENGTH_SHORT).show();

        listAnswers = new ArrayList<>();
        howManyWords = 0;


        gameIsRunning=false;
        btnIsClicked1 = true;
        btnIsClicked2 = true;
        btnIsClicked3 = true;
        btnIsClicked4 = true;
        btnIsClicked5 = true;
        btnIsClicked6 = true;
        btnIsClicked7 = true;
        btnIsClicked8 = true;
        btnIsClicked9 = true;
        btnIsClicked10 = true;
        btnIsClicked11 = true;
        btnIsClicked12 = true;
        btnIsClicked13 = true;
        btnIsClicked14 = true;
        btnIsClicked15 = true;
        btnIsClicked16 = true;

        currentPoints = 0;
        seqButtons = "";

        //declarar elementos--construir soundPool pra versões antes e depois do Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            spBuildWords = new SoundPool.Builder()
                    .setMaxStreams(7)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            spBuildWords = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
            // Toast.makeText(getApplicationContext(), "oioioi", Toast.LENGTH_SHORT).show();
        }

        //declarar elementos--setar sons básicos
        backSound = spBuildWords.load(this, R.raw.back_puzzle_dreams_3, 1);
        matchSound = spBuildWords.load(this, R.raw.right, 1);
        failSound = spBuildWords.load(this, R.raw.wrong, 1);
        backSpaceSound = spBuildWords.load(this, R.raw.woosh, 1);
        clickSound = spBuildWords.load(this, R.raw.click, 1);
        successSound = spBuildWords.load(this, R.raw.success, 1);
        looseSound = spBuildWords.load(this, R.raw.loose, 1);




        //setar cabeçalho - botão voltar
        btnWBReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainGames.class);
                startActivity(i);
                finish();
            }
        });

        //função setar recorde
        verifyBest();

        //função setar função dos botões
        buttonsFunctions();

        //setar botar start
        btnWBStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spBuildWords.stop(1);
                spBuildWords.play(backSound, 1, 1, 1, -1, 1);
                txtWBTime.setText("0");
                txtWBPoints.setText("0");
                txtWBCurrentWord.setText("");
                currentPoints=0;
                seqButtons = "";
                txtAnswers.setText("");
                listAnswers = new ArrayList<>();
                howManyWords = 0;
                verifyBest();
                shufleLetters();
                buttonsFunctions();
                conter();

            }
        });

        //setar botão verify
        btnWBVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtWBCurrentWord.getText()==""){

                }else {

                    verifyMatch();

                }

            }
        });

        //setar botão backspace
        btnWBBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spBuildWords.play(backSpaceSound, 1, 1, 0, 0, 1);

                String textBackspace = txtWBCurrentWord.getText().toString();
                if (textBackspace.length()>0){
                    textBackspace = textBackspace.substring(0, textBackspace.length() - 1);
                    txtWBCurrentWord.setText(textBackspace);
                    rebornLastButton();
                   // Toast.makeText(getApplicationContext(), textBackspace, Toast.LENGTH_LONG).show();
                }else {
                   // Toast.makeText(getApplicationContext(),"oi"+ textBackspace, Toast.LENGTH_LONG).show();
                }

            }
        });

        //setar botão performance
        btnWBPerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GamePerformance.class);
                startActivity(intent);
                GamePerformance.stgGame = "16 LETTERS";
                GamePerformance.stgNameBest = "bestScore";
                GamePerformance.stgNameGame = "BuildWordGame";
            }
        });

    }

    private void rebornLastButton() {

        String lastButton = seqButtons.substring(seqButtons.length()-1);
        if (lastButton.equals("x")){
            lastButton = seqButtons.substring(seqButtons.length()-3,seqButtons.length()-1);
            seqButtons = seqButtons.substring(0, seqButtons.length()-3);
           // Toast.makeText(getApplicationContext(),"oi"+lastButton+" + "+seqButtons, Toast.LENGTH_LONG).show();
        }else {
            lastButton = seqButtons.substring(seqButtons.length()-1);
            seqButtons = seqButtons.substring(0, seqButtons.length()-1);
          //  Toast.makeText(getApplicationContext(),lastButton+" + "+seqButtons, Toast.LENGTH_LONG).show();
        }


       // Toast.makeText(getApplicationContext(),lastButton+" + "+seqButtons, Toast.LENGTH_LONG).show();

        switch (Integer.parseInt(lastButton)){
            case 1:
                btnWBL1.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL1.setStrokeColorResource(R.color.startblue);
                btnWBL1.setStrokeWidth(1);
                btnIsClicked1=false;
                break;
            case 2:
                btnWBL2.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL2.setStrokeColorResource(R.color.startblue);
                btnWBL2.setStrokeWidth(1);
                btnIsClicked2=false;

                break;
            case 3:
                btnWBL3.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL3.setStrokeColorResource(R.color.startblue);
                btnWBL3.setStrokeWidth(1);
                btnIsClicked3=false;

                break;
            case 4:
                btnWBL4.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL4.setStrokeColorResource(R.color.startblue);
                btnWBL4.setStrokeWidth(1);
                btnIsClicked4=false;

                break;
            case 5:
                btnWBL5.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL5.setStrokeColorResource(R.color.startblue);
                btnWBL5.setStrokeWidth(1);
                btnIsClicked5=false;

                break;
            case 6:
                btnWBL6.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL6.setStrokeColorResource(R.color.startblue);
                btnWBL6.setStrokeWidth(1);
                btnIsClicked6=false;

                break;
            case 7:
                btnWBL7.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL7.setStrokeColorResource(R.color.startblue);
                btnWBL7.setStrokeWidth(1);
                btnIsClicked7=false;

                break;
            case 8:
                btnWBL8.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL8.setStrokeColorResource(R.color.startblue);
                btnWBL8.setStrokeWidth(1);
                btnIsClicked8=false;

                break;
            case 9:
                btnWBL9.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL9.setStrokeColorResource(R.color.startblue);
                btnWBL9.setStrokeWidth(1);
                btnIsClicked9=false;

                break;
            case 10:
                btnWBL10.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL10.setStrokeColorResource(R.color.startblue);
                btnWBL10.setStrokeWidth(1);
                btnIsClicked10=false;

                break;
            case 11:
                btnWBL11.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL11.setStrokeColorResource(R.color.startblue);
                btnWBL11.setStrokeWidth(1);
                btnIsClicked11=false;

                break;
            case 12:
                btnWBL12.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL12.setStrokeColorResource(R.color.startblue);
                btnWBL12.setStrokeWidth(1);
                btnIsClicked12=false;

                break;
            case 13:
                btnWBL13.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL13.setStrokeColorResource(R.color.startblue);
                btnWBL13.setStrokeWidth(1);
                btnIsClicked13=false;

                break;
            case 14:
                btnWBL14.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL14.setStrokeColorResource(R.color.startblue);
                btnWBL14.setStrokeWidth(1);
                btnIsClicked14=false;

                break;
            case 15:
                btnWBL15.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL15.setStrokeColorResource(R.color.startblue);
                btnWBL15.setStrokeWidth(1);
                btnIsClicked15=false;
                break;
            case 16:
                btnWBL16.setTextColor(getResources().getColor(R.color.startblue));
                btnWBL16.setStrokeColorResource(R.color.startblue);
                btnWBL16.setStrokeWidth(1);
                btnIsClicked16=false;
                break;

        }
    }

    private void conter() {
        if(!(myCount==null)){
            myCount.cancel();
        }
        myCount = new CountDownTimer(200000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                txtWBTime.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                verifyBest();
                final SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
                spBuildWords.play(successSound, 1, 1, 0, 0, 1);
                // make the screen unclickable
                //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(GameBuildWords.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Your score was "+currentPoints+". Congratulations!")
                        .setTitle("Time is Over");
                builder.setCancelable(false);
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // make the screen clickable
                        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                });

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }.start();
    }

    private void verifyMatch()  {

            if (listAnswers.contains(txtWBCurrentWord.getText().toString())){
                Toast.makeText(getApplicationContext(), "already typed.", Toast.LENGTH_SHORT).show();


            }else {
                seqButtons = "";
                buttonsFunctions();
                notClickedButtons();
                String currentWord = "english_words_"+txtWBCurrentWord.getText().toString().substring(0, 1).toLowerCase();
                int resID = getResources().getIdentifier(currentWord, "array", getPackageName());


                String[] arrayNames = getResources().getStringArray(resID);


                List<String> listNames =Arrays.asList(arrayNames);
                if (listNames.contains(txtWBCurrentWord.getText().toString().toLowerCase())){
                    spBuildWords.play(matchSound, 1, 1, 0, 0, 1);
                    int qtdLetters = txtWBCurrentWord.getText().toString().length();
                    currentPoints = currentPoints+qtdLetters;
                    txtWBPoints.setText(String.valueOf(currentPoints));

                    listAnswers.add(txtWBCurrentWord.getText().toString());
                    String textAns = listAnswers.get(howManyWords).replace("[", "").replace("]", "")+"\n"+txtAnswers.getText();
                    howManyWords=howManyWords+1;
                    txtAnswers.setText(textAns);
                    txtWBCurrentWord.setText("");



                    // Toast.makeText(getApplicationContext(), "deu match", Toast.LENGTH_SHORT).show();
                }else {
                    spBuildWords.play(failSound, 1, 1, 0, 0, 1);
                    txtWBCurrentWord.setText("");
                    //Toast.makeText(getApplicationContext(), "inventou", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), txtWBCurrentWord.getText().toString(), Toast.LENGTH_SHORT).show();
            }



    }

    private void buttonsFunctions() {
        strokeColorsOriginal();
        btnWBL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked1){
                    String currentText = txtWBCurrentWord.getText()+btnWBL1.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL1.setStrokeWidth(3);
                    btnWBL1.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL1.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked1=true;
                    seqButtons=seqButtons+"1";
                }else {

                }
            }
        });
        btnWBL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked2){
                    String currentText = txtWBCurrentWord.getText()+btnWBL2.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL2.setStrokeWidth(3);
                    btnWBL2.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL2.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked2=true;
                    seqButtons=seqButtons+"2";
                }else {

                }
            }
        });
        btnWBL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked3){
                    String currentText = txtWBCurrentWord.getText()+btnWBL3.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL3.setStrokeWidth(3);
                    btnWBL3.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL3.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked3=true;
                    seqButtons=seqButtons+"3";
                }else {

                }
            }
        });
        btnWBL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked4){
                    String currentText = txtWBCurrentWord.getText()+btnWBL4.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL4.setStrokeWidth(3);
                    btnWBL4.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL4.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked4=true;
                    seqButtons=seqButtons+"4";
                }else {

                }
            }
        });
        btnWBL5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked5){
                    String currentText = txtWBCurrentWord.getText()+btnWBL5.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL5.setStrokeWidth(3);
                    btnWBL5.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL5.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked5=true;
                    seqButtons=seqButtons+"5";
                }else {

                }
            }
        });
        btnWBL6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked6){
                    String currentText = txtWBCurrentWord.getText()+btnWBL6.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL6.setStrokeWidth(3);
                    btnWBL6.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL6.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked6=true;
                    seqButtons=seqButtons+"6";
                }else {

                }
            }
        });
        btnWBL7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked7){
                    String currentText = txtWBCurrentWord.getText()+btnWBL7.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL7.setStrokeWidth(3);
                    btnWBL7.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL7.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked7=true;
                    seqButtons=seqButtons+"7";
                }else {

                }
            }
        });
        btnWBL8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked8){
                    String currentText = txtWBCurrentWord.getText()+btnWBL8.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL8.setStrokeWidth(3);
                    btnWBL8.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL8.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked8=true;
                    seqButtons=seqButtons+"8";
                }else {

                }
            }
        });
        btnWBL9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked9){
                    String currentText = txtWBCurrentWord.getText()+btnWBL9.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL9.setStrokeWidth(3);
                    btnWBL9.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL9.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked9=true;
                    seqButtons=seqButtons+"9";
                }else {

                }
            }
        });
        btnWBL10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked10){
                    String currentText = txtWBCurrentWord.getText()+btnWBL10.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL10.setStrokeWidth(3);
                    btnWBL10.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL10.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked10=true;
                    seqButtons=seqButtons+"10x";
                }else {

                }
            }
        });
        btnWBL11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked11){
                    String currentText = txtWBCurrentWord.getText()+btnWBL11.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL11.setStrokeWidth(3);
                    btnWBL11.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL11.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked11=true;
                    seqButtons=seqButtons+"11x";
                }else {

                }
            }
        });
        btnWBL12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked12){
                    String currentText = txtWBCurrentWord.getText()+btnWBL12.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL12.setStrokeWidth(3);
                    btnWBL12.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL12.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked12=true;
                    seqButtons=seqButtons+"12x";
                }else {

                }
            }
        });
        btnWBL13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked13){
                    String currentText = txtWBCurrentWord.getText()+btnWBL13.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL13.setStrokeWidth(3);
                    btnWBL13.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL13.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked13=true;
                    seqButtons=seqButtons+"13x";
                }else {

                }
            }
        });
        btnWBL14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked14) {
                    String currentText = txtWBCurrentWord.getText()+btnWBL14.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL14.setStrokeWidth(3);
                    btnWBL14.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL14.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked14=true;
                    seqButtons=seqButtons+"14x";
                }else {

                }
            }
        });
        btnWBL15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked15){
                    String currentText = txtWBCurrentWord.getText()+btnWBL15.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL15.setStrokeWidth(3);
                    btnWBL15.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL15.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked15=true;
                    seqButtons=seqButtons+"15x";
                }else {

                }
            }
        });
        btnWBL16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnIsClicked16){
                    String currentText = txtWBCurrentWord.getText()+btnWBL16.getText().toString();
                    txtWBCurrentWord.setText(currentText);
                    btnWBL16.setStrokeWidth(3);
                    btnWBL16.setTextColor(getResources().getColor(R.color.fui_bgAnonymous));
                    btnWBL16.setStrokeColorResource(R.color.fui_bgAnonymous);
                    btnIsClicked16=true;
                    seqButtons=seqButtons+"16x";
                }else {

                }
            }
        });

    }

    private void strokeColorsOriginal() {
        btnWBL1.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL1.setStrokeColorResource(R.color.startblue);
        btnWBL1.setStrokeWidth(1);

        btnWBL2.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL2.setStrokeColorResource(R.color.startblue);
        btnWBL2.setStrokeWidth(1);

        btnWBL3.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL3.setStrokeColorResource(R.color.startblue);
        btnWBL3.setStrokeWidth(1);

        btnWBL4.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL4.setStrokeColorResource(R.color.startblue);
        btnWBL4.setStrokeWidth(1);

        btnWBL5.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL5.setStrokeColorResource(R.color.startblue);
        btnWBL5.setStrokeWidth(1);

        btnWBL6.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL6.setStrokeColorResource(R.color.startblue);
        btnWBL6.setStrokeWidth(1);

        btnWBL7.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL7.setStrokeColorResource(R.color.startblue);
        btnWBL7.setStrokeWidth(1);

        btnWBL8.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL8.setStrokeColorResource(R.color.startblue);
        btnWBL8.setStrokeWidth(1);

        btnWBL9.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL9.setStrokeColorResource(R.color.startblue);
        btnWBL9.setStrokeWidth(1);

        btnWBL10.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL10.setStrokeColorResource(R.color.startblue);
        btnWBL10.setStrokeWidth(1);

        btnWBL11.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL11.setStrokeColorResource(R.color.startblue);
        btnWBL11.setStrokeWidth(1);

        btnWBL12.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL12.setStrokeColorResource(R.color.startblue);
        btnWBL12.setStrokeWidth(1);

        btnWBL13.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL13.setStrokeColorResource(R.color.startblue);
        btnWBL13.setStrokeWidth(1);

        btnWBL14.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL14.setStrokeColorResource(R.color.startblue);
        btnWBL14.setStrokeWidth(1);

        btnWBL15.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL15.setStrokeColorResource(R.color.startblue);
        btnWBL15.setStrokeWidth(1);

        btnWBL16.setTextColor(getResources().getColor(R.color.startblue));
        btnWBL16.setStrokeColorResource(R.color.startblue);
        btnWBL16.setStrokeWidth(1);




    }

    private void verifyBest() {


        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        //setar cabeçalho-recorde
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myBestRef = firebaseDatabase.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("games").child("BuildWordGame").child("bestScore");
        myBestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    if (currentPoints>0){
                        if (currentPoints>Integer.parseInt(dataSnapshot.getValue().toString())){
                            //Toast.makeText(getApplicationContext(),currentPoints+" + "+dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                            myBestRef.setValue(currentPoints);
                        }
                    }


                    txtWBMyBest.setText(String.valueOf(dataSnapshot.getValue()));
                }else {
                    //Toast.makeText(getApplicationContext(), (Integer) dataSnapshot.getValue(), Toast.LENGTH_LONG).show();
                    int iBest = 0;
                    myBestRef.setValue(iBest);
                    txtWBMyBest.setText(String.valueOf(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void shufleLetters() {
        String[] letters = getResources().getStringArray(R.array.alphabet);
        List<String> arrayLetters = Arrays.asList(letters);
        Collections.shuffle(arrayLetters);

        //SETAR LETRAS DOS BOTÕES
        btnWBL1.setText(arrayLetters.get(0));
        btnWBL2.setText(arrayLetters.get(1));
        btnWBL3.setText(arrayLetters.get(2));
        btnWBL4.setText(arrayLetters.get(3));
        btnWBL5.setText(arrayLetters.get(4));
        btnWBL6.setText(arrayLetters.get(5));
        btnWBL7.setText(arrayLetters.get(6));
        btnWBL8.setText(arrayLetters.get(7));
        btnWBL9.setText(arrayLetters.get(8));
        btnWBL10.setText(arrayLetters.get(9));
        btnWBL11.setText(arrayLetters.get(10));
        btnWBL12.setText(arrayLetters.get(11));
        btnWBL13.setText(arrayLetters.get(12));
        btnWBL14.setText(arrayLetters.get(13));
        btnWBL15.setText(arrayLetters.get(14));
        btnWBL16.setText(arrayLetters.get(15));

            notClickedButtons();



    }

    private void notClickedButtons() {
        //setar função dos botões
        btnIsClicked1 = false;
        btnIsClicked2 = false;
        btnIsClicked3 = false;
        btnIsClicked4 = false;
        btnIsClicked5 = false;
        btnIsClicked6 = false;
        btnIsClicked7 = false;
        btnIsClicked8 = false;
        btnIsClicked9 = false;
        btnIsClicked10 = false;
        btnIsClicked11 = false;
        btnIsClicked12 = false;
        btnIsClicked13 = false;
        btnIsClicked14 = false;
        btnIsClicked15 = false;
        btnIsClicked16 = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        spBuildWords.autoPause();
        spBuildWords.release();
       // Toast.makeText(getApplicationContext(), "DESTROY", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        spBuildWords.autoPause();
        spBuildWords.release();
        this.finish();
       // Toast.makeText(getApplicationContext(), "STOP", Toast.LENGTH_SHORT).show();
    }
}
