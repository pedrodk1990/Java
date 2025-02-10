package com.protifolio.taskkiller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protifolio.taskkiller.model.TaskHistory;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

}
