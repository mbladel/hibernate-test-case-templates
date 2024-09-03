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
@Table(name = "student_major")
public class StudentMajor {
	private UUID id;
	private Student student;
	private Major major;
	private String classification;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	
	@Column(name = "classification")
	public String getClassification() { return classification; }
	public void setClassification(String classification) { this.classification = classification; }
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	public Student getStudent() { return student; }
	public void setStudent(Student student) { this.student = student; }

	
	@ManyToOne(optional = false)
	@JoinColumn(name = "major_id", nullable = false)
	public Major getMajor() { return major; }
	public void setMajor(Major major) { this.major = major; }

		
	@Override
	public String toString() {
		return getMajor().getName() + " " + getClassification();
	}
}
