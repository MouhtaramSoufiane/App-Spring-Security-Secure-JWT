package org.sid.securityservice.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class securityConfig {
    private RsaKeysConfig rsaKeysConfig;

    public securityConfig(RsaKeysConfig rsaKeysConfig) {
        this.rsaKeysConfig = rsaKeysConfig;
    }


    @Bean
    public InMemoryUserDetailsManager memoryUserDetailsManager(){

        return new InMemoryUserDetailsManager(
                User.withUsername("user").password("{noop}1234").roles("USER").build(),
                User.withUsername("admin").password("{noop}2345").roles("ADMIN","USER").build()
        );
    }

    @Bean
  public  SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
    @Bean
   public JwtEncoder jwtEncoder(){
     JWK jwk=new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
       JWKSource<SecurityContext> jwkSource=new ImmutableJWKSet<>(new JWKSet(jwk));
       return new NimbusJwtEncoder(jwkSource);

    }
    @Bean
    public JwtDecoder jwtDecoder(){
     return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.publicKey()).build();

    }
}
