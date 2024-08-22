package com.lucaskwak.product_app_backend.security.config.authorization;

import com.lucaskwak.product_app_backend.security.persistence.entity.Operation;
import com.lucaskwak.product_app_backend.security.persistence.entity.User;
import com.lucaskwak.product_app_backend.security.service.OperationService;
import com.lucaskwak.product_app_backend.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final OperationService operationService;

    private final UserService userService;

    @Autowired
    public CustomAuthorizationManager(OperationService operationService, UserService userService) {
        this.operationService = operationService;
        this.userService = userService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestContext) {

        // Primero vemos si el endpoint al que accede es publico o no
        HttpServletRequest request = requestContext.getRequest();

        String uriWithoutContextPath = extractUriWithoutContextPath(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(uriWithoutContextPath, httpMethod);

        if(isPublic) {
            return new AuthorizationDecision(true);
        }

        // Si sigue por aqui, el endpoint requiere de cierta autenticacion y autorizacion

        // Antes de llegar a este authorizationManager, se tuvo que ejecutar el JwtAuthenticationFilter
        // que creamos. En ese filtro se seteara (o no) el authentication del security context, tenemos
        // que ver si hay algo en ese authentication (si no hay nada lanzaremos una excepcion)
        boolean isGranted = isGranted(uriWithoutContextPath, httpMethod, authentication.get());
        return new AuthorizationDecision(isGranted);
    }

    private String extractUriWithoutContextPath(HttpServletRequest request) {

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        return uri.replaceFirst(contextPath, "");
    }

    private static Predicate<Operation> getOperationPredicate(String uriWithoutContextPath, String httpMethod) {
        return operation -> {
            // Hay que coger el path base (se encuentra en el modulo al cual se refiere la operacion)
            String basePath = operation.getModule().getBasePath();

            // El patron sera algo de este estilo: /basePathDelModulo/pathDeLaOperacion
            // Ejemplo: /products/[0-9]*/disabled
            // Esto funciona porque usamos expresiones regulares
            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            // Hacemos matchear ese patron con la uri a la que se quiere acceder
            Matcher matcher = pattern.matcher(uriWithoutContextPath);

            // Si alguna es igual y el metodo tambien es el mismo, podemos decir que es publico el acceso
            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private boolean isPublic(String uriWithoutContextPath, String httpMethod) {

        List<Operation> publicEndpointsOperations = operationService.findAllPublicOperations();

        return publicEndpointsOperations.stream().anyMatch(getOperationPredicate(uriWithoutContextPath, httpMethod));
    }

    private boolean isGranted(String uriWithoutContextPath, String httpMethod, Authentication authentication) {

        // Si no se ha seteado la authentication del security context, va a ser del tipo AnonymousAuthenticationToken
        // Ademas en el jwtAuthenticationFilter si el jwt es valido, seteamos ese authentication con
        // el tipo UsernamePasswordAuthenticationToken (si el jwt es invalido ya habra saltado una excepcion en el
        // propio filtro)
        if(!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = obtainOperations(authentication);

        // Realmente ahora las authorites que se ponen dentro del authentication del security context
        // si usamos este manager personalizado no sirven de mucho, ya que ahora para ver si un usuario esta o no
        // autorizado para ejecutar una operacion se recoge del authentication el principal (de ahi su username) y
        // se busca en bd las operaciones que puede ejecutar. Y luego se compara los endpoints asociados a esas operaciones
        // con el endpoint al que se quiere acceder.
        return operations.stream().anyMatch(
                getOperationPredicate(uriWithoutContextPath, httpMethod)
        );
    }

    private List<Operation> obtainOperations(Authentication authentication) {

        String username = (String) authentication.getPrincipal();
        User user = userService.findOneUserByUsername(username).get(); // Nunca va a ser null

        return user.getRole().getOperations();
    }
}
