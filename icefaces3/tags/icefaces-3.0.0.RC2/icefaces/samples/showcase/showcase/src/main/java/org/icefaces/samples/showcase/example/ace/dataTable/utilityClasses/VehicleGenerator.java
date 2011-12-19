/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.icefaces.samples.showcase.example.ace.dataTable.utilityClasses;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;


public class VehicleGenerator implements Serializable
{
    private List<String> namesPool;
    private List<String> chassisPool;
    private Random randomizer = new Random(System.nanoTime());
    private NumberFormat numberFormatter;
    

    public VehicleGenerator() 
    {
        this.namesPool = getVehicleDescriptions();
        this.chassisPool = getChassisDescriptions();
        this.randomizer = new Random(System.nanoTime());
        this.numberFormatter = makeFormatter();
    }
    
    
    
    public Car getRandomCar()
    {
        
        Car randomCar = new Car(randomizer.nextInt(10000000),
                                  generateName(),
                                  generateChassis(),
                                  generateWeight(),
                                  getAccelerationValue(),
                                  generateMPG(),
                                  generateCost());
        
        return randomCar;
    }
    
    public ArrayList<Car> getRandomCars(int quantity)
    {
        ArrayList<Car> listWithRandomCars = new ArrayList<Car>(quantity);
        for (int i = 0; i < quantity; i++) 
        {
            Car randomCar = getRandomCar();
            listWithRandomCars.add(randomCar);
        }
        return listWithRandomCars;
    }
    
    
    private List<String> getVehicleDescriptions() 
    {
        List<String> listWithNames = new ArrayList<String>();
        listWithNames.add("Spider");
        listWithNames.add("Hawk");
        listWithNames.add("Tomcat");
        listWithNames.add("Gazelle");
        listWithNames.add("Mantis");
        listWithNames.add("Flash");
        listWithNames.add("Iguana");
        listWithNames.add("Swordfish");
        listWithNames.add("Rattler");
        listWithNames.add("Courier");
        listWithNames.add("Pisces");
        listWithNames.add("Superflash");
        listWithNames.add("Doublecharge");
        listWithNames.add("Dart");
        listWithNames.add("Enduro");
        listWithNames.add("King Crab");
        listWithNames.add("Vanguard");
        listWithNames.add("Camel");
        listWithNames.add("Husky");
        return listWithNames;
    }

    private List<String> getChassisDescriptions() 
    {
        List<String> listWithNames = new ArrayList<String>();
        listWithNames.add("Motorcycle");
        listWithNames.add("Subcompact");
        listWithNames.add("Mid-Size");
        listWithNames.add("Luxury");
        listWithNames.add("Station Wagon");
        listWithNames.add("Pickup");
        listWithNames.add("Van");
        listWithNames.add("Bus");
        listWithNames.add("Semi-Truck");
        return listWithNames;
    }
    
    
    private NumberFormat makeFormatter() 
    {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter;
    }
    
    private String generateName() 
    {
       String name = namesPool.get(randomizer.nextInt(namesPool.size()));
       return name;
    }
	
    private String generateChassis() 
    {
        String chassis = chassisPool.get(randomizer.nextInt(chassisPool.size()));
        return chassis;
    }
    
    private int generateWeight() 
    {
        return 1000+randomizer.nextInt(15000);
    }
	
    private int getAccelerationValue() 
    {
        return (1+randomizer.nextInt(3)) * 5;
    }
	
    private double generateMPG() 
    {
        return Double.parseDouble(numberFormatter.format( ((double)(3+randomizer.nextInt(15))) + randomizer.nextDouble()));
    }
	
    private double generateCost() 
    {
        
        return Double.parseDouble(numberFormatter.format( ((double)(2000+randomizer.nextInt(40000))) + randomizer.nextDouble()) );
    }

    public List<String> getChassisPool() {
        return chassisPool;
    }

    public List<String> getNamesPool() {
        return namesPool;
    }
}
