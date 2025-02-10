package com.protifolio.taskkiller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.Role;
import com.protifolio.taskkiller.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository repo;

	public List<Role> list() {
		return repo.findAll();
	}

	public Role findByName(String roleName) {
		return repo.findByName(roleName);
	}

	public Role getById(Long id) {
		return repo.getReferenceById(id);
	}

	public Role create(Role role) {
		return repo.save(role);
	}

	public Role edit(Long id, Role role) {
		Role old = getById(id);
		if (old == null)
			return role;
		old.setId(role.getId());
		old.setName(role.getName());
		return repo.save(old);
	}

	public Role updatePartial(Long id, Role role) {
		Role old = getById(id);
		if (old == null)
			return role;
		if (role.getId() != null)
			old.setId(id);
		if (!role.getName().isBlank())
			old.setName(role.getName());
		return repo.save(old);
	}

	public void delete(Long id) {
		Role old = getById(id);
		if (old == null)
			return;
		repo.delete(old);
	}

	public boolean isFirstAccess() {
		return repo.count() == 0 ? true : false;
	}

}
