package com.cria.agora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;

public class PlayMemory extends AppCompatActivity {

    private ImageView imgMemory01, imgMemory02, imgMemory03, imgMemory04, imgMemory05, imgMemory06, imgMemory07, imgMemory08, imgMemory09, imgMemory10, imgMemory11, imgMemory12, imgMemory13, imgMemory14, imgMemory15, imgMemory16, imgMemory17, imgMemory18, imgMemory19, imgMemory20;

    private List<String> stringList;
    private List<String> stringSelect;
    private int stringNumber;
    private int childrenCount;

    private int imgTurn, lastImgTurn, antelastImgTurn, lastUnmatch, antelasteUnmatch, qtdMatches;

    private boolean lastWasMatch;

    private String cardName1;
    private String cardName2;


    //cabecalho
    private TextView txtCurrentPoints;
    private TextView txtMyBest;
    private TextView txtTimer;

    private long currentPoints, totalPoints;

    private CountDownTimer myCount;

    //user infos
    private DatabaseReference myRef;
    private DatabaseReference myBestRef;

    // sounds variables
    private SoundPool spInterface, spCards;
    private int soundClick, soundMatch, soundUnmatch, soundVictory, soundLoose;
    private int sdCard01, sdCard02, sdCard03, sdCard04, sdCard05, sdCard06, sdCard07, sdCard08, sdCard09, sdCard10, sdCard11, sdCard12, sdCard13, sdCard14, sdCard15, sdCard16, sdCard17, sdCard18, sdCard19, sdCard20;



    private String stgThings, stgMatch, stgBestType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //declarar elementos--cards
        setContentView(R.layout.activity_play_memory);


        imgMemory01 = findViewById(R.id.imageMemory01);
        imgMemory02 = findViewById(R.id.imageMemory02);
        imgMemory03 = findViewById(R.id.imageMemory03);
        imgMemory04 = findViewById(R.id.imageMemory04);
        imgMemory05 = findViewById(R.id.imageMemory05);
        imgMemory06 = findViewById(R.id.imageMemory06);
        imgMemory07 = findViewById(R.id.imageMemory07);
        imgMemory08 = findViewById(R.id.imageMemory08);
        imgMemory09 = findViewById(R.id.imageMemory09);
        imgMemory10 = findViewById(R.id.imageMemory10);
        imgMemory11 = findViewById(R.id.imageMemory11);
        imgMemory12 = findViewById(R.id.imageMemory12);
        imgMemory13 = findViewById(R.id.imageMemory13);
        imgMemory14 = findViewById(R.id.imageMemory14);
        imgMemory15 = findViewById(R.id.imageMemory15);
        imgMemory16 = findViewById(R.id.imageMemory16);
        imgMemory17 = findViewById(R.id.imageMemory17);
        imgMemory18 = findViewById(R.id.imageMemory18);
        imgMemory19 = findViewById(R.id.imageMemory19);
        imgMemory20 = findViewById(R.id.imageMemory20);

        //declarar elementos--construir soundPool pra versões antes e depois do Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            spCards = new SoundPool.Builder()
                    .setMaxStreams(20)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            spCards = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
           // Toast.makeText(getApplicationContext(), "oioioi", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            spInterface = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            spInterface = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        //declarar elementos--setar sons básicos
        soundClick = spInterface.load(this, R.raw.click, 1);
        soundMatch = spInterface.load(this, R.raw.correct, 1);
        soundUnmatch = spInterface.load(this, R.raw.wrong, 1);
        soundVictory = spInterface.load(this, R.raw.victory, 1);
        soundLoose = spInterface.load(this, R.raw.loose, 1);


        //declarar elementos--setar tipos de cards (Nomes, expressões ou frases) e match (son ou nomes)
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference refstgMatch = firebaseDatabase.getReference("games").child("memory").child("stgMatch");
        refstgMatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stgMatch= Objects.requireNonNull(dataSnapshot.getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                stgMatch = "sound_";
                Toast.makeText(getApplicationContext(), "firebase connection problem, set to sound_", Toast.LENGTH_SHORT).show();

            }
        });
        stgMatch = "sound_";
        if (GameMatching.intMatch==1){
            stgThings = "thing_";
            stgBestType = "Names";
        }else if(GameMatching.intMatch==2){
            stgThings = "expression_";
            stgBestType = "Expressions";
        }else if(GameMatching.intMatch==3){
            stgThings = "phrase_";
            stgBestType = "Phrases";
        }


        //declarar elementos--setar pontuação correta
        txtCurrentPoints = findViewById(R.id.txtCurrentPointsMemory);
        txtMyBest = findViewById(R.id.txtMyBestMemory);
        txtTimer = findViewById(R.id.txtTimerMemory);
        currentPoints=0;
        totalPoints=0;

        stringList = new ArrayList<String>();
        stringSelect = new ArrayList<String>();
        cardName1="";
        cardName2="";
        lastUnmatch=0;
        antelasteUnmatch=0;
        qtdMatches=0;

        lastWasMatch=false;

        //declarar elementos--setar recorde
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        myBestRef = firebaseDatabase.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("games").child("matchingGame").child("bestScore"+stgBestType);
        myBestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    txtMyBest.setText(String.valueOf(dataSnapshot.getValue()));
                }else {
                    int iBest = 0;
                    myBestRef.setValue(iBest);
                    txtMyBest.setText(String.valueOf(0));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageButton shufleBtn = findViewById(R.id.btn_memory);
        shufleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), String.valueOf(GameMatching.matchType), Toast.LENGTH_SHORT).show();
                spInterface.release();
                txtTimer.setText("0");
                txtCurrentPoints.setText("0");
                shufleCards();
                cardName1="";
                cardName2="";
                lastUnmatch=0;
                antelasteUnmatch=0;
                lastWasMatch=false;
                qtdMatches=0;


            }
        });
        

        //escutar single event do firebase pra contar a quantidade ce children, já que o single event é acionado depois do último child event

        myRef = firebaseDatabase.getReference("games").child("memory").child("arrayNames");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childrenCount = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
               // Toast.makeText(getApplicationContext(), "Total de cartas: "+childrenCount, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //zerar turns
        noTurns();
        //embaralhar cartas
       // shufleCards();

        
    }

    private void noTurns() {
        imgTurn = 0;
        lastImgTurn = 0;
    }

    private void shufleCards() {
        flipAllCards();
        //zerar contador de children
        stringNumber = 0;
        imgTurn = 0;

        //embaralhar cards
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                stringList.add(Objects.requireNonNull(dataSnapshot.getKey()));
                //Toast.makeText(getApplicationContext(), stringList.get(stringNumber), Toast.LENGTH_SHORT).show();
                stringNumber = stringNumber +1;
                //Toast.makeText(getApplicationContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                if (stringNumber == childrenCount){


                    stringSelect.clear();
                    Collections.shuffle(stringList);
                    stringSelect.add(0, stgThings+stringList.get(0));
                    stringSelect.add(1, stgMatch+stringList.get(0));
                    stringSelect.add(2, stgThings+stringList.get(1));
                    stringSelect.add(3, stgMatch+stringList.get(1));
                    stringSelect.add(4, stgThings+stringList.get(2));
                    stringSelect.add(5, stgMatch+stringList.get(2));
                    stringSelect.add(6, stgThings+stringList.get(3));
                    stringSelect.add(7, stgMatch+stringList.get(3));
                    stringSelect.add(8, stgThings+stringList.get(4));
                    stringSelect.add(9, stgMatch+stringList.get(4));
                    stringSelect.add(10, stgThings+stringList.get(5));
                    stringSelect.add(11, stgMatch+stringList.get(5));
                    stringSelect.add(12, stgThings+stringList.get(6));
                    stringSelect.add(13, stgMatch+stringList.get(6));
                    stringSelect.add(14, stgThings+stringList.get(7));
                    stringSelect.add(15, stgMatch+stringList.get(7));
                    stringSelect.add(16, stgThings+stringList.get(8));
                    stringSelect.add(17, stgMatch+stringList.get(8));
                    stringSelect.add(18, stgThings+stringList.get(9));
                    stringSelect.add(19, stgMatch+stringList.get(9));
                    Collections.shuffle(stringSelect);

                    //setar sound das cartas
                    soundCards();

                    stringList.clear();
                    //Toast.makeText(getApplicationContext(), "é iguaasdsadasl "+stringNumber, Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        if(!(myCount==null)){
            myCount.cancel();
        }
        myCount = new CountDownTimer(300000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                spInterface.play(soundLoose, 1, 1, 0, 0, 1);
                //Toast.makeText(getApplicationContext(), "TEMINOU", Toast.LENGTH_LONG).show();
                //função de delay
     /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayMemory.this);

// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Please, try faster next time!")
                        .setTitle("Time is Over");
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent i = new Intent(getApplicationContext(), PlayMemory.class);
                        startActivity(i);
                        finish();
                    }
                });

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }.start();


    }

    private void soundCards() {
        spCards.release();
       spCards=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            spCards = new SoundPool.Builder()
                    .setMaxStreams(20)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            spCards = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        }

        //setar sons para os itens que representam som
        if (stringSelect.get(0).contains("sound_")){
            String iSd = stringSelect.get(0).replace("sound_", "thing_");
            sdCard01 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {

            sdCard01 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(1).contains("sound_")){
            String iSd = stringSelect.get(1).replace("sound_", "thing_");
            sdCard02 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard02 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(2).contains("sound_")){
            String iSd = stringSelect.get(2).replace("sound_", "thing_");
            sdCard03 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard03 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(3).contains("sound_")){
            String iSd = stringSelect.get(3).replace("sound_", "thing_");
            sdCard04 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard04 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(4).contains("sound_")){
            String iSd = stringSelect.get(4).replace("sound_", "thing_");
            sdCard05 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard05 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(5).contains("sound_")){
            String iSd = stringSelect.get(5).replace("sound_", "thing_");
            sdCard06 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard06 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(6).contains("sound_")){
            String iSd = stringSelect.get(6).replace("sound_", "thing_");
            sdCard07 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard07 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(7).contains("sound_")){
            String iSd = stringSelect.get(7).replace("sound_", "thing_");
            sdCard08 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard08 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(8).contains("sound_")){
            String iSd = stringSelect.get(8).replace("sound_", "thing_");
            sdCard09 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard09 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(9).contains("sound_")){
            String iSd = stringSelect.get(9).replace("sound_", "thing_");
            sdCard10 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard10 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(10).contains("sound_")){
            String iSd = stringSelect.get(10).replace("sound_", "thing_");
            sdCard11 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard11 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(11).contains("sound_")){
            String iSd = stringSelect.get(11).replace("sound_", "thing_");
            sdCard12 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard12 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(12).contains("sound_")){
            String iSd = stringSelect.get(12).replace("sound_", "thing_");
            sdCard13 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard13 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(13).contains("sound_")){
            String iSd = stringSelect.get(13).replace("sound_", "thing_");
            sdCard14 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard14 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(14).contains("sound_")){
            String iSd = stringSelect.get(14).replace("sound_", "thing_");
            sdCard15 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard15 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(15).contains("sound_")){
            String iSd = stringSelect.get(15).replace("sound_", "thing_");
            sdCard16 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard16 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(16).contains("sound_")){
            String iSd = stringSelect.get(16).replace("sound_", "thing_");
            sdCard17 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard17 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(17).contains("sound_")){
            String iSd = stringSelect.get(17).replace("sound_", "thing_");
            sdCard18 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard18 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(18).contains("sound_")){
            String iSd = stringSelect.get(18).replace("sound_", "thing_");
            sdCard19 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard19 = spCards.load(this, R.raw.click, 1);
        }
        if (stringSelect.get(19).contains("sound_")){
            String iSd = stringSelect.get(19).replace("sound_", "thing_");
            sdCard20 = spCards.load(this, getResources().getIdentifier(iSd, "raw", getPackageName()), 1);

        }else {
            sdCard20 = spCards.load(this, R.raw.click, 1);
        }

        //setar figuuras e ação dos cards
        //Toast.makeText(getApplicationContext(), "flip sound", Toast.LENGTH_SHORT).show();
        flipCards();

    }

    private void flipAllCards() {
        //card 01
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory01.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory01.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa2.start();
            }
        });
        oa1.start();
        //card 02
        final ObjectAnimator oa3 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 1f, 0f);
        final ObjectAnimator oa4 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 0f, 1f);
        oa3.setInterpolator(new DecelerateInterpolator());
        oa4.setInterpolator(new AccelerateDecelerateInterpolator());
        oa3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory02.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory02.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa4.start();
            }
        });
        oa3.start();
//card 03
        final ObjectAnimator oa5 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 1f, 0f);
        final ObjectAnimator oa6 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 0f, 1f);
        oa5.setInterpolator(new DecelerateInterpolator());
        oa6.setInterpolator(new AccelerateDecelerateInterpolator());
        oa5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory03.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory03.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa6.start();
            }
        });
        oa5.start();
//card 04
        final ObjectAnimator oa7 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 1f, 0f);
        final ObjectAnimator oa8 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 0f, 1f);
        oa7.setInterpolator(new DecelerateInterpolator());
        oa8.setInterpolator(new AccelerateDecelerateInterpolator());
        oa7.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory04.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory04.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa8.start();
            }
        });
        oa7.start();
//card 05
        final ObjectAnimator oa9 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 1f, 0f);
        final ObjectAnimator oa0 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 0f, 1f);
        oa9.setInterpolator(new DecelerateInterpolator());
        oa0.setInterpolator(new AccelerateDecelerateInterpolator());
        oa9.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory05.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory05.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa0.start();
            }
        });
        oa9.start();
//card 06
        final ObjectAnimator oa11 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 1f, 0f);
        final ObjectAnimator oa12 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 0f, 1f);
        oa11.setInterpolator(new DecelerateInterpolator());
        oa12.setInterpolator(new AccelerateDecelerateInterpolator());
        oa11.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory06.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory06.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa12.start();
            }
        });
        oa11.start();
//card 07
        final ObjectAnimator oa13 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 1f, 0f);
        final ObjectAnimator oa14 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 0f, 1f);
        oa13.setInterpolator(new DecelerateInterpolator());
        oa14.setInterpolator(new AccelerateDecelerateInterpolator());
        oa13.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory07.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory07.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa14.start();
            }
        });
        oa13.start();
//card 08
        final ObjectAnimator oa15 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 1f, 0f);
        final ObjectAnimator oa16 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 0f, 1f);
        oa15.setInterpolator(new DecelerateInterpolator());
        oa16.setInterpolator(new AccelerateDecelerateInterpolator());
        oa15.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory08.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory08.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa16.start();
            }
        });
        oa15.start();
//card 09
        final ObjectAnimator oa17 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 1f, 0f);
        final ObjectAnimator oa18 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 0f, 1f);
        oa17.setInterpolator(new DecelerateInterpolator());
        oa18.setInterpolator(new AccelerateDecelerateInterpolator());
        oa17.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory09.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory09.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa18.start();
            }
        });
        oa17.start();
//card 10
        final ObjectAnimator oa19 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 1f, 0f);
        final ObjectAnimator oa20 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 0f, 1f);
        oa19.setInterpolator(new DecelerateInterpolator());
        oa20.setInterpolator(new AccelerateDecelerateInterpolator());
        oa19.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory10.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory10.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa20.start();
            }
        });
        oa19.start();
