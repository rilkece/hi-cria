package com.cria.agora;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import Classes.NothingSelectedSpinnerAdapter;

public class Cadastro extends AppCompatActivity {
    private Spinner spinFuncs;
    private Spinner spinSchools;
    private EditText editFirst;
    private EditText editSur;
    private EditText editMat;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editDia;
    private EditText editMes;
    private EditText editAno;
    private RadioButton radioFe;
    private RadioButton radioMa;
    private MaterialButton btnRegisteer;

    private String sexo = "";
    private String senha;
    private String func;
    private String escola;
    private int tipoUser;
    private int utbSchool;
    private ProgressBar proRegister;


    DatabaseReference dataRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //popular elementos
         editFirst = findViewById(R.id.editFirstName);
         editSur = findViewById(R.id.editSurName);
         editMat = findViewById(R.id.editMatrícula);
         editEmail = findViewById(R.id.editEmail);
         editPhone = findViewById(R.id.editPhone);
         editDia = findViewById(R.id.editDiaNasc);
         editMes = findViewById(R.id.editMesNasc);
         editAno = findViewById(R.id.editAnoNasc);
         radioFe = findViewById(R.id.radioFemme);
         radioMa = findViewById(R.id.radioMasc);
         btnRegisteer = findViewById(R.id.btnRegister);
         proRegister = findViewById(R.id.progressBarRegister);

        //popular spinners
        spinFuncs = findViewById(R.id.spinnerFuncs);
        spinSchools = findViewById(R.id.spinnerSchool);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.funcs, R.layout.spinner_func);
        ArrayAdapter<CharSequence> adapterSchool = ArrayAdapter.createFromResource(this,
                R.array.schools, R.layout.spinner_func);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSchool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinFuncs.setPrompt("Selecione a função:");
        spinFuncs.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.spinner_func, this));

        spinSchools.setPrompt("Selecione a escola:");
        spinSchools.setAdapter(new NothingSelectedSpinnerAdapter(adapterSchool, R.layout.spinner_func, this));
        //TastyToast.makeText(getApplicationContext(), "selected item: "+String.valueOf(spinFuncs.getSelectedItemId()), TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();



        btnRegisteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinFuncs.getSelectedItemId()!=-1){
                    func = spinFuncs.getSelectedItem().toString();
                }else {
                    func ="";
                }
                if (spinSchools.getSelectedItemId()!=-1){
                    escola = "CRIA "+spinSchools.getSelectedItem().toString();
                    switch (String.valueOf(spinSchools.getSelectedItemId())){
                        case "0":
                        utbSchool = 0000;
                        break;
                        case "1":
                        utbSchool = 1011;
                        break;
                        case "2":
                        utbSchool = 110800;
                        break;
                        case "3":
                        utbSchool = 9826;
                        break;

                    }
                }else {

                    escola ="";
                }
                switch (func){
                    case "Professor":
                        tipoUser=1;
                        break;
                    case "Diretor":
                        tipoUser=2;
                        break;
                    case "Coord. Ped.":
                        tipoUser=3;
                        break;
                    case "Coord. Adm.":
                        tipoUser=4;
                        break;
                    case "Outra func.":
                        tipoUser=5;
                        break;
                }
                if(radioFe.isChecked()){
                    sexo = "feminino";
                }else if(radioMa.isChecked()){
                    sexo = "masculino";
                }
                if(editAno.getText().toString().equals("")
                        || editDia.getText().toString().equals("")
                        || editMes.getText().toString().equals("")
                        || editFirst.getText().toString().equals("")
                        || editSur.getText().toString().equals("")
                        || editEmail.getText().toString().equals("")
                        || editMat.getText().toString().equals("")
                        || editPhone.getText().toString().equals("")
                        || sexo.equals("")
                        || func.equals("")
                        || escola.equals("")){
                    TastyToast.makeText(getApplicationContext(), "Todos campos devem ser preenchidos", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                }else {
                    btnRegisteer.setVisibility(View.GONE);
                    proRegister.setVisibility(View.VISIBLE);
                    senha = editDia.getText().toString()+editMes.getText()+editAno.getText();
                    dataRegister = FirebaseDatabase.getInstance().getReference().child("users");
                    dataRegister.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRegister.child(editMat.getText().toString()).child("ano").setValue(editAno.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("dia").setValue(editDia.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("email").setValue(editEmail.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("escola").setValue(escola);
                        dataRegister.child(editMat.getText().toString()).child("firstName").setValue(editFirst.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("func").setValue(func);
                        dataRegister.child(editMat.getText().toString()).child("matricula").setValue(editMat.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("mes").setValue(editMes.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("phone").setValue(editPhone.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("senha").setValue(senha);
                        dataRegister.child(editMat.getText().toString()).child("sexo").setValue(sexo);
                        dataRegister.child(editMat.getText().toString()).child("surName").setValue(editSur.getText().toString());
                        dataRegister.child(editMat.getText().toString()).child("tipoUser").setValue(tipoUser);
                        dataRegister.child(editMat.getText().toString()).child("utbCRIA").setValue(utbSchool);

                        dataRegister.child(editMat.getText().toString()).child("desempenho").child("atvRealizadas").setValue(0);
                        dataRegister.child(editMat.getText().toString()).child("desempenho").child("avatar").setValue(1);
                        dataRegister.child(editMat.getText().toString()).child("desempenho").child("level").setValue("A1");
                        dataRegister.child(editMat.getText().toString()).child("desempenho").child("score").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(getApplicationContext(), MainAdm.class);
                                startActivity(intent);
                                TastyToast.makeText(getApplicationContext(), "Usuário cadastrado.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                                finish();
                            }
                        });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

    }
}
