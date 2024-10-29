package com.project.socialnetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.project.socialnetwork.service.AccountService;
import com.project.socialnetwork.service.CustomUserDetailsService;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder() {
		// return new DisabledPasswordEncoder();
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(AccountService accountService) {
		return new CustomUserDetailsService(accountService);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetailsService);
		provider.setHideUserNotFoundExceptions(false); // default is true, bad
		// credentials will be shown as bad credentials
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.ignoringRequestMatchers("/likePost"))
				.authorizeHttpRequests(authorize -> authorize
						.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE)
						.permitAll()
						.requestMatchers("/", "/login", "/register", "/client/**", "/css/**",
								"/js/**", "/images/**")
						.permitAll()
						// .requestMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				// .anyRequest().permitAll())
				.sessionManagement((sessionManagement) -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
						.invalidSessionUrl("/logout?expired")
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false))
				.logout(logout -> logout.deleteCookies("JSESSIONID").invalidateHttpSession(true))
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/", true)
						.successHandler(customAuthenticationSuccessHandler())
						.failureHandler(authenticationFailureHandler())
						// .failureUrl("/login?error")
						.permitAll());
		// .exceptionHandling(ex -> ex
		// .accessDeniedPage("/page-not-found"));
		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return (request, response, exception) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid email or password.");
		};
	}
}
