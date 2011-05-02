package com.icesoft.icefaces.samples.taxi;

import javax.faces.model.SelectItem;

/**
 * <p>Simple login manager control to facilitate the creation of a new user.  The
 * login screen is unconventional.  A user can either login as  taxi driver or
 * client.  Every time they log in a new login object is created, to persistence
 * here.<p>
 * <p/>
 * <p>The UI will fork after the user selection, cabi's will get he client
 * request views and clients will get the simple request a cab screen. </p>
 */
public class UserFactory {

    /**
     * Possible login types.
     */
    private static final int CABBIE_LOGIN = 0;
    private static final int CLIENT_LOGIN = 1;

    private int loginType;

    /**
     * Available options for the various selection components.
     */
    private static final SelectItem[] LOGIN_ITEMS = new SelectItem[]{
            new SelectItem(CABBIE_LOGIN, "Cabbie"),
            new SelectItem(CLIENT_LOGIN, "Client")
    };

    /**
     * Gets the option items for drinks.
     *
     * @return array of drink items
     */
    public SelectItem[] getLoginTypeItems() {
        return LOGIN_ITEMS;
    }

    /**
     * Creates a user based on the login type criteria.
     *
     * @return system user.
     */
    public User getSelectedUser() {
        if (loginType == CABBIE_LOGIN) {
            return new TaxiDriverUser();
        } else if (loginType == CLIENT_LOGIN) {
            return new TaxiClientUser();
        } else {
            return null;
        }
    }


    public int getLoginType() {
        return loginType;
    }

    /**
     * Sets the login type for the factory method getSelectedUser.
     *
     * @param loginType
     */
    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }
}
