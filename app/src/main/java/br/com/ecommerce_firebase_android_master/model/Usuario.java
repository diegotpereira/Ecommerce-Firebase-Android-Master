package br.com.ecommerce_firebase_android_master.model;

public class Usuario {

    private String nome;
    private String telefone;
    private String senha;
    private String imagem;
    private String endereco;

    public Usuario() {
    }

    public Usuario(String nome, String telefone, String senha, String imagem, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.senha = senha;
        this.imagem = imagem;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
