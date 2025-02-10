package com.protifolio.taskkiller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.protifolio.taskkiller.config.JwtTokenUtil;
import com.protifolio.taskkiller.model.AuthRequest;
import com.protifolio.taskkiller.model.AuthResponse;
import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.service.AuthService;
import com.protifolio.taskkiller.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

	@Autowired
	private AuthService service;
	@Autowired
	private JwtTokenUtil jwtUtil;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UserService userS;
	
	@PostMapping("/register")
	public String register(@RequestBody Users user) {
			return service.register(user);
	}
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request,HttpServletResponse response){//TODO> continuar daqui. documentando erros e respostas no postman
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		Users user = userS.getByEmail(request.getEmail());
		String token = jwtUtil.generateToken(request.getEmail(),user.getRoles());
		if (request.isRememberMe()) {
            Cookie rememberMeCookie = new Cookie("remember-me", token);
            rememberMeCookie.setHttpOnly(true);
            rememberMeCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dias
            rememberMeCookie.setPath("/");
            response.addCookie(rememberMeCookie);
        }
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
