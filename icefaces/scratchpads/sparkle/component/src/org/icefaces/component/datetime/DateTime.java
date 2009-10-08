package org.icefaces.component.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime extends DateTimeBase{

    public Object getValue() {
      Date date = new Date();
      Object format = this.getAttributes().get("format");
      String value = date.toString();
      if (format != null) {
          SimpleDateFormat dFormat = new SimpleDateFormat(format.toString());
          value = dFormat.format(date);
      }
      System.out.println("partialSubmit "+ this.getAttributes().get("partialSubmit"));
      return value;
    }
}
