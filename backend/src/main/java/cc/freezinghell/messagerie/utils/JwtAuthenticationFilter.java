package cc.freezinghell.messagerie.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import cc.freezinghell.messagerie.entities.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");

		if (header != null) {
			final String token;

			if (header.startsWith("Bearer ")) {
				// Extrait le token sans le préfixe "Bearer "
				token = header.substring(7);
			} else {
				token = header;
			}

			final String username;

			try {
				username = jwtTokenUtil.extractUsername(token);
			} catch (SecurityException | ExpiredJwtException e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
				return;
			}

			User user = (User) userService.loadUserByUsername(username);

			if (!jwtTokenUtil.validateToken(token, user)) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT invalide");
				return;
			} else {
				UsernamePasswordAuthenticationToken authenticationToken
						= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		filterChain.doFilter(request, response);
	}
}
