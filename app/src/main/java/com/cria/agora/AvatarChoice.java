package com.cria.agora;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cleveroad.loopbar.widget.LoopBarView;
import com.cleveroad.loopbar.widget.OnItemClickListener;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AvatarChoice extends AppCompatActivity {
   private ImageButton animal1;
   private ImageButton animal2;
   private ImageButton animal3;
   private ImageButton animal4;
   private ImageButton animal5;
   private ImageButton animal6;
   private ImageButton animal7;
   private ImageButton animal8;
   private ImageButton animal9;
    private ImageButton fruit1;
   private ImageButton fruit2;
   private ImageButton fruit3;
   private ImageButton fruit4;
   private ImageButton fruit5;
   private ImageButton fruit6;
   private ImageButton fruit7;
   private ImageButton fruit8;
   private ImageButton fruit9;
    private ImageButton food1;
   private ImageButton food2;
   private ImageButton food3;
   private ImageButton food4;
   private ImageButton food5;
   private ImageButton food6;
   private ImageButton food7;
   private ImageButton food8;
   private ImageButton food9;
    private ImageButton sky1;
   private ImageButton sky2;
   private ImageButton sky3;
   private ImageButton sky4;
   private ImageButton sky5;
   private ImageButton sky6;
   private ImageButton sky7;
   private ImageButton sky8;
   private ImageButton sky9;
    private ImageButton sushi1;
   private ImageButton sushi2;
   private ImageButton sushi3;
   private ImageButton sushi4;
   private ImageButton sushi5;
   private ImageButton sushi6;
   private ImageButton sushi7;
   private ImageButton sushi8;
   private ImageButton sushi9;
    private ImageButton plant1;
   private ImageButton plant2;
   private ImageButton plant3;
   private ImageButton plant4;
   private ImageButton plant5;
   private ImageButton plant6;
   private ImageButton plant7;
   private ImageButton plant8;
   private ImageButton plant9;
    private ImageButton character1;
   private ImageButton character2;
   private ImageButton character3;
   private ImageButton character4;
   private ImageButton character5;
   private ImageButton character6;
   private ImageButton character7;
   private ImageButton character8;
   private ImageButton character9;
    private ImageButton dessert1;
   private ImageButton dessert2;
   private ImageButton dessert3;
   private ImageButton dessert4;
   private ImageButton dessert5;
   private ImageButton dessert6;
   private ImageButton dessert7;
   private ImageButton dessert8;
   private ImageButton dessert9;
   private ImageButton vegetable1;
   private ImageButton vegetable2;
   private ImageButton vegetable3;
   private ImageButton vegetable4;
   private ImageButton vegetable5;
   private ImageButton vegetable6;
   private ImageButton vegetable7;
   private ImageButton vegetable8;
   private ImageButton vegetable9;

    private LinearLayout linearAnimal1;
    private LinearLayout linearAnimal2;
    private LinearLayout linearAnimal3;
    private LinearLayout linearfruit1;
    private LinearLayout linearfruit2;
    private LinearLayout linearfruit3;
    private LinearLayout linearfood1;
    private LinearLayout linearfood2;
    private LinearLayout linearfood3;
    private LinearLayout linearsky1;
    private LinearLayout linearsky2;
    private LinearLayout linearsky3;
    private LinearLayout lineardessert1;
    private LinearLayout lineardessert2;
    private LinearLayout lineardessert3;
    private LinearLayout linearsushi1;
    private LinearLayout linearsushi2;
    private LinearLayout linearsushi3;
    private LinearLayout linearcharacter1;
    private LinearLayout linearcharacter2;
    private LinearLayout linearcharacter3;
    private LinearLayout linearplant1;
    private LinearLayout linearplant2;
    private LinearLayout linearplant3;
    private LinearLayout linearvegetable1;
    private LinearLayout linearvegetable2;
    private LinearLayout linearvegetable3;


    private LoopBarView loopBarView;

    FirebaseDatabase firebaseDatabaseAvatar;
    DatabaseReference databaseReferenceAvatar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_choice);
        //iniciar elementos
        loopBarView = findViewById(R.id.endlessView);
        animal1 = findViewById(R.id.imgbtnanimal1); animal2 = findViewById(R.id.imgbtnanimal2); animal3 = findViewById(R.id.imgbtnanimal3); animal4 = findViewById(R.id.imgbtnanimal4); animal5 = findViewById(R.id.imgbtnanimal5); animal6 = findViewById(R.id.imgbtnanimal6); animal7 = findViewById(R.id.imgbtnanimal7); animal8 = findViewById(R.id.imgbtnanimal8); animal9 = findViewById(R.id.imgbtnanimal9);
        fruit1 = findViewById(R.id.imgbtnfruit1); fruit2 = findViewById(R.id.imgbtnfruit2); fruit3 = findViewById(R.id.imgbtnfruit3); fruit4 = findViewById(R.id.imgbtnfruit4); fruit5 = findViewById(R.id.imgbtnfruit5); fruit6 = findViewById(R.id.imgbtnfruit6); fruit7 = findViewById(R.id.imgbtnfruit7); fruit8 = findViewById(R.id.imgbtnfruit8); fruit9 = findViewById(R.id.imgbtnfruit9);
        food1 = findViewById(R.id.imgbtnfood1); food2 = findViewById(R.id.imgbtnfood2); food3 = findViewById(R.id.imgbtnfood3); food4 = findViewById(R.id.imgbtnfood4); food5 = findViewById(R.id.imgbtnfood5); food6 = findViewById(R.id.imgbtnfood6); food7 = findViewById(R.id.imgbtnfood7); food8 = findViewById(R.id.imgbtnfood8); food9 = findViewById(R.id.imgbtnfood9);
        sky1 = findViewById(R.id.imgbtnsky1); sky2 = findViewById(R.id.imgbtnsky2); sky3 = findViewById(R.id.imgbtnsky3); sky4 = findViewById(R.id.imgbtnsky4); sky5 = findViewById(R.id.imgbtnsky5); sky6 = findViewById(R.id.imgbtnsky6); sky7 = findViewById(R.id.imgbtnsky7); sky8 = findViewById(R.id.imgbtnsky8); sky9 = findViewById(R.id.imgbtnsky9);
        dessert1 = findViewById(R.id.imgbtndessert1); dessert2 = findViewById(R.id.imgbtndessert2); dessert3 = findViewById(R.id.imgbtndessert3); dessert4 = findViewById(R.id.imgbtndessert4); dessert5 = findViewById(R.id.imgbtndessert5); dessert6 = findViewById(R.id.imgbtndessert6); dessert7 = findViewById(R.id.imgbtndessert7); dessert8 = findViewById(R.id.imgbtndessert8); dessert9 = findViewById(R.id.imgbtndessert9);
        sushi1 = findViewById(R.id.imgbtnsushi1); sushi2 = findViewById(R.id.imgbtnsushi2); sushi3 = findViewById(R.id.imgbtnsushi3); sushi4 = findViewById(R.id.imgbtnsushi4); sushi5 = findViewById(R.id.imgbtnsushi5); sushi6 = findViewById(R.id.imgbtnsushi6); sushi7 = findViewById(R.id.imgbtnsushi7); sushi8 = findViewById(R.id.imgbtnsushi8); sushi9 = findViewById(R.id.imgbtnsushi9);
        character1 = findViewById(R.id.imgbtncharacter1); character2 = findViewById(R.id.imgbtncharacter2); character3 = findViewById(R.id.imgbtncharacter3); character4 = findViewById(R.id.imgbtncharacter4); character5 = findViewById(R.id.imgbtncharacter5); character6 = findViewById(R.id.imgbtncharacter6); character7 = findViewById(R.id.imgbtncharacter7); character8 = findViewById(R.id.imgbtncharacter8); character9 = findViewById(R.id.imgbtncharacter9);
        plant1 = findViewById(R.id.imgbtnplant1); plant2 = findViewById(R.id.imgbtnplant2); plant3 = findViewById(R.id.imgbtnplant3); plant4 = findViewById(R.id.imgbtnplant4); plant5 = findViewById(R.id.imgbtnplant5); plant6 = findViewById(R.id.imgbtnplant6); plant7 = findViewById(R.id.imgbtnplant7); plant8 = findViewById(R.id.imgbtnplant8); plant9 = findViewById(R.id.imgbtnplant9);
        vegetable1 = findViewById(R.id.imgbtnvegetable1); vegetable2 = findViewById(R.id.imgbtnvegetable2); vegetable3 = findViewById(R.id.imgbtnvegetable3); vegetable4 = findViewById(R.id.imgbtnvegetable4); vegetable5 = findViewById(R.id.imgbtnvegetable5); vegetable6 = findViewById(R.id.imgbtnvegetable6); vegetable7 = findViewById(R.id.imgbtnvegetable7); vegetable8 = findViewById(R.id.imgbtnvegetable8); vegetable9 = findViewById(R.id.imgbtnvegetable9);

        final LinearLayout linearAvataranimal = findViewById(R.id.linearAvataranimal);
        final LinearLayout linearAvatarfruit = findViewById(R.id.linearAvatarfruit);
        final LinearLayout linearAvatarfood = findViewById(R.id.linearAvatarfood);
        final LinearLayout linearAvatarsky = findViewById(R.id.linearAvatarsky);
        final LinearLayout linearAvatardessert = findViewById(R.id.linearAvatardessert);
        final LinearLayout linearAvatarsushi = findViewById(R.id.linearAvatarsushi);
        final LinearLayout linearAvatarcharacter = findViewById(R.id.linearAvatarcharacter);
        final LinearLayout linearAvatarvegetable = findViewById(R.id.linearAvatarvegetable);
        final LinearLayout linearAvatarplant = findViewById(R.id.linearAvatarplant);

        linearAnimal1 = findViewById(R.id.linearAnimal1);
        linearAnimal2 = findViewById(R.id.linearAnimal2);
        linearAnimal3 = findViewById(R.id.linearAnimal3);
        linearfruit1 = findViewById(R.id.linearFruit1);
        linearfruit2 = findViewById(R.id.linearfruit2);
        linearfruit3 = findViewById(R.id.linearfruit3);
        linearfood1 = findViewById(R.id.linearfood1);
        linearfood2 = findViewById(R.id.linearfood2);
        linearfood3 = findViewById(R.id.linearfood3);
        linearsky1 = findViewById(R.id.linearsky1);
        linearsky2 = findViewById(R.id.linearsky2);
        linearsky3 = findViewById(R.id.linearsky3);
        lineardessert1 = findViewById(R.id.lineardessert1);
        lineardessert2 = findViewById(R.id.lineardessert2);
        lineardessert3 = findViewById(R.id.lineardessert3);
        linearsushi1 = findViewById(R.id.linearsushi1);
        linearsushi2 = findViewById(R.id.linearsushi2);
        linearsushi3 = findViewById(R.id.linearsushi3);
        linearcharacter1 = findViewById(R.id.linearcharacter1);
        linearcharacter2 = findViewById(R.id.linearcharacter2);
        linearcharacter3 = findViewById(R.id.linearcharacter3);
        linearplant1 = findViewById(R.id.linearplant1);
        linearplant2 = findViewById(R.id.linearplant2);
        linearplant3 = findViewById(R.id.linearplant3);
        linearvegetable1 = findViewById(R.id.linearvegetable1);
        linearvegetable2 = findViewById(R.id.linearvegetable2);
        linearvegetable3 = findViewById(R.id.linearvegetable3);

        verificaAvatar();


        firebaseDatabaseAvatar = FirebaseDatabase.getInstance();

        //mudar avatar ao clicar
        animal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(1);
            }
        });
        animal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(2);
            }
        });
        animal3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(3);
            }
        });
        animal4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(4);
            }
        });
        animal5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(5);
            }
        });
        animal6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(6);
            }
        });
        animal7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(7);
            }
        });
        animal8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(8);
            }
        });
        animal9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(9);
            }
        });

        fruit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(11);
            }
        });
        fruit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(12);
            }
        });
        fruit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(13);
            }
        });
        fruit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(14);
            }
        });
        fruit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(15);
            }
        });
        fruit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(16);
            }
        });
        fruit7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(17);
            }
        });
        fruit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(18);
            }
        });
        fruit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(19);
            }
        });

        food1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(21);
            }
        });
        food2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(22);
            }
        });
        food3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(23);
            }
        });
        food4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(24);
            }
        });
        food5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(25);
            }
        });
        food6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(26);
            }
        });
        food7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(27);
            }
        });
        food8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(28);
            }
        });
        food9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(29);
            }
        });

        sky1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(31);
            }
        });
        sky2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(32);
            }
        });
        sky3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(33);
            }
        });
        sky4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(34);
            }
        });
        sky5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(35);
            }
        });
        sky6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(36);
            }
        });
        sky7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(37);
            }
        });
        sky8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(38);
            }
        });
        sky9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(39);
            }
        });

        dessert1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(41);
            }
        });
        dessert2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(42);
            }
        });
        dessert3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(43);
            }
        });
        dessert4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(44);
            }
        });
        dessert5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(45);
            }
        });
        dessert6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(46);
            }
        });
        dessert7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(47);
            }
        });
        dessert8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(48);
            }
        });
        dessert9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(49);
            }
        });

        sushi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(51);
            }
        });
        sushi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(52);
            }
        });
        sushi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(53);
            }
        });
        sushi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(54);
            }
        });
        sushi5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(55);
            }
        });
        sushi6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(56);
            }
        });
        sushi7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(57);
            }
        });
        sushi8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(58);
            }
        });
        sushi9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(59);
            }
        });

        character1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(61);
            }
        });
        character2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(62);
            }
        });
        character3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(63);
            }
        });
        character4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(64);
            }
        });
        character5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(65);
            }
        });
        character6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(66);
            }
        });
        character7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(67);
            }
        });
        character8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(68);
            }
        });
        character9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(69);
            }
        });

        plant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(71);
            }
        });
        plant2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(72);
            }
        });
        plant3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(73);
            }
        });
        plant4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(74);
            }
        });
        plant5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(75);
            }
        });
        plant6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(76);
            }
        });
        plant7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(77);
            }
        });
        plant8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(78);
            }
        });
        plant9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(79);
            }
        });

        vegetable1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(81);
            }
        });
        vegetable2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(82);
            }
        });
        vegetable3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(83);
            }
        });
        vegetable4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(84);
            }
        });
        vegetable5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(85);
            }
        });
        vegetable6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(86);
            }
        });
        vegetable7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(87);
            }
        });
        vegetable8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(88);
            }
        });
        vegetable9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarAvatar(89);
            }
        });


        //setar loopbar pra carregar os lineares layouts correspondentes
        loopBarView.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
           switch (position){
                   case 0:
                     linearAvataranimal.setVisibility(View.VISIBLE);
                     linearAvatarfruit.setVisibility(View.GONE);
                     linearAvatarfood.setVisibility(View.GONE);
                     linearAvatarsky.setVisibility(View.GONE);
                     linearAvatardessert.setVisibility(View.GONE);
                     linearAvatarsushi.setVisibility(View.GONE);
                     linearAvatarcharacter.setVisibility(View.GONE);
                     linearAvatarplant.setVisibility(View.GONE);
                     linearAvatarvegetable.setVisibility(View.GONE);

                   break;

                   case 1:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.VISIBLE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 2:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.VISIBLE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 3:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.VISIBLE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 4:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.VISIBLE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 5:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.VISIBLE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 6:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.VISIBLE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 7:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.VISIBLE);
                       linearAvatarvegetable.setVisibility(View.GONE);
                   break;
                   case 8:
                       linearAvataranimal.setVisibility(View.GONE);
                       linearAvatarfruit.setVisibility(View.GONE);
                       linearAvatarfood.setVisibility(View.GONE);
                       linearAvatarsky.setVisibility(View.GONE);
                       linearAvatardessert.setVisibility(View.GONE);
                       linearAvatarsushi.setVisibility(View.GONE);
                       linearAvatarcharacter.setVisibility(View.GONE);
                       linearAvatarplant.setVisibility(View.GONE);
                       linearAvatarvegetable.setVisibility(View.VISIBLE);
                   break;

           }
            }
        });


    }

    private void verificaAvatar() {
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        FirebaseDatabase fireUser = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReferenceUser = fireUser.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho");

        if (sharedPreferences.getLong("tipoUser", 1) ==0){
            linearAnimal1.setVisibility(View.VISIBLE);
            linearAnimal2.setVisibility(View.VISIBLE);
            linearAnimal3.setVisibility(View.VISIBLE);
            linearfruit1.setVisibility(View.VISIBLE);
            linearfruit2.setVisibility(View.VISIBLE);
            linearfruit3.setVisibility(View.VISIBLE);
            linearfood1.setVisibility(View.VISIBLE);
            linearfood2.setVisibility(View.VISIBLE);
            linearfood3.setVisibility(View.VISIBLE);
            linearsky1.setVisibility(View.VISIBLE);
            linearsky2.setVisibility(View.VISIBLE);
            linearsky3.setVisibility(View.VISIBLE);
            lineardessert1.setVisibility(View.VISIBLE);
            lineardessert2.setVisibility(View.VISIBLE);
            lineardessert3.setVisibility(View.VISIBLE);
            linearsushi1.setVisibility(View.VISIBLE);
            linearsushi2.setVisibility(View.VISIBLE);
            linearsushi3.setVisibility(View.VISIBLE);
            linearcharacter1.setVisibility(View.VISIBLE);
            linearcharacter2.setVisibility(View.VISIBLE);
            linearcharacter3.setVisibility(View.VISIBLE);
            linearplant1.setVisibility(View.VISIBLE);
            linearplant2.setVisibility(View.VISIBLE);
            linearplant3.setVisibility(View.VISIBLE);
            linearvegetable1.setVisibility(View.VISIBLE);
            linearvegetable2.setVisibility(View.VISIBLE);
            linearvegetable3.setVisibility(View.VISIBLE);
        }else {
            //pelos achievements
            if (sharedPreferences.getBoolean("Achieve_eco", false)){

                linearfruit3.setVisibility(View.VISIBLE);

            }
            if (sharedPreferences.getBoolean("Achieve_forest", false)){
                linearAnimal3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_halloween", false)){
                lineardessert3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_labor", false)){
                linearsushi3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_number", false)){
                linearvegetable3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_saturn", false)){
                linearsky3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_summer", false)){
                linearplant3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_thanksgiving", false)){
                linearfood3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_travel", false)){
                linearcharacter3.setVisibility(View.VISIBLE);
            }
            if (sharedPreferences.getBoolean("Achieve_unicorn", false)){

            }
            //pela pontuação
            databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long iAtv = (long)dataSnapshot.child("atvRealizadas").getValue();
                    long iScore = (long) dataSnapshot.child("score").getValue();
                    long iAvatar = (long) dataSnapshot.child("avatar").getValue();
                    String iLevel = (String) dataSnapshot.child("level").getValue();

                    if (iScore>7000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);
                        linearsky2.setVisibility(View.VISIBLE);
                        lineardessert2.setVisibility(View.VISIBLE);
                        linearsushi2.setVisibility(View.VISIBLE);
                        linearcharacter2.setVisibility(View.VISIBLE);
                        linearplant2.setVisibility(View.VISIBLE);
                        linearvegetable2.setVisibility(View.VISIBLE);

                    }else if (iScore>6000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);
                        linearsky2.setVisibility(View.VISIBLE);
                        lineardessert2.setVisibility(View.VISIBLE);
                        linearsushi2.setVisibility(View.VISIBLE);
                        linearcharacter2.setVisibility(View.VISIBLE);
                        linearplant2.setVisibility(View.VISIBLE);
                    }else if (iScore>5000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);
                        linearsky2.setVisibility(View.VISIBLE);
                        lineardessert2.setVisibility(View.VISIBLE);
                        linearsushi2.setVisibility(View.VISIBLE);
                        linearcharacter2.setVisibility(View.VISIBLE);
                    }else if (iScore>4000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);
                        linearsky2.setVisibility(View.VISIBLE);
                        lineardessert2.setVisibility(View.VISIBLE);
                        linearsushi2.setVisibility(View.VISIBLE);

                    }else if (iScore>3000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);
                        linearsky2.setVisibility(View.VISIBLE);
                        lineardessert2.setVisibility(View.VISIBLE);

                    }else if (iScore>2000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);
                        linearsky2.setVisibility(View.VISIBLE);


                    }else if (iScore>1000){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);
                        linearfood2.setVisibility(View.VISIBLE);

                    }else if(iScore>500){
                        linearAnimal2.setVisibility(View.VISIBLE);
                        linearfruit2.setVisibility(View.VISIBLE);

                    }else if(iScore>250){
                        linearAnimal2.setVisibility(View.VISIBLE);

                    }

                    if (iAtv>1000){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);
                        linearsky1.setVisibility(View.VISIBLE);
                        lineardessert1.setVisibility(View.VISIBLE);
                        linearsushi1.setVisibility(View.VISIBLE);
                        linearcharacter1.setVisibility(View.VISIBLE);
                        linearplant1.setVisibility(View.VISIBLE);
                        linearvegetable1.setVisibility(View.VISIBLE);
                    }else if (iAtv>800){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);
                        linearsky1.setVisibility(View.VISIBLE);
                        lineardessert1.setVisibility(View.VISIBLE);
                        linearsushi1.setVisibility(View.VISIBLE);
                        linearcharacter1.setVisibility(View.VISIBLE);
                        linearplant1.setVisibility(View.VISIBLE);
                    }else if (iAtv>600){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);
                        linearsky1.setVisibility(View.VISIBLE);
                        lineardessert1.setVisibility(View.VISIBLE);
                        linearsushi1.setVisibility(View.VISIBLE);
                        linearcharacter1.setVisibility(View.VISIBLE);

                    }else if (iAtv>450){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);
                        linearsky1.setVisibility(View.VISIBLE);
                        lineardessert1.setVisibility(View.VISIBLE);
                        linearsushi1.setVisibility(View.VISIBLE);
                    }else if (iAtv>300){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);
                        linearsky1.setVisibility(View.VISIBLE);
                        lineardessert1.setVisibility(View.VISIBLE);

                    }else if (iAtv>200){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);
                        linearsky1.setVisibility(View.VISIBLE);

                    }else if (iAtv>150){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);
                        linearfood1.setVisibility(View.VISIBLE);

                    }else if (iAtv>100){
                        linearAnimal1.setVisibility(View.VISIBLE);
                        linearfruit1.setVisibility(View.VISIBLE);

                    }else if (iAtv>50){
                        linearAnimal1.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void mudarAvatar(final int i) {
        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);
        final Intent iAvatarMain = new Intent(getApplicationContext(), MainActivity.class);
        final Intent iAvatarAdm = new Intent(getApplicationContext(), MainAdm.class);
        final  SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);


        dialogBuilder
                .withTitle("Hi!")
                .withMessage("Do you want to change your avatar?")
                    .withButton1Text("Yes")
                    .withButton2Text("No")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReferenceAvatar = firebaseDatabaseAvatar.getReference("users").child(sharedPreferences.getString("matricula", "0")).child("desempenho").child("avatar");
                            databaseReferenceAvatar.setValue(i);
                            dialogBuilder.hide();
                            if (sharedPreferences.getLong("tipoUser", 1) == 0){
                                startActivity(iAvatarAdm);
                            }else {
                                startActivity(iAvatarMain);
                            }

                        }
                    })
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.hide();
                            if (sharedPreferences.getLong("tipoUser", 1) == 0){
                                startActivity(iAvatarAdm);
                            }else {
                                startActivity(iAvatarMain);
                            }

                        }
                    })
                .show();

    }
}
