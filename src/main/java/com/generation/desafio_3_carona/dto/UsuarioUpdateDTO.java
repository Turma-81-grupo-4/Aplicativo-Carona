package com.generation.desafio_3_carona.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateDTO {
    @NotBlank(message = "O atributo Nome é obrigatório!")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;
    private String foto;
    private String email;
    private String senha;

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
