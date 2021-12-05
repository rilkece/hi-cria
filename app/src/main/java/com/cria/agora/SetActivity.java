package com.cria.agora;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SetActivity extends AppCompatActivity {

    private TextView confgText;

    public static int ipaginaAtividade;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        //inicializar elementos
        confgText = findViewById(R.id.txtSetActivity);
        if (ipaginaAtividade==999){
            confgText.setText("Edite activity.");
        }else {
            confgText.setText("Set activity screen: "+String.valueOf(ipaginaAtividade)+".");
        }




        settingTheSetting();

    }



    private void settingTheSetting() {
        super.onStop();

        switch (EscolhaTipoAtividade.iAtividadeAtual) {
            case 1:
               // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv1())
                        .commitNowAllowingStateLoss();
                break;
            case 2:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv2())
                        .commitNowAllowingStateLoss();
                break;
            case 3:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv3())
                        .commitNowAllowingStateLoss();
                break;
            case 4:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv4())
                        .commitNowAllowingStateLoss();
                break;
            case 5:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv5())
                        .commitNowAllowingStateLoss();
                break;
            case 6:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv6())
                        .commitNowAllowingStateLoss();
                break;
            case 7:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv7())
                        .commitNowAllowingStateLoss();
                break;
            case 8:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv8())
                        .commitNowAllowingStateLoss();
                break;
            case 9:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv9())
                        .commitNowAllowingStateLoss();
                break;
            case 10:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv10())
                        .commitNowAllowingStateLoss();
                break;
            case 11:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv11())
                        .commitNowAllowingStateLoss();
                break;
            case 12:
                // Toast.makeText(getApplicationContext(), "setting the set2", TastyToast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameConfigAtv, new SetAtv12())
                        .commitNowAllowingStateLoss();
                break;

        }
    }

}
