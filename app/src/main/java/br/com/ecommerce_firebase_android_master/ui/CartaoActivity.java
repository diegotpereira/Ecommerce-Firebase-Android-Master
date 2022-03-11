package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Cartao;
import br.com.ecommerce_firebase_android_master.prevalente.Prevalente;
import br.com.ecommerce_firebase_android_master.view.CartaoView;

public class CartaoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn_proximo;
    private TextView preco_total;
    private TextView msg;
    private TextView total;
    private int precoTotal = 0;
    private ImageView btn_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartao);

        recyclerView = (RecyclerView) findViewById(R.id.cartao_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btn_proximo = (Button) findViewById(R.id.proximo_cartao);
        btn_voltar = (ImageView) findViewById(R.id.retornar);
        preco_total = (TextView) findViewById(R.id.preco_total);
        total = (TextView) findViewById(R.id.total);
        msg = (TextView) findViewById(R.id.msg);

        btn_proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinal = new Intent(CartaoActivity.this, FinalActivity.class);
                intentFinal.putExtra("Preco Total", String.valueOf(precoTotal));
                startActivity(intentFinal);
                finish();
            }
        });
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrincipal = new Intent(CartaoActivity.this, PrincipalActivity.class);
                intentPrincipal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentPrincipal);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        meusPedidos();

        final DatabaseReference cartaoListaRef = FirebaseDatabase.getInstance().getReference().child("Cartao Lista");

        FirebaseRecyclerOptions<Cartao> opcoes = new FirebaseRecyclerOptions.Builder<Cartao>().setQuery(cartaoListaRef.child("Usuario Exibir")
        .child(Prevalente.atualUsuarioOnline.getTelefone()).child("Produtos"), Cartao.class).build();

        FirebaseRecyclerAdapter<Cartao, CartaoView> adapter = new FirebaseRecyclerAdapter<Cartao, CartaoView>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull CartaoView holder, int posicao, @NonNull Cartao cartao) {
                holder.txtProdutoQuantidade.setText(cartao.getQuantidade());
                holder.txtProdutoPreco.setText(cartao.getPreco());
                holder.txtProdutoNome.setText(cartao.getPnome());

                // calculando valor do carrinho
                int precoTotalParaUm = ((Integer.valueOf(cartao.getPreco()))) * Integer.valueOf(cartao.getQuantidade());
                precoTotal = precoTotal + precoTotalParaUm;

                total.setText(String.valueOf(precoTotal));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence charSequence[] = new CharSequence[] {
                                "Modificar", "Remover"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartaoActivity.this);
                        builder.setTitle("Opções");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intentProdutoDetalhe = new Intent(CartaoActivity.this, ProdutoDetalheActivity.class);
                                    intentProdutoDetalhe.putExtra("pid", cartao.getPid());
                                    startActivity(intentProdutoDetalhe);
                                }
                                if (i == 1) {
                                    cartaoListaRef.child("Usuario Exibir").child(Prevalente.atualUsuarioOnline.getTelefone())
                                            .child("Produtos").child(cartao.getPid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CartaoActivity.this, "Produto removido", Toast.LENGTH_SHORT).show();
                                                Intent intentPrincipal = new Intent(CartaoActivity.this, PrincipalActivity.class);
                                                startActivity(intentPrincipal);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartaoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartao_itens_layout, parent, false);
                CartaoView cartaoView = new CartaoView(view);
                return cartaoView;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void meusPedidos() {}
}