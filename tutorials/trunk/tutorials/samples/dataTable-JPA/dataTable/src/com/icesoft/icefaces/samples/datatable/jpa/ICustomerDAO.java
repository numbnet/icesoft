package org.icefaces.jpa;

import java.util.List;

/**
 * Interface for CustomerDAO.
 * 
 * @author MyEclipse Persistence Tools
 */

public interface ICustomerDAO {
	public void save(Customer transientInstance);

	public void delete(Customer persistentInstance);

	public Customer update(Customer detachedInstance);

	public Customer findById(Integer id);

	public List<Customer> findByProperty(String propertyName, Object value);

	public List<Customer> findByCustomername(Object customername);

	public List<Customer> findByContactlastname(Object contactlastname);

	public List<Customer> findByContactfirstname(Object contactfirstname);

	public List<Customer> findByPhone(Object phone);

	public List<Customer> findByAddressline1(Object addressline1);

	public List<Customer> findByAddressline2(Object addressline2);

	public List<Customer> findByCity(Object city);

	public List<Customer> findByState(Object state);

	public List<Customer> findByPostalcode(Object postalcode);

	public List<Customer> findByCountry(Object country);

	public List<Customer> findBySalesrepemployeenumber(
			Object salesrepemployeenumber);

	public List<Customer> findByCreditlimit(Object creditlimit);

	public List<Customer> findPageCustomers(String sortColumnName, boolean sortAscending, int startRow, int maxResults);
}