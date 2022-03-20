package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Produto;
import br.com.ecommerce_firebase_android_master.view.ProdutoExibir;

public class PesquisarProdutoActivity extends AppCompatActivity {

    private Button btn_pesquisar;
    private EditText entradaTexto;
    private RecyclerView pesquisarLista;
    private String entradaPesquisa;
    private ImageView fer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_produto);

        entradaTexto = (EditText) findViewById(R.id.produto_pesquisar);
        btn_pesquisar = (Button) findViewById(R.id.btn_pesquisar);
        pesquisarLista = findViewById(R.id.pesquisar_lista);
        pesquisarLista.setLayoutManager(new LinearLayoutManager(PesquisarProdutoActivity.this));
        fer = (ImageView) findViewById(R.id.fer);

        fer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrincipal = new Intent(PesquisarProdutoActivity.this, MainActivity.class);
                intentPrincipal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentPrincipal);
            }
        });
        btn_pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entradaPesquisa = entradaTexto.getText().toString();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        FirebaseRecyclerOptions<Produto> opcoes = new FirebaseRecyclerOptions.Builder<Produto>()
                .setQuery(dataRef.orderByChild("pnome").startAt(entradaPesquisa).endAt(entradaPesquisa), Produto.class).build();

        FirebaseRecyclerAdapter<Produto, ProdutoExibir> adapter = new FirebaseRecyclerAdapter<Produto, ProdutoExibir>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull ProdutoExibir holder, int i, @NonNull Produto produto) {
                holder.txtProdutoNome.setText(produto.getPnome());
                holder.txtProdutoDescricao.setText(produto.getDescricao());
                holder.txtProdutoPreco.setText(produto.getPreco());

                Picasso.get().load(produto.getImagem()).into(holder.imagemExibir);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentProdutosDetalhe = new Intent(PesquisarProdutoActivity.this, ProdutoDetalheActivity.class);
                        intentProdutosDetalhe.putExtra("pid", produto.getPid());
                        startActivity(intentProdutosDetalhe);
                    }
                });
            }

            @NonNull
            @Override
            public ProdutoExibir onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produtos_layout, parent, false);
                ProdutoExibir holder = new ProdutoExibir(view);

                return holder;
            }
        };
        pesquisarLista.setAdapter(adapter);
        adapter.startListening();
    }
}