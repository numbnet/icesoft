package org.icefaces.samples.showcase.metadata.annotation;

/**
 * Created by IntelliJ IDEA.
 * User: pcorless
 * Date: 26/01/11
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ResourceType {
    java,
    xhtml,
    tld,
    href;

    public static final ResourceType DEFAULT = ResourceType.java;
}
