package com.cria.agora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Classes.MyGamePerformanceAdapter;
import Classes.MyPerformanceAdapter;
import DadosUsuario.UserGamePerformance;
import DadosUsuario.UserWeekPerformance;

public class GamePerformance extends AppCompatActivity {

    private DatabaseReference mReference;
    private RecyclerView mRecyclerAtividades;
    private ArrayList<UserGamePerformance> listaScores;
    private MyGamePerformanceAdapter adapterGame;

    private ImageButton btnVoltar;
    public static String stgGame;
    public static String stgNameBest;
    public static String stgNameGame;
    private  TextView txtGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_performance);

        //iniciar elementos

        mRecyclerAtividades = (RecyclerView) findViewById(R.id.meuRecyclerGamePerformance);
        mRecyclerAtividades.setLayoutManager(new LinearLayoutManager(this));
        btnVoltar = findViewById(R.id.imgbtnGamePerformanceBack);
        txtGame = findViewById(R.id.txtGamePerformanceActivity);

        txtGame.setText(stgGame);


        // iniciar ações
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voltar(stgGame);
            }
        });

        mReference = FirebaseDatabase.getInstance().getReference().child("users");
        //adicionar dados do firebase
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaScores = new ArrayList<UserGamePerformance>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String notNulo = String.valueOf(snapshot.child("desempenho").child("games").child(stgNameGame).child(stgNameBest).getValue());
                    if (notNulo.equals("null")) {
                        notNulo = "0";
                    }
                    final UserGamePerformance usergame = new UserGamePerformance(String.valueOf(snapshot.child("firstName").getValue())
                            , String.valueOf(snapshot.child("surName").getValue())
                            , String.valueOf(snapshot.child("escola").getValue())
                            , notNulo
                            , String.valueOf(snapshot.child("matricula").getValue()));
                    //Toast.makeText(getApplicationContext(), userWeek.getWeekScore(), Toast.LENGTH_SHORT ).show();
                    Collections.sort(listaScores, new Comparator<UserGamePerformance>() {
                                @Override
                                public int compare(UserGamePerformance o1, UserGamePerformance o2) {
                                    if (Integer.parseInt(o1.getgameScore()) > Integer.parseInt(o2.getgameScore())) {
                                        //Toast.makeText(getApplicationContext(), o1.getWeekScore()+" é maior que "+o2.getWeekScore(), Toast.LENGTH_SHORT ).show();
                                        return -1;
                                    } else {
                                        //Toast.makeText(getApplicationContext(), o1.getWeekScore()+" é menor que "+o2.getWeekScore(), Toast.LENGTH_SHORT ).show();
                                        return 1;
                                    }
                                }
                    });
                                    if (Long.parseLong(snapshot.child("tipoUser").getValue().toString()) != 0 && Long.parseLong(usergame.getgameScore())>0) {
                                        listaScores.add(usergame);
                                        adapterGame = new MyGamePerformanceAdapter(GamePerformance.this, listaScores);
                                        mRecyclerAtividades.setAdapter(adapterGame);

                                    }
                                    }
                                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
   }

    private void voltar(String stgGame) {
        Intent intent = new Intent();
        switch (stgGame){
            case "16 LETTERS":
                intent = new Intent(getApplicationContext(), GameBuildWords.class);
                startActivity(intent);
                finish();
                break;
            case "Memory Game - Names":
                intent = new Intent(getApplicationContext(), GameMatching.class);
                startActivity(intent);
                finish();
                break;
            case "Hangman":
                intent = new Intent(getApplicationContext(), GameHangman.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}
