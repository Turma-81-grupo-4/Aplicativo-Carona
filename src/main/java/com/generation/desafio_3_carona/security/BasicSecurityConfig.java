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
                "https://aplicativo-carona-2.onrender.com"
        ));
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults());

        http
                .authorizeHttpRequests((auth) -> auth
                        // 1. Permite requisições OPTIONS para TODAS as rotas (CRUCIAL para CORS preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Permite acesso à raiz (se houver um controlador ou se o Swagger for redirecionado)
                        .requestMatchers(HttpMethod.GET, "/").permitAll() // Adicionado/Modificado

                        // 3. Rotas do Swagger UI e documentação (colocadas logo após a raiz e OPTIONS)
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()

                        // 4. Rotas públicas de autenticação/cadastro
                        .requestMatchers("/usuarios/logar").permitAll()
                        .requestMatchers("/usuarios/cadastrar").permitAll()
                        .requestMatchers("/error/**").permitAll()

                        // 5. Outras rotas GET que devem ser públicas ANTES do login (ex: listar caronas)
                        .requestMatchers(HttpMethod.GET, "/caronas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/caronas/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/caronas/destino/{destino}").permitAll()

                        // 6. Todas as outras requisições requerem autenticação (DEVE SER A ÚLTIMA REGRA)
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                // Certifique-se de que httpBasic(withDefaults()) está ausente ou definitivamente comentado.
        ;

        return http.build();
    }
}