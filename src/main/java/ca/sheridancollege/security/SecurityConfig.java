package ca.sheridancollege.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	LoginAccessDeniedHandler accessDeniedHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		 http
        .csrf().disable() //this line was added
        .authorizeRequests()
        .antMatchers("/login").permitAll()
			.antMatchers("/sm/**").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/somePostURL").hasRole("USER")
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.POST, "/somePostURL").hasRole("ADMIN")
			.antMatchers("/", "/img/**","/css/**", "/js/**", "/**").permitAll() //everybody can access
			.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login").permitAll()
		.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}
	
	/*
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
		.withUser("Anmol").password("root").roles("USER").and().withUser("Harman").password("root")
		.roles("PAND", "ADMIN");
	}
	*/
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	UserDetailsServiceimpl userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}
}
