package br.com.ecommerce_firebase_android_master.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.ecommerce_firebase_android_master.R;

public class AdminCategoriaActivity extends AppCompatActivity {

    private ImageView ff;
    private TextView notebook;
    private TextView celulares;
    private TextView vestidosFemininos;
    private TextView calcados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categoria);

        notebook = (TextView) findViewById(R.id.notebooks);
        celulares = (TextView) findViewById(R.id.telefones);
        vestidosFemininos = (TextView) findViewById(R.id.vestidos_femininos);
        calcados = (TextView) findViewById(R.id.sapatos);
        ff = (ImageView) findViewById(R.id.ff);

        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoriaActivity.this, AdminPrincipalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        vestidosFemininos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVestidos = new Intent(AdminCategoriaActivity.this, AddNovoProdutoActivity.class);
                intentVestidos.putExtra("categoria", "Roupas");
                startActivity(intentVestidos);
            }
        });
        calcados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCalcados = new Intent(AdminCategoriaActivity.this, AddNovoProdutoActivity.class);
                intentCalcados.putExtra("categoria", "Sapatos");
                startActivity(intentCalcados);
            }
        });
        notebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNotebook = new Intent(AdminCategoriaActivity.this, AddNovoProdutoActivity.class);
                intentNotebook.putExtra("categoria", "Computadores");
                startActivity(intentNotebook);
            }
        });
        celulares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTelefones = new Intent(AdminCategoriaActivity.this, AddNovoProdutoActivity.class);
                intentTelefones.putExtra("categoria", "Smartphones");
                startActivity(intentTelefones);
            }
        });
    }
}