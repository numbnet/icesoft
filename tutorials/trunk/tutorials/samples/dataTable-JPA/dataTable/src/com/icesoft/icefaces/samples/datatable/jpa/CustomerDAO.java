package com.icesoft.icefaces.samples.datatable.jpa;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;


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

	@SuppressWarnings("unchecked")
	public List<Customer> findPageCustomers(String sortColumnName, boolean sortAscending, int startRow, int maxResults) {
		EntityManagerHelper.log("finding page Customer instances", Level.INFO,
				null);
		try {
			String queryString = "select c from Customer c order by c." + sortColumnName + " " + (sortAscending?"asc":"desc");
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
			return (Long)getEntityManager().createQuery(queryString).getSingleResult();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find number of Customers failed",
					Level.SEVERE, re);
			throw re;
		}
	}

}