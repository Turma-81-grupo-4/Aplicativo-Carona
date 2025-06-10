package com.generation.desafio_3_carona.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://carona-nu.vercel.app",
                "https://carona-grupo-4-java-81s-projects.vercel.app",
                "https://aplicativo-carona-2.onrender.com" // Adicione o próprio domínio do backend se ele também for um frontend
        ));

        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"));

        configuration.setAllowedHeaders(List.of("*")); // Permite todos os cabeçalhos
        configuration.setAllowCredentials(true); // Importante se você estiver enviando cookies ou headers de autenticação como Authorization

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração CORS para todas as rotas

        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Requerido para JWT
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST
                .cors(withDefaults()) // Aplica a configuração CORS que você definiu no Bean 'corsConfigurationSource()'
                // .httpBasic(withDefaults()) // Comentar ou remover se você usa apenas autenticação JWT
                ;

        http
                .authorizeHttpRequests((auth) -> auth
                        // Permite requisições OPTIONS antes de qualquer outra validação
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // Mova para o início para garantir que seja processado primeiro

                        // Rotas públicas que não requerem autenticação
                        .requestMatchers("/usuarios/logar").permitAll()
                        .requestMatchers("/usuarios/cadastrar").permitAll()
                        .requestMatchers("/error/**").permitAll()

                        // Rotas do Swagger UI
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll() // Pode ser necessário para Swagger
                        .requestMatchers("/webjars/**").permitAll() // Pode ser necessário para Swagger

                        // Todas as outras requisições requerem autenticação
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                ; // Não adicione .httpBasic(withDefaults()) se você usa apenas JWT
                  // O Basic Auth pode causar conflito ou pedir credenciais para OPTIONS

        return http.build();
    }
}