package org.icefaces.samples.showcase.example.compat.series;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

@ManagedBean(name= SeriesData.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SeriesData implements Serializable {
	public static final String BEAN_NAME = "seriesData";
	
	private static Random randomizer = new Random(System.nanoTime());
	public static List<Employee> EMPLOYEES = generateEmployees();
	
	public List<Employee> getEmployees() { return EMPLOYEES; }
	
	private static List<Employee> generateEmployees() {
	    List<Employee> toReturn = new ArrayList<Employee>(10);
	    int baseId = 99;
	    
	    toReturn.add(makeEmployee(++baseId, "Dorothy", "Wentworth", "3517 Lighthouse Drive"));
	    toReturn.add(makeEmployee(++baseId, "Leland", "Carreon", "3939 Smith Street"));
        toReturn.add(makeEmployee(++baseId, "Stevie", "Parodi", "1164 Harper Street"));
        toReturn.add(makeEmployee(++baseId, "Jennie", "Emory", "2489 James Street"));
        toReturn.add(makeEmployee(++baseId, "Claudia", "Brunton", "2116 Brand Road"));
        toReturn.add(makeEmployee(++baseId, "John", "Fears", "4625 5th Avenue"));
        toReturn.add(makeEmployee(++baseId, "Vickie", "Miller", "4021 Kinchant St"));
        toReturn.add(makeEmployee(++baseId, "Gerardo", "Stephens", "452 Alaska Hwy"));
        toReturn.add(makeEmployee(++baseId, "Margaret", "Borchers", "4788 Wentz Avenue"));
        toReturn.add(makeEmployee(++baseId, "Andrew", "Olvera", "1620 Waterton Avenue"));
	    
	    return toReturn;
	}
	
	private static Employee makeEmployee(int id, String fName, String lName, String address) {
	    return new Employee(id,
	                        fName, lName, address,
	                        10000+randomizer.nextInt(80000));
	}
	
	public static boolean empty() {
	    return ((EMPLOYEES == null) || (EMPLOYEES.size() == 0));
	}
	
	public static void defaultEmployees() {
	    EMPLOYEES = generateEmployees();
	}

	public static void addEmployee(Employee employee) {
	    EMPLOYEES.add(employee);
	}
	
	public static Employee removeEmployee(int index) {
        return EMPLOYEES.remove(index);
	}
}
