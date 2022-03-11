package br.com.ecommerce_firebase_android_master.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.model.Produto;
import br.com.ecommerce_firebase_android_master.prevalente.Prevalente;
import br.com.ecommerce_firebase_android_master.view.ProdutoExibir;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference produtosRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        produtosRef = FirebaseDatabase.getInstance().getReference().child("Produtos");

        // Paper é um armazenamento rápido do tipo NoSQL para objetos Java
        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DTP Shop");
        setSupportActionBar(toolbar);

        ImageView fab = (ImageView) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, CartaoActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navegacao_drawer_abrir, R.string.navegacao_drawer_fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View cabecalhoExibir = navigationView.getHeaderView(0);
        TextView usuarioNomeTextView = cabecalhoExibir.findViewById(R.id.usuario_perfil_nome);
        CircleImageView perfilImagemView = cabecalhoExibir.findViewById(R.id.usuario_perfil_imagem);

        usuarioNomeTextView.setText(Prevalente.atualUsuarioOnline.getNome());

        Picasso.get().load(Prevalente.atualUsuarioOnline.getImagem()).placeholder(R.drawable.profile).into(perfilImagemView);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Produto> opcoes =
                new FirebaseRecyclerOptions.Builder<Produto>()
                .setQuery(produtosRef, Produto.class)
                .build();
        FirebaseRecyclerAdapter<Produto, ProdutoExibir> adapter =
                new FirebaseRecyclerAdapter<Produto, ProdutoExibir>(opcoes) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProdutoExibir holder, int i, @NonNull final Produto produto) {
                        holder.txtProdutoNome.setText(produto.getPnome());
                        holder.txtProdutoDescricao.setText(produto.getDescricao());
                        holder.txtProdutoPreco.setText(produto.getPreco());

                        Picasso.get().load(produto.getImagem()).into(holder.imagemExibir);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentProdutoDetalhe = new Intent(PrincipalActivity.this, ProdutoDetalheActivity.class);
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}