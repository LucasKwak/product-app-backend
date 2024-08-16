package com.lucaskwak.product_app_backend.security.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lucaskwak.product_app_backend.dto.out.ApiErrorResponse;
import com.lucaskwak.product_app_backend.security.exception.UserLoggedOutException;
import com.lucaskwak.product_app_backend.security.persistence.entity.JwtToken;
import com.lucaskwak.product_app_backend.security.persistence.entity.User;
import com.lucaskwak.product_app_backend.security.service.JwtService;
import com.lucaskwak.product_app_backend.security.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    // Este filterChain es el de jakarta, no es el de spring
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("ENTRO EN EL JWT AUTHENTICATION");

        // 1. Obtener del header http el encabezado llamado Autherization
        String autherizationHeader = request.getHeader("Authorization");

        // En caso de que venga sin texto o si no empieza como tiene que empezar, seguimos
        // con los filtros, ya que en algun momento saltara una excepcion
        if (!StringUtils.hasText(autherizationHeader) || !autherizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2. Obtener el jwt que hay en el encabezado
        // Separamos el encabezado en dos strings y nos quedamos con el segundo (este es el jwt)
        String jwt = autherizationHeader.split(" ")[1];



        // 3. Obtener el subject (username) del jwt
        //    Esta accion a su vez valida si el jwt es correcto o incorrecto

        try {
            String username = jwtService.extractUsername(jwt); // Esta es la funcion que puede lanzar la excepcion

            // Esta parte es para ver si el token se ha marcado como invalido (en el sentido de que el
            // usuario ha cerrado sesion y ese token ya no sirve)

            if(isUserLoggedOut(jwt)) {
                throw new UserLoggedOutException("El usuario ha cerrado su sesion");
            }

            // 4. Seteamos el objeto Authentication dentro del Security Context a traves del Security Context Holder

            User user = userService.findOneUserByUsername(username).get(); // Nunca va a ser null, ya que hubiese fallado antes
            // Solo hay un caso en el que puede fallar: si usamos un jwt anterior y hemos parado y vuelto a encender
            // la aplicacion (ya que se pierden los datos, y por tanto ese jwt no va a estar en la bd)

            // Tenemos que crear una Authentication con el Principal, Credentials y Authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    user.getAuthorities()
            );
            // Cogemos el contexto y establecemos el objeto Authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. Ejecutamos el resto de filtros
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private boolean isUserLoggedOut(String jwt) {
        JwtToken jwtToken = jwtService.findTokenByToken(jwt).get(); // Nunca va a ser null (si llegamos hasta aqui
        // quiere decir que el token es valido, y por tanto que lo hemos creado y guardado en la bd antes)

        if(jwtToken.isValid()) {
            return false; // Si es valido quiere decir que el usuario no ha cerrado sesion
        }else{
            return true;
        }
    }

    // Para manejar las excepciones que se pueden lanzar en los filtros, necesitamos devolver nosotros en el propio
    // filtro la respuesta. El controllerAdvice no nos sirve porque estas excepciones ocurren antes de llegar al
    // propio controlador (ya sea el de product, customer...)
    // Por eso es necesario crear este metodo que maneje las posibles excepciones
    // En el filtro jwt las excepciones se lanzan cuando extraemos los claims que trae el jwt de la peticion
    // (mi teoria era que cuando una de estas excepciones se lanza, se continua con el siguiente filtro)
    // En verdad lo que ocurre es que se para la ejecucion de la cadena de filtros.
    private void handleException(HttpServletResponse response, Exception e) throws IOException {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setBackendMessage(e.getLocalizedMessage());
        apiErrorResponse.setMessage("JWT invalido");
        apiErrorResponse.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Este string es como si fuese un json
        String apiErrorAsString = objectMapper.writeValueAsString(apiErrorResponse);
        // Escribimos el json en la respuesta
        response.getWriter().write(apiErrorAsString);
    }
}
