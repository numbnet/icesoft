package org.icefaces.samples.showcase.example.compat.dataTable;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.text.NumberFormat;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

@ManagedBean(name= DataTableData.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableData implements Serializable {
    
	public static final String BEAN_NAME = "tableData";
	
	public static final int DEFAULT_ROWS = 8;
	private static final List<String> NAMES_ALL = generateNames();
	private static List<String> NAMES_CURRENT = new ArrayList<String>(NAMES_ALL);
	private static final String[] CHASSIS_ALL = new String[] {
	    "Motorcycle", "Subcompact", "Mid-Size", "Luxury",
	    "Station Wagon", "Pickup", "Van", "Bus", "Semi-Truck"
	};
	private static final NumberFormat DOUBLE_FORMAT = makeFormatter();
	
	private static Random randomizer = new Random(System.nanoTime());
	public String[] getChassisAll() { return CHASSIS_ALL; }
	public static List<Car> CARS = generateCars(30);
	
	public int getDefaultRows() { return DEFAULT_ROWS; }
	public List<Car> getCars() { return CARS; }
	
	private static NumberFormat makeFormatter() {
	    NumberFormat toReturn = NumberFormat.getInstance();
	    
	    toReturn.setGroupingUsed(false);
	    toReturn.setMinimumFractionDigits(2);
	    toReturn.setMaximumFractionDigits(2);
	    
	    return toReturn;
	}
	
	public static List<Car> addRandomCars(int id, List<Car> addTo, int number) {
	    if (number > 0) {
            for (int i = 0; i < number; i++) {
                addTo.add(new Car(id+i,
                                  generateName(),
                                  generateChassis(),
                                  generateWeight(),
                                  generateAcceleration(),
                                  generateMPG(),
                                  generateCost()));
            }
        }
        
        return addTo;
	}
	
	private static List<Car> generateCars(int count) {
	    List<Car> toReturn = new ArrayList<Car>(count);
	    int id = 1;
	    
	    // Generate some hardcoded cars
	    toReturn.add(new Car(id++, "Yellowjacket", "Subcompact",
	                         2400, 5, generateMPG(), 4498.00));
	    toReturn.add(new Car(id++, "Iron Horse", "Mid-Size",
	                         5760, 5, generateMPG(), 14216.00));
	    toReturn.add(new Car(id++, "Hotshot", "Luxury",
	                         6600, 5, generateMPG(), 14600.00));
	    toReturn.add(new Car(id++, "Rockwell", "Station Wagon",
	                         4575, 10, generateMPG(), 10150.00));
	    toReturn.add(new Car(id++, "Hauler", "Pickup",
	                         5405, 5, generateMPG(), 14110.00));
	    toReturn.add(new Car(id++, "Vacationer", "Van",
	                         5280, 5, generateMPG(), 12100.00));
	    toReturn.add(new Car(id++, "Baron", "Bus",
	                         19025, 5, generateMPG(), 104250.00));
	    toReturn.add(new Car(id++, "Wolverine", "Semi-Truck",
	                         16190, 5, generateMPG(), 98350.00));
	    
	    // Generate random items if more are needed
	    if (count > toReturn.size()) {
	        addRandomCars(id, toReturn, (count - toReturn.size()));
        }
	    
	    return toReturn;
	}
	
	private static List<String> generateNames() {
	    List<String> toReturn = new ArrayList<String>(10);
	    
	    toReturn.add("Spider");
	    toReturn.add("Hawk");
	    toReturn.add("Tomcat");
	    toReturn.add("Gazelle");
	    toReturn.add("Mantis");
	    toReturn.add("Flash");
	    toReturn.add("Iguana");
	    toReturn.add("Swordfish");
	    toReturn.add("Rattler");
	    toReturn.add("Courier");
	    toReturn.add("Pisces");
	    toReturn.add("Superflash");
	    toReturn.add("Doublecharge");
	    toReturn.add("Dart");
	    toReturn.add("Enduro");
	    toReturn.add("King Crab");
	    toReturn.add("Vanguard");
	    toReturn.add("Camel");
	    toReturn.add("Husky");
	    
	    return toReturn;
	}
	
	private static String generateName() {
	    String toReturn = NAMES_CURRENT.remove(randomizer.nextInt(NAMES_CURRENT.size()));
	    
	    if (NAMES_CURRENT.size() <= 0) {
	        NAMES_CURRENT = new ArrayList<String>(NAMES_ALL);
	    }
	    
	    return toReturn;
	}
	
	private static String generateChassis() {
	    return CHASSIS_ALL[randomizer.nextInt(CHASSIS_ALL.length-1)];
	}
	
	private static int generateWeight() {
	    return 1000+randomizer.nextInt(15000);
	}
	
	private static int generateAcceleration() {
	    return (1+randomizer.nextInt(3)) * 5;
	}
	
	private static double generateMPG() {
	    return Double.parseDouble(DOUBLE_FORMAT.format(
	                ((double)(3+randomizer.nextInt(15))) + randomizer.nextDouble()));
	}
	
	private static double generateCost() {
	    return Double.parseDouble(DOUBLE_FORMAT.format(
	                ((double)(2000+randomizer.nextInt(40000))) + randomizer.nextDouble()));
	}
}
