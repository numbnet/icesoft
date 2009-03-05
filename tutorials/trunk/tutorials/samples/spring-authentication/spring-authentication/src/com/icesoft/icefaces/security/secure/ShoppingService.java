package com.icesoft.icefaces.security.secure;

import org.springframework.security.annotation.Secured;

public interface ShoppingService {
	@Secured({"ROLE_ALLACCESS"})
	public void buy(String productId);
}
