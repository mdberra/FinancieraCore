package com.financiera.core;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 * Clase singleton para mantener la SessionFactory de Hibernate
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration cfg = new Configuration();
            cfg.setProperties(System.getProperties());
            sessionFactory = cfg.configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear Initial SessionFactory " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}