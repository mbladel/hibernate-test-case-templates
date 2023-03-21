package org.hibernate.bugs;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		ChatChannel chatChannel = new ChatChannel();
		chatChannel.setName("ChatChannel");
		entityManager.persist(chatChannel);

		ChannelMessage channelMessage = new ChannelMessage();
		channelMessage.setChannel(chatChannel);
		entityManager.persist(channelMessage);

		MessagePollingQueue messagePollingQueue = new MessagePollingQueue();
		messagePollingQueue.setChannelId(chatChannel.getId());
		messagePollingQueue.setMessage(channelMessage);
		entityManager.persist(messagePollingQueue);


		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		String hql = "FROM MessagePollingQueue";
		Query query = entityManager.createQuery( hql);
		List resultList = query.getResultList();

		Assert.assertEquals( 1, resultList.size());
		MessagePollingQueue messagePollingQueue = (MessagePollingQueue) resultList.get(0);
		Assert.assertEquals(1, messagePollingQueue.getMessage().getId());
	}
}
