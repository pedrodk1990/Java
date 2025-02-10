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

import com.protifolio.taskkiller.model.TaskHistory;
import com.protifolio.taskkiller.service.TaskHistoryService;

@RestController
@RequestMapping("/admin/history")
public class TaskHistoryController {

	@Autowired
	private TaskHistoryService service;
	
	@GetMapping
	public List<TaskHistory> list(){
		return service.list();
	}
	@GetMapping("/{id}")
	public TaskHistory getById(@PathVariable Long id) {
		return service.getById(id);
	}
	@PostMapping
	public TaskHistory create(@RequestBody TaskHistory taskHistory) {
		return service.create(taskHistory);
	}
	@PutMapping("/{id}")
	public TaskHistory edit(@PathVariable Long id, @RequestBody TaskHistory taskHistory) {
		return service.edit(id, taskHistory);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<TaskHistory> delete(@PathVariable Long id){
		service.delete(id);
		ResponseEntity<TaskHistory> re = new ResponseEntity<TaskHistory>(HttpStatus.ACCEPTED);
		return re;
	}
}
