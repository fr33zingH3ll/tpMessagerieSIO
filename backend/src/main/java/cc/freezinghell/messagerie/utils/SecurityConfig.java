package cc.freezinghell.messagerie.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	private DaoAuthenticationProvider daoAuthenticationProvider;
	private ProviderManager providerManager;

	public SecurityConfig() {
		this.daoAuthenticationProvider = new DaoAuthenticationProvider();
		this.daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);

		this.providerManager = new ProviderManager(daoAuthenticationProvider);
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return this.providerManager;
	}

	@Bean
	public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
				.authenticationManager(this.providerManager);
		return http.build();
	}
}
