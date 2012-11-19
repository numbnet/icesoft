package org.icefaces.samples.showcase.example.ace.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 8/15/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeDataFactory {
    public static LocationNodeImpl getSingleRoot() {
        LocationNodeImpl[] bcCities = {
                new LocationNodeImpl("Vancouver",   "city",     1),
                new LocationNodeImpl("Kelowna",     "city",     1),
                new LocationNodeImpl("Kamloops",    "city",     1)
        };

        LocationNodeImpl[] abCities = {
                new LocationNodeImpl("Edmonton",    "city",     1),
                new LocationNodeImpl("Calgary",     "city",     1),
                new LocationNodeImpl("Red Deer",    "city",     1)
        };

        LocationNodeImpl[] onCities = {
                new LocationNodeImpl("Toronto",     "city",     1),
                new LocationNodeImpl("Waterloo",    "city",     1),
                new LocationNodeImpl("London",      "city",     1)
        };

        LocationNodeImpl[] mbCities = {
                new LocationNodeImpl("Winnipeg",    "city",     1),
                new LocationNodeImpl("Brandon",     "city",     1),
                new LocationNodeImpl("Churchill",   "city",     1)
        };

        LocationNodeImpl[] skCities = {
                new LocationNodeImpl("Regina",      "city",     1),
                new LocationNodeImpl("Saskatoon",   "city",     1),
                new LocationNodeImpl("Moose Jaw",   "city",     1)
        };

        LocationNodeImpl[] qbCities = {
                new LocationNodeImpl("Quebec City", "city",     1),
                new LocationNodeImpl("Ottawa",      "city",     1),
                new LocationNodeImpl("Montreal",    "city",     1)
        };

        LocationNodeImpl[] nbCities = {
                new LocationNodeImpl("Saint John",  "city",     1),
                new LocationNodeImpl("Moncton",     "city",     1),
                new LocationNodeImpl("Fredericton", "city",     1)
        };

        LocationNodeImpl[] nfCities = {
                new LocationNodeImpl("St. John's",      "city",     1),
                new LocationNodeImpl("Conception Bay",  "city",     1),
                new LocationNodeImpl("Mount Pearl",     "city",     1)
        };

        LocationNodeImpl[] nsCities = {
                new LocationNodeImpl("Halifax",     "city",     1),
                new LocationNodeImpl("Cape Breton", "city",     1),
                new LocationNodeImpl("Truro",       "city",     1)
        };

        LocationNodeImpl[] provinces = {
                new LocationNodeImpl("British Columbia",    "province",    1, bcCities),
                new LocationNodeImpl("Alberta",             "province",    1, abCities),
                new LocationNodeImpl("Saskatchewan",        "province",    1, skCities),
                new LocationNodeImpl("Manitoba",            "province",    1, mbCities),
                new LocationNodeImpl("Ontario",             "province",    1, onCities),
                new LocationNodeImpl("Quebec",              "province",    1, qbCities),
                new LocationNodeImpl("New Brunswick",       "province",    1, nbCities),
                new LocationNodeImpl("Newfoundland",        "province",    1, nfCities),
                new LocationNodeImpl("Nova Scotia",         "province",    1, nsCities),
        };

        return new LocationNodeImpl("Canada", "country", 1, provinces);
    }

    public static List<LocationNodeImpl> getTreeRoots() {
        return new ArrayList<LocationNodeImpl>() {{
            add(getSingleRoot());
        }};
    }
}
