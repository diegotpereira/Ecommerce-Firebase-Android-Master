package br.com.ecommerce_firebase_android_master.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.ecommerce_firebase_android_master.R;

public class MainActivity extends AppCompatActivity {

    private Button btn_cadastre_se;
    private Button btn_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cadastre_se = (Button) findViewById(R.id.btn_cadastre_se);
        btn_entrar = (Button) findViewById(R.id.btn_entrar);

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entrar = new Intent(MainActivity.this, EntrarActivity.class);
                startActivity(entrar);
            }
        });

        btn_cadastre_se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastrar = new Intent(MainActivity.this, CadastrarActivity.class);
                startActivity(cadastrar);
            }
        });
    }
}