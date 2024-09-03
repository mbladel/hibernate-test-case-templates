package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
	private UUID id;
	private String name;
	private Set<StudentMajor> majors;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	
	@Column(name = "name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = {CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<StudentMajor> getMajors() { return majors; }	
	public void setMajors(Set<StudentMajor> majors) { this.majors = majors; }
	public void addToMajors(StudentMajor major) {
		if (this.majors == null) this.majors = new HashSet<StudentMajor>();
		this.majors.add(major);
	}
	
	@Override
	public String toString() {
		return getName() + " " + getMajors();
	}
}
