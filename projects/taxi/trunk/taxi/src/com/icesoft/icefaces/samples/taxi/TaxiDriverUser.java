package com.icesoft.icefaces.samples.taxi;

/**
 * The class represents a taxi driver.
 */
public class TaxiDriverUser implements User {

    private boolean loggedIn;

    /**
     * Gets the type of user.  An instance of check can also be used
     * but integers make a little more sense for JSF EL.
     *
     * @return type of user object, either taxi or client.
     */
    public int getType() {
        return TAXI_DRIVER_TYPE;
    }

    /**
     * Is the user Logged into the system.
     *
     * @return true if the user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the logged in state of the user.
     *
     * @param loggedIn set the logged in state of the user
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
