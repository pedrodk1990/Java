package com.protifolio.taskkiller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protifolio.taskkiller.model.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

}
