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
@Table(name = "demand")
public class Demand {
	private UUID id;
	private Student student;
	private Course course;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	public Student getStudent() { return student; }
	public void setStudent(Student student) { this.student = student; }
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "course_id", nullable = false)
	public Course getCourse() { return course; }
	public void setCourse(Course course) { this.course = course; }
	
	@Override
	public String toString() {
		return getStudent() + " for " + getCourse();
	}
}
