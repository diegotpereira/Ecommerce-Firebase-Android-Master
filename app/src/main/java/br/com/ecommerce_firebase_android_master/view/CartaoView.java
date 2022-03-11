package br.com.ecommerce_firebase_android_master.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ecommerce_firebase_android_master.R;
import br.com.ecommerce_firebase_android_master.interfaces.ItemClickListner;

public class CartaoView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProdutoNome;
    public TextView txtProdutoPreco;
    public TextView txtProdutoQuantidade;
    private ItemClickListner itemClickListner;

    public CartaoView(@NonNull View itemView) {
        super(itemView);
        txtProdutoNome = itemView.findViewById(R.id.cartao_produto_nome);
        txtProdutoPreco = itemView.findViewById(R.id.cartao_produto_preco);
        txtProdutoQuantidade = itemView.findViewById(R.id.cartao_produto_quantidade);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
