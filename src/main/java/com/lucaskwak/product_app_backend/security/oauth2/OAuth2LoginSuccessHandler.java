package com.lucaskwak.product_app_backend.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucaskwak.product_app_backend.security.dto.in.UserDto;
import com.lucaskwak.product_app_backend.security.dto.out.AuthenticationResponse;
import com.lucaskwak.product_app_backend.security.dto.out.RegisteredUser;
import com.lucaskwak.product_app_backend.security.persistence.entity.JwtToken;
import com.lucaskwak.product_app_backend.security.persistence.entity.User;
import com.lucaskwak.product_app_backend.security.service.JwtService;
import com.lucaskwak.product_app_backend.security.service.UserService;
import com.lucaskwak.product_app_backend.security.service.util.RegisteredUserWithJwt;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// SavedRequestAwareAuthenticationSuccessHandler es una clase proporcionada por Spring Security que redirige al
// usuario a la URL que intentaba acceder antes de ser redirigido a la página de login (si existe), o a una URL
// predeterminada si no hay una solicitud anterior guardada.

// Como no necesitamos esa funcionalidad nos sirve con SimpleUrlAuthenticationSuccessHandler

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        System.out.println("SE EJECUTA PRIMERO ESTO");

        // Hay que hacer un cast, ya que lo que se establece en el Authentication del SecurityContext
        // es un OAuth2AuthenticationToken
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        // Recogemos el Principal del SecurityContext (hay que hacer de nuevo un cast)
        // Nosotros en el JwtAuthenticationFilter el Principal lo asignamos como String (el propio username)
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();

        // Guardamos los atributos del Principal
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Vemos que proveedor utilizo el usuario
        // (Por ahora solo voy a implementar Google como provider)
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        if("google".equals(authorizedClientRegistrationId)) {
            String email = attributes.getOrDefault("email", "sin email").toString();
            String username = attributes.getOrDefault("name", "sin username").toString();
            String name = attributes.getOrDefault("name", "sin name").toString();

            ObjectMapper objectMapper = new ObjectMapper();

            userService.findOneUserByEmail(email)
                    .ifPresentOrElse(
                            user -> {
                                // Si esta el usuario en la BD quiere decir que solo se necesita iniciar sesion
                                // por tanto hay que devolver el jwt correspondiente.

                                AuthenticationResponse authenticationResponse = loginWithOauth2(user);
                                String jwt = authenticationResponse.getJwt();

                                Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt);
                                jwtCookie.setHttpOnly(true);  // Previene el acceso desde JavaScript en el navegador
                                jwtCookie.setSecure(false);    // Solo se envía a través de conexiones HTTPS
                                jwtCookie.setPath("/");       // Hace que la cookie esté disponible en toda la aplicación
                                jwtCookie.setMaxAge(24 * 60 * 60);  // Duración de la cookie (1 día en este caso)

                                // Agrega la cookie a la respuesta
                                response.addCookie(jwtCookie);

                                // Redirige al frontend (o a donde desees)
                                try {
                                    getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/home");
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                    throw new RuntimeException(e);
                                }

                            },
                            () -> {
                                UserDto saveUser = new UserDto();
                                saveUser.setName(name);
                                saveUser.setEmail(email);
                                saveUser.setUsername(username);
                                saveUser.setPassword(null);
                                saveUser.setAuthProvider(AuthProvider.GOOGLE);

                                RegisteredUserWithJwt registeredUserWithJwt = registerOneCustomer(saveUser);

                                String jwt = registeredUserWithJwt.getJwt();

                                Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt);
                                jwtCookie.setHttpOnly(true);  // Previene el acceso desde JavaScript en el navegador
                                jwtCookie.setSecure(false);    // Solo se envía a través de conexiones HTTPS
                                jwtCookie.setPath("/");       // Hace que la cookie esté disponible en toda la aplicación
                                jwtCookie.setMaxAge(24 * 60 * 60);  // Duración de la cookie (1 día en este caso)

                                // Agrega la cookie a la respuesta
                                response.addCookie(jwtCookie);

                                // Redirige al frontend (o a donde desees)
                                try {
                                    getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/home");
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("http://localhost:8080");

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationResponse loginWithOauth2(User user) {

        // Creamos un jwt con el usuario de la bd y los extraclaims correspondientes
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveJwt(user, jwt);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setJwt(jwt);

        return authenticationResponse;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    private void saveJwt(User user, String jwt) {
        JwtToken tokenToSave = new JwtToken();
        tokenToSave.setToken(jwt);
        tokenToSave.setUser(user);
        tokenToSave.setValid(true);

        jwtService.createToken(tokenToSave);
    }

    public RegisteredUserWithJwt registerOneCustomer(UserDto saveUser) {

        // Guardamos en la BD al usuario (customer)
        User user = userService.registerOneCustomer(saveUser);

        // Preparamos la respuesta
        RegisteredUser userDtoResponse = new RegisteredUser();
        userDtoResponse.setId(user.getId());
        userDtoResponse.setName(user.getName());
        userDtoResponse.setUsername(user.getUsername());
        userDtoResponse.setEmail(user.getEmail());
        userDtoResponse.setAuthProvider(user.getAuthProvider());
        userDtoResponse.setRole(user.getRole().getName());

        // Hay que crear el JWT
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        saveJwt(user, jwt);

        RegisteredUserWithJwt registeredUserWithJwt = new RegisteredUserWithJwt();
        registeredUserWithJwt.setRegisteredUser(userDtoResponse);
        registeredUserWithJwt.setJwt(jwt);

        return registeredUserWithJwt;
    }
}
