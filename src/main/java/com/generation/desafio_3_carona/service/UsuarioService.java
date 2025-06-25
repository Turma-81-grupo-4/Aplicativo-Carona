package com.generation.desafio_3_carona.service;

import java.util.Optional;

import com.generation.desafio_3_carona.dto.AtualizarSenhaDTO;
import com.generation.desafio_3_carona.dto.UsuarioUpdateDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
            return Optional.empty();

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        return Optional.of(usuarioRepository.save(usuario));

    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLoginOptional) {

        var credenciais = new UsernamePasswordAuthenticationToken(
                usuarioLoginOptional.get().getEmail(),
                usuarioLoginOptional.get().getSenha()
        );

        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {


            Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLoginOptional.get().getEmail());

            if(usuario.isPresent()){

                UsuarioLogin usuarioLoginResponse = new UsuarioLogin();

                usuarioLoginResponse.setId(usuario.get().getId());
                usuarioLoginResponse.setNome(usuario.get().getNome());
                usuarioLoginResponse.setEmail(usuario.get().getEmail());
                usuarioLoginResponse.setFoto(usuario.get().getFoto());
                usuarioLoginResponse.setTipo(usuario.get().getTipo());


                String token = gerarToken(usuarioLoginOptional.get().getEmail());

                usuarioLoginResponse.setToken(token);

                return Optional.of(usuarioLoginResponse);
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
    @Transactional
    public Optional<UsuarioUpdateDTO> atualizarDadosUsuario(String emailUsuarioLogado, UsuarioUpdateDTO usuarioUpdateDTO) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(emailUsuarioLogado);

        if (usuarioOptional.isPresent()) {
            Usuario usuarioExistente = usuarioOptional.get();


            String tipoASalvar = (usuarioUpdateDTO.getTipo() != null && !usuarioUpdateDTO.getTipo().isEmpty())
                    ? usuarioUpdateDTO.getTipo()
                    : usuarioExistente.getTipo();

            usuarioRepository.atualizarPerfil(
                    usuarioExistente.getId(),
                    usuarioUpdateDTO.getNome(),
                    usuarioUpdateDTO.getFoto(),
                    tipoASalvar
            );

            return Optional.of(usuarioUpdateDTO);
        }

        return Optional.empty();
    }
    public Optional<Usuario> atualizarSenha(AtualizarSenhaDTO atualizarSenhaDTO) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(emailUsuario);
        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
                    if(passwordEncoder.matches(atualizarSenhaDTO.getSenhaAtual(), usuario.getSenha())){
                        usuario.setSenha(passwordEncoder.encode(atualizarSenhaDTO.getNovaSenha()));
                        return Optional.of(usuarioRepository.save(usuario));
                    }else{
                        return Optional.empty();
                    }
        }
        return Optional.empty();
    }

}
