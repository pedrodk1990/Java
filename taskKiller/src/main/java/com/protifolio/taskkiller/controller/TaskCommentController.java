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

import com.protifolio.taskkiller.model.TaskComment;
import com.protifolio.taskkiller.service.TaskCommentService;

@RestController
@RequestMapping("/admin/Comment")
public class TaskCommentController {

	@Autowired
	private TaskCommentService service;
	@GetMapping
	public List<TaskComment> list(){
		return service.list();
	}
	@GetMapping("/{id}")
	public TaskComment getById(@PathVariable Long id) {
		return service.getById(id);
	}
	@PostMapping
	public TaskComment create(@RequestBody TaskComment taskComment) {
		return service.create(taskComment);
	}
	@PutMapping("/{id}")
	public TaskComment edit(@PathVariable Long id, @RequestBody TaskComment taskComment) {
		return service.edit(id, taskComment);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<TaskComment> delete(@PathVariable Long id){
		service.delete(id);
		ResponseEntity<TaskComment> re = new ResponseEntity<TaskComment>(HttpStatus.ACCEPTED);
		return re;
	}
	
}
