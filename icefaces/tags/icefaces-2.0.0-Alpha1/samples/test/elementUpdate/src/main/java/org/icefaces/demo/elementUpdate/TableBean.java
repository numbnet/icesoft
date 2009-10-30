package org.icefaces.demo.elementUpdate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Random;

@ManagedBean(name = "TableBean")
@SessionScoped
public class TableBean {
    public int a, b, c, d, e, f;

    public TableBean() {
        generateIDs();
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public int getE() {
        return e;
    }

    public int getF() {
        return f;
    }

    public void generateIDs() {
        Random r = new Random();
        a = r.nextInt();
        b = r.nextInt();
        c = r.nextInt();
        d = r.nextInt();
        e = r.nextInt();
        f = r.nextInt();
    }
}
