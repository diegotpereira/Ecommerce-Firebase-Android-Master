package br.com.ecommerce_firebase_android_master.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.interfaces.ItemClickListner;

public class ProdutoExibir extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProdutoNome;
    public TextView txtProdutoDescricao;
    public TextView txtProdutoPreco;
    public ImageView imagemExibir;
    public ItemClickListner listner;

    public ProdutoExibir(@NonNull View itemView) {
        super(itemView);
        imagemExibir = (ImageView) itemView.findViewById(R.id.produto_imagem);
        txtProdutoNome = (TextView) itemView.findViewById(R.id.produto_nome);
        txtProdutoDescricao = (TextView) itemView.findViewById(R.id.produto_descricao);
        txtProdutoPreco = (TextView) itemView.findViewById(R.id.produto_preco);
    }

    public void setItemClickListner(ItemClickListner listner) {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
