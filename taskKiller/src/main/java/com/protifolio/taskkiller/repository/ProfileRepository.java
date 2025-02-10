package com.protifolio.taskkiller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protifolio.taskkiller.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
