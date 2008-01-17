package com.icesoft.faces.env;

public interface AuthenticationVerifier {

    boolean isUserInRole(String role);
    boolean isReusable();
}
