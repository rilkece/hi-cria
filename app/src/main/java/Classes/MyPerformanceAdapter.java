package Classes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cria.agora.Login;
import com.cria.agora.MainActivity;
import com.cria.agora.MainAdm;
import com.cria.agora.Performance;
import com.cria.agora.R;
import com.cria.agora.Week;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;


import java.util.ArrayList;

import DadosUsuario.UserWeekPerformance;

public class MyPerformanceAdapter extends RecyclerView.Adapter<MyPerformanceAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserWeekPerformance> perfilWeekScore;

    public MyPerformanceAdapter(Context c, ArrayList<UserWeekPerformance> p){
        context = c;
        perfilWeekScore = p;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_performance, viewGroup, false));


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        String Name = perfilWeekScore.get(i).getName()+" "+perfilWeekScore.get(i).getSurname();
        String schoolName = perfilWeekScore.get(i).getSchool();
        String pontuacao = String.valueOf(perfilWeekScore.get(i).getWeekScore());
        String stgPosition = i+1+"º";

        myViewHolder.pontos.setText(pontuacao);
        myViewHolder.nome.setText(Name);
        myViewHolder.escola.setText(schoolName);
        myViewHolder.position.setText(stgPosition);

        //mudar avatar
        DatabaseReference refAv = FirebaseDatabase.getInstance().getReference("users").child(perfilWeekScore.get(i).getMat()).child("desempenho").child("avatar");
        refAv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        int iAvatar2 = (int) (long) dataSnapshot.getValue();
        switch (iAvatar2){
            case 1: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal1); break;
            case 2: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal2); break;
            case 3: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal3); break;
            case 4: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal4); break;
            case 5: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal5); break;
            case 6: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal6); break;
            case 7: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal7); break;
            case 8: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal8); break;
            case 9: myViewHolder.imgPerfProfile.setImageResource(R.drawable.animal9); break;
            case 11: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit1); break;
            case 12: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit2); break;
            case 13: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit3); break;
            case 14: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit4); break;
            case 15: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit5); break;
            case 16: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit6); break;
            case 17: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit7); break;
            case 18: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit8); break;
            case 19: myViewHolder.imgPerfProfile.setImageResource(R.drawable.fruit9); break;
            case 21: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food1); break;
            case 22: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food2); break;
            case 23: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food3); break;
            case 24: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food4); break;
            case 25: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food5); break;
            case 26: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food6); break;
            case 27: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food7); break;
            case 28: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food8); break;
            case 29: myViewHolder.imgPerfProfile.setImageResource(R.drawable.food9); break;
            case 31: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky1); break;
            case 32: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky2); break;
            case 33: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky3); break;
            case 34: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky4); break;
            case 35: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky5); break;
            case 36: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky6); break;
            case 37: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky7); break;
            case 38: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky8); break;
            case 39: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sky9); break;
            case 41: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert1); break;
            case 42: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert2); break;
            case 43: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert3); break;
            case 44: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert4); break;
            case 45: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert5); break;
            case 46: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert6); break;
            case 47: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert7); break;
            case 48: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert8); break;
            case 49: myViewHolder.imgPerfProfile.setImageResource(R.drawable.dessert9); break;
            case 51: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi1); break;
            case 52: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi2); break;
            case 53: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi3); break;
            case 54: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi4); break;
            case 55: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi5); break;
            case 56: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi6); break;
            case 57: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi7); break;
            case 58: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi8); break;
            case 59: myViewHolder.imgPerfProfile.setImageResource(R.drawable.sushi9); break;
            case 61: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character1); break;
            case 62: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character2); break;
            case 63: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character3); break;
            case 64: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character4); break;
            case 65: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character5); break;
            case 66: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character6); break;
            case 67: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character7); break;
            case 68: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character8); break;
            case 69: myViewHolder.imgPerfProfile.setImageResource(R.drawable.character9); break;
            case 71: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant1); break;
            case 72: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant2); break;
            case 73: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant3); break;
            case 74: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant4); break;
            case 75: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant5); break;
            case 76: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant6); break;
            case 77: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant7); break;
            case 78: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant8); break;
            case 79: myViewHolder.imgPerfProfile.setImageResource(R.drawable.plant9); break;
            case 81: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable1); break;
            case 82: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable2); break;
            case 83: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable3); break;
            case 84: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable4); break;
            case 85: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable5); break;
            case 86: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable6); break;
            case 87: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable7); break;
            case 88: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable8); break;
            case 89: myViewHolder.imgPerfProfile.setImageResource(R.drawable.vegetable9); break;

        }
        myViewHolder.imgPerfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =  context.getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
                if (sharedPreferences.getLong("tipoUser", 0) ==0){
                    DatabaseReference refAv = FirebaseDatabase.getInstance().getReference("users").child(perfilWeekScore.get(i).getMat()).child("desempenho").child("weekLastLogin");

                    //como ler data do último login
                    refAv.child(Week.codAtividade).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue()==null){
                                TastyToast.makeText(context.getApplicationContext(), "unregistered date", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            }else {
                                getDate(Long.parseLong(String.valueOf(dataSnapshot.getValue())));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            TastyToast.makeText(context.getApplicationContext(), "unregistered date", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        }
                    });
                }

            }
        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        if (i==0){
            myViewHolder.imgPerf.setImageResource(R.drawable.trophy1);
            myViewHolder.cardPerformance.setCardBackgroundColor(context.getResources().getColor(R.color.position1));
            myViewHolder.position.setVisibility(View.GONE);
            myViewHolder.imgPerf.setVisibility(View.VISIBLE);
        }else if (i==1){
            myViewHolder.imgPerf.setImageResource(R.drawable.trophy2);
            myViewHolder.cardPerformance.setCardBackgroundColor(context.getResources().getColor(R.color.position2));
            myViewHolder.position.setVisibility(View.GONE);
            myViewHolder.imgPerf.setVisibility(View.VISIBLE);
        }else if (i==2){
            myViewHolder.imgPerf.setImageResource(R.drawable.trophy3);
            myViewHolder.cardPerformance.setCardBackgroundColor(context.getResources().getColor(R.color.position3));
            myViewHolder.position.setVisibility(View.GONE);
            myViewHolder.imgPerf.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.cardPerformance.setCardBackgroundColor(context.getResources().getColor(R.color.backranking));
            myViewHolder.imgPerf.setVisibility(View.GONE);
            myViewHolder.position.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public int getItemCount() {
        return perfilWeekScore.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome;
        TextView escola;
        TextView pontos;
        TextView position;
        CardView cardPerformance;
        ImageView imgPerf;
        ImageView imgPerfProfile;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = (TextView) itemView.findViewById(R.id.txtPerfName);
            escola = (TextView) itemView.findViewById(R.id.txtPerfSchool);
            pontos = (TextView) itemView.findViewById(R.id.txtPerfScore);
            cardPerformance= (CardView) itemView.findViewById(R.id.cardPerformance);
            imgPerf = (ImageView)itemView.findViewById(R.id.imgPerformance);
            position = (TextView) itemView.findViewById(R.id.txtPerfposition);
            imgPerfProfile = (ImageView)itemView.findViewById(R.id.imgPerfProfile);


        }
        }
    //função para ler data do último login
    private String getDate(long time) {
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", time).toString();
        Toast.makeText(context.getApplicationContext(), Performance.txtActivity.getText()+" last login: "+date, Toast.LENGTH_LONG).show();
        return date;
    }


}
