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
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
public class SetAtv10 extends Fragment {

    private EditText question;
    private EditText alt1;
    private EditText alt2;
    private EditText alt3;
    private EditText alt4;
    private EditText sentence;
    private Button pointPlus;
    private Button pointMinus;
    private TextView pooints;
    private TextView descAtv10_2;
    private String altCerta = "";
    private ImageButton imgBtnNextSet;
    private RoundCornerProgressBar progressSet;
    private StorageReference mStorageRef;
    private EditText editurlAtv10;
    private String linkDownload1 = "https://drive.google.com/uc?export=download&id=";
    private String linkDownload2 = "&export=download";
    private String linkDownloadID;

    public SetAtv10() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_atv10, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        question = view.findViewById(R.id.editQuestionAtv10);
        alt1 = view.findViewById(R.id.editAlterna1Atv10);
        alt2 = view.findViewById(R.id.editAlterna2Atv10);
        alt3 = view.findViewById(R.id.editAlterna3Atv10);
        alt4 = view.findViewById(R.id.editAlterna4Atv10);
        sentence = view.findViewById(R.id.editAltCertaAtv10);
        pointPlus = view.findViewById(R.id.maisAtv10);
        pointMinus = view.findViewById(R.id.menosAtv10);
        pooints = view.findViewById(R.id.pointsAtv10);
        imgBtnNextSet = view.findViewById(R.id.imgBtnNextSetAtv10);
        progressSet = view.findViewById(R.id.progressAtv10);
        descAtv10_2 = view.findViewById(R.id.descAtv10_2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        editurlAtv10 = view.findViewById(R.id.editurlAtv10);

        //setar visibilidade
        progressSet.setVisibility(View.GONE);
        alt1.setVisibility(View.VISIBLE);
        alt2.setVisibility(View.VISIBLE);
        alt3.setVisibility(View.VISIBLE);
        alt4.setVisibility(View.VISIBLE);
        sentence.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        descAtv10_2.setVisibility(View.VISIBLE);
        pointMinus.setVisibility(View.VISIBLE);
        pointPlus.setVisibility(View.VISIBLE);
        pooints.setVisibility(View.VISIBLE);
        imgBtnNextSet.setVisibility(View.VISIBLE);
        editurlAtv10.setVisibility(View.VISIBLE);

        //setar função dos botões + e - para o score
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
                altCerta = sentence.getText().toString();
                if(question.getText().toString().equals("") || alt1.getText().toString().equals("") || alt2.getText().toString().equals("") || alt3.getText().toString().equals("") || alt4.getText().toString().equals("") || editurlAtv10.getText().toString().equals("") ){
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "fill in all required fields", Toast.LENGTH_LONG).show();
                }else {


                    postarAtividade();
                    progressSet.setVisibility(View.VISIBLE);
                    alt1.setVisibility(View.GONE);
                    alt2.setVisibility(View.GONE);
                    alt3.setVisibility(View.GONE);
                    alt4.setVisibility(View.GONE);
                    sentence.setVisibility(View.GONE);
                    question.setVisibility(View.GONE);
                    descAtv10_2.setVisibility(View.GONE);
                    pointMinus.setVisibility(View.GONE);
                    pointPlus.setVisibility(View.GONE);
                    pooints.setVisibility(View.GONE);
                    imgBtnNextSet.setVisibility(View.GONE);
                    editurlAtv10.setVisibility(View.GONE);
                    //Toast.makeText(getActivity().getApplicationContext(), "o valor é: "+pooints.getText().toString(), Toast.LENGTH_LONG).show();


                }
            }
        });


    }

    private void postarAtividade() {

        linkDownloadID = editurlAtv10.getText().toString();
        String GdriveDownload = linkDownload1+linkDownloadID.substring(linkDownloadID.indexOf("d/") + 2, linkDownloadID.indexOf("/v"));
        // Toast.makeText(getActivity().getApplicationContext(), "o valor é: "+pooints.getText().toString(), Toast.LENGTH_LONG).show();
        final PerguntaAlterna pergSet = new PerguntaAlterna(Integer.parseInt(pooints.getText().toString())
                , PaginaDeAtividades.codPageDay
                , question.getText().toString()
                , alt1.getText().toString()
                , alt2.getText().toString()
                , alt3.getText().toString()
                , alt4.getText().toString()
                , altCerta
                ,GdriveDownload);
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
        dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("tipoAtv").setValue(10).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("url").setValue(pergSet.getUrl()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressSet.setProgress(8);
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
            }
        });
    }
}
