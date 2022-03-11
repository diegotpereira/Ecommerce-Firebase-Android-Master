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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import br.com.ecommerce_firebase_android_master.R;

public class AddNovoProdutoActivity extends AppCompatActivity {

    private String categoriaNome;
    private String descricao;
    private String preco;
    private String pNome;
    private String salvarDataAtual;
    private String salvarHoraAtual;
    private Button btn_add_produto;
    private ImageView entradaProdutoImagem;
    private ImageView ret;
    private EditText entradaProdutoNome;
    private TextView entradaProdutoDescricao;
    private TextView entradaProdutoPreco;
    private static final int GALERIA_FOTO = 1;

    private Uri imagemUri;
    private String produtoAleatorioChave;
    private String downloadImagemUrl;

    Random numeroAleatorio = new Random();
    int numero = numeroAleatorio.nextInt();

    private StorageReference produtoImagensRef;
    private DatabaseReference produtosRef;
    private ProgressDialog carregarBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_novo_produto);

        categoriaNome = getIntent().getExtras().get("categoria").toString();
        produtoImagensRef = FirebaseStorage.getInstance().getReference().child("Produto Imagens");
        produtosRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        btn_add_produto = (Button) findViewById(R.id.add_novo_produto);
        entradaProdutoImagem = (ImageView) findViewById(R.id.selecione_produto_imagem);
        entradaProdutoNome = (EditText) findViewById(R.id.produto_nome);
        entradaProdutoDescricao = (EditText) findViewById(R.id.produto_descricao);
        entradaProdutoPreco = (EditText) findViewById(R.id.produto_preco);

        carregarBar =  new ProgressDialog(this);
        ret = (ImageView) findViewById(R.id.ret);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRet = new Intent(AddNovoProdutoActivity.this, AdminCategoriaActivity.class);
                startActivity(intentRet);
            }
        });
        entradaProdutoImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });
        btn_add_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDadosProduto();
            }
        });
    }
    private void abrirGaleria() {
        Intent intentGaleria = new Intent();
        intentGaleria.setAction(Intent.ACTION_GET_CONTENT);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria, GALERIA_FOTO);
    }
    private void validarDadosProduto() {
        descricao = entradaProdutoDescricao.getText().toString();
        preco = entradaProdutoPreco.getText().toString();
        pNome = entradaProdutoNome.getText().toString();
        
        if(imagemUri == null) {
            Toast.makeText(this, "Imagem não carregada...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descricao)) {
            Toast.makeText(this, "Preencha a descrição por favor...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(preco)) {
            Toast.makeText(this, "Por favor, preencha o preço...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pNome)) {
            Toast.makeText(this, "Por favor, preencha o nome do preoduto...", Toast.LENGTH_SHORT).show();
        } else {
            armazenarInformacoesDoPproduto();
        }
    }
    private void armazenarInformacoesDoPproduto() {
        carregarBar.setTitle("Adicionando o produto");
        carregarBar.setMessage("Envio de dados do produto em andamento. Aguarde");
        carregarBar.setCanceledOnTouchOutside(false);
        carregarBar.show();

        Calendar calendario = Calendar.getInstance();

        SimpleDateFormat dataAtual = new SimpleDateFormat("MM, dd, yyyy");
        salvarDataAtual = dataAtual.format(calendario.getTime());

        SimpleDateFormat horaAtual = new SimpleDateFormat("HH:mm:ss a");
        salvarHoraAtual = horaAtual.format(calendario.getTime());

        produtoAleatorioChave = String.valueOf(numero);

        final StorageReference caminhoArquivo = produtoImagensRef.child(imagemUri.getLastPathSegment() + produtoAleatorioChave + ".jpg");

        final UploadTask subirTarefa = caminhoArquivo.putFile(imagemUri);

        subirTarefa.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensagem = e.toString();

                Toast.makeText(AddNovoProdutoActivity.this, "Erro: " + mensagem, Toast.LENGTH_SHORT).show();
                carregarBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddNovoProdutoActivity.this, "OK", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTarefa = subirTarefa.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImagemUrl = caminhoArquivo.getDownloadUrl().toString();

                        return caminhoArquivo.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImagemUrl = task.getResult().toString();

                            Toast.makeText(AddNovoProdutoActivity.this, "OK", Toast.LENGTH_SHORT).show();

                            salvarInformacoesDoProdutoNoBancoDeDados();
                        }
                    }
                });
            }
        });
    }
    private void salvarInformacoesDoProdutoNoBancoDeDados() {
        HashMap<String, Object> produtoMapa = new HashMap<>();
        produtoMapa.put("pid", produtoAleatorioChave);
        produtoMapa.put("data", salvarDataAtual);
        produtoMapa.put("hora", salvarHoraAtual);
        produtoMapa.put("descricao", descricao);
        produtoMapa.put("imagem", downloadImagemUrl);
        produtoMapa.put("categoria", categoriaNome);
        produtoMapa.put("preco", preco);
        produtoMapa.put("pnome", pNome);

        produtosRef.child(produtoAleatorioChave).updateChildren(produtoMapa)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intentAdmin = new Intent(AddNovoProdutoActivity.this, AdminCategoriaActivity.class);
                        startActivity(intentAdmin);

                        carregarBar.dismiss();

                        Toast.makeText(AddNovoProdutoActivity.this, "Adição bem sucedida!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        carregarBar.dismiss();
                        String mensagem = task.getException().toString();

                        Toast.makeText(AddNovoProdutoActivity.this, "Error: " + mensagem, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERIA_FOTO && resultCode == RESULT_OK && data != null) {
            imagemUri = data.getData();
            entradaProdutoImagem.setImageURI(imagemUri);
        }
    }
}