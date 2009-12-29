package org.icepush.samples.icechat.gwt.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="Test", urlPatterns={"/Test"})
public class ServletTest extends HttpServlet{


    public void doGet(HttpServletRequest req, 
                          HttpServletResponse res) {
                System.out.println("Servlet Hit");
    }

}