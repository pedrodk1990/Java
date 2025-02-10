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

import com.protifolio.taskkiller.model.Profile;
import com.protifolio.taskkiller.service.ProfileService;

@RestController
@RequestMapping("/admin/profile")
public class ProfileController {

	@Autowired
	private ProfileService service;
	@GetMapping
	public List<Profile> list() {
		return service.list();
	}
	@GetMapping("/{id}")
	public Profile getById(@PathVariable Long id) {
		return service.getById(id);
	}
	@PostMapping
	public Profile create(@RequestBody Profile profile) {
		return service.create(profile);
	}
	@PutMapping("/{id}")
	public Profile edit(@PathVariable Long id, @RequestBody Profile profile) {
		return service.edit(id, profile);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Profile> delete(@PathVariable Long id) {
		service.delete(id);
		ResponseEntity<Profile> re = new ResponseEntity<Profile>(HttpStatus.ACCEPTED);
		return re;
		
	}
}
