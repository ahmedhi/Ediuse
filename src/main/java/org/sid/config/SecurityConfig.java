package org.sid.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource ;

	@Override
	protected void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}password").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER");
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select login_user as principal ,pwd_user as credentials, type_user as role from Utilisateur where login_user=?")
		.authoritiesByUsernameQuery("select utilisateur as principal , role as role from UsersRole where utilisateur=?")
		.rolePrefix("ROLE_");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.formLogin().loginPage("/login");
		http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/admin/users", true);
		//http.authorizeRequests().antMatchers("/*").hasRole("USER");
		//http.authorizeRequests().antMatchers("/").hasRole("ADMIN");
		//http.exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().antMatchers("/*").permitAll();
	}
	
}
