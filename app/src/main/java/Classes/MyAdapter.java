package Classes;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cria.agora.Atividades;
import com.cria.agora.Login;
import com.cria.agora.Performance;
import com.cria.agora.R;
import com.cria.agora.Week;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;
import com.suke.widget.SwitchButton;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<PerfilAtividade> perfilAtividades;
    private String lastWeekLogin;

    public MyAdapter(Context c, ArrayList<PerfilAtividade> p){
        context = c;
        perfilAtividades = p;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {


        final int intCodAtv = (int) perfilAtividades.get(i).getCodAtividade();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("imsActivities").child(String.valueOf(intCodAtv));
        final SharedPreferences sharedPreferences =  context.getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        myViewHolder.nomeAtv.setText(perfilAtividades.get(i).getNomeAtividade().toString());
        myViewHolder.play.setText(Integer.toString(intCodAtv));
        //popular o weekscore no textview
        DatabaseReference refWeekScore= FirebaseDatabase.getInstance().getReference("users").child(sharedPreferences.getString("matricula", "09")).child("desempenho").child("weekScore").child(myViewHolder.play.getText().toString());
        refWeekScore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               /* ArrayList<Long> d = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // save all millis in array list you can take your Bean class also
                    try {
                        long daa = Long.parseLong(child.getValue().toString());
                        d.add(daa);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // sort array list you can use comparator if you have bean class
                Collections.sort(d);*/

               if (sharedPreferences.getLong("tipoUser", 1) ==0){
                   myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.ScoreMore100));
                   myViewHolder.weekScore.setVisibility(View.GONE);
               }else {
                   if (dataSnapshot.getValue() == null) {
                       myViewHolder.weekScore.setText("You have not played this week yet.");
                       myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.NoScore));
                   } else if (Integer.valueOf(dataSnapshot.getValue().toString()) < 25) {
                       myViewHolder.weekScore.setText("Your week score: " + String.valueOf(dataSnapshot.getValue()) + " points.");
                       myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.Score25));
                   } else if (Integer.valueOf(dataSnapshot.getValue().toString()) < 50) {
                       myViewHolder.weekScore.setText("Your week score: " + String.valueOf(dataSnapshot.getValue()) + " points.");
                       myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.Score50));
                   } else if (Integer.valueOf(dataSnapshot.getValue().toString()) < 75) {
                       myViewHolder.weekScore.setText("Your week score: " + String.valueOf(dataSnapshot.getValue()) + " points.");
                       myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.Score75));
                   } else if (Integer.valueOf(dataSnapshot.getValue().toString()) < 100) {
                       myViewHolder.weekScore.setText("Your week score: " + String.valueOf(dataSnapshot.getValue()) + " points.");
                       myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.Score100));
                   } else {
                       myViewHolder.weekScore.setText("Your week score: " + String.valueOf(dataSnapshot.getValue()) + " points.");
                       myViewHolder.cardAtv.setCardBackgroundColor(context.getResources().getColor(R.color.ScoreMore100));
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myViewHolder.nomeAtv.setText(perfilAtividades.get(i).getNomeAtividade().toString());
        myViewHolder.play.setText(Integer.toString(intCodAtv));

        Glide.with(context)
                .load(storageReference)
                .into (Objects.requireNonNull(myViewHolder).imgAtividade);
        //Toast.makeText(context, String.valueOf(storageReference), Toast.LENGTH_SHORT).show();

        if (perfilAtividades.get(i).isLiberaAtividade()){
            myViewHolder.lock.setVisibility(View.INVISIBLE);
            myViewHolder.play.setVisibility(View.VISIBLE);
            myViewHolder.onClick(i);
        }
        if (sharedPreferences.getLong("tipoUser", 1) ==0){
            myViewHolder.lock.setVisibility(View.INVISIBLE);
            myViewHolder.play.setVisibility(View.VISIBLE);
            myViewHolder.onClick(i);
            myViewHolder.switchButton.setVisibility(View.VISIBLE);
            if (perfilAtividades.get(i).isLiberaAtividade()){
                myViewHolder.switchButton.setChecked(true);

            }
        }
        myViewHolder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                DatabaseReference dataSwitch = FirebaseDatabase.getInstance().getReference("perfisAtividades").child(String.valueOf(intCodAtv)).child("liberaAtividade");
                if(isChecked==true){
                    dataSwitch.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            TastyToast.makeText(context, "Atividade Liberada", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        }
                    });

                }else{
                    dataSwitch.setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            TastyToast.makeText(context, "Atividade Bloqueada", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return perfilAtividades.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nomeAtv;
        TextView weekScore;
        MaterialButton lock, play;
        ImageView imgAtividade;
        com.suke.widget.SwitchButton switchButton;
        CardView cardAtv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeAtv = (TextView) itemView.findViewById(R.id.nomeAtividade);
            weekScore = (TextView) itemView.findViewById(R.id.nomeWeekScore);
            cardAtv = (CardView) itemView.findViewById(R.id.cardAtv);
            lock = (MaterialButton) itemView.findViewById(R.id.lockButton);
            play = (MaterialButton) itemView.findViewById(R.id.playAtividades);
            imgAtividade = (ImageView) itemView.findViewById(R.id.ActivityPic);
            switchButton = (com.suke.widget.SwitchButton) itemView.findViewById(R.id.switch_button);

        }

        public void onClick(final int position){
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Atividades.isPerformance){
                        Week.codAtividade = play.getText().toString();
                        Intent i = new Intent(context.getApplicationContext(), Performance.class);
                        context.startActivity(i);
                        //Toast.makeText(context.getApplicationContext(), ""+Atividades.isPerformance, Toast.LENGTH_LONG).show();
                    }else {
                        SharedPreferences sharedPreferencesClick =  context.getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
                        DatabaseReference  myRef = FirebaseDatabase.getInstance().getReference("users").child(sharedPreferencesClick.getString("matricula", "0")).child("desempenho").child("weekLastLogin");
                        Map map = new HashMap();
                        map.put(play.getText().toString(), ServerValue.TIMESTAMP);
                        myRef.updateChildren(map);

                        Week.codAtividade = play.getText().toString();
                        Intent i = new Intent(context.getApplicationContext(), Week.class);
                        context.startActivity(i);

                        //Toast.makeText(context.getApplicationContext(), "olha"+Atividades.isPerformance, Toast.LENGTH_LONG).show();

                    }


                }
            });
        }
    }



}
