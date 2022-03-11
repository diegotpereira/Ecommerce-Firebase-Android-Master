package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Produto;

public class ProdutoDetalheActivity extends AppCompatActivity {

    private Button btn_add_cartao;
    private ImageView produtoImagem;
    private ImageView rr;
    private ElegantNumberButton numeroBotao;
    private TextView produtoPreco;
    private TextView produtoDescricao;
    private TextView produtoNome;
    private String produtoID = "", estado = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);

        produtoID = getIntent().getStringExtra("pid");
        btn_add_cartao = (Button) findViewById(R.id.btn_produto_add_no_carrinho);
        numeroBotao = (ElegantNumberButton) findViewById(R.id.btn_numero);
        produtoImagem = (ImageView) findViewById(R.id.produto_imagem_detalhes);
        produtoPreco = (TextView) findViewById(R.id.produto_preco_detalhes);
        produtoNome = (TextView) findViewById(R.id.produto_nome_detalhes);
        produtoDescricao = (TextView) findViewById(R.id.produto_descricao_detalhes);
        rr = (ImageView) findViewById(R.id.rr);

        rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProdutoDetalheActivity.this, PrincipalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        getProdutoDetalhes(produtoID);
        
        btn_add_cartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estado == "livre" || estado == "nao libre") {
                    Toast.makeText(ProdutoDetalheActivity.this, "Aguarde a confirmação do seu pedido anterior.", Toast.LENGTH_SHORT).show();
                }
                else {
                    addNoCartao();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        meuComando();
    }
    private void addNoCartao() {}

    private void getProdutoDetalhes(String produtoID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Produtos");

        databaseReference.child(produtoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Produto produto = snapshot.getValue(Produto.class);
                    produtoNome.setText(produto.getPnome());
                    produtoDescricao.setText(produto.getDescricao());
                    produtoPreco.setText(produto.getPreco());

                    Picasso.get().load(produto.getImagem()).into(produtoImagem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void meuComando() {}
}