package org.icefaces.component.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIOutput;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

@Component(component_class="org.icefaces.component.datetime.DateTime",
        tag_name="dateTime",
        renderer_class="org.icefaces.component.datetime.DateTimeRenderer"
        )
public class DateTimeAnnotated extends UIOutput{
    public static final String COMPONENT_TYPE = "javax.faces.DateTime";
    public static final String RENDERER_TYPE = "com.icesoft.faces.DateTimeRenderer";
    
    @Property
    protected String format;
    
    public DateTimeAnnotated() {
        super();
        setRendererType(RENDERER_TYPE);
    }
    
    public Object getValue() {
      Date date = new Date();
      Object format = this.getAttributes().get("format");
      String value = date.toString();
      if (format != null) {
          SimpleDateFormat dFormat = new SimpleDateFormat(format.toString());
          value = dFormat.format(date);
      }
      return value;
    }
}
