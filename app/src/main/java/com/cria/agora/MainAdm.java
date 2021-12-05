package com.cria.agora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;
import java.util.Objects;

import Classes.PerfilAtividade;

public class MainAdm extends AppCompatActivity {
    private TextView seuNomeTitle;
    private ImageButton avatar;

    private FloatingActionButton NewAct;
    private FloatingActionButton EditAct;
    private FloatingActionButton Perfor;
    private FloatingActionButton School;
    private FloatingActionButton AddProf;
    private FloatingActionButton Back;
    private FloatingActionButton goStudent;

    private StorageReference mStorageRef;
    private DatabaseReference refAtv;
    private Uri uriAct;

    private EditText editNomeAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adm);

        //declarar os elementos
        seuNomeTitle = findViewById(R.id.textName_adm);
        avatar = findViewById(R.id.img_avatar_adm);
        NewAct = findViewById(R.id.fabAdd);
        EditAct = findViewById(R.id.fabEdit);
        Perfor = findViewById(R.id.fabPerformance);
        School = findViewById(R.id.fabSchool);
        AddProf = findViewById(R.id.fabAddProf);
        Back = findViewById(R.id.fabBack);
        goStudent = findViewById(R.id.fabGoToStudent);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //setar ações
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        String tes = sharedPreferences.getString("matricula", "000");
        //Toast.makeText(getApplicationContext(), tes, Toast.LENGTH_LONG).show();

        //Ir para student activity
        goStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("tipoUser", 8);
                editor.apply();
            }
        });

        //New Activity
        final NiftyDialogBuilder dialogImg=NiftyDialogBuilder.getInstance(this);
        NewAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 editNomeAct =new EditText(getApplicationContext());
                dialogImg.withTitle("Type the activity's name:")
                        .withTitleColor("#E0F2F1")                                  //def
                        .withDividerColor("#009688")                              //def
                        .withMessageColor("#E0F2F1")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#00BFA5")
                        .setCustomView(editNomeAct, getApplicationContext())
                        .withButton1Text("cancel")                                      //def gone
                        .withButton2Text("OK")
                        .isCancelableOnTouchOutside(false)   //def gone//def  | withDialogColor(int resid)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogImg.dismiss();

                            }
                        })

                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogImg.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, 1);


                            }
                        })
                        .withEffect(Effectstype.SlideBottom)
                        .show();


            }
        });
        //Edit Activity
        EditAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Atividades.class);
                Atividades.isPerformance=false;
                startActivity(i);
            }
        });
        //Teachers performance
        Perfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Atividades.class);
                Atividades.isPerformance=true;
                startActivity(i);
            }
        });

        //Games
        School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //   String endereco = "https://drive.google.com/file/d/1kgBKzIUSAxTClPk8O5gdjcO5DxACkk9L/view?usp=drivesdk";
                //String parte= endereco.substring(endereco.indexOf("d/") + 2, endereco.indexOf("/v")-1);
                Intent i = new Intent(getApplicationContext(), MainGames.class);
                Atividades.isPerformance=true;
                startActivity(i);
                //Toast.makeText(getApplicationContext(), parte, Toast.LENGTH_LONG).show();
            }
        });


        //Add Student
        AddProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Cadastro.class);
                startActivity(i);
            }
        });
        //Log out
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sair();
            }
        });

        //popular os txt com infos do usuário que fez login através do objeto usuario da classe UserInfo

           seuNomeTitle.setText(sharedPreferences.getString("firstName", "unknown"));




        //mudar avatar
        DatabaseReference refAv = FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "0"));

        //como ler data do último login
        /*refAv.child("lastLogin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            getDate(Long.parseLong(String.valueOf(dataSnapshot.getValue())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        refAv.child("desempenho").child("avatar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer iAvatar = (int) (long) dataSnapshot.getValue();
                switch (iAvatar){
                    case 1: avatar.setImageResource(R.drawable.animal1); break;
                    case 2: avatar.setImageResource(R.drawable.animal2); break;
                    case 3: avatar.setImageResource(R.drawable.animal3); break;
                    case 4: avatar.setImageResource(R.drawable.animal4); break;
                    case 5: avatar.setImageResource(R.drawable.animal5); break;
                    case 6: avatar.setImageResource(R.drawable.animal6); break;
                    case 7: avatar.setImageResource(R.drawable.animal7); break;
                    case 8: avatar.setImageResource(R.drawable.animal8); break;
                    case 9: avatar.setImageResource(R.drawable.animal9); break;
                    case 11: avatar.setImageResource(R.drawable.fruit1); break;
                    case 12: avatar.setImageResource(R.drawable.fruit2); break;
                    case 13: avatar.setImageResource(R.drawable.fruit3); break;
                    case 14: avatar.setImageResource(R.drawable.fruit4); break;
                    case 15: avatar.setImageResource(R.drawable.fruit5); break;
                    case 16: avatar.setImageResource(R.drawable.fruit6); break;
                    case 17: avatar.setImageResource(R.drawable.fruit7); break;
                    case 18: avatar.setImageResource(R.drawable.fruit8); break;
                    case 19: avatar.setImageResource(R.drawable.fruit9); break;
                    case 21: avatar.setImageResource(R.drawable.food1); break;
                    case 22: avatar.setImageResource(R.drawable.food2); break;
                    case 23: avatar.setImageResource(R.drawable.food3); break;
                    case 24: avatar.setImageResource(R.drawable.food4); break;
                    case 25: avatar.setImageResource(R.drawable.food5); break;
                    case 26: avatar.setImageResource(R.drawable.food6); break;
                    case 27: avatar.setImageResource(R.drawable.food7); break;
                    case 28: avatar.setImageResource(R.drawable.food8); break;
                    case 29: avatar.setImageResource(R.drawable.food9); break;
                    case 31: avatar.setImageResource(R.drawable.sky1); break;
                    case 32: avatar.setImageResource(R.drawable.sky2); break;
                    case 33: avatar.setImageResource(R.drawable.sky3); break;
                    case 34: avatar.setImageResource(R.drawable.sky4); break;
                    case 35: avatar.setImageResource(R.drawable.sky5); break;
                    case 36: avatar.setImageResource(R.drawable.sky6); break;
                    case 37: avatar.setImageResource(R.drawable.sky7); break;
                    case 38: avatar.setImageResource(R.drawable.sky8); break;
                    case 39: avatar.setImageResource(R.drawable.sky9); break;
                    case 41: avatar.setImageResource(R.drawable.dessert1); break;
                    case 42: avatar.setImageResource(R.drawable.dessert2); break;
                    case 43: avatar.setImageResource(R.drawable.dessert3); break;
                    case 44: avatar.setImageResource(R.drawable.dessert4); break;
                    case 45: avatar.setImageResource(R.drawable.dessert5); break;
                    case 46: avatar.setImageResource(R.drawable.dessert6); break;
                    case 47: avatar.setImageResource(R.drawable.dessert7); break;
                    case 48: avatar.setImageResource(R.drawable.dessert8); break;
                    case 49: avatar.setImageResource(R.drawable.dessert9); break;
                    case 51: avatar.setImageResource(R.drawable.sushi1); break;
                    case 52: avatar.setImageResource(R.drawable.sushi2); break;
                    case 53: avatar.setImageResource(R.drawable.sushi3); break;
                    case 54: avatar.setImageResource(R.drawable.sushi4); break;
                    case 55: avatar.setImageResource(R.drawable.sushi5); break;
                    case 56: avatar.setImageResource(R.drawable.sushi6); break;
                    case 57: avatar.setImageResource(R.drawable.sushi7); break;
                    case 58: avatar.setImageResource(R.drawable.sushi8); break;
                    case 59: avatar.setImageResource(R.drawable.sushi9); break;
                    case 61: avatar.setImageResource(R.drawable.character1); break;
                    case 62: avatar.setImageResource(R.drawable.character2); break;
                    case 63: avatar.setImageResource(R.drawable.character3); break;
                    case 64: avatar.setImageResource(R.drawable.character4); break;
                    case 65: avatar.setImageResource(R.drawable.character5); break;
                    case 66: avatar.setImageResource(R.drawable.character6); break;
                    case 67: avatar.setImageResource(R.drawable.character7); break;
                    case 68: avatar.setImageResource(R.drawable.character8); break;
                    case 69: avatar.setImageResource(R.drawable.character9); break;
                    case 71: avatar.setImageResource(R.drawable.plant1); break;
                    case 72: avatar.setImageResource(R.drawable.plant2); break;
                    case 73: avatar.setImageResource(R.drawable.plant3); break;
                    case 74: avatar.setImageResource(R.drawable.plant4); break;
                    case 75: avatar.setImageResource(R.drawable.plant5); break;
                    case 76: avatar.setImageResource(R.drawable.plant6); break;
                    case 77: avatar.setImageResource(R.drawable.plant7); break;
                    case 78: avatar.setImageResource(R.drawable.plant8); break;
                    case 79: avatar.setImageResource(R.drawable.plant9); break;
                    case 81: avatar.setImageResource(R.drawable.vegetable1); break;
                    case 82: avatar.setImageResource(R.drawable.vegetable2); break;
                    case 83: avatar.setImageResource(R.drawable.vegetable3); break;
                    case 84: avatar.setImageResource(R.drawable.vegetable4); break;
                    case 85: avatar.setImageResource(R.drawable.vegetable5); break;
                    case 86: avatar.setImageResource(R.drawable.vegetable6); break;
                    case 87: avatar.setImageResource(R.drawable.vegetable7); break;
                    case 88: avatar.setImageResource(R.drawable.vegetable8); break;
                    case 89: avatar.setImageResource(R.drawable.vegetable9); break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AvatarChoice.class);
                startActivity(intent);
            }
        });
    }

    private void sair() {
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //Setar o shared Preferences

        editor.putBoolean("LoggedIn", false);
        editor.putString("matricula", "");
        editor.putString("firstName", "");
        editor.putString("surName","");
        editor.putString("email", "");
        editor.putString("phone", "");
        editor.putString("escola", "");
        editor.putString("func","");
        editor.putString("dia", "");
        editor.putString("mes", "");
        editor.putString("ano", "");
        editor.putString("sexo", "");
        editor.putLong("tipoUser", 1);
        editor.putLong("utbCRIA", 0);

        editor.putBoolean("Achieve_eco", false);
        editor.putBoolean("Achieve_forest", false);
        editor.putBoolean("Achieve_halloween", false);
        editor.putBoolean("Achieve_labor",false);
        editor.putBoolean("Achieve_number", false);
        editor.putBoolean("Achieve_saturn", false);
        editor.putBoolean("Achieve_summer", false);
        editor.putBoolean("Achieve_thanksgiving", false);
        editor.putBoolean("Achieve_travel", false);
        editor.putBoolean("Achieve_unicorn", false);
        editor.apply();


        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //passar path da foto para a variável
        assert data != null;
        uriAct = data.getData();

        //Toast.makeText(getApplicationContext(),String.valueOf(resultCode), Toast.LENGTH_LONG).show();

        //pegar cod da última atividade, somar 1 e passar pra o arquivo no storage
        refAtv = FirebaseDatabase.getInstance().getReference("Activities").child("codAtv");
        refAtv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int icod = Integer.valueOf(dataSnapshot.getValue().toString())+1;
                mStorageRef.child("imsActivities").child(String.valueOf(icod)).putFile(uriAct)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                refAtv.setValue(icod);
                                PerfilAtividade perfilAtividade = new PerfilAtividade(editNomeAct.getText().toString(), Long.valueOf(icod), false);
                                DatabaseReference dataPerfisAct = FirebaseDatabase.getInstance().getReference("perfisAtividades").child(String.valueOf(icod));
                                dataPerfisAct.setValue(perfilAtividade);

                                Toast.makeText(getApplicationContext(), "Atividade adicionada.", Toast.LENGTH_LONG).show();
                               // TastyToast.makeText(getApplicationContext(),"atividade adicionada.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //Toast.makeText(getApplicationContext(), "ouuu dfgdfg    uu", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                               // Toast.makeText(getApplicationContext(),"aqui", Toast.LENGTH_LONG).show();

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


        //função para ler data do último login
    private String getDate(long time) {
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", time).toString();
        Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
        return date;
    }
}
