package com.protifolio.taskkiller.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonMerge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonMerge
	private String oldDescription;
	@JsonMerge
	private String newDescription;
	private LocalDateTime updateData;
	@ManyToOne
	@JoinColumn(name="task_id")
	@JsonBackReference
	private Tasks task;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOldDescription() {
		return oldDescription;
	}
	public void setOldDescription(String oldDescription) {
		this.oldDescription = oldDescription;
	}
	public String getNewDescription() {
		return newDescription;
	}
	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}
	public LocalDateTime getUpdateData() {
		return updateData;
	}
	public void setUpdateData(LocalDateTime updateData) {
		this.updateData = updateData;
	}
	public Tasks getTask() {
		return task;
	}
	public void setTask(Tasks task) {
		this.task = task;
	}
	
}
