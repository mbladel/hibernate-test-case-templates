package org.hibernate.bugs;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "major")
public class Major {
	private UUID id;
	private String name;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	
	@Column(name = "name")
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	@Override
	public String toString() {
		return name;
	}
}
