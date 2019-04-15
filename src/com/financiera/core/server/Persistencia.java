package com.financiera.core.server;

import org.hibernate.Session;

public class Persistencia {
	PersistenceService persistenceService = (PersistenceService) ServiceLocator.getInstance().getService(PersistenceService.class);

	static public Session getSession() {
		PersistenceService persistenceService = (PersistenceService) ServiceLocator.getInstance().getService(PersistenceService.class);
		Session session = persistenceService.getSessionFactory().getCurrentSession();
		return session;
	}
	static public void clearSession() {
		PersistenceService persistenceService = (PersistenceService) ServiceLocator.getInstance().getService(PersistenceService.class);
		persistenceService.clearSessionFactory();
	}
//	static public void clearSessionReal() {
//		PersistenceService persistenceService = (PersistenceService) ServiceLocator.getInstance().getService(PersistenceService.class);
//		persistenceService.clearSessionFactory();
//	}
	static public Session abrirSesion() {
		PersistenceService persistenceService = (PersistenceService) ServiceLocator.getInstance().getService(PersistenceService.class);
		return persistenceService.getSessionFactory().openSession();
	}
	static public void cerrarSesion(Session s) {
		s.close();
	}
}
