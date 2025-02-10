package com.protifolio.taskkiller.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonMerge;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonMerge
	private String nome;
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
	private Users user;
	// um perfil tem varias tasks
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tasks> task = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
		if (user != null && user.getProfile() != this.id) {
			user.setProfile(this);
		}
	}

	public List<Tasks> getTask() {
		return task;
	}

	public void setTask(List<Tasks> task) {
		this.task = task;
	}

	public void addTask(Tasks task) {
		task.setProfile(this); // Vincula a task ao perfil
		this.task.add(task);
	}
}
