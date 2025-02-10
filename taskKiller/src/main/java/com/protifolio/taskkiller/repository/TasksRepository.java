package com.protifolio.taskkiller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protifolio.taskkiller.model.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

}
