/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

import java.util.List;

/**
 * Utility class for view purposes.
 *
 * @since 1.0
 */
public class ContactViewUtility {

    /**
     * Form a table that contains contact list and a column of booleancheckbox
     *
     * @param models
     * @param defaultValue
     * @return array of table entries
     */
    public static ContactTableEntry[] toViewTable(List models, boolean defaultValue) {

        ContactTableEntry[] table = new ContactTableEntry[models.size()];
        for (int i = 0; i < models.size(); i++) {
            table[i] = new ContactTableEntry((ContactModel) models.get(i), defaultValue);
        }
        return table;
    }
}