//card 11
        final ObjectAnimator oa21 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 1f, 0f);
        final ObjectAnimator oa22 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 0f, 1f);
        oa21.setInterpolator(new DecelerateInterpolator());
        oa22.setInterpolator(new AccelerateDecelerateInterpolator());
        oa21.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory11.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory11.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa22.start();
            }
        });
        oa21.start();
//card 12
        final ObjectAnimator oa23 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 1f, 0f);
        final ObjectAnimator oa24 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 0f, 1f);
        oa23.setInterpolator(new DecelerateInterpolator());
        oa24.setInterpolator(new AccelerateDecelerateInterpolator());
        oa23.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory12.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory12.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa24.start();
            }
        });
        oa23.start();
//card 13
        final ObjectAnimator oa25 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 1f, 0f);
        final ObjectAnimator oa26 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 0f, 1f);
        oa25.setInterpolator(new DecelerateInterpolator());
        oa26.setInterpolator(new AccelerateDecelerateInterpolator());
        oa25.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory13.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory13.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa26.start();
            }
        });
        oa25.start();
//card 14
        final ObjectAnimator oa27 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 1f, 0f);
        final ObjectAnimator oa28 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 0f, 1f);
        oa27.setInterpolator(new DecelerateInterpolator());
        oa28.setInterpolator(new AccelerateDecelerateInterpolator());
        oa27.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory14.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory14.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa28.start();
            }
        });
        oa27.start();
//card 15
        final ObjectAnimator oa29 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 1f, 0f);
        final ObjectAnimator oa30 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 0f, 1f);
        oa29.setInterpolator(new DecelerateInterpolator());
        oa30.setInterpolator(new AccelerateDecelerateInterpolator());
        oa29.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory15.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory15.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa30.start();
            }
        });
        oa29.start();
//card 16
        final ObjectAnimator oa31 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 1f, 0f);
        final ObjectAnimator oa32 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 0f, 1f);
        oa31.setInterpolator(new DecelerateInterpolator());
        oa32.setInterpolator(new AccelerateDecelerateInterpolator());
        oa31.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory16.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory16.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa32.start();
            }
        });
        oa31.start();
//card 17
        final ObjectAnimator oa33 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 1f, 0f);
        final ObjectAnimator oa34 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 0f, 1f);
        oa33.setInterpolator(new DecelerateInterpolator());
        oa34.setInterpolator(new AccelerateDecelerateInterpolator());
        oa33.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory17.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory17.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa34.start();
            }
        });
        oa33.start();
//card 18
        final ObjectAnimator oa35 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 1f, 0f);
        final ObjectAnimator oa36 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 0f, 1f);
        oa35.setInterpolator(new DecelerateInterpolator());
        oa36.setInterpolator(new AccelerateDecelerateInterpolator());
        oa35.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory18.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory18.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa36.start();
            }
        });
        oa35.start();
//card 19
        final ObjectAnimator oa37 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 1f, 0f);
        final ObjectAnimator oa38 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 0f, 1f);
        oa37.setInterpolator(new DecelerateInterpolator());
        oa38.setInterpolator(new AccelerateDecelerateInterpolator());
        oa37.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory19.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory19.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa38.start();
            }
        });
        oa37.start();
