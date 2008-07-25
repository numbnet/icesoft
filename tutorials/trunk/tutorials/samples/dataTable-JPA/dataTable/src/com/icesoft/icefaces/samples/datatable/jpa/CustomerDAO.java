package com.icesoft.icefaces.samples.datatable.jpa;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;

import org.hibernate.CacheMode;


/**
 * Data access object (DAO) for domain model class Customer.
 * 
 * @see org.icefaces.jpa.Customer
 * @author MyEclipse Persistence Tools
 */

public class CustomerDAO implements ICustomerDAO{
	// property constants
	public static final String CUSTOMERNAME = "customername";
	public static final String CONTACTLASTNAME = "contactlastname";
	public static final String CONTACTFIRSTNAME = "contactfirstname";
	public static final String PHONE = "phone";
	public static final String ADDRESSLINE1 = "addressline1";
	public static final String ADDRESSLINE2 = "addressline2";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String POSTALCODE = "postalcode";
	public static final String COUNTRY = "country";
	public static final String SALESREPEMPLOYEENUMBER = "salesrepemployeenumber";
	public static final String CREDITLIMIT = "creditlimit";
    
	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
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

	public void delete(Customer persistentInstance) {
		EntityManagerHelper.log("deleting Customer instance", Level.INFO, null);
		try {
			getEntityManager().remove(persistentInstance);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
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

	public Customer findById(Integer id) {
		EntityManagerHelper.log("finding Customer instance with id: " + id,
				Level.INFO, null);
		try {
			Customer instance = getEntityManager().find(Customer.class, id);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Customer> findByProperty(String propertyName, Object value) {
		EntityManagerHelper.log("finding Customer instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			String queryString = "select model from Customer model where model."
					+ propertyName + "= :propertyValue";
			return getEntityManager().createQuery(queryString).setParameter(
					"propertyValue", value).getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find by property name failed",
					Level.SEVERE, re);
			;
			throw re;
		}
	}

	public List<Customer> findByCustomername(Object customername) {
		return findByProperty(CUSTOMERNAME, customername);
	}

	public List<Customer> findByContactlastname(Object contactlastname) {
		return findByProperty(CONTACTLASTNAME, contactlastname);
	}

	public List<Customer> findByContactfirstname(Object contactfirstname) {
		return findByProperty(CONTACTFIRSTNAME, contactfirstname);
	}

	public List<Customer> findByPhone(Object phone) {
		return findByProperty(PHONE, phone);
	}

	public List<Customer> findByAddressline1(Object addressline1) {
		return findByProperty(ADDRESSLINE1, addressline1);
	}

	public List<Customer> findByAddressline2(Object addressline2) {
		return findByProperty(ADDRESSLINE2, addressline2);
	}

	public List<Customer> findByCity(Object city) {
		return findByProperty(CITY, city);
	}

	public List<Customer> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List<Customer> findByPostalcode(Object postalcode) {
		return findByProperty(POSTALCODE, postalcode);
	}

	public List<Customer> findByCountry(Object country) {
		return findByProperty(COUNTRY, country);
	}

	public List<Customer> findBySalesrepemployeenumber(
			Object salesrepemployeenumber) {
		return findByProperty(SALESREPEMPLOYEENUMBER, salesrepemployeenumber);
	}

	public List<Customer> findByCreditlimit(Object creditlimit) {
		return findByProperty(CREDITLIMIT, creditlimit);
	}

	@SuppressWarnings("unchecked")
	public List<Customer> findPageCustomers(String sortColumnName, boolean sortAscending, int startRow, int maxResults) {
		EntityManagerHelper.log("finding page Customer instances", Level.INFO,
				null);
		try {
			String queryString = "select c from Customer c order by c." + sortColumnName + " " + (sortAscending?"asc":"desc");
			// Hint required to force update from database, which prevents stale
			// data being assigned from the l2 cache.
			return getEntityManager().createQuery(queryString).setFirstResult(startRow).setMaxResults(maxResults).setHint("org.hibernate.cacheable", new Boolean(false)).setHint("org.hibernate.cacheMode", CacheMode.REFRESH).getResultList(); 
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find all failed", Level.SEVERE, re);
			;
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
			;
			throw re;
		}
	}

}