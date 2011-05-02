package com.icesoft.icefaces.samples.taxi;

/**
 * Screens manager is a central place where the default dynamic include paths
 * can be found.  The application runs on only one URL, different content is
 * substituted in via dynamic includes.
 */
public class ScreensManager {

    public static final Screen LOGIN_INCLUDE = new Screen(
            "/WEB-INF/inc-common/login.jspx", "Mobile AJAX - Taxi Login");

    public static final Screen CLIENT_INCLUDE = new Screen(
            "/WEB-INF/inc-common/clientContent.jspx", "Mobile AJAX - Request a Taxi");

    public static final Screen REQUEST_VIEW_INCLUDE = new Screen(
            "/WEB-INF/inc-common/taxicabRequestsContent.jspx", "Mobile AJAX - Pickup Requests");

    public static final Screen REQUEST_DETAILS_INCLUDE = new Screen(
            "/WEB-INF/inc-common/taxicabRequestDetailContent.jspx", "Mobile AJAX - Pickup Details");

    private Screen currentContentScreen;


    public Screen getCurrentContentScreen() {
        return currentContentScreen;
    }

    public void setCurrentContenScreen(Screen currentContentInclude) {
        this.currentContentScreen = currentContentInclude;
    }

    public static class Screen {

        private String include;
        private String title;

        public Screen(String include, String title) {
            this.include = include;
            this.title = title;
        }

        public String getInclude() {
            return include;
        }

        public void setInclude(String include) {
            this.include = include;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
