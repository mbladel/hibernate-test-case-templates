package org.hibernate.bugs;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class Course {
	private UUID id;
	private String number;
	private Subject subject;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	
	@Column(name = "number")
	public String getNumber() { return number; }
	public void setNumber(String number) { this.number = number; }
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "subject_id", nullable = false)
	public Subject getSubject() { return subject; }
	public void setSubject(Subject subject) { this.subject = subject; }


	@Override
	public String toString() {
		return getSubject() + " " + getNumber();
	}
}
