package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.ecommerce_firebase_android_master.R;

public class CadastrarActivity extends AppCompatActivity {

    private Button btn_criar_conta;
    private EditText entrada_nome;
    private EditText entrada_telefone;
    private EditText entrada_senha;
    private ProgressDialog carregarBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        btn_criar_conta = (Button) findViewById(R.id.btn_registrar);
        entrada_nome = (EditText)  findViewById(R.id.registrar_entrada_usuarionome);
        entrada_telefone = (EditText) findViewById(R.id.registrar_entrada_numero_telefone);
        entrada_senha = (EditText) findViewById(R.id.registrar_entrada_senha);
        carregarBar = new ProgressDialog(this);

        btn_criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarConta();
            }
        });
    }
    private void criarConta() {
        String nome = entrada_nome.getText().toString();
        String telefone = entrada_telefone.getText().toString();
        String senha = entrada_senha.getText().toString();
        
        // validação
        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(this, "Por favor, insira seu nome", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(telefone)) {
            Toast.makeText(this, "Por favor, insira seu telefone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor, insira sua senha", Toast.LENGTH_SHORT).show();
        } else {
            carregarBar.setTitle("Cadastrando a conta");
            carregarBar.setMessage("Por favor, aguarde");
            carregarBar.setCanceledOnTouchOutside(false);
            carregarBar.show();

            validar(nome, telefone, senha);
        }
    }
    private void validar(final String nome, final String telefone, final String senha) {
        final DatabaseReference dadosRef;
        dadosRef = FirebaseDatabase.getInstance().getReference();

        dadosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Usuarios").child(telefone).exists())) {
                    HashMap<String, Object> usuarioDadosMap = new HashMap<>();
                    usuarioDadosMap.put("telefone", telefone);
                    usuarioDadosMap.put("senha", senha);
                    usuarioDadosMap.put("nome", nome);

                    dadosRef.child("Usuarios").child(telefone).updateChildren(usuarioDadosMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CadastrarActivity.this, "Conta criada com sucesso.", Toast.LENGTH_SHORT).show();
                                        carregarBar.dismiss();

                                        Intent entrar = new Intent(CadastrarActivity.this, EntrarActivity.class);
                                        startActivity(entrar);
                                    } else {
                                        carregarBar.dismiss();
                                        Toast.makeText(CadastrarActivity.this, "Erro na conexão", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CadastrarActivity.this, "Esta conta já existe", Toast.LENGTH_SHORT).show();
                    carregarBar.dismiss();
                    Toast.makeText(CadastrarActivity.this, "Por favor, escolha outro número", Toast.LENGTH_SHORT).show();

                    Intent principal = new Intent(CadastrarActivity.this, MainActivity.class);
                    startActivity(principal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}