package com.cria.agora;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EscolhaTipoAtividade extends AppCompatActivity {

    private ImageButton ibtnAtv1;
    private ImageButton ibtnAtv2;
    private ImageButton ibtnAtv3;
    private ImageButton ibtnAtv4;
    private ImageButton ibtnAtv5;
    private ImageButton ibtnAtv6;
    private ImageButton ibtnAtv7;
    private ImageButton ibtnAtv8;
    private ImageButton ibtnAtv9;
    private ImageButton ibtnAtv10;
    private ImageButton ibtnAtv11;
    private ImageButton ibtnAtv12;
    private TextView txtEscolha;

    public static int iAtividadeAtual=0;
    public static int COD_EDIT_ATV = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_tipo_atividade);


        //inicializar elementos

        if (COD_EDIT_ATV == 1) {

        }else  if (SetActivity.ipaginaAtividade >0){
            SetActivity.ipaginaAtividade=SetActivity.ipaginaAtividade+1;

        }else {
            SetActivity.ipaginaAtividade=1;
        }
        ibtnAtv1 = findViewById(R.id.ibtnTipoAtv1);
        ibtnAtv2 = findViewById(R.id.ibtnTipoAtv2);
        ibtnAtv3 = findViewById(R.id.ibtnTipoAtv3);
        ibtnAtv4 = findViewById(R.id.ibtnTipoAtv4);
        ibtnAtv5 = findViewById(R.id.ibtnTipoAtv5);
        ibtnAtv6 = findViewById(R.id.ibtnTipoAtv6);
        ibtnAtv7 = findViewById(R.id.ibtnTipoAtv7);
        ibtnAtv8 = findViewById(R.id.ibtnTipoAtv8);
        ibtnAtv9 = findViewById(R.id.ibtnTipoAtv9);
        ibtnAtv10 = findViewById(R.id.ibtnTipoAtv10);
        ibtnAtv11 = findViewById(R.id.ibtnTipoAtv11);
        ibtnAtv12 = findViewById(R.id.ibtnTipoAtv12);
        txtEscolha = findViewById(R.id.txtEscolhaAtv);
        //String escolha = R.string.escolhaAtividade+String.valueOf(SetActivity.ipaginaAtividade)+".";

            txtEscolha.setText("Escolha a atividade da página " + String.valueOf(SetActivity.ipaginaAtividade) + ".");


        //ação dos botões
        ibtnAtv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 1;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 2;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 3;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 4;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 5;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 6;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 7;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 8;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 9;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 10;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 11;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });
        ibtnAtv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iAtividadeAtual = 12;
                Intent i = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(i);
            }
        });

    }
}
