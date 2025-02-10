package com.protifolio.taskkiller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.TaskComment;
import com.protifolio.taskkiller.repository.TaskCommentRepository;

@Service
public class TaskCommentService {

	@Autowired
	private TaskCommentRepository repo;

	public List<TaskComment> list() {
		return repo.findAll();
	}

	public TaskComment getById(Long id) {
		Optional<TaskComment> task = repo.findById(id);
		return task.isPresent() ? task.get() : null;
	}

	public TaskComment create(TaskComment taskComment) {
		return repo.save(taskComment);
	}

	public TaskComment edit(Long id, TaskComment taskComment) {
		TaskComment old = getById(id);
		if (old == null)
			return null;
		old.setTask(taskComment.getTask());
		old.setText(taskComment.getText());
		return repo.save(old);
	}
	public TaskComment updatePartial(Long id, TaskComment taskComment) {
		TaskComment old = getById(id);
		if (old == null)
			return null;
		if(taskComment.getText()!=null) old.setText(taskComment.getText());
		return repo.save(old);
	}

	public void delete(Long id) {
		TaskComment taskC = getById(id);
		if (taskC == null)
			return;
		repo.delete(taskC);
	}
}
