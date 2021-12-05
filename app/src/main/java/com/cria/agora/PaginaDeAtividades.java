package com.cria.agora;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Classes.PageActivities;
import Classes.SoundPlayer;
import TiposdeAtividades.ButtonsAlterna;
import TiposdeAtividades.PerguntaAlterna;

public class PaginaDeAtividades extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public static String codPageDay;
    private int pontuacaoAcumulada = 0;
    private MaterialButton btnVoltar;
    private MaterialButton btnEditAtv;
    private TextView pontuacao;
    private FirebaseDatabase fireDataAtividades;
    private DatabaseReference dataAtividade;
    private DatabaseReference dataPageActivity;
    private DatabaseReference parametersReference;
    private String dia;
    private String pagina = "page";

    private ImageButton floatNextpage;

    private TextView pergunta;

    private int codpageAtual = 0;

    private PageActivities pageActivities;
    private RoundCornerProgressBar progressBar;
    private PerguntaAlterna perguntaAlterna;
    private ButtonsAlterna buttonsAlterna;

    private MaterialButton btnAlt1;
    private MaterialButton btnAlt2;
    private MaterialButton btnAlt3;
    private MaterialButton btnAlt4;
    private MaterialButton btnAlt5;
    private MaterialButton btnAlt6;
    private MaterialButton btnAlt7;
    private MaterialButton btnAlt8;
    private MaterialButton btnAlt9;
    private MaterialButton btnVerify;
    private Boolean clickBtnAlt1, clickBtnAlt2, clickBtnAlt3, clickBtnAlt4, clickBtnAlt5, clickBtnAlt6, clickBtnAlt7, clickBtnAlt8, clickBtnAlt9;

    private ImageView imagePages;
    private FloatingActionButton playAudio;
    private VideoView videoView;
    private WebView webPDFView;
    private ProgressBar progressPDFBar;

    private SoundPlayer soundPlayer;
    private FloatingActionButton fabMicAtv6;

    private TextView textAnswer;
    private TextView txtLoading;
    private IconRoundCornerProgressBar iconRoundCornerProgressBarAtv7;
    private int iTimesListened = 0;

    private int iPlay = 0;

    private TextInputEditText inputEditText;
    private TextView txtComplemento;
    private TextView txtAnswerFromButtons;

    private MediaPlayer mediaPlayer;

    private String iTipoAtv;
    private List<String> stringList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_de_atividades_activity);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        //iniciar elementos de layout
        progressBar = findViewById(R.id.progressBarPages);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnEditAtv = findViewById(R.id.btnEditAtv);
        pontuacao = findViewById(R.id.txtScorePage);
        pontuacao.setText(String.valueOf(pontuacaoAcumulada));
        floatNextpage = findViewById(R.id.imgBtnNextPage);
        soundPlayer = new SoundPlayer(getApplicationContext());
        clickBtnAlt1 = false; clickBtnAlt2 = false; clickBtnAlt3 = false; clickBtnAlt4 = false; clickBtnAlt5 = false; clickBtnAlt6 = false; clickBtnAlt7 = false; clickBtnAlt8 = false; clickBtnAlt9 = false;


        floatNextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getLong("tipoUser", 1) == 0){
                    Intent iVolta = new Intent(getApplicationContext(), MainAdm.class);
                    startActivity(iVolta);
                    finish();
                }else{
                    Intent iVolta = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(iVolta);
                    finish();
                }

            }
        });

        if (sharedPreferences.getLong("tipoUser", 1) == 0){
            btnEditAtv.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState == null) {
            //iniciar firebasedatabase
            fireDataAtividades = FirebaseDatabase.getInstance();


            //setar pageActivities com dados da activity do dia
            dataPageActivity = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("numPages");
            dataPageActivity.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pageActivities = new PageActivities(Integer.valueOf(dataSnapshot.getValue().toString()), codPageDay, Week.codAtividade);
                    progressBar.setMax(pageActivities.getNumPage());

                   // Toast.makeText(getApplicationContext(), codPageDay, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            construirPaginas();

        }


    }

    private void nextPage() {

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        if (pageActivities.getNumPage()>codpageAtual){
            if (!(stringList==null)){
                stringList.clear();
            }
            construirPaginas();
            floatNextpage.setVisibility(View.INVISIBLE);
            soundPlayer.playDingSound();
        }else {
            if (!(stringList==null)){
                stringList.clear();
            }
            if(!(mediaPlayer ==null)){
                mediaPlayer.stop();
            }
            final Intent iMain;

            if(sharedPreferences.getLong("tipoUser", 1) ==0) {
                iMain = new Intent(getApplicationContext(), MainAdm.class);

            }else {
                iMain = new Intent(getApplicationContext(), MainActivity.class);
            }

            FirebaseDatabase.getInstance().getReference("schools").child(String.valueOf(sharedPreferences.getLong("utbCRIA", 0))).child("desempenho").child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null){
                        final long scoreCRIA = Long.parseLong(dataSnapshot.getValue().toString())+pontuacaoAcumulada;
                        DatabaseReference dataScoreCRIA = FirebaseDatabase.getInstance().getReference("schools").child(String.valueOf(sharedPreferences.getLong("utbCRIA", 0))).child("desempenho").child("score");
                        dataScoreCRIA.setValue(scoreCRIA);
                    }else {
                        DatabaseReference dataScoreCRIA = FirebaseDatabase.getInstance().getReference("schools").child(String.valueOf(sharedPreferences.getLong("utbCRIA", 0))).child("desempenho").child("score");
                        dataScoreCRIA.setValue(pontuacaoAcumulada);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            final DatabaseReference datanewScore = fireDataAtividades.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("score");
            datanewScore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    datanewScore.setValue(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString())+pontuacaoAcumulada);
                    startActivity(iMain);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    datanewScore.setValue(pontuacaoAcumulada);
                    startActivity(iMain);
                    finish();

                }
            });
            final DatabaseReference dataweekScore = fireDataAtividades.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("weekScore").child(Week.codAtividade);
            dataweekScore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue()==null){
                        dataweekScore.setValue(pontuacaoAcumulada);
                    }else {
                        dataweekScore.setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + pontuacaoAcumulada);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final DatabaseReference dataDayScore = fireDataAtividades.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("dayScore").child(codPageDay);
            dataDayScore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue()==null){
                        dataDayScore.setValue(pontuacaoAcumulada);
                    }else {
                        dataDayScore.setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + pontuacaoAcumulada);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            soundPlayer.playCompletedSound();
        }
    }

    private void construirPaginas() {
        if(!(mediaPlayer ==null)){
            mediaPlayer.stop();
        }
        //Buscar o tipo de atividade e setar o fragment de acordo com ela
        codpageAtual = codpageAtual+1;
        progressBar.setProgress(codpageAtual);

        dataAtividade = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("tipoAtv");
        dataAtividade.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                iTipoAtv = String.valueOf(dataSnapshot.getValue());
                btnEditAtv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    SetActivity.ipaginaAtividade=codpageAtual;
                    EscolhaTipoAtividade.COD_EDIT_ATV=1;
                    Intent iEdit = new Intent(getApplicationContext(), EscolhaTipoAtividade.class);
                    startActivity(iEdit);
                    finish();
                    }
                });


                switch (iTipoAtv){
                    case "1":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv1())
                                .commitNow();

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela
                        parametersFragmentPoints();
                        //setar pergunta, esse parâmetro tem todas as telas
                        parametersPergunta(Integer.valueOf(iTipoAtv));
                        //setar parâmetros específicos
                        parametersPerguntaAlterna(1);
                        //verificar se atividae já foi realizada
                        verificaAtividade();

                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getCodPag(), Toast.LENGTH_SHORT).show();

                        break;
                    case "2":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv2())
                                .commitNow();

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(2);
                        } catch (IOException e) {
                           //Toast.makeText(getApplicationContext(),"nao deu certo "+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //setar parâmetros específicos - igual pra todos que tem alternativa
                        parametersPerguntaAlterna(2);
                        //verificar se atividae já foi realizada - igual pra todos
                        verificaAtividade();

                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getCodPag(), Toast.LENGTH_SHORT).show();

                        break;
                    case "3":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv3())
                                .commitNow();
                        txtLoading= findViewById(R.id.txtLoadingAtv3);

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(3);
                        } catch (IOException e) {
                           //Toast.makeText(getApplicationContext(),"nao deu certo, pois  "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        //setar parâmetros específicos - igual pra todos que tem alternativa
                        parametersPerguntaAlterna(3);
                        //verificar se atividae já foi realizada - igual pra todos
                        verificaAtividade();

                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getCodPag(), Toast.LENGTH_SHORT).show();

                        break;
                    case "4":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv4())
                                .commitNow();

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(4);
                        } catch (IOException e) {
                           //Toast.makeText(getApplicationContext(),"nao deu certo, pois  "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        //setar parâmetros específicos - igual pra todos que tem alternativa
                        parametersPerguntaAlterna(4);
                        //verificar se atividae já foi realizada - igual pra todos
                        verificaAtividade();

                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getCodPag(), Toast.LENGTH_SHORT).show();

                        break;
                    case "5":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv5())
                                .commitNow();

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(5);
                        } catch (IOException e) {
                           //Toast.makeText(getApplicationContext(),"nao deu certo, pois  "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        //verificar se atividade já foi realizada - igual pra todos
                        verificaAtividade();
                        floatNextpage.setVisibility(View.VISIBLE);
                        break;
                    case "6":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv6())
                                .commitNow();

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //setar parâmetros específicos - igual pra todos que tem alternativa
                        parametersVoiceAlterna(6);
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(6);
                        } catch (IOException e) {
                           //Toast.makeText(getApplicationContext(),"nao deu certo, pois  "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        //verificar se atividae já foi realizada - igual pra todos
                        verificaAtividade();
                        floatNextpage.setVisibility(View.VISIBLE);
                        break;
                    case "7":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv7())
                                .commitNow();

                        txtLoading= findViewById(R.id.txtLoadingAtv7);

                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        iconRoundCornerProgressBarAtv7 = findViewById(R.id.progressAtv7);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(7);
                        } catch (IOException e) {
                           //Toast.makeText(getApplicationContext(),"nao deu certo, pois  "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        //verificar se atividae já foi realizada - igual pra todos
                        verificaAtividade();
                        break;
                    case "8":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv8())
                                .commitNow();
                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar inputText
                        inputEditText = findViewById(R.id.inputAtv8);
                        //setar text answer
                        textAnswer = findViewById(R.id.respostaAtv8);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //setar complemento e alternativas corretas
                        carregaAlternas(8);
                        //setar botao verifica
                        btnVerify = findViewById(R.id.btnVerifyAtv8);
                        btnVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verificaInput();
                            }
                        });

                        verificaAtividade();
                        floatNextpage.setVisibility(View.VISIBLE);
                        break;
                    case "9":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv9())
                                .commitNow();
                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar inputText
                        inputEditText = findViewById(R.id.inputAtv9);
                        //setar text answer
                        textAnswer = findViewById(R.id.respostaAtv9);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //setar complemento e alternativas corretas
                        carregaAlternas(9);
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(9);
                        } catch (IOException e) {
                            //Toast.makeText(getApplicationContext(),"nao deu certo "+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //setar botao verifica
                        btnVerify = findViewById(R.id.btnVerifyAtv9);
                        btnVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verificaInput();
                            }
                        });

                        verificaAtividade();
                        floatNextpage.setVisibility(View.VISIBLE);
                        break;
                    case "10":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2,new fragAtv10())
                                .commitNow();
                        txtLoading= findViewById(R.id.txtLoadingAtv10);
                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar inputText
                        inputEditText = findViewById(R.id.inputAtv10);
                        //setar text answer
                        textAnswer = findViewById(R.id.respostaAtv10);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //setar complemento e alternativas corretas
                        carregaAlternas(10);
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(10);
                        } catch (IOException e) {
                            //Toast.makeText(getApplicationContext(),"nao deu certo "+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //setar botao verifica
                        btnVerify = findViewById(R.id.btnVerifyAtv10);
                        btnVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verificaInput();
                            }
                        });

                        verificaAtividade();
                        floatNextpage.setVisibility(View.VISIBLE);
                        break;
                    case "11":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2,new fragAtv11())
                                .commitNow();
                        perguntaAlterna = new PerguntaAlterna();
                        perguntaAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar inputText
                        inputEditText = findViewById(R.id.inputAtv11);
                        //setar text answer
                        textAnswer = findViewById(R.id.respostaAtv11);
                        //setar pontos da tela - igual pra todos
                        parametersFragmentPoints();
                        //setar pergunta - igual pra todos que tem perguta ou instrucao.
                        parametersPergunta(Integer.parseInt(iTipoAtv));
                        //setar complemento e alternativas corretas
                        carregaAlternas(11);
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(11);
                        } catch (IOException e) {
                            //Toast.makeText(getApplicationContext(),"nao deu certo "+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //setar botao verifica
                        btnVerify = findViewById(R.id.btnVerifyAtv11);
                        btnVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verificaInput();
                            }
                        });

                        verificaAtividade();
                        floatNextpage.setVisibility(View.VISIBLE);
                        break;
                    case "12":
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, new fragAtv12())
                                .commitNow();
                        txtAnswerFromButtons = findViewById(R.id.txtAnswerAtv12);
                        txtLoading= findViewById(R.id.txtLoadingAtv12);

                        buttonsAlterna = new ButtonsAlterna();
                        buttonsAlterna.setCodPag(codPageDay+codpageAtual);
                        //setar pontos da tela
                        parametersFragmentPoints();
                        //setar pergunta, esse parâmetro tem todas as telas
                        parametersPergunta(Integer.valueOf(iTipoAtv));
                        //setar complemento e alternativas corretas
                        carregaAlternas(12);
                        //carregar imagens - igual para todos que têm imagens
                        try {
                            loadMedia(12);
                        } catch (IOException e) {
                            //Toast.makeText(getApplicationContext(),"nao deu certo "+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //setar botao verifica
                        btnVerify = findViewById(R.id.btnVerifyAtv12);
                        btnVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                verificaTxtAnswer();
                            }
                        });                        //verificar se atividae já foi realizada
                        verificaAtividade();

                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getCodPag(), Toast.LENGTH_SHORT).show();

                        break;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "ops, sem tipo de atividade", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verificaTxtAnswer() {
        String resposta = buttonsAlterna.getResposta();

        if (resposta.contentEquals(txtAnswerFromButtons.getText())){
            txtAnswerFromButtons.setTextColor(getResources().getColor(R.color.verde_correto));
            btnAlt1.setVisibility(View.INVISIBLE);
            btnAlt2.setVisibility(View.INVISIBLE);
            btnAlt3.setVisibility(View.INVISIBLE);
            btnAlt4.setVisibility(View.INVISIBLE);
            btnAlt5.setVisibility(View.INVISIBLE);
            btnAlt6.setVisibility(View.INVISIBLE);
            btnAlt7.setVisibility(View.INVISIBLE);
            btnAlt8.setVisibility(View.INVISIBLE);
            btnAlt9.setVisibility(View.INVISIBLE);
            pontuacaoAcumulada = pontuacaoAcumulada+buttonsAlterna.getPontos();
            pontuacao.setText(String.valueOf("+"+pontuacaoAcumulada));
            btnVerify.setVisibility(View.GONE);
            soundPlayer.playCorrectSound();
            floatNextpage.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "resposta certa", Toast.LENGTH_SHORT).show();

        }else {
            soundPlayer.playWrongSound();
           // Toast.makeText(getApplicationContext(), "resposta errada", Toast.LENGTH_SHORT).show();

        }
    }

    private void verificaInput() {
        textAnswer.setVisibility(View.VISIBLE);
        String resposta = Objects.requireNonNull(inputEditText.getText()).toString();
        if(resposta.equals(perguntaAlterna.getAlt1())
                || resposta.equals(perguntaAlterna.getAlt2())
                || resposta.equals(perguntaAlterna.getAlt3())
                || resposta.equals(perguntaAlterna.getAlt4())){
            textAnswer.setText(resposta);
            textAnswer.setTextColor(getResources().getColor(R.color.branco));
            textAnswer.setBackgroundColor(getResources().getColor(R.color.verde_correto));
            pontuacaoAcumulada = pontuacaoAcumulada+perguntaAlterna.getPontos();
            pontuacao.setText(String.valueOf("+"+pontuacaoAcumulada));
            btnVerify.setVisibility(View.GONE);
            soundPlayer.playCorrectSound();

        }else{
            inputEditText.setText("");
            textAnswer.setText(resposta);
            textAnswer.setTextColor(getResources().getColor(R.color.branco));
            textAnswer.setBackgroundColor(getResources().getColor(R.color.vermelho_errado));
            TastyToast.makeText(getApplicationContext(), "Wrong answer!", TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            soundPlayer.playWrongSound();
        }

        // pontuacaoAcumulada = pontuacaoAcumulada+perguntaAlterna.getPontos();
        //pontuacao.setText(String.valueOf("+"+pontuacaoAcumulada));
    }

    private void carregaAlternas(final int atvInt) {

        stringList = new ArrayList<String>();

        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("correctAlt");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String complementoStr;
                if (atvInt==12){
                    buttonsAlterna.setAltCerta(String.valueOf(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                    complementoStr = buttonsAlterna.getAltCerta();
                }else {
                    perguntaAlterna.setAltCerta(String.valueOf(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                    complementoStr = perguntaAlterna.getAltCerta();
                }

                switch (atvInt){
                    case 8:
                        txtComplemento = findViewById(R.id.complementoAtv8);

                        break;
                    case 9:
                        txtComplemento = findViewById(R.id.complementoAtv9);

                        break;
                    case 10:
                        txtComplemento = findViewById(R.id.complementoAtv10);
                        break;
                    case 11:
                        txtComplemento = findViewById(R.id.complementoAtv11);
                        break;
                    case 12:
                        btnAlt1 = findViewById(R.id.btnWordAtv12_1);
                        btnAlt2 = findViewById(R.id.btnWordAtv12_2);
                        btnAlt3 = findViewById(R.id.btnWordAtv12_3);
                        btnAlt4 = findViewById(R.id.btnWordAtv12_4);
                        btnAlt5 = findViewById(R.id.btnWordAtv12_5);
                        btnAlt6 = findViewById(R.id.btnWordAtv12_6);
                        btnAlt7 = findViewById(R.id.btnWordAtv12_7);
                        btnAlt8 = findViewById(R.id.btnWordAtv12_8);
                        btnAlt9 = findViewById(R.id.btnWordAtv12_9);
                        txtComplemento = findViewById(R.id.complementoAtv12);
                        break;
                }

                txtComplemento.setText(complementoStr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "algum erro com correctAlt", Toast.LENGTH_SHORT).show();

            }
        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("resposta");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (atvInt){
                    case 12:
                        String espaco = Objects.requireNonNull(dataSnapshot.getValue()).toString()+" ";
                        buttonsAlterna.setResposta(espaco);
                        //Toast.makeText(getApplicationContext(), espaco, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt1");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt1(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt1());
                }else {
                    perguntaAlterna.setAlt1(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt2");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt2(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt2());
                }else {
                    perguntaAlterna.setAlt2(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt3");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt3(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt3());
                }else {
                    perguntaAlterna.setAlt3(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt4");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt4(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt4());
                }else {
                    perguntaAlterna.setAlt4(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt5");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt5(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt5());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt6");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt6(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt6());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt7");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt7(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt7());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt8");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt8(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt8());
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt9");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (atvInt==12){
                    buttonsAlterna.setAlt9(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    stringList.add(buttonsAlterna.getAlt9());
                    setButtonsToAnswer();
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });





    }

    private void setButtonsToAnswer() {

        Collections.shuffle(stringList);
        if (stringList.get(0).equals("")){
            btnAlt1.setVisibility(View.GONE);
        }else {
            btnAlt1.setText(stringList.get(0));
        }
        if (stringList.get(1).equals("")){
            btnAlt2.setVisibility(View.GONE);
        }else {
            btnAlt2.setText(stringList.get(1));
        }
        if (stringList.get(2).equals("")){
            btnAlt3.setVisibility(View.GONE);
        }else {
            btnAlt3.setText(stringList.get(2));
        }
        if (stringList.get(3).equals("")){
            btnAlt4.setVisibility(View.GONE);
        }else {
            btnAlt4.setText(stringList.get(3));
        }
        if (stringList.get(4).equals("")){
            btnAlt5.setVisibility(View.GONE);
        }else {
            btnAlt5.setText(stringList.get(4));
        }
        if (stringList.get(5).equals("")){
            btnAlt6.setVisibility(View.GONE);
        }else {
            btnAlt6.setText(stringList.get(5));
        }
        if (stringList.get(6).equals("")){
            btnAlt7.setVisibility(View.GONE);
        }else {
            btnAlt7.setText(stringList.get(6));
        }
        if (stringList.get(7).equals("")){
            btnAlt8.setVisibility(View.GONE);
        }else {
            btnAlt8.setText(stringList.get(7));
        }
        if (stringList.get(8).equals("")){
            btnAlt9.setVisibility(View.GONE);
        }else {
            btnAlt9.setText(stringList.get(8));
        }







        txtAnswerFromButtons.setText("");
        btnAlt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt1){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt1.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt1.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));



                    clickBtnAlt1=true;
                }else {
                    String backAns = btnAlt1.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt1.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));

                    clickBtnAlt1=false;
                }

            }
        });
        btnAlt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt2){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt2.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt2.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt2=true;
                }else {
                    String backAns = btnAlt2.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt2.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));

                    clickBtnAlt2=false;
                }

            }
        });
        btnAlt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt3){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt3.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt3.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt3=true;
                }else {
                    String backAns = btnAlt3.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt3.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt3=false;
                }

            }
        });
        btnAlt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt4){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt4.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt4.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt4=true;
                }else {
                    String backAns = btnAlt4.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt4.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt4=false;
                }

            }
        });
        btnAlt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt5){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt5.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt5.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt5=true;
                }else {
                    String backAns = btnAlt5.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt5.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt5=false;
                }

            }
        });
        btnAlt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt6){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt6.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt6.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));

                    clickBtnAlt6=true;
                }else {
                    String backAns = btnAlt6.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt6.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt6=false;
                }

            }
        });
        btnAlt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt7){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt7.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt7.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt7=true;
                }else {
                    String backAns = btnAlt7.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt7.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt7=false;
                }

            }
        });
        btnAlt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt8){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt8.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt8.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt8=true;
                }else {
                    String backAns = btnAlt8.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt8.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt8=false;
                }

            }
        });
        btnAlt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickBtnAlt9){
                    String ans = txtAnswerFromButtons.getText().toString()+btnAlt9.getText()+" ";
                    txtAnswerFromButtons.setText(ans);
                    btnAlt9.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));


                    clickBtnAlt9=true;
                }else {
                    String backAns = btnAlt9.getText()+" ";
                    txtAnswerFromButtons.setText(txtAnswerFromButtons.getText().toString().replace(backAns, ""));
                    btnAlt9.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary_dark));
                    clickBtnAlt9=false;
                }

            }
        });

    }

    private void loadMedia(int iMages) throws IOException {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("imgPages").child(Week.codAtividade).child(Week.diaDaSemana).child(codPageDay+codpageAtual);
        switch (iMages){
            case 1:
                break;
            case 2:
                imagePages = findViewById(R.id.imgAtv2);
                Glide.with(getApplicationContext())
                        .load(storageReference)
                        .into(imagePages);
                break;
            case 3:
                playAudio = findViewById(R.id.playerAtv3);
                mediaPlayer = new MediaPlayer();

                playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference dataAudio = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                        dataAudio.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                    try {
                                        if (!mediaPlayer.isPlaying()) {
                                            txtLoading.setVisibility(View.VISIBLE);
                                            playAudio.hide();
                                            Toast.makeText(getApplicationContext(), "Loading audio, please wait...", Toast.LENGTH_LONG).show();
                                            final String url = String.valueOf(dataSnapshot.getValue()); // your URL here
                                            mediaPlayer.setDataSource(url);
                                            mediaPlayer.prepareAsync();
                                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mp) {
                                                    playAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                                    mediaPlayer.stop();
                                                    mediaPlayer.reset();
                                                }
                                            });
                                            //Toast.makeText(getApplicationContext(), "setou url", Toast.LENGTH_LONG).show();

                                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    mp.setLooping(false);
                                                    mediaPlayer.start();
                                                    txtLoading.setVisibility(View.INVISIBLE);
                                                    playAudio.setImageResource(R.drawable.ic_stop_white_24dp);
                                                    playAudio.show();
                                                   // Toast.makeText(getApplicationContext(), "erer"+mediaPlayer.isPlaying(), Toast.LENGTH_LONG).show();

                                                }
                                            });
                                        }else {
                                            mediaPlayer.stop();
                                            mediaPlayer.reset();
                                            playAudio.hide();
                                            playAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                            playAudio.show();
                                            txtLoading.setVisibility(View.INVISIBLE);
                                            //Toast.makeText(getApplicationContext(), "oi"+mediaPlayer.isPlaying(), Toast.LENGTH_LONG).show();

                                        }
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                        //Toast.makeText(getApplicationContext(), "deu erro", Toast.LENGTH_LONG).show();

                                    }

                                    //Toast.makeText(getApplicationContext(), "async", Toast.LENGTH_LONG).show();


                                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        //Toast.makeText(getApplicationContext(),url, Toast.LENGTH_LONG).show();
                                        return false;
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "áudio nao encontrado.", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });


                break;
            case 4:
                videoView = findViewById(R.id.playerAtv4);
                final ImageButton imgPlayStop = findViewById(R.id.btnPlayStopAtv4);
                imgPlayStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog mDialog = new ProgressDialog(PaginaDeAtividades.this);

                        DatabaseReference dataVideo = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                        dataVideo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {
                                    if (!videoView.isPlaying()){
                                        if(videoView.getTag(1) == null){
                                            Toast.makeText(getApplicationContext(), "é nulo", Toast.LENGTH_LONG).show();
                                        mDialog.setMessage("please wait");
                                        mDialog.setCanceledOnTouchOutside(false);
                                        mDialog.show();
                                        final String urlVideo = String.valueOf(dataSnapshot.getValue()); // your URL here
                                        Uri uriVideo = Uri.parse(urlVideo);
                                        videoView.setVideoURI(uriVideo);
                                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                imgPlayStop.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                            }
                                        });
                                        }
                                    }else {
                                        videoView.pause();
                                        imgPlayStop.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                    }

                                }catch (Exception ex){

                                }


                                videoView.requestFocus();
                                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mDialog.dismiss();
                                        mp.setLooping(false);
                                        ViewGroup.LayoutParams params = videoView.getLayoutParams();
                                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                        videoView.setLayoutParams(params);
                                        videoView.start();
                                        imgPlayStop.setImageResource(R.drawable.ic_stop_white_24dp);

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                break;
            case 5:
                webPDFView = findViewById(R.id.webPDFview);
                progressPDFBar = findViewById(R.id.progressPDFbar);
                webPDFView.getSettings().setJavaScriptEnabled(true);
                DatabaseReference dataPDF = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                dataPDF.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String urlPDF = String.valueOf(dataSnapshot.getValue()); // your URL here
                        webPDFView.loadUrl(urlPDF);
                        webPDFView.setWebViewClient(new WebViewClient(){
                            public void  onPageFinished(WebView web, String url){
                                progressPDFBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            case 6:
                fabMicAtv6 = findViewById(R.id.fabAtv6);
                final TextView textMain = findViewById(R.id.txtMainAtv6);
                DatabaseReference datatextMain = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("correctAlt");
                datatextMain.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textMain.setText(String.valueOf(dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                textAnswer = findViewById(R.id.txtAnswerAtv6);
                fabMicAtv6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        speak();
                    }
                });

                break;
            case 7:

                playAudio = findViewById(R.id.playerAtv7);
                playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtLoading.setVisibility(View.VISIBLE);
                        playAudio.hide();
                        if (iPlay == 0) {
                        DatabaseReference dataAudio = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page" + codpageAtual).child("parameters").child("url");
                        dataAudio.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String url = String.valueOf(dataSnapshot.getValue()); // your URL here
                                final MediaPlayer mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mediaPlayer.setDataSource(url);
                                } catch (IOException e) {
                                    e.printStackTrace();

                                }
                                mediaPlayer.setLooping(false);
                                mediaPlayer.prepareAsync();


                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        playAudio.setImageDrawable(getResources().getDrawable(R.drawable.ic_sound));
                                        iPlay = 1;
                                        mediaPlayer.start();
                                        playAudio.show();
                                        txtLoading.setVisibility(View.INVISIBLE);
                                        //Toast.makeText(getApplicationContext(), String.valueOf(mediaPlayer.getDuration()), Toast.LENGTH_LONG).show();

                                    }
                                });
                                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                       //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                                        return false;
                                    }
                                });
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        playAudio.show();playAudio.hide();
                                        playAudio.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                                        playAudio.show();
                                       // Toast.makeText(getApplicationContext(), "mudou pra seta", Toast.LENGTH_LONG).show();
                                        iPlay =0;
                                        if (iTimesListened == 3) {

                                        } else if (iTimesListened == 2) {
                                            iconRoundCornerProgressBarAtv7.setProgress(3);
                                            iconRoundCornerProgressBarAtv7.setIconBackgroundColor(getResources().getColor(R.color.Icon3));
                                            iconRoundCornerProgressBarAtv7.setProgressColor(getResources().getColor(R.color.Progress3));
                                            iconRoundCornerProgressBarAtv7.setIconImageResource(R.drawable.ic_super);
                                            iTimesListened = 3;
                                            pontuacaoAcumulada = pontuacaoAcumulada + perguntaAlterna.getPontos();
                                            pontuacao.setText(String.valueOf("+" + pontuacaoAcumulada));
                                            floatNextpage.setVisibility(View.VISIBLE);
                                            mediaPlayer.release();
                                        } else if (iTimesListened == 1) {
                                            iconRoundCornerProgressBarAtv7.setProgress(2);
                                            iconRoundCornerProgressBarAtv7.setIconBackgroundColor(getResources().getColor(R.color.Icon2));
                                            iconRoundCornerProgressBarAtv7.setProgressColor(getResources().getColor(R.color.Progress2));
                                            iconRoundCornerProgressBarAtv7.setIconImageResource(R.drawable.ic_happy);
                                            iTimesListened = 2;
                                        } else if (iTimesListened == 0) {
                                            iconRoundCornerProgressBarAtv7.setProgress(1);
                                            iconRoundCornerProgressBarAtv7.setIconBackgroundColor(getResources().getColor(R.color.Icon1));
                                            iconRoundCornerProgressBarAtv7.setProgressColor(getResources().getColor(R.color.Progress1));
                                            iconRoundCornerProgressBarAtv7.setIconImageResource(R.drawable.ic_normal);
                                            iTimesListened = 1;
                                        }


                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "áudio nao encontrado.", Toast.LENGTH_LONG).show();
                                playAudio.show();
                            }
                        });


                    }else {
                            Toast.makeText(getApplicationContext(), "Listen the audio.", Toast.LENGTH_SHORT).show();
                            playAudio.show();
                            txtLoading.setVisibility(View.INVISIBLE);
                        }
                }
                });
                break;
            case 8:
                break;
            case 9:
                imagePages = findViewById(R.id.imgAtv9);
                Glide.with(getApplicationContext())
                        .load(storageReference)
                        .into(imagePages);
                break;
            case 10:
                playAudio = findViewById(R.id.playerAtv10);
                mediaPlayer = new MediaPlayer();

                playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference dataAudio = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                        dataAudio.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                try {
                                    if (!mediaPlayer.isPlaying()) {
                                        txtLoading.setVisibility(View.VISIBLE);
                                        playAudio.hide();
                                        Toast.makeText(getApplicationContext(), "Loading audio, please wait...", Toast.LENGTH_LONG).show();
                                        final String url = String.valueOf(dataSnapshot.getValue()); // your URL here
                                        mediaPlayer.setDataSource(url);
                                        mediaPlayer.prepareAsync();
                                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                playAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                                mediaPlayer.stop();
                                                mediaPlayer.reset();
                                            }
                                        });
                                        //Toast.makeText(getApplicationContext(), "setou url", Toast.LENGTH_LONG).show();

                                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                mp.setLooping(false);
                                                mediaPlayer.start();
                                                txtLoading.setVisibility(View.INVISIBLE);
                                                playAudio.setImageResource(R.drawable.ic_stop_white_24dp);
                                                playAudio.show();
                                                // Toast.makeText(getApplicationContext(), "erer"+mediaPlayer.isPlaying(), Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }else {
                                        mediaPlayer.stop();
                                        mediaPlayer.reset();
                                        playAudio.hide();
                                        playAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                        playAudio.show();
                                        txtLoading.setVisibility(View.INVISIBLE);
                                        //Toast.makeText(getApplicationContext(), "oi"+mediaPlayer.isPlaying(), Toast.LENGTH_LONG).show();

                                    }
                                }catch (IOException e) {
                                    e.printStackTrace();
                                    //Toast.makeText(getApplicationContext(), "deu erro", Toast.LENGTH_LONG).show();

                                }

                                //Toast.makeText(getApplicationContext(), "async", Toast.LENGTH_LONG).show();


                                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        //Toast.makeText(getApplicationContext(),url, Toast.LENGTH_LONG).show();
                                        return false;
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "áudio nao encontrado.", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });
                break;
            case 11:
                videoView = findViewById(R.id.playerAtv11);
                final ImageButton imgPlayStop11 = findViewById(R.id.btnPlayStopAtv11);
                imgPlayStop11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog mDialog = new ProgressDialog(PaginaDeAtividades.this);

                        DatabaseReference dataVideo = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                        dataVideo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    if (!videoView.isPlaying()){
                                        mDialog.setMessage("please wait");
                                        mDialog.setCanceledOnTouchOutside(false);
                                        mDialog.show();
                                        final String urlVideo = String.valueOf(dataSnapshot.getValue()); // your URL here
                                        Uri uriVideo = Uri.parse(urlVideo);
                                        videoView.setVideoURI(uriVideo);
                                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                imgPlayStop11.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                            }
                                        });
                                    }else {
                                        videoView.pause();
                                        imgPlayStop11.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                    }

                                }catch (Exception ex){

                                }
                                videoView.requestFocus();
                                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mDialog.dismiss();
                                        mp.setLooping(false);
                                        ViewGroup.LayoutParams params = videoView.getLayoutParams();
                                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                        videoView.setLayoutParams(params);
                                        videoView.start();
                                        imgPlayStop11.setImageResource(R.drawable.ic_stop_white_24dp);

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                break;
            case 12:

                playAudio = findViewById(R.id.playerAtv12);
                DatabaseReference urlAudio = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                urlAudio.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().toString().equals("")){
                            playAudio.hide();
                        }else{
                            mediaPlayer = new MediaPlayer();

                            playAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DatabaseReference dataAudio = FirebaseDatabase.getInstance().getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child("page"+codpageAtual).child("parameters").child("url");
                                    dataAudio.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                            try {
                                                if (!mediaPlayer.isPlaying()) {
                                                    txtLoading.setVisibility(View.VISIBLE);
                                                    playAudio.hide();
                                                    Toast.makeText(getApplicationContext(), "Loading audio, please wait...", Toast.LENGTH_LONG).show();
                                                    final String url = String.valueOf(dataSnapshot.getValue()); // your URL here
                                                    mediaPlayer.setDataSource(url);
                                                    mediaPlayer.prepareAsync();
                                                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                        @Override
                                                        public void onCompletion(MediaPlayer mp) {
                                                            playAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                                            mediaPlayer.stop();
                                                            mediaPlayer.reset();
                                                        }
                                                    });
                                                    //Toast.makeText(getApplicationContext(), "setou url", Toast.LENGTH_LONG).show();

                                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                        @Override
                                                        public void onPrepared(MediaPlayer mp) {
                                                            mp.setLooping(false);
                                                            mediaPlayer.start();
                                                            txtLoading.setVisibility(View.INVISIBLE);
                                                            playAudio.setImageResource(R.drawable.ic_stop_white_24dp);
                                                            playAudio.show();
                                                            // Toast.makeText(getApplicationContext(), "erer"+mediaPlayer.isPlaying(), Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                                }else {
                                                    mediaPlayer.stop();
                                                    mediaPlayer.reset();
                                                    playAudio.hide();
                                                    playAudio.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                                    playAudio.show();
                                                    txtLoading.setVisibility(View.INVISIBLE);
                                                    //Toast.makeText(getApplicationContext(), "oi"+mediaPlayer.isPlaying(), Toast.LENGTH_LONG).show();

                                                }
                                            }catch (IOException e) {
                                                e.printStackTrace();
                                                //Toast.makeText(getApplicationContext(), "deu erro", Toast.LENGTH_LONG).show();

                                            }

                                            //Toast.makeText(getApplicationContext(), "async", Toast.LENGTH_LONG).show();


                                            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                                @Override
                                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                                    //Toast.makeText(getApplicationContext(),url, Toast.LENGTH_LONG).show();
                                                    return false;
                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), "áudio nao encontrado.", Toast.LENGTH_LONG).show();
                                        }
                                    });


                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;

        }

    }

    private void speak() {
        //intent to show speech to text dialog
        Intent intentSpeak = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeak.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeak.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        intentSpeak.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, speak, please!");

        //start intent
        try {
            //if there was no error
            //show dialog
            startActivityForResult(intentSpeak, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception ex){
            //if there was some error
            Toast.makeText(getApplicationContext(), ""+ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case  REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && null!= data){
                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text view
                    textAnswer.setText(result.get(0));
                    verificaVoice(String.valueOf(textAnswer.getText()));

                }
            }
        }
    }

    private void verificaVoice(String answer) {
        textAnswer.setVisibility(View.VISIBLE);
        if(answer.equals(perguntaAlterna.getAlt1()) || answer.equals(perguntaAlterna.getAlt2()) || answer.equals(perguntaAlterna.getAlt3()) || answer.equals(perguntaAlterna.getAlt4())){
            textAnswer.setBackgroundColor(getResources().getColor(R.color.verde_correto));
            pontuacaoAcumulada = pontuacaoAcumulada+perguntaAlterna.getPontos();
            pontuacao.setText(String.valueOf("+"+pontuacaoAcumulada));
            soundPlayer.playCorrectSound();
            fabMicAtv6.hide();

        } else {
            textAnswer.setBackgroundColor(getResources().getColor(R.color.vermelho_errado));
            soundPlayer.playWrongSound();
            //Toast.makeText(getApplicationContext(), String.valueOf(perguntaAlterna.getAlt3()), Toast.LENGTH_LONG).show();
        }

    }

    private void verificaAtividade() {

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        final String codPg;
        if (iTipoAtv.equals("12")){
            codPg = buttonsAlterna.getCodPag();
        }else {
            codPg = perguntaAlterna.getCodPag();
        }
        final DatabaseReference dataVerificaAtv = fireDataAtividades.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("AtvRealizadas").child(codPg);
        dataVerificaAtv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (String.valueOf(dataSnapshot.getValue()).equals("sim")){

                } else{
                    FirebaseDatabase.getInstance().getReference("schools").child(String.valueOf(sharedPreferences.getLong("utbCRIA", 0))).child("desempenho").child("atvRealizadas").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue()!=null){
                                final long atvCRIA = Long.parseLong(dataSnapshot.getValue().toString())+1;
                                DatabaseReference dataAtvCRIA = FirebaseDatabase.getInstance().getReference("schools").child(String.valueOf(sharedPreferences.getLong("utbCRIA", 0))).child("desempenho").child("atvRealizadas");
                                dataAtvCRIA.setValue(atvCRIA);
                                //Toast.makeText(getApplicationContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            }else {
                                DatabaseReference dataAtvCRIA = FirebaseDatabase.getInstance().getReference("schools").child(String.valueOf(sharedPreferences.getLong("utbCRIA", 0))).child("desempenho").child("atvRealizadas");
                                dataAtvCRIA.setValue(1);
                                //Toast.makeText(getApplicationContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    final DatabaseReference databaseReferenceAtv =  fireDataAtividades.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("atvRealizadas");
                    databaseReferenceAtv.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReferenceAtv.setValue(Long.parseLong(Objects.requireNonNull(dataSnapshot.getValue()).toString())+1);
                            dataVerificaAtv.setValue("sim");
                            //Toast.makeText(getApplicationContext(), "dois", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Toast.makeText(getApplicationContext(), "tres", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                //Toast.makeText(getApplicationContext(), String.valueOf(dataSnapshot.getValue()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void parametersVoiceAlterna(int iVoiceAlterna) {
        //setar correctAlt
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("correctAlt");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAltCerta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com correctAlt", Toast.LENGTH_SHORT).show();

            }
        });
        //setar Alt1
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt1");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt1(Objects.requireNonNull(dataSnapshot.getValue()).toString());
               //Toast.makeText(getApplicationContext(), "setant alt 1, alt1: "+perguntaAlterna.getAlt1(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt1", Toast.LENGTH_SHORT).show();

            }
        });
        //setar Alt2
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt2");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt2(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                //Toast.makeText(getApplicationContext(), "setant alt 2, alt2: "+perguntaAlterna.getAlt2(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt2", Toast.LENGTH_SHORT).show();

            }
        });
        //setar Alt3
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt3");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt3(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                //Toast.makeText(getApplicationContext(), "setant alt 3, alt3: "+perguntaAlterna.getAlt3(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt3", Toast.LENGTH_SHORT).show();

            }
        });
        //setar Alt4
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt4");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt4(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                //Toast.makeText(getApplicationContext(), "setant alt 4, alt4: "+perguntaAlterna.getAlt4(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt4", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void parametersPerguntaAlterna(final int iAlterna) {
        //variável pra verificar se os botões já foram apertados
        final int[] apertouBotao = {0};

        //declara elementos
        switch (iAlterna){
            case 1:
                btnAlt1 = findViewById(R.id.btn1Atv1);
                btnAlt2 = findViewById(R.id.btn2Atv1);
                btnAlt3 = findViewById(R.id.btn3Atv1);
                btnAlt4 = findViewById(R.id.btn4Atv1);
                break;
            case 2:
                btnAlt1 = findViewById(R.id.btn1Atv2);
                btnAlt2 = findViewById(R.id.btn2Atv2);
                btnAlt3 = findViewById(R.id.btn3Atv2);
                btnAlt4 = findViewById(R.id.btn4Atv2);
                break;
            case 3:
                btnAlt1 = findViewById(R.id.btn1Atv3);
                btnAlt2 = findViewById(R.id.btn2Atv3);
                btnAlt3 = findViewById(R.id.btn3Atv3);
                btnAlt4 = findViewById(R.id.btn4Atv3);
                break;
            case 4:
                btnAlt1 = findViewById(R.id.btn1Atv4);
                btnAlt2 = findViewById(R.id.btn2Atv4);
                btnAlt3 = findViewById(R.id.btn3Atv4);
                btnAlt4 = findViewById(R.id.btn4Atv4);
                break;


        }


        //setar correctAlt
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("correctAlt");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    perguntaAlterna.setAltCerta(Objects.requireNonNull(dataSnapshot.getValue()).toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com correctAlt", Toast.LENGTH_SHORT).show();

            }
        });
        //setar alt1
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt1");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt1(Objects.requireNonNull(dataSnapshot.getValue()).toString());
               btnAlt1.setText(perguntaAlterna.getAlt1());
                btnAlt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (apertouBotao[0] ==0) {
                            apertouBotao[0] = 1;
                            verificaPerguntaAlterna(perguntaAlterna.getAltCerta(), 1);
                        }else {
                            Toast.makeText(getApplicationContext(), "Você já respondeu a essa questao.", Toast.LENGTH_SHORT).show();
                        }
                                            }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt1 ", Toast.LENGTH_SHORT).show();

            }
        });
        //setar alt2
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt2");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt2(Objects.requireNonNull(dataSnapshot.getValue()).toString());

                btnAlt2.setText(perguntaAlterna.getAlt2());
                btnAlt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (apertouBotao[0] ==0) {
                            apertouBotao[0] = 1;
                            verificaPerguntaAlterna(perguntaAlterna.getAltCerta(), 2);
                        }else {
                            Toast.makeText(getApplicationContext(), "Você já respondeu a essa questao.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt2 ", Toast.LENGTH_SHORT).show();

            }
        });
        //setar alt3
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt3");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt3(Objects.requireNonNull(dataSnapshot.getValue()).toString());

                btnAlt3.setText(perguntaAlterna.getAlt3());
                btnAlt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (apertouBotao[0] ==0) {
                            apertouBotao[0] = 1;
                            verificaPerguntaAlterna(perguntaAlterna.getAltCerta(), 3);
                        }else {
                            Toast.makeText(getApplicationContext(), "Você já respondeu a essa questao.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt3 ", Toast.LENGTH_SHORT).show();

            }
        });
        //setar alt4
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("alt4");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntaAlterna.setAlt4(Objects.requireNonNull(dataSnapshot.getValue()).toString());

                btnAlt4.setText(perguntaAlterna.getAlt4());
                btnAlt4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (apertouBotao[0] ==0) {
                            apertouBotao[0] = 1;
                            verificaPerguntaAlterna(perguntaAlterna.getAltCerta(), 4);
                        }else {
                            Toast.makeText(getApplicationContext(), "Você já respondeu a essa questao.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro com alt4 ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void verificaPerguntaAlterna(String altCerta, int iResposta) {


        btnAlt1.setTextColor(getResources().getColor(R.color.branco));
        btnAlt2.setTextColor(getResources().getColor(R.color.branco));
        btnAlt3.setTextColor(getResources().getColor(R.color.branco));
        btnAlt4.setTextColor(getResources().getColor(R.color.branco));

        switch (altCerta){
            case "1":
                btnAlt1.setBackgroundTintList(getResources().getColorStateList(R.color.verde_correto));
                btnAlt2.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt3.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt4.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                break;
            case "2":
                btnAlt1.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt2.setBackgroundTintList(getResources().getColorStateList(R.color.verde_correto));
                btnAlt3.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt4.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                break;
            case "3":
                btnAlt1.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt2.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt3.setBackgroundTintList(getResources().getColorStateList(R.color.verde_correto));
                btnAlt4.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                break;
            case "4":
                btnAlt1.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt2.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt3.setBackgroundTintList(getResources().getColorStateList(R.color.vermelho_errado));
                btnAlt4.setBackgroundTintList(getResources().getColorStateList(R.color.verde_correto));
                break;
        }

        if (altCerta.equals(String.valueOf(iResposta))){
            pontuacaoAcumulada = pontuacaoAcumulada+perguntaAlterna.getPontos();
            pontuacao.setText(String.valueOf("+"+pontuacaoAcumulada));
            soundPlayer.playCorrectSound();
        }else {
            soundPlayer.playWrongSound();
        }
        floatNextpage.setVisibility(View.VISIBLE);
    }

    private void parametersPergunta(final int tipoAtv) {

        //setar pergunta
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("parameters").child("pergunta");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (tipoAtv){
                    case 1:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv1);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());

                        break;
                    case 2:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv2);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());

                        break;
                    case 3:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv3);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());

                        break;
                    case 4:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv4);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());

                        break;
                    case 5:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        final NiftyDialogBuilder dialogPDF=NiftyDialogBuilder.getInstance(PaginaDeAtividades.this);
                        final TextView textViewPDF = new TextView(getApplicationContext());
                        textViewPDF.setText(perguntaAlterna.getPergunta());
                        textViewPDF.setTextSize(24);
                        textViewPDF.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        textViewPDF.setGravity(Gravity.CENTER_HORIZONTAL);
                        textViewPDF.setTextColor(getResources().getColor(R.color.branco));
                        dialogPDF.withTitleColor("#E0F2F1")                                  //def
                                .withDividerColor("#009688")                              //def
                                .withMessageColor("#E0F2F1")                              //def  | withMessageColor(int resid)
                                .withDialogColor("#00BFA5")
                                .setCustomView(textViewPDF, getApplicationContext())
                                .withButton2Text("OK")
                                .isCancelableOnTouchOutside(false)   //def gone//def  | withDialogColor(int resid)
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    dialogPDF.dismiss();
                                    }
                                })
                                .withEffect(Effectstype.SlideBottom)
                                .show();
                        break;
                    case 6:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv6);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());

                        break;
                    case 8:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv8);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());
                        break;
                    case 9:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv9);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());
                        break;
                    case 10:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv10);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());
                        break;
                    case 11:
                        perguntaAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv11);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(perguntaAlterna.getPergunta());
                        break;
                    case 12:
                        buttonsAlterna.setPergunta(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        pergunta = findViewById(R.id.txtPerguntaAtv12);
                        //Toast.makeText(getApplicationContext(), perguntaAlterna.getPergunta(), Toast.LENGTH_SHORT).show();
                        pergunta.setText(buttonsAlterna.getPergunta());
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro para pergunta ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void parametersFragmentPoints() {
        parametersReference = fireDataAtividades.getReference("Activities").child(Week.codAtividade).child(Week.diaDaSemana).child("pages").child(pagina+String.valueOf(codpageAtual)).child("points");
        parametersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (iTipoAtv.equals("12")){
                    buttonsAlterna.setPontos(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                }else {
                    perguntaAlterna.setPontos(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               //Toast.makeText(getApplicationContext(), "algum erro para points ", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
