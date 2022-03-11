package br.com.ecommerce_firebase_android_master.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    }
}