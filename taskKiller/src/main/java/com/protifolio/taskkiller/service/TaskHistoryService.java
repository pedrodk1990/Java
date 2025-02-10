package com.protifolio.taskkiller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.TaskHistory;
import com.protifolio.taskkiller.repository.TaskHistoryRepository;

@Service
public class TaskHistoryService {

	@Autowired
	private TaskHistoryRepository repo;
	
	public List<TaskHistory> list(){
		return repo.findAll();
	}
	public TaskHistory getById(Long id) {
		Optional<TaskHistory> taskH = repo.findById(id);
		return taskH.isPresent()?taskH.get():null;
	}
	public TaskHistory create(TaskHistory taskHistory) {
		return repo.save(taskHistory);
	}
	public TaskHistory edit(Long id, TaskHistory taskHistory) {
		TaskHistory old = getById(id);
		if(old == null) return null;
		old.setNewDescription(taskHistory.getNewDescription());
		old.setOldDescription(taskHistory.getOldDescription());
		old.setTask(taskHistory.getTask());
		old.setUpdateData(taskHistory.getUpdateData());
		return repo.save(old);		
	}
	public TaskHistory updatePartial(Long id, TaskHistory taskHistory) {
		TaskHistory old = getById(id);
		if(old == null) return null;
		if(taskHistory.getNewDescription()!=null) old.setNewDescription(taskHistory.getNewDescription());
		if(taskHistory.getOldDescription()!=null) old.setOldDescription(taskHistory.getOldDescription());
		return repo.save(old);		
	}
	public void delete(Long id) {
		TaskHistory taskH = getById(id);
		if(taskH==null)return;
		repo.delete(taskH);
	}
}
