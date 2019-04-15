package com.financiera.core.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PersistenceService extends AbstractService {
	//Hibernate session factory
	private SessionFactory sessionFactory;
	private Configuration configuration;
	
	public PersistenceService(){
		this("com/financiera/core/conf/hibernate.cfg.xml");
	}
	
	public PersistenceService(String configurationLocation) {
		super();
		configuration = new Configuration();
		configuration.configure(configurationLocation);
		this.sessionFactory = configuration.buildSessionFactory();
	}	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void clearSessionFactory() {
		this.sessionFactory.close();
		this.sessionFactory = configuration.buildSessionFactory();
	}
}