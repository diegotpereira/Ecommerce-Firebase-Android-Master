package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Cartao;
import br.com.ecommerce_firebase_android_master.view.CartaoView;

public class AdminDisplayActivity extends AppCompatActivity {

    private RecyclerView produtosLista;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference produtoRef;
    private String pid = "";
    private ImageView fer_disp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display);

        pid = getIntent().getStringExtra("pid");
        produtosLista = findViewById(R.id.produto_lista);
        produtosLista.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        produtosLista.setLayoutManager(layoutManager);
        produtosLista.setLayoutManager(layoutManager);
        fer_disp = (ImageView) findViewById(R.id.fer_disp);

        fer_disp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDisplayActivity.this, AdminPedidoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        produtoRef = FirebaseDatabase.getInstance().getReference().child("Cartao Lista")
                .child("Admin Exibir").child(pid).child("Produtos");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cartao> opcoes = new FirebaseRecyclerOptions.Builder<Cartao>()
                .setQuery(produtoRef, Cartao.class).build();

        FirebaseRecyclerAdapter<Cartao, CartaoView> adapter = new FirebaseRecyclerAdapter<Cartao, CartaoView>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull CartaoView holder, int i, @NonNull Cartao cartao) {
                holder.txtProdutoQuantidade.setText(cartao.getQuantidade());
                holder.txtProdutoPreco.setText(cartao.getPreco());
                holder.txtProdutoNome.setText(cartao.getPnome());
            }

            @NonNull
            @Override
            public CartaoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartao_itens_layout, parent, false);
                CartaoView cartaoView = new CartaoView(view);
                return cartaoView;
            }
        };
        produtosLista.setAdapter(adapter);
        adapter.startListening();
    }
}