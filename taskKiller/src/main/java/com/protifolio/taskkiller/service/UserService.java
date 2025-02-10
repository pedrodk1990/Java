package com.protifolio.taskkiller.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.repository.UserRepository;

import jakarta.persistence.EntityExistsException;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private ProfileService profileS;

	public List<Users> list() {
		return repo.findAll().stream().collect(Collectors.toList());
	}

	public Users create(Users user) {
		if(UserExist(user.getEmail())) throw new EntityExistsException("O correto seria disparar um e-mail de confirmação.");
		user.setPassword(encoder.encode(user.getPassword()));
		return repo.save(user);
	}

	public Users edit(String email, Users user) {
		Users old = getByEmail(email);
		if (old == null)
			return null;
		old.setEmail(user.getEmail());
		old.setPassword(encoder.encode(user.getPassword()));
		old.setRoles(user.getRoles());
		profileS.getById(user.getProfile());
		return repo.save(old);
	}

	public Users updatePartial(String email, Users user) {
		Users old = getByEmail(email);
		if (old == null)
			return null;
		if (user.getPassword() != null)
			old.setPassword(encoder.encode(user.getPassword()));
		if (user.getRoles() != null)
			old.setRoles(user.getRoles());
		if (user.getProfile() != null)
			old.setProfile(profileS.getById(user.getProfile()));
		return repo.save(old);
	}

	public void delete(String email) {
		Users user = getByEmail(email);
		if (user == null)
			return;
		repo.delete(user);
	}

	public Users getByEmail(String email) {
		List<Users> list = repo.findAll().stream().filter(n -> n.getEmail().equals(email)).collect(Collectors.toList());
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	public Users getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			UserDetails userDetails = null;
			Users temp = new Users();
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				userDetails = (UserDetails) principal;
				temp.setEmail(userDetails.getUsername());
//				temp.setPassword(encoder.encode(userDetails.getPassword()));
			} else {
				temp.setEmail(principal.toString());
			}
			return getByEmail(temp.getEmail());
		}
		return null;
	}

	public static boolean isInRole(String roleName) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean test = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch(role -> role.equals(roleName));
		return test;
	}
	public boolean UserExist(String email) {
		Optional<Users> valid = repo.getByEmail(email);
		return valid.isPresent();
	}
}
