package com.generation.desafio_3_carona.service;

import java.util.Optional;
import com.generation.desafio_3_carona.dto.UsuarioUpdateDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.desafio_3_carona.model.Usuario;
import com.generation.desafio_3_carona.model.UsuarioLogin;
import com.generation.desafio_3_carona.repository.UsuarioRepository;
import com.generation.desafio_3_carona.security.JwtService;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
            return Optional.empty();

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        return Optional.of(usuarioRepository.save(usuario));

    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmail(),
                usuarioLogin.get().getSenha());

        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {

            Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getEmail());

            if (usuario.isPresent()) {

                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setTipo(usuario.get().getTipo());
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getEmail()));
                usuarioLogin.get().setSenha("");

                return usuarioLogin;
            }

        }

        return Optional.empty();

    }

    private String criptografarSenha(String senha) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(senha);
    }

    private String gerarToken(String usuario) {

        return "Bearer " + jwtService.generateToken(usuario);
    }
    public Optional<Usuario> atualizarDadosUsuario(String emailUsuarioLogado, UsuarioUpdateDTO usuarioUpdateDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(emailUsuarioLogado);

        if (usuarioOptional.isPresent()) {
            Usuario usuarioExistente = usuarioOptional.get();

            usuarioExistente.setNome(usuarioUpdateDTO.getNome());
            //usuarioExistente.setFoto(usuarioUpdateDTO.getFoto());

            if (usuarioUpdateDTO.getTipo() != null && !usuarioUpdateDTO.getTipo().isEmpty()) {
                usuarioExistente.setTipo(usuarioUpdateDTO.getTipo());
            }

            return Optional.of(usuarioRepository.save(usuarioExistente));
        }

        return Optional.empty();
    }

}
