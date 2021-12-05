package com.cria.agora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Classes.MyAdapter;
import Classes.PerfilAtividade;

public class Atividades extends AppCompatActivity {

    private  DatabaseReference mReference;
    private RecyclerView mRecyclerAtividades;
    private ArrayList<PerfilAtividade> listaAtividades;
    private MyAdapter adapterAtividade;
    public static Boolean isPerformance = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);


        //iniciar elementos

        mRecyclerAtividades = (RecyclerView) findViewById(R.id.meuRecycler);
        mRecyclerAtividades.setLayoutManager(new LinearLayoutManager(this));


        mReference = FirebaseDatabase.getInstance().getReference().child("perfisAtividades");

        //adicionar dados do firebase
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAtividades = new ArrayList<PerfilAtividade>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    PerfilAtividade perfil = snapshot.getValue(PerfilAtividade.class);
                    listaAtividades.add(perfil);

                }


                adapterAtividade = new MyAdapter(Atividades.this, listaAtividades);
                mRecyclerAtividades.setAdapter(adapterAtividade);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "opsss", Toast.LENGTH_LONG).show();
            }
        });



    }


}
