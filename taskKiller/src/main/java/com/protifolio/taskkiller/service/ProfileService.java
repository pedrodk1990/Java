package com.protifolio.taskkiller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.Profile;
import com.protifolio.taskkiller.repository.ProfileRepository;

@Service
public class ProfileService {

	@Autowired
	private ProfileRepository repo;
	
	public List<Profile> list(){
		return repo.findAll();
	}
	public Profile getById(Long id) {
		if(id==null)return null;
		Optional<Profile> found = repo.findById(id);
		return found.isPresent()? found.get():null;
	}
	public Profile create(Profile profile) {
		
		return repo.save(profile);
	}
	public Profile edit(Long id, Profile profile) {
		Profile old = getById(id);
		if(old==null)return null;
		old.setNome(profile.getNome());
		old.setTask(profile.getTask());
		return repo.save(old);		
	}
	public Profile updatePartial(Long id, Profile profile) {
		Profile old = getById(id);
		if(old==null)return null;
		if(profile.getNome()!=null) old.setNome(profile.getNome());
		return repo.save(old);		
	}
	public void delete(Long id) {
		Profile profile = getById(id);
		if(profile==null)return;
		repo.delete(getById(id));
	}
}
