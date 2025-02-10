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

import com.protifolio.taskkiller.model.Tasks;
import com.protifolio.taskkiller.service.TasksService;

@RestController
@RequestMapping("/admin/tasks")
public class TaskController {

	@Autowired
	private TasksService service;
	
	@GetMapping
	public List<Tasks> list(){
		return service.list();
	}
	@GetMapping("/{id}")
	public Tasks getById(@PathVariable Long id) {
		return service.getById(id);
	}
	@PostMapping
	public Tasks create(@RequestBody Tasks tasks) {
		return service.create(tasks);
	}
	@PutMapping("/{id}")
	public Tasks edit(@PathVariable Long id, @RequestBody Tasks tasks) {
		return service.edit(id,tasks);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Tasks> delete(@PathVariable Long id){
		service.delete(id);
		ResponseEntity<Tasks> re = new ResponseEntity<Tasks>(HttpStatus.ACCEPTED);
		return re;
	}
}
