package com.protifolio.taskkiller.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.Role;
import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private RoleService roleS;
	@Autowired
	private AuthenticationManager authManager;

	public String register(Users user) {
		firstAccess();//TODO:ao rodar a primeira vez, comente esta linha
		user.setPassword(encoder.encode(user.getPassword()));
		user.getRoles().add(roleS.findByName("USER"));
		userRepo.save(user);
		return "Usuário cadastrado com sucesso!";
	}

	public String login(String email, String password) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		return "Usuário logado com sucesso!";
	}

	private void firstAccess() {
		if (roleS.isFirstAccess()) {
			Role usr = new Role();
			usr.setName("USER");
			Role adm = new Role();
			adm.setName("ADMIN");
			roleS.create(usr);
			Role admRole = roleS.create(adm);
			Long countAdm = userRepo.findAll().stream()
					.filter(n -> n.getRoles().stream().anyMatch(m -> m.getName().equalsIgnoreCase(admRole.getName())))
					.count();
			if (countAdm <= 0) {
				Users adm1 = new Users();
				adm1.setEmail("adm@adm.com");
				adm1.setPassword(encoder.encode("adm123"));
				adm1.getRoles().add(admRole);
				userRepo.save(adm1);
			}
			return;
		}
	}

}
