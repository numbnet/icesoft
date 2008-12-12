/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
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
