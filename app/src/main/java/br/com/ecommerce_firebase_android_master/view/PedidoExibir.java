package br.com.ecommerce_firebase_android_master.view;



import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ecommerce_firebase_android_master.R;

public class PedidoExibir extends RecyclerView.ViewHolder {

    public TextView usuarioNome;
    public TextView usuarioTelefone;
    public TextView usuarioPreco;
    public TextView usuarioCidade;
    public TextView usuarioDataHora;
    public TextView btn_todos_produtos;

    public PedidoExibir(@NonNull View itemView) {
        super(itemView);

        usuarioNome = itemView.findViewById(R.id.pedido_usuario_nome);
        usuarioTelefone = itemView.findViewById(R.id.pedido_usuario_numero);
        usuarioPreco = itemView.findViewById(R.id.pedido_preco_total);
        usuarioCidade = itemView.findViewById(R.id.pedido_endereco_cidade);
        usuarioDataHora = itemView.findViewById(R.id.pedido_data_hora);
        btn_todos_produtos = itemView.findViewById(R.id.btn_todos_produto);
    }
}
