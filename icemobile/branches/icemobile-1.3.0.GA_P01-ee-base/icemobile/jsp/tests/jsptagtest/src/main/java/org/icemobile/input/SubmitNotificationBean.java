package org.icemobile.input;


import java.lang.String;

public class SubmitNotificationBean {

    private String style;
    private String styleClass;
    private String testInput;

    public SubmitNotificationBean(){

    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    public String getTestInput() {
        return testInput;
    }

    public void setTestInput(String testInput) {
        try{
            System.out.println("bean is sleeping 3000ms");
            Thread.sleep(3000);
            System.out.println("bean is awake!");
        }  catch (Exception e){
            System.out.println("problem with sleeping thread test");
        }
        this.testInput = testInput;
    }
}
