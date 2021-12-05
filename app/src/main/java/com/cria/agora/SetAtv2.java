package com.cria.agora;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import TiposdeAtividades.PerguntaAlterna;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetAtv2 extends Fragment {
    private RadioButton radio1 ;
    private RadioButton radio2;
    private RadioButton radio3 ;
    private RadioButton radio4;
    private EditText question;
    private EditText alt1;
    private EditText alt2;
    private EditText alt3;
    private EditText alt4;
    private MaterialButton pointPlus;
    private MaterialButton pointMinus;
    private TextView pooints;
    private TextView descAtv2;
    private TextView descAtv2_2;
    private String altCerta = "";
    private ImageButton imgBtnNextSet;
    private RoundCornerProgressBar progressSet;
    private StorageReference mStorageRef;
    private Uri uriAct;

    public SetAtv2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_atv02, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //declarar e inicializar
        radio1 = view.findViewById(R.id.rdBtnAlterna1Atv2);
        radio2 = view.findViewById(R.id.rdBtnAlterna2Atv2);
        radio3 = view.findViewById(R.id.rdBtnAlterna3Atv2);
        radio4 = view.findViewById(R.id.rdBtnAlterna4Atv2);
        question = view.findViewById(R.id.editQuestionAtv2);
        alt1 = view.findViewById(R.id.editAlterna1Atv2);
        alt2 = view.findViewById(R.id.editAlterna2Atv2);
        alt3 = view.findViewById(R.id.editAlterna3Atv2);
        alt4 = view.findViewById(R.id.editAlterna4Atv2);
        pointPlus = view.findViewById(R.id.maisAtv2);
        pointMinus = view.findViewById(R.id.menosAtv2);
        pooints = view.findViewById(R.id.pointsAtv2);
        imgBtnNextSet = view.findViewById(R.id.imgBtnNextSetAtv2);
        progressSet = view.findViewById(R.id.progressAtv2);
        descAtv2 = view.findViewById(R.id.descAtv2);
        descAtv2_2 = view.findViewById(R.id.descAtv2_2);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //setar visibilidade
        progressSet.setVisibility(View.GONE);
        radio1.setVisibility(View.VISIBLE);
        radio2.setVisibility(View.VISIBLE);
        radio3.setVisibility(View.VISIBLE);
        radio4.setVisibility(View.VISIBLE);
        alt1.setVisibility(View.VISIBLE);
        alt2.setVisibility(View.VISIBLE);
        alt3.setVisibility(View.VISIBLE);
        alt4.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        descAtv2.setVisibility(View.VISIBLE);
        descAtv2_2.setVisibility(View.VISIBLE);
        pointMinus.setVisibility(View.VISIBLE);
        pointPlus.setVisibility(View.VISIBLE);
        pooints.setVisibility(View.VISIBLE);
        imgBtnNextSet.setVisibility(View.VISIBLE);


        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2.setChecked(false);
                radio3.setChecked(false);
                radio4.setChecked(false);
                altCerta = "1";
            }
        });
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(false);
                radio3.setChecked(false);
                radio4.setChecked(false);
                altCerta = "2";
            }
        });
        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2.setChecked(false);
                radio1.setChecked(false);
                radio4.setChecked(false);
                altCerta = "3";
            }
        });
        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2.setChecked(false);
                radio3.setChecked(false);
                radio1.setChecked(false);
                altCerta = "4";
            }
        });

        pointPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pooints.setText(String.valueOf(Integer.parseInt(pooints.getText().toString())+1));
            }
        });
        pointMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(pooints.getText().toString())>0){
                    pooints.setText(String.valueOf(Integer.parseInt(pooints.getText().toString())-1));
                }

            }
        });

        imgBtnNextSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.getText().toString().equals("") || alt1.getText().toString().equals("") || alt2.getText().toString().equals("") || alt3.getText().toString().equals("") || alt4.getText().toString().equals("") || altCerta.equals("")){
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "fill in all required fields", Toast.LENGTH_SHORT).show();
                }else {


                    postarAtividade();
                    progressSet.setVisibility(View.VISIBLE);
                    radio1.setVisibility(View.GONE);
                    radio2.setVisibility(View.GONE);
                    radio3.setVisibility(View.GONE);
                    radio4.setVisibility(View.GONE);
                    alt1.setVisibility(View.GONE);
                    alt2.setVisibility(View.GONE);
                    alt3.setVisibility(View.GONE);
                    alt4.setVisibility(View.GONE);
                    question.setVisibility(View.GONE);
                    descAtv2.setVisibility(View.GONE);
                    descAtv2_2.setVisibility(View.GONE);
                    pointMinus.setVisibility(View.GONE);
                    pointPlus.setVisibility(View.GONE);
                    pooints.setVisibility(View.GONE);
                    imgBtnNextSet.setVisibility(View.GONE);
                    //Toast.makeText(getActivity().getApplicationContext(), "o valor é: "+pooints.getText().toString(), Toast.LENGTH_LONG).show();


                }
            }
        });
    }

    private void postarAtividade() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uriAct = data.getData();

        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Please, wait...", Toast.LENGTH_LONG).show();
        final PerguntaAlterna pergSet = new PerguntaAlterna(Integer.parseInt(pooints.getText().toString())
                , PaginaDeAtividades.codPageDay
                , question.getText().toString()
                , alt1.getText().toString()
                , alt2.getText().toString()
                , alt3.getText().toString()
                , alt4.getText().toString()
                , altCerta
                , "not");
        mStorageRef.child("imgPages").child(Week.codAtividade).child(Week.diaDaSemana).child(pergSet.getCodPag()+SetActivity.ipaginaAtividade).putFile(uriAct).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                DatabaseReference dataSetAtv = FirebaseDatabase.getInstance().getReference("Activities").child(String.valueOf(Week.codAtividade)).child(Week.diaDaSemana);
                dataSetAtv.child("codDay").setValue(pergSet.getCodPag()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(1);
                    }
                });
                if(EscolhaTipoAtividade.COD_EDIT_ATV==0){
                    dataSetAtv.child("numPages").setValue(Week.numPages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressSet.setProgress(2);
                        }
                    });

                }

                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("codPage").setValue(pergSet.getCodPag()+SetActivity.ipaginaAtividade).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(3);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("points").setValue(pergSet.pontos).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(4);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("tipoAtv").setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(5);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("alt1").setValue(pergSet.alt1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(6);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("alt2").setValue(pergSet.alt2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(6);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("alt3").setValue(pergSet.alt3).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(7);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("alt4").setValue(pergSet.alt4).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(9);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("correctAlt").setValue(pergSet.altCerta).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(9);
                    }
                });
                dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("pergunta").setValue(pergSet.pergunta).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressSet.setProgress(9);
                        if(EscolhaTipoAtividade.COD_EDIT_ATV==1){
                            SetActivity.ipaginaAtividade = 0;
                            EscolhaTipoAtividade.COD_EDIT_ATV=0;
                            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "activity edited.", Toast.LENGTH_SHORT).show();
                            Intent iVolta = new Intent(getContext(), MainAdm.class);
                            startActivity(iVolta);

                        }else if (SetActivity.ipaginaAtividade==Week.numPages) {
                            SetActivity.ipaginaAtividade = 0;
                            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "activity completed.", Toast.LENGTH_SHORT).show();
                            Intent iVolta = new Intent(getContext(), MainAdm.class);
                            startActivity(iVolta);
                        } else  {
                            Intent iNextPage = new Intent(getContext(), EscolhaTipoAtividade.class);
                            startActivity(iNextPage);

                        }
                        imgBtnNextSet.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


    }



}
