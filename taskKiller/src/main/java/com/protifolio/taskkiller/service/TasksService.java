package com.protifolio.taskkiller.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protifolio.taskkiller.model.TaskHistory;
import com.protifolio.taskkiller.model.Tasks;
import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.repository.TaskHistoryRepository;
import com.protifolio.taskkiller.repository.TasksRepository;
import com.protifolio.taskkiller.repository.UserRepository;

@Service
public class TasksService {

	@Autowired
	private TasksRepository repo;
	@Autowired
	private TaskHistoryRepository thRepo;
	@Autowired
	private UserRepository userRepo;

	public List<Tasks> list() {
		return repo.findAll();
	};

	public List<Tasks> myTasks(String email) {
		Optional<Users> me = userRepo.getByEmail(email);
		if (me.isEmpty())
			return null;

		return repo.findAll().stream().filter(n -> n.getProfile().getUser().getId() == me.get().getId())
				.collect(Collectors.toList());
	};

	public Tasks getById(Long id) {
		Optional<Tasks> task = repo.findById(id);
		return task.isPresent() ? task.get() : null;
	};

	public Tasks create(Tasks tasks) {
		return repo.save(tasks);
	};

	public Tasks edit(Long id, Tasks tasks) {
		Tasks old = getById(id);
		if (old == null)
			return null;
		TaskHistory history = new TaskHistory();
		history.setOldDescription(old.getDescription());
		old.setComments(tasks.getComments());
		old.setDate(tasks.getDate());
		old.setDescription(tasks.getDescription());
		old.setFrequency(tasks.getFrequency());
		old.setHistory(tasks.getHistory());
		old.setStatus(tasks.getStatus());
		old.setTitle(tasks.getTitle());
		history.setNewDescription(tasks.getDescription());
		history.setUpdateData(LocalDateTime.now());
		Tasks saved = repo.save(old);
		history.setTask(old);
		thRepo.save(history);
		return saved;
	};

	public Tasks updatePartial(Long id, Tasks tasks) {
		Tasks old = getById(id);
		if (old == null)
			return null;
		TaskHistory history = new TaskHistory();
		if (tasks.getDate() != null)
			old.setDate(tasks.getDate());
		if (!tasks.getDescription().isBlank())
			old.setDescription(tasks.getDescription());
		if (tasks.getFrequency() != null)
			old.setFrequency(tasks.getFrequency());
		if (tasks.getComments() != null)
			old.setStatus(tasks.getStatus());
		if (!tasks.getTitle().isBlank())
			old.setTitle(tasks.getTitle());
		
		history.setNewDescription(tasks.getDescription());
		history.setUpdateData(LocalDateTime.now());
		Tasks saved = repo.save(old);
		history.setTask(old);
		thRepo.save(history);
		return saved;
	};

	public void delete(Long id) {
		Tasks task = getById(id);
		if (task == null)
			return;
		repo.delete(getById(id));
	};
}
