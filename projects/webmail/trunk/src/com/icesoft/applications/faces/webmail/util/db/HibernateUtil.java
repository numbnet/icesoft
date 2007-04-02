/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.util.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * <p>The <code>HibernateUtil</code> class is a singleton class responsible for
 * for getting an instance of a Hibernate <code>SessionFactory</code>.  This
 * utility class insure that there is only one session factory per application.</p>
 * <p/>
 * <p>The Application's hibernate.cfg.xml must be configured correctly or this
 * class will throw a ExceptionInInitializerError on a connection failure. </p>
 *
 * @see ExceptionInInitializerError
 * @since 0.3.0
 */
public class HibernateUtil {

    private static Log log = LogFactory.getLog(HibernateUtil.class);

    private static final SessionFactory sessionFactory;


    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.error("Error getting Hibernate Session Factory ", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private HibernateUtil() {
    }

    /**
     * Gets an instance of SessionFactory.  This method whould be used to
     * get the current session for a hibernate transaction.
     *
     * @return immutable SessionFactory object.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
