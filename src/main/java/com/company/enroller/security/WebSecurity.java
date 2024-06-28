package com.company.enroller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Value("${security.secret}")
    String secret;

    @Value("${security.issuer}")
    String issuer;

    @Value("${security.tokenExpiration}")
    int tokenExpiration;


    @Autowired
    private ParticipantProvider participantProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/participants").permitAll()
                .antMatchers("/tokens").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), secret, issuer, tokenExpiration), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), secret))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(participantProvider).passwordEncoder(passwordEncoder());
    }
}