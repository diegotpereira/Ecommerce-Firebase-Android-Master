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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Usuario;
import br.com.ecommerce_firebase_android_master.prevalente.Prevalente;

public class EntrarActivity extends AppCompatActivity {

    private EditText entrada_telefone_numero;
    private EditText entrada_senha;
    private Button btn_entrar;
    private ProgressDialog carregarBar;
    private TextView adminLink;
    private TextView naoAdminLink;
    private TextView esqueceu_senha;

    private String dadosDbNome = "Usuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        btn_entrar = (Button) findViewById(R.id.btn_entrar);
        entrada_telefone_numero = (EditText) findViewById(R.id.entrar_telefone_numero_entrada);
        entrada_senha = (EditText) findViewById(R.id.enntrar_senha_entrada);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        naoAdminLink = (TextView) findViewById(R.id.nao_admin_painel_link);
        esqueceu_senha = (TextView) findViewById(R.id.esqueceu_senha_link);
        
        esqueceu_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EntrarActivity.this, "Em breve", Toast.LENGTH_SHORT).show();
            }
        });
        carregarBar = new ProgressDialog(this);

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrarUsuario();
            }
        });
        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_entrar.setText("Administrador");
                adminLink.setVisibility(View.INVISIBLE);
                naoAdminLink.setVisibility(View.VISIBLE);
                dadosDbNome = "Admins";
            }
        });
        naoAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_entrar.setText("Entrar");
                adminLink.setVisibility(View.VISIBLE);
                naoAdminLink.setVisibility(View.INVISIBLE);
                dadosDbNome = "Usuarios";
            }
        });
    }
    private void entrarUsuario() {
        String telefone = entrada_telefone_numero.getText().toString();
        String senha = entrada_senha.getText().toString();
        
        if (TextUtils.isEmpty(telefone)) {
            Toast.makeText(this, "Por favor, insira seu número", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Por favor, insira sua senha", Toast.LENGTH_SHORT).show();
        } else {
            carregarBar.setTitle("Conectando");
            carregarBar.setMessage("Por favor, aguarde.");
            carregarBar.setCanceledOnTouchOutside(false);
            carregarBar.show();
            permitirAcessoAhConta(telefone, senha);
        }
    }
    private void permitirAcessoAhConta(final String telefone, String senha) {
        final DatabaseReference dadosRef;
        dadosRef = FirebaseDatabase.getInstance().getReference();

        dadosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(dadosDbNome).child(telefone).exists()) {
                    Usuario usuarioDado = snapshot.child(dadosDbNome).child(telefone).getValue(Usuario.class);
                    if (usuarioDado.getTelefone().equals(telefone)) {
                        if (usuarioDado.getSenha().equals(senha)) {
                            if (dadosDbNome == "Admins") {
                                Toast.makeText(EntrarActivity.this, "Bem-vindo", Toast.LENGTH_SHORT).show();
                                carregarBar.dismiss();

                                Intent adminHome = new Intent(EntrarActivity.this, AdminPrincipalActivity.class);
                                Prevalente.atualUsuarioOnline = usuarioDado;
                                startActivity(adminHome);
                            } else if (dadosDbNome == "Usuarios") {
                                Toast.makeText(EntrarActivity.this, "Bem-vindo", Toast.LENGTH_SHORT).show();
                                carregarBar.dismiss();
                                Prevalente.atualUsuarioOnline = usuarioDado;

                                Intent homeAcitivity = new Intent(EntrarActivity.this, HomeActivity.class);
                                startActivity(homeAcitivity);
                            }
                        }
                    }
                } else {
                    Toast.makeText(EntrarActivity.this, "Essa conta não existe", Toast.LENGTH_SHORT).show();
                    carregarBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}