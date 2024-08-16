package com.lucaskwak.product_app_backend.security.config;

import com.lucaskwak.product_app_backend.security.config.filter.JwtAuthenticationFilter;
import com.lucaskwak.product_app_backend.security.config.handler.CustomAccessDeniedHandler;
import com.lucaskwak.product_app_backend.security.config.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    private final AuthenticationProvider daoAuthProvider;

    // Para validar los jwt
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Para manejar las excepciones y para lanzar un 401
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // Para manejar las excepciones y para lanzar un 403
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    // Para comprobar nosotros manualmente las autorizaciones de las peticiones
    private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;

    @Autowired
    public HttpSecurityConfig(AuthenticationProvider authenticationProvider,
                              JwtAuthenticationFilter jwtAuthenticationFilter,
                              CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                              CustomAccessDeniedHandler customAccessDeniedHandler,
                              AuthorizationManager<RequestAuthorizationContext> authorizationManager) {
        this.daoAuthProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.authorizationManager = authorizationManager;
    }

    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf( csrfConfigurer -> csrfConfigurer.disable())
                .sessionManagement( sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthProvider)
                // Creamos nuestro filtro personalizado para recoger los jwt
                // Tenemos que hacer que se ejecute este filtro antes del UsernamePasswordAuthenticationFilter
                // ya que este filtro intercepta peticiones POST y se encarga del inicio de sesión de formularios
                // Si no se ejecuta el JwtAuthenticationFilter antes que UsernamePasswordAuthenticationFilter,
                // se puede poblar el security context con informacion incompleta y no con el jwt
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcher -> {
                            authorizationManagerRequestMatcher.anyRequest().access(authorizationManager);
                        }
                )
                .exceptionHandling(
                        exceptionHandlingConfigurer -> {
                            exceptionHandlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint);
                            exceptionHandlingConfigurer.accessDeniedHandler(customAccessDeniedHandler);
                        }
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        // Para que funcione en todos los controladores hacemos un generico
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
