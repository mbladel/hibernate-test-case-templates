package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void addOrphanNodeInBetweenTest() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// ()
		{
			entityManager.getTransaction().begin();
			Node a = node( "a" );
			Node b = node( "b" );
			bind( a, b );
			entityManager.persist( a );
			entityManager.getTransaction().commit();
		}

		// (a (b))
		{
			entityManager.getTransaction().begin();
			Node a = entityManager.find( Node.class, "a" );
			Node b = entityManager.find( Node.class, "b" );
			unbind( a, b );
			Node x = node( "x" );
			bind( a, x );
			bind( x, b );
			entityManager.getTransaction().commit();
		}

		// (a (x (b)))
		{
			entityManager.getTransaction().begin();
			Node a = entityManager.find(Node.class, "a");
			Node b = entityManager.find(Node.class, "b");
			Node x = entityManager.find(Node.class, "x");
			assertNotNull(a);
			assertNotNull(b);
			assertNotNull(x);
			entityManager.getTransaction().commit();
		}

		entityManager.close();
	}

	private Node node(String id) {
		Node node = new Node();
		node.setId(id);
		node.setParent(null);
		node.setChildren(new HashSet<>());
		return node;
	}

	private void bind(Node parent, Node child) {
		parent.getChildren().add(child);
		child.setParent(parent);
	}

	private void unbind(Node parent, Node child) {
		parent.getChildren().remove(child);
		child.setParent(null);
	}

	@Entity
	public static class Node {
		@Id
		private String id;
		@ManyToOne
		private Node parent;
		@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
		private Set<Node> children;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public Set<Node> getChildren() {
			return children;
		}

		public void setChildren(Set<Node> children) {
			this.children = children;
		}
	}
}
