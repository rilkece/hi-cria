package com.cria.agora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class Week extends AppCompatActivity {

    public static String codAtividade;
    public static String diaDaSemana;
    public static long numPages = 1;

    private TextView weekScore;

    private ImageButton monday;
    private ImageButton tuesday;
    private ImageButton wednesday;
    private ImageButton thursday;
    private ImageButton friday;

    private ImageView imgMonday;
    private ImageView imgTuesday;
    private ImageView imgWednesday;
    private ImageView imgThursday;
    private ImageView imgFriday;

    private MaterialButton editMo;
    private MaterialButton editTu;
    private MaterialButton editWe;
    private MaterialButton editTh;
    private MaterialButton editFr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        //inicializar elementos
        monday = findViewById(R.id.ibMonday);
        tuesday = findViewById(R.id.ibTuesday);
        wednesday = findViewById(R.id.ibWednesday);
        thursday = findViewById(R.id.ibThursday);
        friday = findViewById(R.id.ibFriday);
        weekScore = findViewById(R.id.txtWeekScore);

        imgMonday = findViewById(R.id.imgCheckMonday);
        imgTuesday = findViewById(R.id.imgCheckTuesday);
        imgWednesday = findViewById(R.id.imgCheckWednesday);
        imgThursday = findViewById(R.id.imgCheckThursday);
        imgFriday = findViewById(R.id.imgCheckFriday);

        editMo = findViewById(R.id.weekBtnMo);
        editTu = findViewById(R.id.weekBtnTu);
        editWe = findViewById(R.id.weekBtnWe);
        editTh = findViewById(R.id.weekBtnTh);
        editFr = findViewById(R.id.weekBtnFr);





        //visibilidade dos botões caso seja um adm
        if (sharedPreferences.getLong("tipoUser", 1) ==0){
            editMo.setVisibility(View.VISIBLE);
            editTu.setVisibility(View.VISIBLE);
            editWe.setVisibility(View.VISIBLE);
            editTh.setVisibility(View.VISIBLE);
            editFr.setVisibility(View.VISIBLE);

        }


        //editar atividades
        editMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaDaSemana = "monday";
                PaginaDeAtividades.codPageDay = codAtividade+"mo";
                criarAtividade(PaginaDeAtividades.codPageDay);
            }
        });
        editTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaDaSemana = "tuesday";
                PaginaDeAtividades.codPageDay = codAtividade+"tu";
                criarAtividade(PaginaDeAtividades.codPageDay);
            }
        });
        editWe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaDaSemana = "wednesday";
                PaginaDeAtividades.codPageDay = codAtividade+"we";
                criarAtividade(PaginaDeAtividades.codPageDay);
            }
        });editTh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaDaSemana = "thursday";
                PaginaDeAtividades.codPageDay = codAtividade+"th";
                criarAtividade(PaginaDeAtividades.codPageDay);
            }
        });editFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaDaSemana = "friday";
                PaginaDeAtividades.codPageDay = codAtividade+"fr";
                criarAtividade(PaginaDeAtividades.codPageDay);
            }
        });


        //popular o weekscore
        DatabaseReference refWeekScore= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("weekScore").child(Week.codAtividade);
        refWeekScore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                }else {
                    weekScore.setText(String.valueOf(dataSnapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //setar imagem dos checks
            setarImgChecks();


        //setar os clicklisteners
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diaDaSemana = "monday";
                PaginaDeAtividades.codPageDay = codAtividade+"mo";
                DatabaseReference dataDayRef = FirebaseDatabase.getInstance().getReference("Activities").child(codAtividade).child(diaDaSemana).child("codDay");
                dataDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (String.valueOf(dataSnapshot.getValue()).equals(PaginaDeAtividades.codPageDay)){
                            Intent i = new Intent(getApplicationContext(), PaginaDeAtividades.class);
                            startActivity(i);
                        }else {
                            TastyToast.makeText(getApplicationContext(), "This activity is not ready!", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getApplicationContext(), PaginaDeAtividades.codPage, Toast.LENGTH_LONG).show();

            }
        });
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diaDaSemana = "tuesday";
                PaginaDeAtividades.codPageDay = codAtividade+"tu";
                DatabaseReference dataDayRef = FirebaseDatabase.getInstance().getReference("Activities").child(codAtividade).child(diaDaSemana).child("codDay");
                dataDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (String.valueOf(dataSnapshot.getValue()).equals(PaginaDeAtividades.codPageDay)){
                            Intent i = new Intent(getApplicationContext(), PaginaDeAtividades.class);
                            startActivity(i);
                        }else {
                            TastyToast.makeText(getApplicationContext(), "This activity is not ready!", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getApplicationContext(), PaginaDeAtividades.codPage, Toast.LENGTH_LONG).show();

            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diaDaSemana = "wednesday";
                PaginaDeAtividades.codPageDay = codAtividade+"we";
                DatabaseReference dataDayRef = FirebaseDatabase.getInstance().getReference("Activities").child(codAtividade).child(diaDaSemana).child("codDay");
                dataDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (String.valueOf(dataSnapshot.getValue()).equals(PaginaDeAtividades.codPageDay)){
                            Intent i = new Intent(getApplicationContext(), PaginaDeAtividades.class);
                            startActivity(i);
                        }else {
                            TastyToast.makeText(getApplicationContext(), "This activity is not ready!", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getApplicationContext(), PaginaDeAtividades.codPage, Toast.LENGTH_LONG).show();

            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diaDaSemana = "thursday";
                PaginaDeAtividades.codPageDay = codAtividade+"th";
                DatabaseReference dataDayRef = FirebaseDatabase.getInstance().getReference("Activities").child(codAtividade).child(diaDaSemana).child("codDay");
                dataDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (String.valueOf(dataSnapshot.getValue()).equals(PaginaDeAtividades.codPageDay)){
                            Intent i = new Intent(getApplicationContext(), PaginaDeAtividades.class);
                            startActivity(i);
                        }else {
                            TastyToast.makeText(getApplicationContext(), "This activity is not ready!", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getApplicationContext(), PaginaDeAtividades.codPage, Toast.LENGTH_LONG).show();

            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diaDaSemana = "friday";
                PaginaDeAtividades.codPageDay = codAtividade+"fr";
                DatabaseReference dataDayRef = FirebaseDatabase.getInstance().getReference("Activities").child(codAtividade).child(diaDaSemana).child("codDay");
                dataDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (String.valueOf(dataSnapshot.getValue()).equals(PaginaDeAtividades.codPageDay)){
                            Intent i = new Intent(getApplicationContext(), PaginaDeAtividades.class);
                            startActivity(i);
                        }else {
                            TastyToast.makeText(getApplicationContext(), "This activity is not ready!", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(getApplicationContext(), PaginaDeAtividades.codPage, Toast.LENGTH_LONG).show();

            }
        });





    }


    private void criarAtividade(String codPageDay) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(16,00,16,0);
        final NiftyDialogBuilder dialogSet = NiftyDialogBuilder.getInstance(this);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        final TextView textPages =new TextView(getApplicationContext());
        textPages.setText("1");
        textPages.setTextSize(32);
        textPages.setTypeface(Typeface.DEFAULT_BOLD);
        textPages.setLayoutParams(params);
        final Button bMais = new Button(getApplicationContext());
        bMais.setText("+");
        bMais.setTypeface(Typeface.DEFAULT_BOLD);
        bMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textPages.setText(String.valueOf(Integer.valueOf(textPages.getText().toString())+1));
            }
        });
        final Button bMenos = new Button(getApplicationContext());
        bMenos.setText("-");
        bMenos.setTypeface(Typeface.DEFAULT_BOLD);
        bMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(textPages.getText().toString())-1>0) {
                    textPages.setText(String.valueOf(Integer.valueOf(textPages.getText().toString()) - 1));
                }else{
                    TastyToast.makeText(getApplicationContext(), "Valor mínimo de telas é 1.", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                }
            }
        });
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(bMenos);
        linearLayout.addView(textPages);
        linearLayout.addView(bMais);



        dialogSet.withTitle("Activity screens?")
                .withTitleColor("#E0F2F1")                                  //def
                .withDividerColor("#009688")                              //def
                .withMessageColor("#E0F2F1")                              //def  | withMessageColor(int resid)
                .withDialogColor("#00BFA5")
                .setCustomView(linearLayout, getApplicationContext())
                .withButton1Text("cancel")                                      //def gone
                .withButton2Text("OK")
                .isCancelableOnTouchOutside(false)   //def gone//def  | withDialogColor(int resid)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    dialogSet.dismiss();
                    }
                })

                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numPages = Long.valueOf(textPages.getText().toString());
                    Intent iEscolha = new Intent(getApplicationContext(), EscolhaTipoAtividade.class);
                    startActivity(iEscolha);
                    dialogSet.dismiss();

                    }
                })
                .withEffect(Effectstype.SlideBottom)
                .show();
    }


    private void setarImgChecks() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        DatabaseReference refChecksMonday= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("dayScore").child(Week.codAtividade+"mo");
        refChecksMonday.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                }else {
                   imgMonday.setImageResource(R.drawable.check1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference refChecksTuesday= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("dayScore").child(Week.codAtividade+"tu");
        refChecksTuesday.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                }else {
                    imgTuesday.setImageResource(R.drawable.check2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference refChecksWednesday= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("dayScore").child(Week.codAtividade+"we");
        refChecksWednesday.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                }else {
                    imgWednesday.setImageResource(R.drawable.check3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference refChecksThursday= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("dayScore").child(Week.codAtividade+"th");
        refChecksThursday.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                }else {
                    imgThursday.setImageResource(R.drawable.check6);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference refChecksFriday= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("dayScore").child(Week.codAtividade+"fr");
        refChecksFriday.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){

                }else {
                    imgFriday.setImageResource(R.drawable.check5);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
