package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.util.List;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min=2)
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;
    @Email(message = "O e-mail é obrigatório.")
    private String email;
    @NotBlank(message = "A senha é obrigatória.")
    @Size(min=8, message = "A senha deve conter no mínimo 8 caracteres.")
    private String senha;
    @NotNull(message = "O tipo do usuário é obrigatório.")
    private String tipo;
    @Column(length = 2000)
    private String foto;


    @OneToMany(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("usuario_caronas")
    private List<Carona> caronasOferecidas;

    @OneToMany(mappedBy = "passageiro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("usuario_passagens")
    private List<Passagem> passagens;

    public Usuario(Long id, String nome, String email, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Usuario() {
    }
    //implementar enum para tipo
    public enum TipoUsuario {
        ADMIN,
        MOTORISTA,
        PASSAGEIRO
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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