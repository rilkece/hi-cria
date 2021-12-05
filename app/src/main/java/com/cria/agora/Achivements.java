package com.cria.agora;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Achivements extends AppCompatActivity {

    private ImageButton imgBtnvoltarfromAchieve;

    private ImageView imgEco;
    private ImageView imgForest;
    private ImageView imgHalloween;
    private ImageView imgLabor;
    private ImageView imgNumber;
    private ImageView imgSaturn;
    private ImageView imgThanksgiving;
    private ImageView imgTravel;
    private ImageView imgSummer;
    private ImageView imgUnicorn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivements);

        //declarar elementos
        imgEco = findViewById(R.id.imgBadgeEco);
        imgForest = findViewById(R.id.imgBadgeForest);
        imgHalloween = findViewById(R.id.imgBadgeHalloween);
        imgLabor = findViewById(R.id.imgBadgeLabor);
        imgNumber = findViewById(R.id.imgBadgeNumber);
        imgSaturn = findViewById(R.id.imgBadgeSaturn);
        imgSummer = findViewById(R.id.imgBadgeSummer);
        imgThanksgiving = findViewById(R.id.imgBadgeThanksgiving);
        imgTravel = findViewById(R.id.imgBadgeTravel);
        imgUnicorn = findViewById(R.id.imgBadgeUnicorn);

        imgBtnvoltarfromAchieve = findViewById(R.id.imgbtnAchieveBack);

        //ações
        imgBtnvoltarfromAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAvatarMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(iAvatarMain);
            }
        });

        //funções
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
        setarBadges(sharedPreferences.getLong("tipoUser", 1));
    }

    private void setarBadges(long tipoUser) {
        if(tipoUser==0 || tipoUser==8){
            imgEco.setImageResource(R.drawable.badge_ecofriendly);
            imgForest.setImageResource(R.drawable.badge_forest);
            imgHalloween.setImageResource(R.drawable.badge_halloween);
            imgLabor.setImageResource(R.drawable.badge_laborday);
            imgNumber.setImageResource(R.drawable.badge_numberone);
            imgSaturn.setImageResource(R.drawable.badge_saturn);
            imgSummer.setImageResource(R.drawable.badge_summertime);
            imgThanksgiving.setImageResource(R.drawable.badge_thanksgiving);
            imgTravel.setImageResource(R.drawable.badge_travel);
            imgUnicorn.setImageResource(R.drawable.badge_unicorn);
        }else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("Achieve_eco", false)){
                imgEco.setImageResource(R.drawable.badge_ecofriendly);
            }
            if (sharedPreferences.getBoolean("Achieve_forest", false)){
                imgForest.setImageResource(R.drawable.badge_forest);
            }
            if (sharedPreferences.getBoolean("Achieve_halloween", false)){
                imgHalloween.setImageResource(R.drawable.badge_halloween);
            }
            if (sharedPreferences.getBoolean("Achieve_labor", false)){
                imgLabor.setImageResource(R.drawable.badge_laborday);
            }
            if (sharedPreferences.getBoolean("Achieve_number", false)){
                imgNumber.setImageResource(R.drawable.badge_numberone);
            }
            if (sharedPreferences.getBoolean("Achieve_saturn", false)){
                imgSaturn.setImageResource(R.drawable.badge_saturn);
            }
            if (sharedPreferences.getBoolean("Achieve_summer", false)){
                imgSummer.setImageResource(R.drawable.badge_summertime);
            }
            if (sharedPreferences.getBoolean("Achieve_thanksgiving", false)){
                imgThanksgiving.setImageResource(R.drawable.badge_thanksgiving);
            }
            if (sharedPreferences.getBoolean("Achieve_travel", false)){
                imgTravel.setImageResource(R.drawable.badge_travel);
            }
            if (sharedPreferences.getBoolean("Achieve_unicorn", false)){
                imgUnicorn.setImageResource(R.drawable.badge_unicorn);
            }

        }

    }
}
