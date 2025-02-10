package com.protifolio.taskkiller.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.protifolio.taskkiller.model.Profile;
import com.protifolio.taskkiller.model.TaskComment;
import com.protifolio.taskkiller.model.TaskHistory;
import com.protifolio.taskkiller.model.Tasks;
import com.protifolio.taskkiller.model.Users;
import com.protifolio.taskkiller.service.ProfileService;
import com.protifolio.taskkiller.service.TaskCommentService;
import com.protifolio.taskkiller.service.TaskHistoryService;
import com.protifolio.taskkiller.service.TasksService;
import com.protifolio.taskkiller.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class MainController {
	// register: ok [adicionar role user]
	// login: ok
	// me: get[ok]|edit[ok]|patch[ok]|
	// me/profile: post[nao vincula o usuario]
	// me/profile/tasks: post[nao vincula o perfil]
	/**
	 * /register: ok [adicionar role user] /login ok /me [getbylogin,edit,delete]
	 * /me/profile [crud nao pode criar mais de um] /me/profile/tasks [lista minhas
	 * tasks] /me/profile/tasks/{task_id} [crud]
	 * /me/profile/tasks/{task_id}/comments [lista comentarios de uma task de um
	 * profile {me}] /me/profile/tasks/{task_id}/comments/{comment_id} [crud]
	 * /me/profile/tasks/{task_id}/history [lista historico de uma task de um
	 * profile {me}] /me/profile/tasks/{task_id}/history/{history_id} [crud] /login
	 * /register
	 */

	@Autowired
	private UserService userS;
	@Autowired
	private ProfileService profileS;
	@Autowired
	private TasksService taskS;
	@Autowired
	private TaskCommentService taskCS;
	@Autowired
	private TaskHistoryService taskHS;

	
	private void sendErr(HttpServletResponse response, String message) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
	}

	private Users userNotFound(HttpServletResponse response) throws IOException {
		Users me = userS.getMe();
		if (me == null)
			sendErr(response, "Usuário não encontrado.");
		return me;
	}

	private Profile profileNotFound(HttpServletResponse response) throws IOException {
		Users me = userNotFound(response);
		if (me == null)
			return null;
		Profile myProfile = profileS.getById(me.getProfile());
		if (myProfile == null)
			sendErr(response, "Perfil não encontrado.");
		return myProfile;
	}

	private List<Tasks> taskListNotFound(HttpServletResponse response) throws IOException {
		Profile myProfile = profileNotFound(response);
		if (myProfile == null)
			return null;
		List<Tasks> myTasks = myProfile.getTask();
		if (myTasks == null)
			sendErr(response, "Tarefas não encontradas.");
		return myTasks;
	}

	private Tasks taskNotFound(HttpServletResponse response, Long task_id) throws IOException {
		List<Tasks> myTasks = taskListNotFound(response);
		Tasks found = myTasks.stream().filter(n -> n.getId() == task_id).collect(Collectors.toList()).get(0);
		if (found == null)
			sendErr(response, "Tarefa não encontrada.");
		return found;
	}

	private List<TaskComment> taskCommentListNotFound(HttpServletResponse response, Long task_id) throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		List<TaskComment> myTaskComments = myTask.getComments();
		if (myTaskComments == null)
			sendErr(response, "Comentários não encontrados.");
		return myTaskComments;
	}

	private TaskComment myTaskCommentNotFound(HttpServletResponse response, Long task_id, Long comment_id)
			throws IOException {
		List<TaskComment> myTaskComments = taskCommentListNotFound(response, task_id);
		if (myTaskComments == null)
			return null;
		TaskComment myTaskComment = myTaskComments.stream().filter(n -> n.getId() == comment_id)
				.collect(Collectors.toList()).get(0);
		if (myTaskComment == null)
			sendErr(response, "Comentário não encontrado.");
		return myTaskComment;
	}

	private List<TaskHistory> taskHistoryListNotFound(HttpServletResponse response, Long task_id) throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		List<TaskHistory> myTaskHistory = myTask.getHistory();
		if (myTaskHistory == null)
			sendErr(response, "Histórico de alteração não encontrado.");
		return myTaskHistory;
	}

	private TaskHistory myTaskHistoryNotFound(HttpServletResponse response, Long task_id, Long history_id)
			throws IOException {
		List<TaskHistory> myTaskHistoryList = taskHistoryListNotFound(response, task_id);
		if (myTaskHistoryList == null)
			return null;
		TaskHistory myTaskHistory = myTaskHistoryList.stream().filter(n -> n.getId() == history_id)
				.collect(Collectors.toList()).get(0);
		if (myTaskHistory == null)
			sendErr(response, "Alteração não encontrada.");
		return myTaskHistory;
	}

	private void sendOk(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK); // Define o código 200 (OK)
		response.getWriter().write(message); // Envia resposta personalizada
		response.getWriter().flush();
	}

	/********************************* USERS */
	@GetMapping("/me")
	public Users me(HttpServletResponse response) throws IOException {
		Users me = userNotFound(response);
		return me;
	}

	@PutMapping("/me")
	public Users editMe(@RequestBody Users user, HttpServletResponse response) throws IOException {
		Users me = userNotFound(response);
		return me == null ? null : userS.edit(me.getEmail(), user);
	}

	@PatchMapping("/me")
	public Users updatePartialMe(@RequestBody Users user, HttpServletResponse response) throws IOException {
		Users me = userNotFound(response);
		return me == null ? null : userS.updatePartial(me.getEmail(), user);
	}

	@DeleteMapping("/me")
	public void deleteMe(HttpServletResponse response) throws IOException {
		Users me = userNotFound(response);
		if (me == null)
			return;
		userS.delete(me.getEmail());
		sendOk(response, "Operação concluída com sucesso!");
	}

	/********************************* PROFILE */
	@GetMapping("/me/profile")
	public Profile myProfile(HttpServletResponse response) throws IOException {
		return profileNotFound(response);
	}

	@PostMapping("/me/profile")
	public Profile createMyProfile(@RequestBody Profile profile, HttpServletResponse response) throws IOException {
		Users me = userNotFound(response);
		if (me == null)
			return null;
		Profile myProfile = profileS.getById(me.getProfile());
		if (myProfile == null) {
			profile.setUser(me);
			return profileS.create(profile);
		}
			return myProfile;
	}

	@PutMapping("/me/profile")
	public Profile editMyProfile(@RequestBody Profile profile, HttpServletResponse response) throws IOException {
		Profile myProfile = profileNotFound(response);
		return myProfile == null ? null : profileS.edit(myProfile.getId(), profile);
	}

	@PatchMapping("/me/profile")
	public Profile updatePartialMyProfile(@RequestBody Profile profile, HttpServletResponse response)
			throws IOException {
		Profile myProfile = profileNotFound(response);
		return myProfile == null ? null : profileS.updatePartial(myProfile.getId(), profile);
	}

	@DeleteMapping("/me/profile")
	public void DeleteMyProfile(HttpServletResponse response) throws IOException {
		Profile myProfile = profileNotFound(response);
		if (myProfile == null)
			return;
		profileS.delete(myProfile.getId());
		sendOk(response, "Operação concluída com sucesso!");

	}

	/********************************* TASKS */
	@GetMapping("/me/profile/tasks")
	public List<Tasks> taskList(HttpServletResponse response) throws IOException {
		return taskListNotFound(response);
	}

	@GetMapping("/me/profile/tasks/{task_id}")
	public Tasks getTaskById(@PathVariable Long task_id, HttpServletResponse response) throws IOException {
		return taskNotFound(response, task_id);
	}

	@PostMapping("/me/profile/tasks")
	public Tasks createTask(@RequestBody Tasks task, HttpServletResponse response) throws IOException {
		if (task == null) {
			sendErr(response, "Tarefa não pode ser nula.");
			return null;
		}
		Users me = userNotFound(response);
		task.setProfile(profileS.getById(me.getProfile()));
		return taskS.create(task);
	}

	@PutMapping("/me/profile/tasks/{task_id}")
	public Tasks editTask(@PathVariable Long task_id, @RequestBody Tasks task, HttpServletResponse response)
			throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		return taskS.edit(task_id, myTask);
	}

	@PatchMapping("/me/profile/tasks/{task_id}")
	public Tasks updatePartialTask(@PathVariable Long task_id, @RequestBody Tasks task, HttpServletResponse response)
			throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		return taskS.updatePartial(task_id, myTask);
	}

	@DeleteMapping("/me/profile/tasks/{task_id}")
	public void deleteTask(@PathVariable Long task_id, HttpServletResponse response) throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		if (myTask != null)
			sendOk(response, "Operação concluída com sucesso!");
	}

	/********************************* COMMENTS */
	@GetMapping("/me/profile/tasks/{task_id}/comments")
	public List<TaskComment> taskCommentList(@PathVariable Long task_id, HttpServletResponse response)
			throws IOException {
		return taskCommentListNotFound(response, task_id);
	}

	@GetMapping("/me/profile/tasks/{task_id}/comments/{comment_id}")
	public TaskComment getTaskCommentById(@PathVariable Long task_id, @PathVariable Long comment_id,
			HttpServletResponse response) throws IOException {
		return myTaskCommentNotFound(response, task_id, comment_id);
	}

	@PostMapping("/me/profile/tasks/{task_id}/comments")
	public TaskComment createTaskComment(@PathVariable Long task_id, @RequestBody TaskComment taskComment,
			HttpServletResponse response) throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		if (myTask == null)
			return null;
		taskComment.setTask(taskS.getById(task_id));
		return taskCS.create(taskComment);
	}

	@PutMapping("/me/profile/tasks/{task_id}/comments/{comment_id}")
	public TaskComment editTaskComment(@PathVariable Long task_id, @PathVariable Long comment_id,
			@RequestBody TaskComment taskComment, HttpServletResponse response) throws IOException {
		TaskComment myTaskComment = myTaskCommentNotFound(response, task_id, comment_id);
		if (myTaskComment == null)
			return null;
		return taskCS.edit(comment_id, taskComment);
	}

	@PatchMapping("/me/profile/tasks/{task_id}/comments/{comment_id}")
	public TaskComment updatePartialTaskComment(@PathVariable Long task_id, @PathVariable Long comment_id,
			@RequestBody TaskComment taskComment, HttpServletResponse response) throws IOException {
		TaskComment myTaskComment = myTaskCommentNotFound(response, task_id, comment_id);
		if (myTaskComment == null)
			return null;
		return taskCS.updatePartial(comment_id, taskComment);
	}

	@DeleteMapping("/me/profile/tasks/{task_id}/comments/{comment_id}")
	public void deleteTaskComment(@PathVariable Long task_id, @PathVariable Long comment_id,
			HttpServletResponse response) throws IOException {
		TaskComment myTaskComment = myTaskCommentNotFound(response, task_id, comment_id);
		if (myTaskComment == null)
			return;
		taskCS.delete(myTaskComment.getId());
		sendOk(response, "Operação concluída com sucesso.");
	}

	/********************************* HISTORY */
	@GetMapping("/me/profile/tasks/{task_id}/history")
	public List<TaskHistory> taskHistoryList(@PathVariable Long task_id, HttpServletResponse response)
			throws IOException {
		return taskHistoryListNotFound(response, task_id);
	}

	@GetMapping("/me/profile/tasks/{task_id}/history/{history_id}")
	public TaskHistory getTaskHistoryById(@PathVariable Long task_id, @PathVariable Long history_id,
			HttpServletResponse response) throws IOException {
		return myTaskHistoryNotFound(response, task_id, history_id);
	}

	@PostMapping("/me/profile/tasks/{task_id}/history")
	public TaskHistory createTaskHistory(@PathVariable Long task_id, @RequestBody TaskHistory taskHistory,
			HttpServletResponse response) throws IOException {
		Tasks myTask = taskNotFound(response, task_id);
		if (myTask == null)
			return null;
		taskHistory.setTask(taskS.getById(task_id));
		return taskHS.create(taskHistory);
	}

	@PutMapping("/me/profile/tasks/{task_id}/history/{history_id}")
	public TaskHistory editTaskHistory(@PathVariable Long task_id, @PathVariable Long history_id,
			@RequestBody TaskHistory taskHistory, HttpServletResponse response) throws IOException {
		TaskHistory myTaskHistory = myTaskHistoryNotFound(response, task_id, history_id);
		if (myTaskHistory == null)
			return null;
		return taskHS.edit(history_id, taskHistory);
	}

	@PatchMapping("/me/profile/tasks/{task_id}/history/{history_id}")
	public TaskHistory updatePartialTaskHistory(@PathVariable Long task_id, @PathVariable Long history_id,
			@RequestBody TaskHistory taskHistory, HttpServletResponse response) throws IOException {
		TaskHistory myTaskHistory = myTaskHistoryNotFound(response, task_id, history_id);
		if (myTaskHistory == null)
			return null;
		return taskHS.updatePartial(history_id, taskHistory);
	}

	@DeleteMapping("/me/profile/tasks/{task_id}/history/{history_id}")
	public void deleteTaskHistory(@PathVariable Long task_id, @PathVariable Long history_id,
			HttpServletResponse response) throws IOException {
		TaskHistory myTaskHistory = myTaskHistoryNotFound(response, task_id, history_id);
		if (myTaskHistory == null)
			return;
		taskHS.delete(myTaskHistory.getId());
		sendOk(response, "Operação concluída com sucesso.");
	}
}
