package com.cria.agora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import DadosUsuario.UserPerformance;

public class MainActivity extends AppCompatActivity {

    private TextView seuNomeTitle;
    private TextView atividades;
    private TextView score;
    private TextView nivel;
    private FloatingActionButton goToAdm;


    public static UserPerformance performance;



    private ImageButton avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //declarar elementos
        seuNomeTitle = findViewById(R.id.textName);
        atividades = findViewById(R.id.txt_atividades);
        score = findViewById(R.id.txt_score);
        nivel = findViewById(R.id.txt_nivel);
        avatar = findViewById(R.id.img_avatar);
        goToAdm = findViewById(R.id.fabGoToAdm);


        //popular os txt com infos do usuário que fez login através do objeto usuario da classe UserInfo
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        seuNomeTitle.setText(sharedPreferences.getString("firstName", "unknown"));


        if (sharedPreferences.getLong("tipoUser", 1) ==0 || sharedPreferences.getLong("tipoUser", 1) ==8){
            goToAdm.show();
        }else {
            goToAdm.hide();
        }
        goToAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("tipoUser", 0);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainAdm.class);
                startActivity(intent);
            }
        });
        ImageButton badge = findViewById(R.id.btn_badge);
        badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Achivements.class);
                startActivity(intent);
            }
        });

        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Atividades.class);
                Atividades.isPerformance=false;
                startActivity(intent);
            }
        });
        ImageButton game = (ImageButton) findViewById(R.id.btn_game);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainGames.class);
                Atividades.isPerformance=false;
                startActivity(intent);
            }
        });
        ImageButton perf = (ImageButton) findViewById(R.id.btn_perf);
        perf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Atividades.class);
                Atividades.isPerformance=true;
                startActivity(i);
               // TastyToast.makeText(getApplicationContext(), "Soon!", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            }
        });

        ImageButton social = (ImageButton) findViewById(R.id.btn_social);
        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "Soon!", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            }
        });

        ImageButton sair = findViewById(R.id.btn_sair);
        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sair();


            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AvatarChoice.class);
                startActivity(intent);
            }
        });

    }

    private void sair() {
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //Setar o shared Preferences

        editor.putBoolean("LoggedIn", false);
        editor.putString("matricula", "");
        editor.putString("firstName", "");
        editor.putString("surName","");
        editor.putString("email", "");
        editor.putString("phone", "");
        editor.putString("escola", "");
        editor.putString("func","");
        editor.putString("dia", "");
        editor.putString("mes", "");
        editor.putString("ano", "");
        editor.putString("sexo", "");
        editor.putLong("tipoUser", 1);
        editor.putLong("utbCRIA", 0);

        editor.putBoolean("Achieve_eco", false);
        editor.putBoolean("Achieve_forest", false);
        editor.putBoolean("Achieve_halloween", false);
        editor.putBoolean("Achieve_labor",false);
        editor.putBoolean("Achieve_number", false);
        editor.putBoolean("Achieve_saturn", false);
        editor.putBoolean("Achieve_summer", false);
        editor.putBoolean("Achieve_thanksgiving", false);
        editor.putBoolean("Achieve_travel", false);
        editor.putBoolean("Achieve_unicorn", false);
        editor.apply();


        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        //popular a performance
        String refUser = sharedPreferences.getString("matricula", "0");
        FirebaseDatabase fireUser = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReferenceUser = fireUser.getReference("users").child(refUser).child("desempenho");
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long iAtv = (long)dataSnapshot.child("atvRealizadas").getValue();
                long iScore = (long) dataSnapshot.child("score").getValue();
                long iAvatar = (long) dataSnapshot.child("avatar").getValue();
                String iLevel = (String) dataSnapshot.child("level").getValue();

                //Toast.makeText(getApplicationContext(), String.valueOf(iScore), Toast.LENGTH_LONG).show();

                performance = new UserPerformance(iAtv, iScore, iLevel, iAvatar);

                atividades.setText(String.valueOf(performance.getAtividades()));
                score.setText(String.valueOf(performance.getPontos()));
                nivel.setText(performance.getLevel());


                int iAvatar2 = (int) performance.getAvatar();
                switch (iAvatar2){
                    case 1: avatar.setImageResource(R.drawable.animal1); break;
                    case 2: avatar.setImageResource(R.drawable.animal2); break;
                    case 3: avatar.setImageResource(R.drawable.animal3); break;
                    case 4: avatar.setImageResource(R.drawable.animal4); break;
                    case 5: avatar.setImageResource(R.drawable.animal5); break;
                    case 6: avatar.setImageResource(R.drawable.animal6); break;
                    case 7: avatar.setImageResource(R.drawable.animal7); break;
                    case 8: avatar.setImageResource(R.drawable.animal8); break;
                    case 9: avatar.setImageResource(R.drawable.animal9); break;
                    case 11: avatar.setImageResource(R.drawable.fruit1); break;
                    case 12: avatar.setImageResource(R.drawable.fruit2); break;
                    case 13: avatar.setImageResource(R.drawable.fruit3); break;
                    case 14: avatar.setImageResource(R.drawable.fruit4); break;
                    case 15: avatar.setImageResource(R.drawable.fruit5); break;
                    case 16: avatar.setImageResource(R.drawable.fruit6); break;
                    case 17: avatar.setImageResource(R.drawable.fruit7); break;
                    case 18: avatar.setImageResource(R.drawable.fruit8); break;
                    case 19: avatar.setImageResource(R.drawable.fruit9); break;
                    case 21: avatar.setImageResource(R.drawable.food1); break;
                    case 22: avatar.setImageResource(R.drawable.food2); break;
                    case 23: avatar.setImageResource(R.drawable.food3); break;
                    case 24: avatar.setImageResource(R.drawable.food4); break;
                    case 25: avatar.setImageResource(R.drawable.food5); break;
                    case 26: avatar.setImageResource(R.drawable.food6); break;
                    case 27: avatar.setImageResource(R.drawable.food7); break;
                    case 28: avatar.setImageResource(R.drawable.food8); break;
                    case 29: avatar.setImageResource(R.drawable.food9); break;
                    case 31: avatar.setImageResource(R.drawable.sky1); break;
                    case 32: avatar.setImageResource(R.drawable.sky2); break;
                    case 33: avatar.setImageResource(R.drawable.sky3); break;
                    case 34: avatar.setImageResource(R.drawable.sky4); break;
                    case 35: avatar.setImageResource(R.drawable.sky5); break;
                    case 36: avatar.setImageResource(R.drawable.sky6); break;
                    case 37: avatar.setImageResource(R.drawable.sky7); break;
                    case 38: avatar.setImageResource(R.drawable.sky8); break;
                    case 39: avatar.setImageResource(R.drawable.sky9); break;
                    case 41: avatar.setImageResource(R.drawable.dessert1); break;
                    case 42: avatar.setImageResource(R.drawable.dessert2); break;
                    case 43: avatar.setImageResource(R.drawable.dessert3); break;
                    case 44: avatar.setImageResource(R.drawable.dessert4); break;
                    case 45: avatar.setImageResource(R.drawable.dessert5); break;
                    case 46: avatar.setImageResource(R.drawable.dessert6); break;
                    case 47: avatar.setImageResource(R.drawable.dessert7); break;
                    case 48: avatar.setImageResource(R.drawable.dessert8); break;
                    case 49: avatar.setImageResource(R.drawable.dessert9); break;
                    case 51: avatar.setImageResource(R.drawable.sushi1); break;
                    case 52: avatar.setImageResource(R.drawable.sushi2); break;
                    case 53: avatar.setImageResource(R.drawable.sushi3); break;
                    case 54: avatar.setImageResource(R.drawable.sushi4); break;
                    case 55: avatar.setImageResource(R.drawable.sushi5); break;
                    case 56: avatar.setImageResource(R.drawable.sushi6); break;
                    case 57: avatar.setImageResource(R.drawable.sushi7); break;
                    case 58: avatar.setImageResource(R.drawable.sushi8); break;
                    case 59: avatar.setImageResource(R.drawable.sushi9); break;
                    case 61: avatar.setImageResource(R.drawable.character1); break;
                    case 62: avatar.setImageResource(R.drawable.character2); break;
                    case 63: avatar.setImageResource(R.drawable.character3); break;
                    case 64: avatar.setImageResource(R.drawable.character4); break;
                    case 65: avatar.setImageResource(R.drawable.character5); break;
                    case 66: avatar.setImageResource(R.drawable.character6); break;
                    case 67: avatar.setImageResource(R.drawable.character7); break;
                    case 68: avatar.setImageResource(R.drawable.character8); break;
                    case 69: avatar.setImageResource(R.drawable.character9); break;
                    case 71: avatar.setImageResource(R.drawable.plant1); break;
                    case 72: avatar.setImageResource(R.drawable.plant2); break;
                    case 73: avatar.setImageResource(R.drawable.plant3); break;
                    case 74: avatar.setImageResource(R.drawable.plant4); break;
                    case 75: avatar.setImageResource(R.drawable.plant5); break;
                    case 76: avatar.setImageResource(R.drawable.plant6); break;
                    case 77: avatar.setImageResource(R.drawable.plant7); break;
                    case 78: avatar.setImageResource(R.drawable.plant8); break;
                    case 79: avatar.setImageResource(R.drawable.plant9); break;
                    case 81: avatar.setImageResource(R.drawable.vegetable1); break;
                    case 82: avatar.setImageResource(R.drawable.vegetable2); break;
                    case 83: avatar.setImageResource(R.drawable.vegetable3); break;
                    case 84: avatar.setImageResource(R.drawable.vegetable4); break;
                    case 85: avatar.setImageResource(R.drawable.vegetable5); break;
                    case 86: avatar.setImageResource(R.drawable.vegetable6); break;
                    case 87: avatar.setImageResource(R.drawable.vegetable7); break;
                    case 88: avatar.setImageResource(R.drawable.vegetable8); break;
                    case 89: avatar.setImageResource(R.drawable.vegetable9); break;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }









}
