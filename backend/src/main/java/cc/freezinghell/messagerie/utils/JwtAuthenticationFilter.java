package cc.freezinghell.messagerie.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cc.freezinghell.messagerie.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extrait le token JWT du header
    	// Récupère le token du header de la requête, par exemple, du header "Authorization"
        String header = request.getHeader("Authorization");
        System.out.println("PRINTLN : " + header);
        String token = null;
        String username;
        User user;

        if (header != null && header.startsWith("Bearer ")) {
            // Extrait le token sans le préfixe "Bearer "
            token =  header.substring(7);
        }

        if (token != null) {
            // Valide le token
        	username = jwtTokenUtil.extractUsername(token);
        	user = (User) userService.loadUserByUsername(username);
            

            if (jwtTokenUtil.validateToken(token, user)) {
            	
                // Vérifie le rôle de l'utilisateur à partir des claims du token
                if ("ADMIN".equals(user.getRole())) {
                    // Si l'utilisateur a le rôle "admin", vous pouvez autoriser la requête
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        // Si le token est invalide ou si l'utilisateur n'a pas le rôle "admin", renvoyez une réponse non autorisée
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

