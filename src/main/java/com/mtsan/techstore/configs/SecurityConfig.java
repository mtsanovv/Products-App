package com.mtsan.techstore.configs;

import com.mtsan.techstore.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final DataSource dataSource;

	private final BasicAuthEntryPoint basicAuthEntryPoint;

	@Autowired
	public SecurityConfig(DataSource dataSource, BasicAuthEntryPoint basicAuthEntryPoint) {
		this.dataSource = dataSource;
		this.basicAuthEntryPoint = basicAuthEntryPoint;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(new BCryptPasswordEncoder())
			.usersByUsernameQuery("SELECT `username`, `password`, `enabled` FROM `users` WHERE username = ?")
			.authoritiesByUsernameQuery("SELECT u.username, u.rank FROM users u WHERE u.username = ?");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
					.cors()
				.and()
				.csrf().disable()
					.authorizeRequests()
						//Permissions for /products
						.antMatchers(HttpMethod.POST, "/products").hasAuthority(Rank.Administrator.toString())
						.antMatchers(HttpMethod.GET, "/products").hasAnyAuthority(Rank.Administrator.toString(), Rank.Merchant.toString())

						//Permissions for /products/*/tweet
						.antMatchers(HttpMethod.POST, "/products/*/tweet").hasAuthority(Rank.Merchant.toString())

						//Permissions for /products/*
						.antMatchers(HttpMethod.GET, "/products/*").hasAnyAuthority(Rank.Administrator.toString(), Rank.Merchant.toString())
						.antMatchers(HttpMethod.PUT, "/products/*").hasAuthority(Rank.Administrator.toString())
						.antMatchers(HttpMethod.DELETE, "/products/*").hasAuthority(Rank.Administrator.toString())

						//Permissions for /merchants
						.antMatchers(HttpMethod.POST, "/merchants").hasAuthority(Rank.Administrator.toString())
						.antMatchers(HttpMethod.GET, "/merchants").hasAuthority(Rank.Administrator.toString())

						//Permissions for /merchants/*
						.antMatchers(HttpMethod.GET,"/merchants/*").hasAuthority(Rank.Administrator.toString())
						.antMatchers(HttpMethod.PUT,"/merchants/*").hasAuthority(Rank.Administrator.toString())
						.antMatchers(HttpMethod.DELETE,"/merchants/*").hasAuthority(Rank.Administrator.toString())

						//Permissions for /clients
						.antMatchers(HttpMethod.GET, "/clients").hasAuthority(Rank.Merchant.toString())
						.antMatchers(HttpMethod.POST, "/clients").hasAuthority(Rank.Merchant.toString())

						//Permissions for /clients/*
						.antMatchers(HttpMethod.GET, "/clients/*").hasAuthority(Rank.Merchant.toString())
						.antMatchers(HttpMethod.PUT, "/clients/*").hasAuthority(Rank.Merchant.toString())
						.antMatchers(HttpMethod.DELETE, "/clients/*").hasAuthority(Rank.Merchant.toString())

						//Permissions for /sales
						.antMatchers(HttpMethod.GET, "/sales").hasAnyAuthority(Rank.Administrator.toString(), Rank.Merchant.toString())
						.antMatchers(HttpMethod.POST, "/sales").hasAuthority(Rank.Merchant.toString())

						//Permissions for /user/tweet
						.antMatchers(HttpMethod.POST, "/user/tweet").hasAuthority(Rank.Merchant.toString())

						.anyRequest().authenticated()
				.and()
					.httpBasic()
					.authenticationEntryPoint(basicAuthEntryPoint);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(@Value("${http.allowed-origins}") String httpAllowedOrigins) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(httpAllowedOrigins.split(",")));
		configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
		configuration.setAllowCredentials(true);
		configuration.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}