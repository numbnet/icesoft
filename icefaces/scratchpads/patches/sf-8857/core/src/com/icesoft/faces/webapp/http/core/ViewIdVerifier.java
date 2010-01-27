package com.icesoft.faces.webapp.http.core;

public class ViewIdVerifier {

    public static boolean isValid(String s) {
        try {
            int i = Integer.parseInt(s);
            return i > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
