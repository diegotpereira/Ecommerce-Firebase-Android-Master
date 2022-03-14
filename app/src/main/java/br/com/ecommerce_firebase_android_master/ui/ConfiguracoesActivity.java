package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.provider.FirebaseInitProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.prevalente.Prevalente;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private CircleImageView perfilImagemView;
    private EditText nomeCompleto;
    private EditText usuarioTelefone;
    private EditText usuarioEndereco;
    private TextView perfilAlterarTextoBtn;
    private TextView salvarTextoBotao;
    private ImageView btn_fechar_texto;

    private Uri imagemUri;
    private String meuUrl = "";
    private StorageTask subirTarefa;
    private StorageReference perfilFotoRef;
    private String verificador = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        perfilFotoRef = FirebaseStorage.getInstance().getReference().child("Perfil fotos");

        perfilImagemView = (CircleImageView) findViewById(R.id.configuracoes_perfil_imagem);
        nomeCompleto = (EditText) findViewById(R.id.configuracoes_nomecompleto);
        usuarioTelefone = (EditText) findViewById(R.id.configuracoes_telefone_numero);
        usuarioEndereco = (EditText) findViewById(R.id.configuracoes_endereco);
        perfilAlterarTextoBtn = (TextView) findViewById(R.id.perfil_imagem_alterar_botao);
        btn_fechar_texto = (ImageView) findViewById(R.id.btn_fechar_configuracoes);
        salvarTextoBotao = (TextView) findViewById(R.id.btn_atualizar_configuracoes_conta);

        exibicaoDeInformacoesDoUsuario(perfilImagemView, nomeCompleto, usuarioTelefone, usuarioEndereco);

        btn_fechar_texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        salvarTextoBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificador.equals("clicado")) {
                    usuarioInfoSalvas();
                } else {
                    atualizarApenasInfoUsuario();
                }
            }
        });
        perfilAlterarTextoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificador = "clicado";

                CropImage.activity(imagemUri)
                        .setAspectRatio(1, 1)
                        .start(ConfiguracoesActivity.this);
            }
        });
    }

    private void atualizarApenasInfoUsuario() {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        HashMap<String, Object> usuarioMapa = new HashMap<>();
        usuarioMapa.put("nome", nomeCompleto.getText().toString());
        usuarioMapa.put("endereco", usuarioEndereco.getText().toString());
        usuarioMapa.put("telefonePedido", usuarioTelefone.getText().toString());

        dataRef.child(Prevalente.atualUsuarioOnline.getTelefone()).updateChildren(usuarioMapa);
        
        startActivity(new Intent(ConfiguracoesActivity.this, PrincipalActivity.class));

        Toast.makeText(ConfiguracoesActivity.this, "Atualização bem sucedida",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK &&
                data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imagemUri = result.getUri();

            perfilImagemView.setImageURI(imagemUri);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(ConfiguracoesActivity.this, ConfiguracoesActivity.class));
            finish();
        }
    }

    private void usuarioInfoSalvas() {
        if (TextUtils.isEmpty(nomeCompleto.getText().toString())) {
            Toast.makeText(this, "Seu nome por favor", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(usuarioEndereco.getText().toString())) {
            Toast.makeText(this, "Seu endereço por favor", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(usuarioTelefone.getText().toString())) {
            Toast.makeText(this, "Seu endereço por favor", Toast.LENGTH_SHORT).show();
        } else if (verificador.equals("clicado")) {
            enviarImagem();
        }
    }
    private void enviarImagem() {
        final ProgressDialog carregarDialogo = new ProgressDialog(this);
        carregarDialogo.setTitle("Atualizar");
        carregarDialogo.setMessage("Por favor, aguarde");
        carregarDialogo.setCanceledOnTouchOutside(false);
        carregarDialogo.show();

        if (imagemUri != null) {
            final StorageReference arquivoRef = perfilFotoRef.child(Prevalente.atualUsuarioOnline
                    .getTelefone() + ".jpg");

            subirTarefa = arquivoRef.putFile(imagemUri);

            subirTarefa.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return arquivoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        meuUrl = downloadUrl.toString();

                        DatabaseReference databaseReference=  FirebaseDatabase.getInstance()
                                .getReference().child("Usuarios");

                        HashMap<String, Object> usuarioMapa = new HashMap<>();
                        usuarioMapa.put("nome", nomeCompleto.getText().toString());
                        usuarioMapa.put("endereco", usuarioEndereco.getText().toString());
                        usuarioMapa.put("telefonePedido", usuarioTelefone.getText().toString());
                        usuarioMapa.put("imagem", meuUrl);

                        databaseReference.child(Prevalente.atualUsuarioOnline.getTelefone())
                                .updateChildren(usuarioMapa);

                        carregarDialogo.dismiss();
                        
                        startActivity(new Intent(ConfiguracoesActivity.this,
                                PrincipalActivity.class));

                        Toast.makeText(ConfiguracoesActivity.this,
                                "Atualização bem sucedida", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        carregarDialogo.dismiss();

                        Toast.makeText(ConfiguracoesActivity.this, "Error",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();
        }
    }
    private void exibicaoDeInformacoesDoUsuario(final CircleImageView perfilImagemView, final EditText nomeCompleto, final EditText usuarioTelefone, final EditText usuarioEndereco) {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("imagem").exists()) {
                        String imagem = snapshot.child("imagem").getValue().toString();
                        String nome = snapshot.child("nome").getValue().toString();
                        String telefone = snapshot.child("telefone").getValue().toString();
                        String endereco = snapshot.getValue().toString();

                        Picasso.get().load(imagem).into(perfilImagemView);
                        nomeCompleto.setText(nome);
                        usuarioTelefone.setText(telefone);
                        usuarioEndereco.setText(endereco);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}