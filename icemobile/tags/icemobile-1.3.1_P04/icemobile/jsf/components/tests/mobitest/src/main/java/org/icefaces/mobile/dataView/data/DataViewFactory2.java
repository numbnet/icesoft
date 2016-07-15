package org.icefaces.dataView.data;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class DataViewFactory2 implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static List<Car> CARS = generateCars(12);
    public static List<CarOrder> CARORDER = generateCarOrders(12);

	private static List<Car> generateCars(int count) {
        List<Car> carList = new ArrayList<Car>(count);
        int id = 1;

        // Generate some hardcoded cars
        carList.add(new Car(id++, "Agera R", "Super", 5722, 15, 10.96, 4268.29));
        carList.add(new Car(id++, "E63 AMG", "Sport Luxury", 2400, 5, 14.94, 4498.00));
        carList.add(new Car(id++, "GT-R", "Sport Coupe", 3840, 99, 3.43, 14216.00));
        carList.add(new Car(id++, "R8", "Super", 4575, 10, 7.12, 10150.00));
        carList.add(new Car(id++, "Eclipse", "", 722, 15, 20.96, 6228.20));
        carList.add(new Car(id++, "CLS63 AMG", "Executive Sedan", 5280, 5, 17.43, 12100.00));
        carList.add(new Car(id++, "Prelude", "Sport Coupe", 6190, 2, 3.43, 9150.00));
        carList.add(new Car(id++, "Maxima", "Full Size", 1025, 45, 14.98, 20450.00));
        carList.add(new Car(id++, "M3", "Sport Sedan", 19025, 5, 14.98, 104250.00));
        carList.add(new Car(id++, "X918 Spyder", "Sport Coupe", 6600, 5, 9.59, 14600.00));
        carList.add(new Car(id++, "RS6", "Executive Sedan", 16190, 5, 9.43, 98350.00));
        carList.add(new Car(id++, "Supra", "Sport Coupe", 5405, 5, 5.68, 14110.00));
        return carList;
	}

    private static List<CarOrder> generateCarOrders(int count) {
        List<CarOrder> carOrderList = new ArrayList<CarOrder>(count);
        int id = 1;
        Calendar cal = Calendar.getInstance();
        // Generate some hardcoded car orders
        cal.set(2013,0,23);
        carOrderList.add(new CarOrder(id++, "", 4268.29, true, new Date(cal.getTimeInMillis()), "resource/AgeraR.png"));
        cal.set(2013,1,14);
        carOrderList.add(new CarOrder(id++, "", 4498.00, false, new Date(cal.getTimeInMillis()), "resource/e63AMG.png"));
        cal.set(2013,2,17);
        carOrderList.add(new CarOrder(id++, "", 14216.00, true, new Date(cal.getTimeInMillis()), "resource/GT-R.png"));
        cal.set(2013,3,24);
        carOrderList.add(new CarOrder(id++, "", 10150.00, false, new Date(cal.getTimeInMillis()), "resource/R8.png"));
        cal.set(2013,4,2);
        carOrderList.add(new CarOrder(id++, "", 6228.20, true, new Date(cal.getTimeInMillis()), "resource/ComingSoon.png"));
        cal.set(2013,5,9);
        carOrderList.add(new CarOrder(id++, "", 12100.00, false, new Date(cal.getTimeInMillis()), "resource/CLS63AMG.png"));
        cal.set(2013,6,28);
        carOrderList.add(new CarOrder(id++, "", 9150.00, true, new Date(cal.getTimeInMillis()), "resource/ComingSoon.png"));
        cal.set(2013,7,5);
        carOrderList.add(new CarOrder(id++, "", 20450.00, false, new Date(cal.getTimeInMillis()), "resource/ComingSoon.png"));
        cal.set(2013,8,11);
        carOrderList.add(new CarOrder(id++, "", 104250.00, false, new Date(cal.getTimeInMillis()), "resource/M3.png"));
        cal.set(2013,9,6);
        carOrderList.add(new CarOrder(id++, "", 14600.00, true, new Date(cal.getTimeInMillis()), "resource/918.png"));
        cal.set(2013,10,15);
        carOrderList.add(new CarOrder(id++, "", 98350.00, true, new Date(cal.getTimeInMillis()), "resource/RS6.png"));
        cal.set(2013,11,31);
        carOrderList.add(new CarOrder(id++, "", 14110.00, false, new Date(cal.getTimeInMillis()), "resource/ComingSoon.png"));
        return carOrderList;
    }
}
