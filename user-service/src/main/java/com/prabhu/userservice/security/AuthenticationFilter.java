package com.prabhu.userservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabhu.userservice.service.UserService;
import com.prabhu.userservice.shared.UserDto;
import com.prabhu.userservice.ui.model.UserLoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This belongs to user-service
 *
 * @author Prabhu Madipalli
 */


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;

    private Environment env;

    public AuthenticationFilter(UserService userService,
                                Environment env,
                                AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
                    );
                    
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailsByUsername(userName);
        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(
                        env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret") )
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
    }
}
