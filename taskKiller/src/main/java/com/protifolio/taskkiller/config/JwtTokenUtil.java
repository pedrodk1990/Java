package com.protifolio.taskkiller.config;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import com.protifolio.taskkiller.model.Role;

@Component
public class JwtTokenUtil {

	private final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final static int EXPIRATION_TIME = 1000*60*60;
	
	
	public String generateToken(String email,Set<Role> roles) {
		
		List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
		
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.claim("roles", roleNames)
				.signWith(key)
				.compact();
	}
	public String validateToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	@SuppressWarnings("unchecked")
	public List<String> getRolesFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("roles", List.class);
//				.getSubject();
	}
}