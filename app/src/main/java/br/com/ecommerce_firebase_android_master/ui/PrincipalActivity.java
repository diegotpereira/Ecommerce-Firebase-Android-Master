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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.prevalente.Prevalente;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}