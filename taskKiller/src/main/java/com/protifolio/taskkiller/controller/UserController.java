package com.protifolio.taskkiller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.service.UserService;


@RestController
@RequestMapping("/admin/users")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping
	public ResponseEntity<List<Users>> list() {
		if (UserService.isInRole("ADMIN"))
			return ResponseEntity.ok(service.list());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

	}

	@GetMapping("/{email}")
	public Users get(@PathVariable String email) {
		return service.getByEmail(email);
	}

	@PostMapping
	public Users create(@RequestBody Users user) {
		return service.create(user);
	}

	@PutMapping("/{email}")
	public Users edit(@PathVariable String email, @RequestBody Users user) {
		return service.edit(email, user);
	}

	@DeleteMapping("/{email}") // TODO: nao funciona e nao tem erros
	public ResponseEntity<Users> delete(@PathVariable String email) {
		service.delete(email);
		ResponseEntity<Users> re = new ResponseEntity<Users>(HttpStatus.ACCEPTED);
		return re;
	}

}
