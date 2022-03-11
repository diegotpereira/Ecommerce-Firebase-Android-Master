package br.com.ecommerce_firebase_android_master.model;

public class Cartao {

    private String pid;
    private String pnome;
    private String preco;
    private String desconto;
    private String quantidade;

    public Cartao() {
    }

    public Cartao(String pid, String pnome, String preco, String desconto, String quantidade) {
        this.pid = pid;
        this.pnome = pnome;
        this.preco = preco;
        this.desconto = desconto;
        this.quantidade = quantidade;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPnome() {
        return pnome;
    }

    public void setPnome(String pnome) {
        this.pnome = pnome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
