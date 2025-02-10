package com.protifolio.taskkiller.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
		Users user = userRepo.getByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("Usuário não encontrado!"));
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),user.getPassword(),
				user.getRoles().stream().map(role->
				new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
//		user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));//list string rolenames
		
	}
	

}
