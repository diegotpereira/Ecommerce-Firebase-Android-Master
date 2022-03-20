package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Produto;
import br.com.ecommerce_firebase_android_master.view.ProdutoExibir;

public class CategoriaEspecActivity extends AppCompatActivity {

    private String categoria;
    private RecyclerView categoria_lista;
    private ImageView ferm;
    private TextView cate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_espec);

        ferm = (ImageView) findViewById(R.id.ferm);
        cate = (TextView) findViewById(R.id.categoria);

        ferm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrincipal = new Intent(CategoriaEspecActivity.this, PrincipalActivity.class);
                intentPrincipal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentPrincipal);
                finish();
            }
        });
        categoria = getIntent().getExtras().get("categoria").toString();
        cate.setText(categoria);

        categoria_lista = findViewById(R.id.categoria_lista);
        categoria_lista.setLayoutManager(new LinearLayoutManager(CategoriaEspecActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        FirebaseRecyclerOptions<Produto> opcoes = new FirebaseRecyclerOptions.Builder<Produto>()
                .setQuery(dataRef.orderByChild("categoria").startAt(categoria).endAt(categoria), Produto.class).build();

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
                        Intent intentProdutoDetalhe = new Intent(CategoriaEspecActivity.this, ProdutoDetalheActivity.class);
                        intentProdutoDetalhe.putExtra("pid", produto.getPid());
                        startActivity(intentProdutoDetalhe);
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
        categoria_lista.setAdapter(adapter);
        adapter.startListening();
    }
}