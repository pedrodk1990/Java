package com.protifolio.taskkiller.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonMerge;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonMerge
	private LocalDateTime date;
	@Enumerated(EnumType.STRING) // Guarda os valores como STRING
	@JsonMerge
	private TaskFrequency frequency;
	@JsonMerge
	private String title;
	@JsonMerge
	private String description;
	@Transient//variavel temporaria
	private String oldDescription;
	@Enumerated(EnumType.STRING)
	@JsonMerge
	private TaskStatus status;
	// Uma task pertence a um unico profile
	@ManyToOne
	@JoinColumn(name = "profile_id")
	@JsonBackReference
	private Profile profile;
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskComment> taskComments = new ArrayList<>();
	@OneToMany(mappedBy = "task", cascade = {CascadeType.ALL, CascadeType.PERSIST}, orphanRemoval = true)
	private List<TaskHistory> history = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public TaskFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(TaskFrequency frequency) {
		this.frequency = frequency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<TaskComment> getComments() {
		return taskComments;
	}

	public void setComments(List<TaskComment> taskComments) {
		this.taskComments = taskComments;
	}

	public List<TaskHistory> getHistory() {
		return history;
	}

	public void setHistory(List<TaskHistory> histoy) {
		this.history = histoy;
	}
	@PrePersist //Armazena o valor da description antes da entidade ser salva pela primeira vez
    public void prePersist() {
        this.oldDescription = this.description; // Armazena a descrição inicial
    }
	@PreUpdate//antes da atualização, verifica se a description mudou e adiciona um novo registro em taskhistory
	public void preUpdate() {
		if (!this.description.equals(oldDescription)) {
			TaskHistory h = new TaskHistory();
			h.setOldDescription(oldDescription);
			h.setNewDescription(description);
			h.setUpdateData(LocalDateTime.now());
			h.setTask(this);

			history.add(h);
//			taskHistoryRepository.save(history);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOldDescription() {
		return oldDescription;
	}

	public void setOldDescription(String oldDescription) {
		this.oldDescription = oldDescription;
	}
}
