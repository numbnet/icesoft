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

package com.icesoft.icefaces.samples.datatable.jpa;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Level;


/**
 * Data access object (DAO) for domain model class Customer.
 *
 * @see com.icesoft.icefaces.samples.datatable.jpa.Customer
 */

public class CustomerDAO {

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    public Customer update(Customer detachedInstance) {
        EntityManagerHelper.log("updating Customer instance", Level.INFO, null);
        try {
            Customer result = getEntityManager().merge(detachedInstance);
            EntityManagerHelper.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            EntityManagerHelper.log("update failed", Level.SEVERE, re);
            throw re;
        }
    }

	public void delete(Customer persistentInstance) {
		EntityManagerHelper.log("deleting Customer instance", Level.INFO, null);
		try {
			getEntityManager().remove(getEntityManager().merge(persistentInstance));
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(Customer transientInstance) {
		EntityManagerHelper.log("saving Customer instance", Level.INFO, null);
		try {
			getEntityManager().persist(transientInstance);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

    @SuppressWarnings("unchecked")
    public List<Customer> findPageCustomers(String sortColumnName, boolean sortAscending, int startRow, int maxResults) {
        EntityManagerHelper.log("finding page Customer instances", Level.INFO,
                null);
        try {
            String queryString = "select c from Customer c order by c." + sortColumnName + " " + (sortAscending ? "asc" : "desc");
            // In the next statement, a Hint is required with TopLink to force
            // an update from the database.
            // This prevents stale data being assigned from the l2 cache when we
            // push updates out from the server.
            // With Hibernate, we refresh the l1 cache by calling
            // EntityManagerHelper.getEntityManager().clear() in the SessionBean.
            return getEntityManager().createQuery(queryString).setFirstResult(startRow).setMaxResults(maxResults).getResultList();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find all failed", Level.SEVERE, re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public Long findTotalNumberCustomers() {
        EntityManagerHelper.log("finding number of Customer instances", Level.INFO, null);
        try {
            String queryString = "select count(c) from Customer c";
            return (Long) getEntityManager().createQuery(queryString).getSingleResult();
        } catch (RuntimeException re) {
            EntityManagerHelper.log("find number of Customers failed",
                    Level.SEVERE, re);
            throw re;
        }
    }

}