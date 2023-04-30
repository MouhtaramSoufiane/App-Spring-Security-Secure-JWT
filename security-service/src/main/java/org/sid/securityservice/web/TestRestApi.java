package org.sid.securityservice.web;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestRestApi {

    @GetMapping("/dataTest")
    public Map<String, Object> dataTest(Authentication authentication) {

        return Map.of(
                "Message :", "Data Test",
                "Username :", authentication.getName(),
                "authoristy :", authentication.getAuthorities()
        );
    }


}
