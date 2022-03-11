package br.com.ecommerce_firebase_android_master.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.ecommerce_firebase_android_master.R;

public class AdminPrincipalActivity extends AppCompatActivity {

    private Button btn_sair;
    private Button btn_exibirComando;
    private Button btn_add_categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_principal);

        btn_sair = (Button) findViewById(R.id.btn_sair_admin);
        btn_exibirComando = (Button) findViewById(R.id.btn_comando);
        btn_add_categoria = (Button) findViewById(R.id.btn_add_produto);


         btn_add_categoria.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent adminCategoria = new Intent(AdminPrincipalActivity.this, AdminCategoriaActivity.class);
                 startActivity(adminCategoria);
                 finish();
             }
         });
    }
}