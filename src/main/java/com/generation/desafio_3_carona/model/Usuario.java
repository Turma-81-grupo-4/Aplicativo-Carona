package com.generation.desafio_3_carona.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O Atributo Nome é Obrigatório!")
    private String nome;

    @Schema(example = "email@email.com")
    @NotNull(message = "O atributo E-mail é obrigatório!")
    @Email(message = "O atributo E-mail deve ser um email válido!")
    private String email;

    @NotBlank(message = "O atributo Senha é obrigatório!")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    @NotBlank
    private String tipo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "motorista", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("motorista")
    private List<Carona> caronasOferecidas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "passageiro", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("passageiro")
    private List<Passagem> passagens;

    public Usuario(Long id, String nome, String email, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Usuario() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String usuario) {
        this.email = usuario;
    }

    public String getSenha() {
        return this.senha;
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

    public List<Carona> getCaronasOferecidas() {
        return caronasOferecidas;
    }

    public void setCaronasOferecidas(List<Carona> caronasOferecidas) {
        this.caronasOferecidas = caronasOferecidas;
    }

    public List<Passagem> getPassagens() {
        return passagens;
    }

    public void setPassagens(List<Passagem> passagens) {
        this.passagens = passagens;
    }

}