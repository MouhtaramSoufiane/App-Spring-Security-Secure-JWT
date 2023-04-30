package org.sid.securityservice.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    private JwtEncoder jwtEncoder;

    public AuthController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

     @PostMapping("/token")
    public Map<String ,String> jwtToken(Authentication authentication){
        Map<String,String> idToken=new HashMap<>();
        Instant instant=Instant.now();
        String scope = authentication.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet= JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(instant)
                .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
                .issuer("security-service")
                .claim("scope",scope)
                .build();

         String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();

        idToken.put("accessToken",jwtAccessToken);
        return idToken;


     }
}
