package com.ketul.demo.securityConfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.ketul.demo.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	 @Autowired
	 private UserDetailsService userDetailsService;    
	 
	 @Autowired
	 private JwtRequestFilter jwtRequestFilter;
	    
	    @Bean
	    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	        StrictHttpFirewall firewall = new StrictHttpFirewall();
	        firewall.setAllowSemicolon(true);    
	        firewall.setAllowUrlEncodedSlash(true);
	        return firewall;
	    }
	    
	    @Override
	    public void configure(WebSecurity web) throws Exception {
	        super.configure(web);
	        web.httpFirewall(allowUrlEncodedSlashHttpFirewall()); 

	    }
	    
	    @Bean
	    public BCryptPasswordEncoder bCryptPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	    }


	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	        		http
	        		.csrf() 
	    			.disable()
	    			.authorizeRequests()
	    			.antMatchers("/authenticate").permitAll()
	    			.anyRequest().authenticated()
	    			
	    			.and()
	                .formLogin()
	                .usernameParameter("email")
	                .passwordParameter("pass")
	                .permitAll()
	                
	                .defaultSuccessUrl("/users")
	                .failureUrl("/login?error")
	                .permitAll()
	                
	                .and()
	                .rememberMe()
	                .tokenRepository(persistantTokenRepo())
	                
	                .and()
	                .exceptionHandling()
	                
	                .and().sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        		
	        		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	    		      
	    }

	    @Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
	    
	    private PersistentTokenRepository persistantTokenRepo() {
	    	JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
	    	jdbcTokenRepositoryImpl.setDataSource(dataSource);
			return jdbcTokenRepositoryImpl;
		}

		@Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	    }
	    
}
