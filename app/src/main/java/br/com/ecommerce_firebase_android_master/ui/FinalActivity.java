package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.prevalente.Prevalente;

public class FinalActivity extends AppCompatActivity {

    private EditText nomeTxt;
    private EditText telefoneTxt;
    private EditText enderecoTxt;
    private EditText cidadeTxt;
    private Button btn_confirmar;
    private String precoTotal = "";
    private ImageView btn_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        precoTotal = getIntent().getStringExtra("Preco Total");
        btn_confirmar = (Button) findViewById(R.id.btn_finalizar);
        btn_voltar = (ImageView) findViewById(R.id.btn_retornar);
        nomeTxt = (EditText) findViewById(R.id.final_nome);
        telefoneTxt = (EditText) findViewById(R.id.final_telefone);
        cidadeTxt = (EditText) findViewById(R.id.final_cidade);
        enderecoTxt = (EditText) findViewById(R.id.final_endereco);

        nomeTxt.setText(Prevalente.atualUsuarioOnline.getNome());
        telefoneTxt.setText(Prevalente.atualUsuarioOnline.getTelefone());

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificar();
            }
        });
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrincipal = new Intent(FinalActivity.this, PrincipalActivity.class);
                intentPrincipal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentPrincipal);
            }
        });
    }
    private void verificar() {
        if (TextUtils.isEmpty(nomeTxt.getText().toString())) {
            Toast.makeText(this, "Por favor, insira seu nome", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(telefoneTxt.getText().toString())) {
            Toast.makeText(this, "Por favor, digite seu número de telefone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(enderecoTxt.getText().toString())) {
            Toast.makeText(this, "Por favor, digite seu endereço", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cidadeTxt.getText().toString())) {
            Toast.makeText(this, "Por favor, digite sua cidade", Toast.LENGTH_SHORT).show();
        } else {
            confirmar();
        }
    }
    private void confirmar() {
        final String salvarAtualData;
        final String salvarAtualHora;

        Calendar calendario = Calendar.getInstance();

        SimpleDateFormat atualdata = new SimpleDateFormat("dd/MM/yyyy");
        salvarAtualData = atualdata.format(calendario.getTime());

        SimpleDateFormat atualHora = new SimpleDateFormat("HH:mm:ss a");
        salvarAtualHora = atualHora.format(calendario.getTime());

        final DatabaseReference pedidoRef = FirebaseDatabase.getInstance().getReference().child("Pedidos")
                .child(Prevalente.atualUsuarioOnline.getTelefone());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("precoTotal", precoTotal);
        hashMap.put("nome", nomeTxt.getText().toString());
        hashMap.put("telefone", telefoneTxt.getText().toString());
        hashMap.put("endereco", enderecoTxt.getText().toString());
        hashMap.put("cidade", cidadeTxt.getText().toString());
        hashMap.put("data", salvarAtualData);
        hashMap.put("hora", salvarAtualHora);
        hashMap.put("estado", "nao livre");
        
        pedidoRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cartao Lista")
                            .child("Usuario Exibir").child(Prevalente.atualUsuarioOnline.getTelefone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FinalActivity.this, "Validar Pedido", Toast.LENGTH_SHORT).show();
                                Intent intentPrincipal = new Intent(FinalActivity.this, PrincipalActivity.class);
                                intentPrincipal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentPrincipal);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}