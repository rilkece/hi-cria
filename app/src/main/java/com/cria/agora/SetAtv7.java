package com.cria.agora;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import TiposdeAtividades.PerguntaAlterna;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetAtv7 extends Fragment {

    private ImageButton imgBtnNextSet;
    private RoundCornerProgressBar progressSet;
    private EditText editurlAtv7;

    private String linkDownload1 = "https://drive.google.com/uc?export=download&id=";
    private String linkDownload2 = "&export=download";
    private String linkDownloadID;

    public SetAtv7() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_atv07, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //declarar e inicializar

        imgBtnNextSet = view.findViewById(R.id.imgBtnNextSetAtv7);
        progressSet = view.findViewById(R.id.progressAtv7);
        editurlAtv7 = view.findViewById(R.id.editurlAtv7);

        //setar visibilidade
        progressSet.setVisibility(View.GONE);
        imgBtnNextSet.setVisibility(View.VISIBLE);
        editurlAtv7.setVisibility(View.VISIBLE);


        imgBtnNextSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editurlAtv7.getText().toString().equals("")){
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "fill in all required fields", Toast.LENGTH_SHORT).show();
                }else {

                    progressSet.setVisibility(View.VISIBLE);
                    imgBtnNextSet.setVisibility(View.GONE);
                    editurlAtv7.setVisibility(View.GONE);
                    postarAtividade();
                    //Toast.makeText(getActivity().getApplicationContext(), "o valor é: "+pooints.getText().toString(), Toast.LENGTH_LONG).show();


                }
            }
        });
    }

    private void postarAtividade(){
        // Toast.makeText(getActivity().getApplicationContext(), "o valor é: "+pooints.getText().toString(), Toast.LENGTH_LONG).show();

        linkDownloadID = editurlAtv7.getText().toString();
        String GdriveDownload = linkDownload1+linkDownloadID.substring(linkDownloadID.indexOf("d/") + 2, linkDownloadID.indexOf("/v"));
        final PerguntaAlterna pergSet = new PerguntaAlterna(0
                , PaginaDeAtividades.codPageDay
                , "ND"
                , "ND"
                , "ND"
                , "ND"
                , "ND"
                , "ND"
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
        dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("tipoAtv").setValue(7).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        dataSetAtv.child("pages").child("page"+SetActivity.ipaginaAtividade).child("parameters").child("url").setValue(pergSet.getUrl()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressSet.setProgress(8);
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
                progressSet.setProgress(10);
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
