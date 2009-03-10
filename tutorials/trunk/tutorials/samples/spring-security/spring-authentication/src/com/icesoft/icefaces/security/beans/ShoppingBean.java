package com.icesoft.icefaces.security.beans;

import java.rmi.RemoteException;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.security.AccessDeniedException;

import com.icesoft.icefaces.security.secure.ShoppingService;


public class ShoppingBean {

	// properties
	private String productId;

	private ShoppingService shoppingService;

	/**
	 * default empty constructor
	 */
	public ShoppingBean() {
	}

	/**
	 * Method that is backed to a submit button of a form.
	 */
	public String send() {
		try {

			shoppingService.buy(productId);
		} catch (AccessDeniedException e) {
			// e.printStackTrace();
			return ("accessDenied");
		} catch (Exception e) {

			Throwable nestedEx = e.getCause();
			if (nestedEx instanceof RemoteException) {
				Throwable ex = nestedEx.getCause();

				if (ex instanceof AccessDeniedException) {
					return ("accessDenied");
				}
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e.getMessage());
			}
		}
		return ("success");
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setShoppingService(ShoppingService shoppingService) {
		this.shoppingService = shoppingService;
	}
	
	public void logout(ActionEvent e) throws java.io.IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("/spring-authentication/j_spring_security_logout.jsp");
	}
}