//card 20
        final ObjectAnimator oa39 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 1f, 0f);
        final ObjectAnimator oa40 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 0f, 1f);
        oa39.setInterpolator(new DecelerateInterpolator());
        oa40.setInterpolator(new AccelerateDecelerateInterpolator());
        oa39.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgMemory20.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                imgMemory20.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                oa40.start();
            }
        });
        oa39.start();

    }

    private void flipCards() {
        //Toast.makeText(getApplicationContext(), "flip", Toast.LENGTH_SHORT).show();
        imgMemory01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(antelastImgTurn==1)){

                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(0).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory01.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(0).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }

                            }else {
                                imgMemory01.setImageResource(getResources().getIdentifier(stringSelect.get(0), "drawable", getPackageName()));
                            }

                            imgMemory01.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard01, 1, 1, 1, 0, 1);                        }
                    });

                    imgTurn= imgTurn+1;
                    oa1.start();
                    if (cardName1.equals("") ){

                        cardName1= stringSelect.get(0).replace("sound_", "thing_");
                        
                        antelastImgTurn=1;
                        //Toast.makeText(getApplicationContext(), "...cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                        vericaMatch();

                    }else if (!(antelastImgTurn==1)){

                        cardName2=stringSelect.get(0).replace("sound_", "thing_");
                        
                        lastImgTurn=1;
                        //Toast.makeText(getApplicationContext(), stringSelect.get(0).replace("sound_", "")+"...cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                        vericaMatch();

                    }
                
                    
                }else {
                   // Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
                if(!(antelastImgTurn==2)){
                   // Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(1).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory02.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(1).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory02.setImageResource(getResources().getIdentifier(stringSelect.get(1), "drawable", getPackageName()));
                            }
                            imgMemory02.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard02, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(1).replace("sound_", "thing_");
                        
                        antelastImgTurn=2;
                        vericaMatch();
                       //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==2)){
                        cardName2=stringSelect.get(1).replace("sound_", "thing_");
                        
                        lastImgTurn=2;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }

                    
                }else {
                    //Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==3)){
                    //Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(2).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory03.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }  if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(2).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory03.setImageResource(getResources().getIdentifier(stringSelect.get(2), "drawable", getPackageName()));
                            }
                            imgMemory03.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard03, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(2).replace("sound_", "thing_");
                        
                        antelastImgTurn=3;
                        vericaMatch();
                       //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==3)){
                        cardName2=stringSelect.get(2).replace("sound_", "thing_");
                        
                        lastImgTurn=3;
                        vericaMatch();
                     //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }

                    
                }else {
                   // Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==4)){
                    //Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(3).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory04.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(3).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory04.setImageResource(getResources().getIdentifier(stringSelect.get(3), "drawable", getPackageName()));
                            }
                            imgMemory04.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard04, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(3).replace("sound_", "thing_");
                        
                        antelastImgTurn=4;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==4)){
                        cardName2=stringSelect.get(3).replace("sound_", "thing_");
                        
                        lastImgTurn=4;
                        vericaMatch();
                      //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                    
                }else {
                   // Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==5)){
                    //Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(4).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory05.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(4).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory05.setImageResource(getResources().getIdentifier(stringSelect.get(4), "drawable", getPackageName()));
                            }
                            imgMemory05.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard05, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(4).replace("sound_", "thing_");
                        
                        antelastImgTurn=5;
                        vericaMatch();
                     //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==5)){
                        cardName2=stringSelect.get(4).replace("sound_", "thing_");
                        
                        lastImgTurn=5;
                        vericaMatch();
                     //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                   // Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==6)){
                   // Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(5).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory06.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(5).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory06.setImageResource(getResources().getIdentifier(stringSelect.get(5), "drawable", getPackageName()));
                            }
                            imgMemory06.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard06, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(5).replace("sound_", "thing_");
                        
                        antelastImgTurn=6;
                        vericaMatch();
                      //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==6)){
                        cardName2=stringSelect.get(5).replace("sound_", "thing_");
                        
                        lastImgTurn=6;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }

                    
                }else {
                   // Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==7)){
                    //Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(6).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory07.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(6).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory07.setImageResource(getResources().getIdentifier(stringSelect.get(6), "drawable", getPackageName()));
                            }
                            imgMemory07.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard07, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(6).replace("sound_", "thing_");
                        
                        antelastImgTurn=7;
                        vericaMatch();
                   //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==7)){
                        cardName2=stringSelect.get(6).replace("sound_", "thing_");
                        
                        lastImgTurn=7;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }

                    
                }else {
                    //Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==8)){
                    //Toast.makeText(getApplicationContext(), "chamou aqui", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(7).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory08.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(7).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory08.setImageResource(getResources().getIdentifier(stringSelect.get(7), "drawable", getPackageName()));
                            }
                            imgMemory08.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard08, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(7).replace("sound_", "thing_");
                        
                        antelastImgTurn=8;
                        vericaMatch();
                      //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==8)){
                        cardName2=stringSelect.get(7).replace("sound_", "thing_");
                        
                        lastImgTurn=8;
                        vericaMatch();
                       //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }

                    
                }else {
                  //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==9)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(8).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory09.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(8).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory09.setImageResource(getResources().getIdentifier(stringSelect.get(8), "drawable", getPackageName()));
                            }
                            imgMemory09.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard09, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(8).replace("sound_", "thing_");
                        
                        antelastImgTurn=9;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==9)){
                        cardName2=stringSelect.get(8).replace("sound_", "thing_");
                        
                        lastImgTurn=9;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==10)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(9).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory10.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(9).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory10.setImageResource(getResources().getIdentifier(stringSelect.get(9), "drawable", getPackageName()));
                            }
                            imgMemory10.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard10, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(9).replace("sound_", "thing_");
                        
                        antelastImgTurn=10;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==10)){
                        cardName2=stringSelect.get(9).replace("sound_", "thing_");
                        
                        lastImgTurn=10;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==11)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(10).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory11.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(10).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory11.setImageResource(getResources().getIdentifier(stringSelect.get(10), "drawable", getPackageName()));
                            }
                            imgMemory11.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard11, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(10).replace("sound_", "thing_");
                        
                        antelastImgTurn=11;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==11)){
                        cardName2=stringSelect.get(10).replace("sound_", "thing_");
                        
                        lastImgTurn=11;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==12)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(11).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory12.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(11).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory12.setImageResource(getResources().getIdentifier(stringSelect.get(11), "drawable", getPackageName()));
                            }
                            imgMemory12.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard12, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(11).replace("sound_", "thing_");
                        
                        antelastImgTurn=12;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==12)){
                        cardName2=stringSelect.get(11).replace("sound_", "thing_");
                        
                        lastImgTurn=12;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==13)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(12).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory13.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(12).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory13.setImageResource(getResources().getIdentifier(stringSelect.get(12), "drawable", getPackageName()));
                            }
                            imgMemory13.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard13, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(12).replace("sound_", "thing_");
                        
                        antelastImgTurn=13;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==13)){
                        cardName2=stringSelect.get(12).replace("sound_", "thing_");
                        
                        lastImgTurn=13;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==14)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(13).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory14.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(13).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory14.setImageResource(getResources().getIdentifier(stringSelect.get(13), "drawable", getPackageName()));
                            }
                            imgMemory14.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard14, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(13).replace("sound_", "thing_");
                        
                        antelastImgTurn=14;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==14)){
                        cardName2=stringSelect.get(13).replace("sound_", "thing_");
                        
                        lastImgTurn=14;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==15)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(14).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory15.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(14).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory15.setImageResource(getResources().getIdentifier(stringSelect.get(14), "drawable", getPackageName()));
                            }
                            imgMemory15.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard15, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(14).replace("sound_", "thing_");
                        
                        antelastImgTurn=15;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==15)){
                        cardName2=stringSelect.get(14).replace("sound_", "thing_");
                        
                        lastImgTurn=15;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==16)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(15).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory16.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(15).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory16.setImageResource(getResources().getIdentifier(stringSelect.get(15), "drawable", getPackageName()));
                            }
                            imgMemory16.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard16, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(15).replace("sound_", "thing_");
                        
                        antelastImgTurn=16;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==16)){
                        cardName2=stringSelect.get(15).replace("sound_", "thing_");
                        
                        lastImgTurn=16;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==17)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(16).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory17.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(16).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory17.setImageResource(getResources().getIdentifier(stringSelect.get(16), "drawable", getPackageName()));
                            }
                            imgMemory17.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard17, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(16).replace("sound_", "thing_");
                        
                        antelastImgTurn=17;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==17)){
                        cardName2=stringSelect.get(16).replace("sound_", "thing_");
                        
                        lastImgTurn=17;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==18)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(17).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory18.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(17).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory18.setImageResource(getResources().getIdentifier(stringSelect.get(17), "drawable", getPackageName()));
                            }
                            imgMemory18.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard18, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(17).replace("sound_", "thing_");
                        
                        antelastImgTurn=18;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==18)){
                        cardName2=stringSelect.get(17).replace("sound_", "thing_");
                        
                        lastImgTurn=18;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==19)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(18).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory19.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(18).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory19.setImageResource(getResources().getIdentifier(stringSelect.get(18), "drawable", getPackageName()));
                            }
                            imgMemory19.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard19, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(18).replace("sound_", "thing_");
                        
                        antelastImgTurn=19;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==19)){
                        cardName2=stringSelect.get(18).replace("sound_", "thing_");
                        
                        lastImgTurn=19;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
        imgMemory20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(!(antelastImgTurn==20)){
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (stringSelect.get(19).startsWith("sound_")){
                                if (!lastWasMatch) {
                                    imgMemory20.setImageResource(getResources().getIdentifier("question1", "drawable", getPackageName()));
                                }
                                if (GameMatching.matchType==2){
                                    TastyToast.makeText(getApplicationContext(), stringSelect.get(19).replace("sound_", "").replace("_", " "), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            }else {
                                imgMemory20.setImageResource(getResources().getIdentifier(stringSelect.get(19), "drawable", getPackageName()));
                            }
                            imgMemory20.setBackgroundColor(getResources().getColor(R.color.branco));
                            oa2.start();
                            spCards.play(sdCard20, 1, 1, 0, 0, 1);
                        }
                    });
                    imgTurn= imgTurn+1;

                    oa1.start();
                    if (cardName1.equals("") ){
                        cardName1=stringSelect.get(19).replace("sound_", "thing_");
                        
                        antelastImgTurn=20;
                        vericaMatch();
                        //Toast.makeText(getApplicationContext(), "cardname1 é '': "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }else if (!(antelastImgTurn==20)){
                        cardName2=stringSelect.get(19).replace("sound_", "thing_");
                        
                        lastImgTurn=20;
                        vericaMatch();
                         //Toast.makeText(getApplicationContext(), "cardname1 não carta1: "+cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    //  Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), cardName1+" & "+cardName2, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vericaMatch() {
        if (imgTurn == 1) {

            if (!lastWasMatch){

                unflipPartially();
                //Toast.makeText(getApplicationContext(), "primeira carta", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(getApplicationContext(), "rolou  match na última", Toast.LENGTH_SHORT).show();

                lastWasMatch=false;
            }

        } else {
            final int previousPoints = Integer.parseInt(txtCurrentPoints.getText().toString());
            imgTurn = 0;
            if (cardName1.equals(cardName2)) {
                // Toast.makeText(getApplicationContext(), cardName1+" = "+cardName2, Toast.LENGTH_LONG).show();

                if (GameMatching.matchType==1){
                    currentPoints = previousPoints+10;
                }else {
                    currentPoints = previousPoints+5;
                }

                txtCurrentPoints.setText(String.valueOf(currentPoints));
                matchCards(antelastImgTurn, lastImgTurn, cardName1, cardName2);
                lastWasMatch=true;
                lastImgTurn = 0;
                antelastImgTurn = 0;
                cardName1 = "";
                cardName2 = "";
                qtdMatches=qtdMatches+1;
                if (qtdMatches==10){
                   spInterface.play(soundVictory, 1, 1, 0, 0, 1);
                    victory();
                }else {
                    spInterface.play(soundMatch, 1, 1, 0, 0, 1);
                }

               // Toast.makeText(getApplicationContext(), "segunda carta: MATCH!", Toast.LENGTH_SHORT).show();


            } else {
               //  Toast.makeText(getApplicationContext(), cardName1+" != "+cardName2, Toast.LENGTH_LONG).show();
                //unflipCards(antelastImgTurn, lastImgTurn);
                currentPoints = previousPoints-1;
                txtCurrentPoints.setText(String.valueOf(currentPoints));
                lastUnmatch = lastImgTurn;
                antelasteUnmatch = antelastImgTurn;
                lastImgTurn = 0;
                antelastImgTurn = 0;
                cardName1 = "";
                cardName2 = "";
               // Toast.makeText(getApplicationContext(), "segunada carta, não rolou.", Toast.LENGTH_SHORT).show();
                //spInterface.play(soundUnmatch, 1, 1, 0, 0, 1);
            }
        }
    }

    private void victory() {

        totalPoints=currentPoints+Integer.parseInt(txtTimer.getText().toString());
        myCount.cancel();
        //verify best
        final int vBest = Integer.parseInt((String) txtMyBest.getText());
        if (totalPoints>vBest){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
            //Toast.makeText(getApplicationContext(), totalPoints+" + "+vBest, Toast.LENGTH_LONG).show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            myBestRef = firebaseDatabase.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("games").child("matchingGame").child("bestScore"+stgBestType);
            myBestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Toast.makeText(getApplicationContext(), totalPoints+" + "+vBest+" + "+dataSnapshot.getValue(), Toast.LENGTH_LONG).show();
                    myBestRef.setValue(totalPoints);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        final AlertDialog.Builder builder = new AlertDialog.Builder(PlayMemory.this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("congratulations, you have completed the memory game with "+ txtTimer.getText().toString()+" seconds remaining. Your final score is: "+totalPoints+" points.")
                .setTitle("Victory!");
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

               Intent i = new Intent(getApplicationContext(), PlayMemory.class);
                startActivity(i);
                finish();
            }
        });

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void unflipPartially() {
        if(antelastImgTurn==lastUnmatch){
            switch (antelasteUnmatch){
                case 1:
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory01.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory01.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa2.start();
                        }
                    });
                    oa1.start();
                    break;
                case 2:
                    final ObjectAnimator oa3 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 1f, 0f);
                    final ObjectAnimator oa4 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 0f, 1f);
                    oa3.setInterpolator(new DecelerateInterpolator());
                    oa4.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory02.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory02.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa4.start();
                        }
                    });
                    oa3.start();
                    break;
                case 3:
                    final ObjectAnimator oa5 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 1f, 0f);
                    final ObjectAnimator oa6 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 0f, 1f);
                    oa5.setInterpolator(new DecelerateInterpolator());
                    oa6.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa5.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory03.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory03.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa6.start();
                        }
                    });
                    oa5.start();
                    break;
                case 4:
                    final ObjectAnimator oa7 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 1f, 0f);
                    final ObjectAnimator oa8 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 0f, 1f);
                    oa7.setInterpolator(new DecelerateInterpolator());
                    oa8.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa7.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory04.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory04.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa8.start();
                        }
                    });
                    oa7.start();
                    break;
                case 5:
                    final ObjectAnimator oa9 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 1f, 0f);
                    final ObjectAnimator oa0 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 0f, 1f);
                    oa9.setInterpolator(new DecelerateInterpolator());
                    oa0.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa9.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory05.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory05.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa0.start();
                        }
                    });
                    oa9.start();
                    break;
                case 6:
                    final ObjectAnimator oa11 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 1f, 0f);
                    final ObjectAnimator oa12 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 0f, 1f);
                    oa11.setInterpolator(new DecelerateInterpolator());
                    oa12.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa11.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory06.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory06.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa12.start();
                        }
                    });
                    oa11.start();
                    break;
                case 7:
                    final ObjectAnimator oa13 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 1f, 0f);
                    final ObjectAnimator oa14 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 0f, 1f);
                    oa13.setInterpolator(new DecelerateInterpolator());
                    oa14.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa13.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory07.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory07.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa14.start();
                        }
                    });
                    oa13.start();
                    break;
                case 8:
                    final ObjectAnimator oa15 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 1f, 0f);
                    final ObjectAnimator oa16 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 0f, 1f);
                    oa15.setInterpolator(new DecelerateInterpolator());
                    oa16.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa15.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory08.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory08.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa16.start();
                        }
                    });
                    oa15.start();
                    break;
                case 9:
                    final ObjectAnimator oa17 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 1f, 0f);
                    final ObjectAnimator oa18 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 0f, 1f);
                    oa17.setInterpolator(new DecelerateInterpolator());
                    oa18.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa17.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory09.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory09.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa18.start();
                        }
                    });
                    oa17.start();
                    break;
                case 10:
                    final ObjectAnimator oa19 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 1f, 0f);
                    final ObjectAnimator oa20 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 0f, 1f);
                    oa19.setInterpolator(new DecelerateInterpolator());
                    oa20.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa19.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory10.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory10.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa20.start();
                        }
                    });
                    oa19.start();
                    break;
                case 11:
                    final ObjectAnimator oa21 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 1f, 0f);
                    final ObjectAnimator oa22 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 0f, 1f);
                    oa21.setInterpolator(new DecelerateInterpolator());
                    oa22.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa21.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory11.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory11.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa22.start();
                        }
                    });
                    oa21.start();
                    break;
                case 12:
                    final ObjectAnimator oa23 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 1f, 0f);
                    final ObjectAnimator oa24 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 0f, 1f);
                    oa23.setInterpolator(new DecelerateInterpolator());
                    oa24.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa23.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory12.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory12.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa24.start();
                        }
                    });
                    oa23.start();
                    break;
                case 13:
                    final ObjectAnimator oa25 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 1f, 0f);
                    final ObjectAnimator oa26 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 0f, 1f);
                    oa25.setInterpolator(new DecelerateInterpolator());
                    oa26.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa25.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory13.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory13.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa26.start();
                        }
                    });
                    oa25.start();
                    break;
                case 14:
                    final ObjectAnimator oa27 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 1f, 0f);
                    final ObjectAnimator oa28 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 0f, 1f);
                    oa27.setInterpolator(new DecelerateInterpolator());
                    oa28.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa27.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory14.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory14.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa28.start();
                        }
                    });
                    oa27.start();
                    break;
                case 15:
                    final ObjectAnimator oa29 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 1f, 0f);
                    final ObjectAnimator oa30 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 0f, 1f);
                    oa29.setInterpolator(new DecelerateInterpolator());
                    oa30.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa29.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory15.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory15.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa30.start();
                        }
                    });
                    oa29.start();
                    break;
                case 16:
                    final ObjectAnimator oa31 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 1f, 0f);
                    final ObjectAnimator oa32 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 0f, 1f);
                    oa31.setInterpolator(new DecelerateInterpolator());
                    oa32.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa31.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory16.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory16.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa32.start();
                        }
                    });
                    oa31.start();
                    break;
                case 17:
                    final ObjectAnimator oa33 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 1f, 0f);
                    final ObjectAnimator oa34 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 0f, 1f);
                    oa33.setInterpolator(new DecelerateInterpolator());
                    oa34.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa33.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory17.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory17.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa34.start();
                        }
                    });
                    oa33.start();
                    break;
                case 18:
                    final ObjectAnimator oa35 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 1f, 0f);
                    final ObjectAnimator oa36 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 0f, 1f);
                    oa35.setInterpolator(new DecelerateInterpolator());
                    oa36.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa35.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory18.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory18.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa36.start();
                        }
                    });
                    oa35.start();
                    break;
                case 19:
                    final ObjectAnimator oa37 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 1f, 0f);
                    final ObjectAnimator oa38 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 0f, 1f);
                    oa37.setInterpolator(new DecelerateInterpolator());
                    oa38.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa37.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory19.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory19.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa38.start();
                        }
                    });
                    oa37.start();
                    break;
                case 20:
                    final ObjectAnimator oa39 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 1f, 0f);
                    final ObjectAnimator oa40 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 0f, 1f);
                    oa39.setInterpolator(new DecelerateInterpolator());
                    oa40.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa39.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory20.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory20.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa40.start();
                        }
                    });
                    oa39.start();
                    break;
            }
        }else if(antelastImgTurn==antelasteUnmatch){
            switch (lastUnmatch){
                case 1:
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory01.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory01.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa2.start();
                        }
                    });
                    oa1.start();
                    break;
                case 2:
                    final ObjectAnimator oa3 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 1f, 0f);
                    final ObjectAnimator oa4 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 0f, 1f);
                    oa3.setInterpolator(new DecelerateInterpolator());
                    oa4.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory02.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory02.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa4.start();
                        }
                    });
                    oa3.start();
                    break;
                case 3:
                    final ObjectAnimator oa5 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 1f, 0f);
                    final ObjectAnimator oa6 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 0f, 1f);
                    oa5.setInterpolator(new DecelerateInterpolator());
                    oa6.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa5.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory03.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory03.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa6.start();
                        }
                    });
                    oa5.start();
                    break;
                case 4:
                    final ObjectAnimator oa7 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 1f, 0f);
                    final ObjectAnimator oa8 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 0f, 1f);
                    oa7.setInterpolator(new DecelerateInterpolator());
                    oa8.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa7.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory04.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory04.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa8.start();
                        }
                    });
                    oa7.start();
                    break;
                case 5:
                    final ObjectAnimator oa9 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 1f, 0f);
                    final ObjectAnimator oa0 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 0f, 1f);
                    oa9.setInterpolator(new DecelerateInterpolator());
                    oa0.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa9.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory05.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory05.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa0.start();
                        }
                    });
                    oa9.start();
                    break;
                case 6:
                    final ObjectAnimator oa11 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 1f, 0f);
                    final ObjectAnimator oa12 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 0f, 1f);
                    oa11.setInterpolator(new DecelerateInterpolator());
                    oa12.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa11.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory06.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory06.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa12.start();
                        }
                    });
                    oa11.start();
                    break;
                case 7:
                    final ObjectAnimator oa13 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 1f, 0f);
                    final ObjectAnimator oa14 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 0f, 1f);
                    oa13.setInterpolator(new DecelerateInterpolator());
                    oa14.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa13.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory07.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory07.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa14.start();
                        }
                    });
                    oa13.start();
                    break;
                case 8:
                    final ObjectAnimator oa15 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 1f, 0f);
                    final ObjectAnimator oa16 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 0f, 1f);
                    oa15.setInterpolator(new DecelerateInterpolator());
                    oa16.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa15.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory08.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory08.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa16.start();
                        }
                    });
                    oa15.start();
                    break;
                case 9:
                    final ObjectAnimator oa17 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 1f, 0f);
                    final ObjectAnimator oa18 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 0f, 1f);
                    oa17.setInterpolator(new DecelerateInterpolator());
                    oa18.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa17.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory09.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory09.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa18.start();
                        }
                    });
                    oa17.start();
                    break;
                case 10:
                    final ObjectAnimator oa19 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 1f, 0f);
                    final ObjectAnimator oa20 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 0f, 1f);
                    oa19.setInterpolator(new DecelerateInterpolator());
                    oa20.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa19.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory10.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory10.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa20.start();
                        }
                    });
                    oa19.start();
                    break;
                case 11:
                    final ObjectAnimator oa21 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 1f, 0f);
                    final ObjectAnimator oa22 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 0f, 1f);
                    oa21.setInterpolator(new DecelerateInterpolator());
                    oa22.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa21.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory11.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory11.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa22.start();
                        }
                    });
                    oa21.start();
                    break;
                case 12:
                    final ObjectAnimator oa23 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 1f, 0f);
                    final ObjectAnimator oa24 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 0f, 1f);
                    oa23.setInterpolator(new DecelerateInterpolator());
                    oa24.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa23.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory12.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory12.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa24.start();
                        }
                    });
                    oa23.start();
                    break;
                case 13:
                    final ObjectAnimator oa25 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 1f, 0f);
                    final ObjectAnimator oa26 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 0f, 1f);
                    oa25.setInterpolator(new DecelerateInterpolator());
                    oa26.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa25.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory13.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory13.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa26.start();
                        }
                    });
                    oa25.start();
                    break;
                case 14:
                    final ObjectAnimator oa27 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 1f, 0f);
                    final ObjectAnimator oa28 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 0f, 1f);
                    oa27.setInterpolator(new DecelerateInterpolator());
                    oa28.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa27.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory14.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory14.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa28.start();
                        }
                    });
                    oa27.start();
                    break;
                case 15:
                    final ObjectAnimator oa29 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 1f, 0f);
                    final ObjectAnimator oa30 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 0f, 1f);
                    oa29.setInterpolator(new DecelerateInterpolator());
                    oa30.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa29.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory15.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory15.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa30.start();
                        }
                    });
                    oa29.start();
                    break;
                case 16:
                    final ObjectAnimator oa31 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 1f, 0f);
                    final ObjectAnimator oa32 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 0f, 1f);
                    oa31.setInterpolator(new DecelerateInterpolator());
                    oa32.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa31.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory16.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory16.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa32.start();
                        }
                    });
                    oa31.start();
                    break;
                case 17:
                    final ObjectAnimator oa33 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 1f, 0f);
                    final ObjectAnimator oa34 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 0f, 1f);
                    oa33.setInterpolator(new DecelerateInterpolator());
                    oa34.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa33.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory17.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory17.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa34.start();
                        }
                    });
                    oa33.start();
                    break;
                case 18:
                    final ObjectAnimator oa35 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 1f, 0f);
                    final ObjectAnimator oa36 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 0f, 1f);
                    oa35.setInterpolator(new DecelerateInterpolator());
                    oa36.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa35.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory18.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory18.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa36.start();
                        }
                    });
                    oa35.start();
                    break;
                case 19:
                    final ObjectAnimator oa37 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 1f, 0f);
                    final ObjectAnimator oa38 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 0f, 1f);
                    oa37.setInterpolator(new DecelerateInterpolator());
                    oa38.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa37.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory19.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory19.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa38.start();
                        }
                    });
                    oa37.start();
                    break;
                case 20:
                    final ObjectAnimator oa39 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 1f, 0f);
                    final ObjectAnimator oa40 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 0f, 1f);
                    oa39.setInterpolator(new DecelerateInterpolator());
                    oa40.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa39.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory20.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory20.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa40.start();
                        }
                    });
                    oa39.start();
                    break;
            }
        }else {
            switch (lastUnmatch){
                case 1:
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory01.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory01.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa2.start();
                        }
                    });
                    oa1.start();
                    break;
                case 2:
                    final ObjectAnimator oa3 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 1f, 0f);
                    final ObjectAnimator oa4 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 0f, 1f);
                    oa3.setInterpolator(new DecelerateInterpolator());
                    oa4.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory02.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory02.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa4.start();
                        }
                    });
                    oa3.start();
                    break;
                case 3:
                    final ObjectAnimator oa5 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 1f, 0f);
                    final ObjectAnimator oa6 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 0f, 1f);
                    oa5.setInterpolator(new DecelerateInterpolator());
                    oa6.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa5.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory03.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory03.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa6.start();
                        }
                    });
                    oa5.start();
                    break;
                case 4:
                    final ObjectAnimator oa7 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 1f, 0f);
                    final ObjectAnimator oa8 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 0f, 1f);
                    oa7.setInterpolator(new DecelerateInterpolator());
                    oa8.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa7.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory04.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory04.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa8.start();
                        }
                    });
                    oa7.start();
                    break;
                case 5:
                    final ObjectAnimator oa9 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 1f, 0f);
                    final ObjectAnimator oa0 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 0f, 1f);
                    oa9.setInterpolator(new DecelerateInterpolator());
                    oa0.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa9.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory05.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory05.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa0.start();
                        }
                    });
                    oa9.start();
                    break;
                case 6:
                    final ObjectAnimator oa11 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 1f, 0f);
                    final ObjectAnimator oa12 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 0f, 1f);
                    oa11.setInterpolator(new DecelerateInterpolator());
                    oa12.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa11.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory06.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory06.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa12.start();
                        }
                    });
                    oa11.start();
                    break;
                case 7:
                    final ObjectAnimator oa13 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 1f, 0f);
                    final ObjectAnimator oa14 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 0f, 1f);
                    oa13.setInterpolator(new DecelerateInterpolator());
                    oa14.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa13.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory07.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory07.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa14.start();
                        }
                    });
                    oa13.start();
                    break;
                case 8:
                    final ObjectAnimator oa15 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 1f, 0f);
                    final ObjectAnimator oa16 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 0f, 1f);
                    oa15.setInterpolator(new DecelerateInterpolator());
                    oa16.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa15.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory08.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory08.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa16.start();
                        }
                    });
                    oa15.start();
                    break;
                case 9:
                    final ObjectAnimator oa17 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 1f, 0f);
                    final ObjectAnimator oa18 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 0f, 1f);
                    oa17.setInterpolator(new DecelerateInterpolator());
                    oa18.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa17.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory09.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory09.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa18.start();
                        }
                    });
                    oa17.start();
                    break;
                case 10:
                    final ObjectAnimator oa19 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 1f, 0f);
                    final ObjectAnimator oa20 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 0f, 1f);
                    oa19.setInterpolator(new DecelerateInterpolator());
                    oa20.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa19.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory10.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory10.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa20.start();
                        }
                    });
                    oa19.start();
                    break;
                case 11:
                    final ObjectAnimator oa21 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 1f, 0f);
                    final ObjectAnimator oa22 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 0f, 1f);
                    oa21.setInterpolator(new DecelerateInterpolator());
                    oa22.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa21.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory11.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory11.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa22.start();
                        }
                    });
                    oa21.start();
                    break;
                case 12:
                    final ObjectAnimator oa23 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 1f, 0f);
                    final ObjectAnimator oa24 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 0f, 1f);
                    oa23.setInterpolator(new DecelerateInterpolator());
                    oa24.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa23.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory12.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory12.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa24.start();
                        }
                    });
                    oa23.start();
                    break;
                case 13:
                    final ObjectAnimator oa25 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 1f, 0f);
                    final ObjectAnimator oa26 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 0f, 1f);
                    oa25.setInterpolator(new DecelerateInterpolator());
                    oa26.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa25.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory13.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory13.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa26.start();
                        }
                    });
                    oa25.start();
                    break;
                case 14:
                    final ObjectAnimator oa27 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 1f, 0f);
                    final ObjectAnimator oa28 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 0f, 1f);
                    oa27.setInterpolator(new DecelerateInterpolator());
                    oa28.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa27.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory14.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory14.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa28.start();
                        }
                    });
                    oa27.start();
                    break;
                case 15:
                    final ObjectAnimator oa29 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 1f, 0f);
                    final ObjectAnimator oa30 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 0f, 1f);
                    oa29.setInterpolator(new DecelerateInterpolator());
                    oa30.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa29.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory15.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory15.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa30.start();
                        }
                    });
                    oa29.start();
                    break;
                case 16:
                    final ObjectAnimator oa31 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 1f, 0f);
                    final ObjectAnimator oa32 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 0f, 1f);
                    oa31.setInterpolator(new DecelerateInterpolator());
                    oa32.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa31.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory16.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory16.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa32.start();
                        }
                    });
                    oa31.start();
                    break;
                case 17:
                    final ObjectAnimator oa33 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 1f, 0f);
                    final ObjectAnimator oa34 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 0f, 1f);
                    oa33.setInterpolator(new DecelerateInterpolator());
                    oa34.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa33.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory17.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory17.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa34.start();
                        }
                    });
                    oa33.start();
                    break;
                case 18:
                    final ObjectAnimator oa35 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 1f, 0f);
                    final ObjectAnimator oa36 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 0f, 1f);
                    oa35.setInterpolator(new DecelerateInterpolator());
                    oa36.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa35.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory18.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory18.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa36.start();
                        }
                    });
                    oa35.start();
                    break;
                case 19:
                    final ObjectAnimator oa37 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 1f, 0f);
                    final ObjectAnimator oa38 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 0f, 1f);
                    oa37.setInterpolator(new DecelerateInterpolator());
                    oa38.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa37.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory19.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory19.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa38.start();
                        }
                    });
                    oa37.start();
                    break;
                case 20:
                    final ObjectAnimator oa39 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 1f, 0f);
                    final ObjectAnimator oa40 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 0f, 1f);
                    oa39.setInterpolator(new DecelerateInterpolator());
                    oa40.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa39.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory20.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory20.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa40.start();
                        }
                    });
                    oa39.start();
                    break;
            }
            switch (antelasteUnmatch){
                case 1:
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgMemory01, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory01.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory01.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa2.start();
                        }
                    });
                    oa1.start();
                    break;
                case 2:
                    final ObjectAnimator oa3 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 1f, 0f);
                    final ObjectAnimator oa4 = ObjectAnimator.ofFloat(imgMemory02, "scaleX", 0f, 1f);
                    oa3.setInterpolator(new DecelerateInterpolator());
                    oa4.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory02.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory02.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa4.start();
                        }
                    });
                    oa3.start();
                    break;
                case 3:
                    final ObjectAnimator oa5 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 1f, 0f);
                    final ObjectAnimator oa6 = ObjectAnimator.ofFloat(imgMemory03, "scaleX", 0f, 1f);
                    oa5.setInterpolator(new DecelerateInterpolator());
                    oa6.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa5.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory03.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory03.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa6.start();
                        }
                    });
                    oa5.start();
                    break;
                case 4:
                    final ObjectAnimator oa7 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 1f, 0f);
                    final ObjectAnimator oa8 = ObjectAnimator.ofFloat(imgMemory04, "scaleX", 0f, 1f);
                    oa7.setInterpolator(new DecelerateInterpolator());
                    oa8.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa7.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory04.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory04.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa8.start();
                        }
                    });
                    oa7.start();
                    break;
                case 5:
                    final ObjectAnimator oa9 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 1f, 0f);
                    final ObjectAnimator oa0 = ObjectAnimator.ofFloat(imgMemory05, "scaleX", 0f, 1f);
                    oa9.setInterpolator(new DecelerateInterpolator());
                    oa0.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa9.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory05.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory05.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa0.start();
                        }
                    });
                    oa9.start();
                    break;
                case 6:
                    final ObjectAnimator oa11 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 1f, 0f);
                    final ObjectAnimator oa12 = ObjectAnimator.ofFloat(imgMemory06, "scaleX", 0f, 1f);
                    oa11.setInterpolator(new DecelerateInterpolator());
                    oa12.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa11.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory06.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory06.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa12.start();
                        }
                    });
                    oa11.start();
                    break;
                case 7:
                    final ObjectAnimator oa13 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 1f, 0f);
                    final ObjectAnimator oa14 = ObjectAnimator.ofFloat(imgMemory07, "scaleX", 0f, 1f);
                    oa13.setInterpolator(new DecelerateInterpolator());
                    oa14.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa13.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory07.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory07.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa14.start();
                        }
                    });
                    oa13.start();
                    break;
                case 8:
                    final ObjectAnimator oa15 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 1f, 0f);
                    final ObjectAnimator oa16 = ObjectAnimator.ofFloat(imgMemory08, "scaleX", 0f, 1f);
                    oa15.setInterpolator(new DecelerateInterpolator());
                    oa16.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa15.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory08.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory08.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa16.start();
                        }
                    });
                    oa15.start();
                    break;
                case 9:
                    final ObjectAnimator oa17 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 1f, 0f);
                    final ObjectAnimator oa18 = ObjectAnimator.ofFloat(imgMemory09, "scaleX", 0f, 1f);
                    oa17.setInterpolator(new DecelerateInterpolator());
                    oa18.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa17.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory09.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory09.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa18.start();
                        }
                    });
                    oa17.start();
                    break;
                case 10:
                    final ObjectAnimator oa19 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 1f, 0f);
                    final ObjectAnimator oa20 = ObjectAnimator.ofFloat(imgMemory10, "scaleX", 0f, 1f);
                    oa19.setInterpolator(new DecelerateInterpolator());
                    oa20.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa19.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory10.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory10.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa20.start();
                        }
                    });
                    oa19.start();
                    break;
                case 11:
                    final ObjectAnimator oa21 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 1f, 0f);
                    final ObjectAnimator oa22 = ObjectAnimator.ofFloat(imgMemory11, "scaleX", 0f, 1f);
                    oa21.setInterpolator(new DecelerateInterpolator());
                    oa22.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa21.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory11.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory11.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa22.start();
                        }
                    });
                    oa21.start();
                    break;
                case 12:
                    final ObjectAnimator oa23 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 1f, 0f);
                    final ObjectAnimator oa24 = ObjectAnimator.ofFloat(imgMemory12, "scaleX", 0f, 1f);
                    oa23.setInterpolator(new DecelerateInterpolator());
                    oa24.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa23.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory12.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory12.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa24.start();
                        }
                    });
                    oa23.start();
                    break;
                case 13:
                    final ObjectAnimator oa25 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 1f, 0f);
                    final ObjectAnimator oa26 = ObjectAnimator.ofFloat(imgMemory13, "scaleX", 0f, 1f);
                    oa25.setInterpolator(new DecelerateInterpolator());
                    oa26.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa25.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory13.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory13.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa26.start();
                        }
                    });
                    oa25.start();
                    break;
                case 14:
                    final ObjectAnimator oa27 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 1f, 0f);
                    final ObjectAnimator oa28 = ObjectAnimator.ofFloat(imgMemory14, "scaleX", 0f, 1f);
                    oa27.setInterpolator(new DecelerateInterpolator());
                    oa28.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa27.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory14.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory14.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa28.start();
                        }
                    });
                    oa27.start();
                    break;
                case 15:
                    final ObjectAnimator oa29 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 1f, 0f);
                    final ObjectAnimator oa30 = ObjectAnimator.ofFloat(imgMemory15, "scaleX", 0f, 1f);
                    oa29.setInterpolator(new DecelerateInterpolator());
                    oa30.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa29.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory15.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory15.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa30.start();
                        }
                    });
                    oa29.start();
                    break;
                case 16:
                    final ObjectAnimator oa31 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 1f, 0f);
                    final ObjectAnimator oa32 = ObjectAnimator.ofFloat(imgMemory16, "scaleX", 0f, 1f);
                    oa31.setInterpolator(new DecelerateInterpolator());
                    oa32.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa31.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory16.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory16.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa32.start();
                        }
                    });
                    oa31.start();
                    break;
                case 17:
                    final ObjectAnimator oa33 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 1f, 0f);
                    final ObjectAnimator oa34 = ObjectAnimator.ofFloat(imgMemory17, "scaleX", 0f, 1f);
                    oa33.setInterpolator(new DecelerateInterpolator());
                    oa34.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa33.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory17.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory17.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa34.start();
                        }
                    });
                    oa33.start();
                    break;
                case 18:
                    final ObjectAnimator oa35 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 1f, 0f);
                    final ObjectAnimator oa36 = ObjectAnimator.ofFloat(imgMemory18, "scaleX", 0f, 1f);
                    oa35.setInterpolator(new DecelerateInterpolator());
                    oa36.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa35.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory18.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory18.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa36.start();
                        }
                    });
                    oa35.start();
                    break;
                case 19:
                    final ObjectAnimator oa37 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 1f, 0f);
                    final ObjectAnimator oa38 = ObjectAnimator.ofFloat(imgMemory19, "scaleX", 0f, 1f);
                    oa37.setInterpolator(new DecelerateInterpolator());
                    oa38.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa37.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory19.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory19.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa38.start();
                        }
                    });
                    oa37.start();
                    break;
                case 20:
                    final ObjectAnimator oa39 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 1f, 0f);
                    final ObjectAnimator oa40 = ObjectAnimator.ofFloat(imgMemory20, "scaleX", 0f, 1f);
                    oa39.setInterpolator(new DecelerateInterpolator());
                    oa40.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa39.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            imgMemory20.setImageResource(getResources().getIdentifier("logo_hi_cria", "drawable", getPackageName()));
                            imgMemory20.setBackgroundColor(getResources().getColor(R.color.botaoEntrar));
                            oa40.start();
                        }
                    });
                    oa39.start();
                    break;
            }
        }

    }

    private void matchCards(int ante, int last, String cardName1, String cardName2) {
      //  Toast.makeText(getApplicationContext(), cardName1+" + "+cardName2, Toast.LENGTH_SHORT).show();
        switch (ante){
            case 1:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory01.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                break;
            case 2:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory02.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 3:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory03.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 4:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory04.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 5:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory05.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory05.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 6:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory06.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory06.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 7:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory07.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory07.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 8:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory08.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory08.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 9:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory09.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory09.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 10:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory10.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 11:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory11.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 12:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory12.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 13:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory13.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 14:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory14.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 15:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory15.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 16:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory16.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 17:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory17.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory17.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 18:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory18.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory18.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 19:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory19.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory19.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 20:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory20.setImageResource(getResources().getIdentifier(cardName2, "drawable", getPackageName()));
                imgMemory20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;

        }
        switch (last){

            case 1:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory01.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                break;
            case 2:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory02.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 3:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory03.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 4:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory04.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 5:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory05.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory05.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 6:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory06.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory06.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 7:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory07.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory07.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 8:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory08.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory08.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 9:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory09.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory09.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 10:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory10.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 11:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory11.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 12:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory12.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 13:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory13.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 14:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory14.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 15:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory15.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 16:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory16.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 17:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory17.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory17.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 18:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory18.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory18.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 19:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory19.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory19.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case 20:
                //TastyToast.makeText(getApplicationContext(), "Maaatch", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                imgMemory20.setImageResource(getResources().getIdentifier(cardName1, "drawable", getPackageName()));
                imgMemory20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        spInterface.release();
        spCards.release();
    }
}

