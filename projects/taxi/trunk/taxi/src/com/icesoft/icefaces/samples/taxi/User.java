package com.icesoft.icefaces.samples.taxi;

/**
 * Base user interface.
 */
public interface User {

    /**
     * Indicates that the user is a client or person riding in the cab
     */
    public static final int TAXI_CLIENT_TYPE = 1;


    /**
     * indicates that the user is the cabbie.
     */
    public static final int TAXI_DRIVER_TYPE = 2;


    /**
     * Gets the type of user.  An instance of check can also be used
     * but integers make a little more sense for JSF EL.
     *
     * @return type of user object, either taxi or client.
     */
    public int getType();


    /**
     * Is the user Logged into the system.
     *
     * @return true if the user is logged in, false otherwise.
     */
    public boolean isLoggedIn();

    /**
     * Sets the logged in state of the user.
     *
     * @param loggedIn set the logged in state of the user
     */
    public void setLoggedIn(boolean loggedIn);


}
