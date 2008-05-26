package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.AddonException;
import com.sun.appserv.addons.AddonVersion;
import com.sun.appserv.addons.ConfigurationContext;
import com.sun.appserv.addons.Configurator;
import org.icefaces.module.ahs.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class ConfiguratorImpl implements Configurator {

    public void configure(ConfigurationContext configurationContext) throws AddonException {

        ConfigurationLocations locs = new ConfigurationLocations(configurationContext);

        //Extract the application war from the configurator jar and
        //save it to the deploy directory
        try {
            FileUtils.extractAndSave(locs.getConfiguratorFile(),
                    ConfigurationLocations.APP_NAME,
                    locs.getDeployDir());
        } catch (IOException e) {
            throw new AddonException("could not extract and save " +
                    ConfigurationLocations.APP_NAME + " to " +
                    locs.getDeployDir(), e);
        }

        //Modify the domain.xml file to include comet support
        DomainConfig config = new DomainConfig(locs.getDomainConfigFile());
        try {
            config.addCometSetting();
            config.addJMSTopics();
            config.save();
        } catch (Exception e) {
            throw new AddonException("could not modify " + locs.getDomainConfigFile() + " " + e, e);
        }
    }

    public void unconfigure(ConfigurationContext configurationContext) throws AddonException {
        ConfigurationLocations locs = new ConfigurationLocations(configurationContext);

        //According to Sun (both the sparse documentation along with emails from Sun employees involved
        //with the Update Center), there are two ways to get "unconfigure" called.
        (new File(locs.getDeployDir() + File.separator + ConfigurationLocations.APP_NAME)).deleteOnExit();
        System.out.println("ConfiguratorImpl.unconfigure: called " + locs.getDeployDir() + File.separator + ConfigurationLocations.APP_NAME);
    }

    public void disable(ConfigurationContext configurationContext) throws AddonException {
        //System.out.println("ConfiguratorImpl.disable: no action");
    }

    public void enable(ConfigurationContext configurationContext) throws AddonException {
        //System.out.println("ConfiguratorImpl.enable: no action");
    }

    public void upgrade(ConfigurationContext configurationContext, AddonVersion addonVersion) throws AddonException {
        //System.out.println("ConfiguratorImpl.upgrade: no action");
    }

}
