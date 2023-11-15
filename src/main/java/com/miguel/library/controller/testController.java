package com.miguel.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@RequestMapping("/test")
public class testController {
    @GetMapping("/get-headers")
    public ResponseEntity<Map<String, String>> getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader)));

        return ResponseEntity.ok(headers.toSingleValueMap());
    }

    @GetMapping("/get-auth-header")
    public String getAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return "Authorization Header: " + (authorizationHeader != null ? authorizationHeader : "Not present");
    }

    @GetMapping("/get-authorization-header")
    public ResponseEntity<Map<String, String>> getAuthorizationHeader(
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        Map<String, String> returnValue = new HashMap<>();
        returnValue.put("Authorization", authorizationHeader);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }


    @GetMapping("/get-token")
    public ResponseEntity<Map<String, String>> getUserToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("authorization");

        Map<String, String> response = new HashMap<>();
        response.put("token", (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : "No token found");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-authentication")
    public ResponseEntity<Map<String, String>> getUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, String> response = new HashMap<>();
        response.put("authentication", (authentication != null) ? authentication.toString() : "No authentication found");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-authToken")
    public ResponseEntity<Map<String, String>> getUsernamePasswordAuthToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, String> response = new HashMap<>();
        response.put("authToken", (authentication instanceof UsernamePasswordAuthenticationToken) ? authentication.toString() : "No UsernamePasswordAuthenticationToken found");

        return ResponseEntity.ok(response);
    }


}

