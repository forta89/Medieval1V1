package com.medieval.v1;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout tela = new LinearLayout(this);
        tela.setOrientation(LinearLayout.VERTICAL);
        tela.setGravity(Gravity.CENTER);
        tela.setBackgroundColor(Color.rgb(35, 30, 25));

        TextView titulo = new TextView(this);
        titulo.setText("MEDIEVAL V1");
        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(32);
        titulo.setGravity(Gravity.CENTER);

        TextView status = new TextView(this);
        status.setText("\nMundo medieval carregando...\n\nVida: 100   Estamina: 100");
        status.setTextColor(Color.WHITE);
        status.setTextSize(18);
        status.setGravity(Gravity.CENTER);

        tela.addView(titulo);
        tela.addView(status);

        setContentView(tela);
    }
}
