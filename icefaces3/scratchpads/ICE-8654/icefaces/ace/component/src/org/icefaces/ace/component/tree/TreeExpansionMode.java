package org.icefaces.ace.component.tree;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 9/10/12
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
public enum TreeExpansionMode {
    client, server;

    public boolean isServer() {
        return this == server;
    }

    public boolean isClient() {
        return this == client;
    }
}
