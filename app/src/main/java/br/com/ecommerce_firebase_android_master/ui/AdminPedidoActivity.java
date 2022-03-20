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
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Pedido;
import br.com.ecommerce_firebase_android_master.view.PedidoExibir;

public class AdminPedidoActivity extends AppCompatActivity {

    private RecyclerView pedidoLista;
    DatabaseReference pedidoRef;
    private ImageView fer_com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pedido);

        pedidoRef = FirebaseDatabase.getInstance().getReference().child("Pedidos");
        pedidoLista = findViewById(R.id.pedido_lista);

        pedidoLista.setLayoutManager(new LinearLayoutManager(this));

        fer_com = (ImageView) findViewById(R.id.fer_com);

        fer_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrincipal = new Intent(AdminPedidoActivity.this, AdminPrincipalActivity.class);
                intentPrincipal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentPrincipal);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Pedido> opcoes = new FirebaseRecyclerOptions.Builder<Pedido>()
                .setQuery(pedidoRef, Pedido.class)
                .build();
        FirebaseRecyclerAdapter<Pedido, PedidoExibir> adapter = new FirebaseRecyclerAdapter<Pedido, PedidoExibir>(opcoes) {
            @Override
            protected void onBindViewHolder(@NonNull PedidoExibir holder, int position, @NonNull Pedido pedido) {
                holder.usuarioNome.setText(pedido.getNome());
                holder.usuarioTelefone.setText(pedido.getTelefone());
                holder.usuarioCidade.setText(pedido.getEndereco() + " " + pedido.getCidade());
                holder.usuarioDataHora.setText(pedido.getData() + " " + pedido.getHora());
                holder.usuarioPreco.setText(pedido.getPrecoTotal() + " R$");

                holder.btn_todos_produtos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pid = getRef(position).getKey();
                        Intent intentExibir = new Intent(AdminPedidoActivity.this, AdminDisplayActivity.class);
                        intentExibir.putExtra("pid", pid);
                        startActivity(intentExibir);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence sequence[] = new CharSequence[] {
                                "Sim", "Nao"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPedidoActivity.this);
                        builder.setTitle("Pedido entregue ou não:");
                        builder.setItems(sequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    String pid = getRef(position).getKey();
                                    removerPedido(pid);
                                } else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public PedidoExibir onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_layout, parent, false);
                return new PedidoExibir(view);
            }
        };
        pedidoLista.setAdapter(adapter);
        adapter.startListening();
    }
    private void removerPedido(String pid) {
        pedidoRef.child(pid).removeValue();
    }
}