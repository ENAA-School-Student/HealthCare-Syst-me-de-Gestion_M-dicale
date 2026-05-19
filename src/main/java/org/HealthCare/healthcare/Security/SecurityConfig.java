package org.HealthCare.healthcare.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private AuthTokenFilter authTokenFilter;

    public SecurityConfig(UserDetailsService userDetailsService , AuthTokenFilter authTokenFilter){
        this.userDetailsService = userDetailsService;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> {
                    CorsConfigurationSource source = corsConfigurationSource();
                    c.configurationSource(source);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/patients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/patients").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/patients/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/patients/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/medecins").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/medecins").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/medecins/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/medecins/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/rendezvous").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/api/rendezvous/{id}").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT,"/api/rendezvous/{id}/annuler").hasAnyRole("PATIENT" , "MEDECIN")
                        .requestMatchers(HttpMethod.GET,"/api/rendezvous").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/rendezvous/medecin/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/rendezvous/patient/").hasAnyRole("ADMIN" , "PATIENT")
                        .requestMatchers(HttpMethod.POST,"/api/dossiers").hasRole("MEDECIN")
                        .requestMatchers(HttpMethod.PUT,"/api/dossiers/{id}/diagnostic").hasRole("MEDECIN")
                        .requestMatchers(HttpMethod.PUT,"/api/dossiers/{id}/observation").hasRole("MEDECIN")
                        .requestMatchers(HttpMethod.GET,"/api/dossiers/{id}").hasRole("MEDECIN")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider());


        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
