package com.generation.desafio_3_carona.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateDTO {
    @NotBlank(message = "O atributo Nome é obrigatório!")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;
    private String foto;
    private String senha;
    private String tipo;

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
