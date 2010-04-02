package org.icefaces.component.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime extends DateTimeBase{

    public Object getValue() {
      Date currentTime = new Date(System.currentTimeMillis());
      Object format = this.getAttributes().get("format");
      String value = currentTime.toString();
      if (format != null) {
          SimpleDateFormat dFormat = new SimpleDateFormat(format.toString());
          value = dFormat.format(currentTime);
      }
      return value;
    }
}
