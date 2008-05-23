package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.AddonException;

import java.io.*;
import java.util.Properties;
import java.util.Date;

public class DomainRegistry {

    public static final String ENABLED = "enabled";
    public static final String CONFIGURED = "configured";

    private String configurator;
    private File domainRegistryFile;
    private Properties reg;

    public DomainRegistry(String configurator, File domainRegistryFile) throws AddonException {
        this.configurator = configurator;
        this.domainRegistryFile = domainRegistryFile;
        try {
            reg.load( new BufferedInputStream( new FileInputStream(domainRegistryFile)));
        } catch (IOException e) {
            throw new AddonException("could not load " + domainRegistryFile, e);
        }
    }

    public void enable() throws AddonException {
        setProperty(ENABLED,true);
    }

    public void disable() throws AddonException {
        setProperty(ENABLED,false);
    }

    public void configure() throws AddonException {
        setProperty(CONFIGURED,true);
    }

    public void unconfigure() throws AddonException {
        setProperty(CONFIGURED,false);
    }

    private void setProperty(String state, boolean trueOrFalse ) throws AddonException {
        reg.setProperty(configurator + "." + state, Boolean.toString(trueOrFalse));
        try {
            reg.store( new BufferedOutputStream(new FileOutputStream(domainRegistryFile)), new Date().toString());
        } catch (IOException e) {
            throw new AddonException("could not store " + domainRegistryFile, e);
        }
    }

}
