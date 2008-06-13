package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.*;
import org.icefaces.module.ahs.util.FileUtils;

import java.io.File;

public class ConfiguratorImpl implements Configurator {

    public void configure(ConfigurationContext configurationContext) throws AddonException {

        ConfigurationLocations locs = new ConfigurationLocations(configurationContext);

        //Extract the application war from the configurator jar and
        //save it to the deploy directory
        try {
            FileUtils.extractAndSave(locs.getConfiguratorFile(),
                    ConfigurationLocations.APP_NAME,
                    locs.getDeployDir());
        } catch (Exception e) {
            throw new AddonFatalException("could not extract and save " +
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
            throw new AddonException("could not modify " + locs.getDomainConfigFile(), e);
        }
    }

    public void unconfigure(ConfigurationContext configurationContext) throws AddonException {
        ConfigurationLocations locs = new ConfigurationLocations(configurationContext);

        //According to Sun (both the sparse documentation along with emails from Sun employees involved
        //with the Update Center), there are two ways to get "unconfigure" called.
        (new File(locs.getDeployDir() + File.separator + ConfigurationLocations.APP_NAME)).deleteOnExit();
    }

    public void disable(ConfigurationContext configurationContext) throws AddonException {
        throw new AddonException(new UnsupportedOperationException("not yet implemented"));
    }

    public void enable(ConfigurationContext configurationContext) throws AddonException {
        throw new AddonException(new UnsupportedOperationException("not yet implemented"));
    }

    public void upgrade(ConfigurationContext configurationContext, AddonVersion addonVersion) throws AddonException {
        throw new AddonException(new UnsupportedOperationException("not yet implemented"));
    }

}
