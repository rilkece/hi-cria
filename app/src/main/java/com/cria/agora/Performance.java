package com.cria.agora;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import Classes.MyPerformanceAdapter;
import DadosUsuario.UserWeekPerformance;

public class Performance extends AppCompatActivity {

    private  DatabaseReference mReference;
    private RecyclerView mRecyclerAtividades;
    private ArrayList<UserWeekPerformance> listaAtividades;
    private MyPerformanceAdapter adapterAtividade;

    private ImageButton btnVoltar;
    public static TextView txtActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);


        //iniciar elementos

        mRecyclerAtividades = (RecyclerView) findViewById(R.id.meuRecyclerPerformance);
        mRecyclerAtividades.setLayoutManager(new LinearLayoutManager(this));
        btnVoltar = findViewById(R.id.imgbtnPerformanceBack);
        txtActivity = findViewById(R.id.txtPerformanceActivity);

        // iniciar ações
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
                if(sharedPreferences.getLong("tipoUser", 1) ==0) {

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
        FirebaseDatabase.getInstance().getReference().child("perfisAtividades").child(Week.codAtividade).child("nomeAtividade").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtActivity.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference = FirebaseDatabase.getInstance().getReference().child("users");

        //adicionar dados do firebase
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaAtividades = new ArrayList<UserWeekPerformance>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String notNulo = String.valueOf(snapshot.child("desempenho").child("weekScore").child(String.valueOf(Week.codAtividade)).getValue());
                    if (notNulo.equals("null")) {
                        notNulo ="0";
                    }
                    final UserWeekPerformance userWeek = new UserWeekPerformance(String.valueOf(snapshot.child("firstName").getValue())
                            , String.valueOf(snapshot.child("surName").getValue())
                            , String.valueOf(snapshot.child("escola").getValue())
                            , notNulo, String.valueOf(snapshot.child("matricula").getValue()));
                    //Toast.makeText(getApplicationContext(), userWeek.getWeekScore(), Toast.LENGTH_SHORT ).show();
                   Collections.sort(listaAtividades, new  Comparator<UserWeekPerformance>() {
                       @Override
                       public int compare(UserWeekPerformance o1, UserWeekPerformance o2) {
                           if ( Integer.parseInt(o1.getWeekScore())>Integer.parseInt(o2.getWeekScore())){
                                   //Toast.makeText(getApplicationContext(), o1.getWeekScore()+" é maior que "+o2.getWeekScore(), Toast.LENGTH_SHORT ).show();
                                return -1;
                           }else {
                                   //Toast.makeText(getApplicationContext(), o1.getWeekScore()+" é menor que "+o2.getWeekScore(), Toast.LENGTH_SHORT ).show();
                                   return 1;
                               }


                       }
                   });
                   if (Long.parseLong(snapshot.child("tipoUser").getValue().toString()) != 0  && Long.parseLong(userWeek.getWeekScore())>0) {
                       listaAtividades.add(userWeek);
                       adapterAtividade = new MyPerformanceAdapter(Performance.this, listaAtividades);
                       mRecyclerAtividades.setAdapter(adapterAtividade);
                   }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "opsss", Toast.LENGTH_LONG).show();
            }
        });



 }
    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

}