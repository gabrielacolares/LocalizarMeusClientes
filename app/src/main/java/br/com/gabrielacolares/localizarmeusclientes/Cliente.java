package br.com.gabrielacolares.localizarmeusclientes;

/**
 * Created by aluno on 19/10/2016.
 */

public class Cliente {

    String nome;
    String email;
    String telefone;
    String rua;
    String numero;
    String bairro;
    String cidade;
    String urlPerfilImage;

    public Cliente() {
    }

    public Cliente(String nome, String email, String telefone, String rua, String numero, String bairro, String cidade, String urlPerfilImage) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.urlPerfilImage = urlPerfilImage;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUrlPerfilImage() {
        return urlPerfilImage;
    }

    public void setUrlPerfilImage(String urlPerfilImage) {
        this.urlPerfilImage = urlPerfilImage;
    }
}