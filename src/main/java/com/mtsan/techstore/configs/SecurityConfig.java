package com.mtsan.techstore.configs;

import com.mtsan.techstore.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private BasicAuthEntryPoint basicAuthEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder())
			.usersByUsernameQuery("SELECT `username`, `password`, `enabled` FROM `users` WHERE username = ?")
			.authoritiesByUsernameQuery("SELECT u.username, u.rank FROM users u WHERE u.username = ?");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/merchants*").hasAnyAuthority(Rank.Administrator.toString())
				.antMatchers("/products*").hasAnyAuthority(Rank.Administrator.toString())
				.antMatchers("/clients*").hasAuthority(Rank.Merchant.toString())
				.anyRequest().authenticated()
				.and()
				.httpBasic()
				.authenticationEntryPoint(basicAuthEntryPoint);
	}
}