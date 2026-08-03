package com.dailycodework.demo.Controllers;

import com.dailycodework.demo.Response.ApiResponse;
import com.dailycodework.demo.Response.JwtResponse;
import com.dailycodework.demo.Security.jwt.JwtUtils;
import com.dailycodework.demo.Security.user.ShopUserDetails;
import com.dailycodework.demo.request.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login( @Valid @RequestBody  LoginRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail() , request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId() , jwt);
            return ResponseEntity.ok(new ApiResponse("Login Successful" , jwtResponse));
        } catch (AuthenticationException e) {
             return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage() , null));
        }
    }
}
