package cc.freezinghell.messagerie.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private DaoAuthenticationProvider daoAuthenticationProvider;
	private ProviderManager providerManager;

	public SecurityConfig(@Autowired UserService userService) {
		this.daoAuthenticationProvider = new DaoAuthenticationProvider();
		this.daoAuthenticationProvider.setUserDetailsService(userService);

		this.providerManager = new ProviderManager(daoAuthenticationProvider);
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return this.providerManager;
	}

	@Bean
	public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
				.authenticationManager(this.providerManager);

		return http.build();
	}
}