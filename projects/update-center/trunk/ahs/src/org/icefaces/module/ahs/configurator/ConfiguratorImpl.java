package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.*;
import org.icefaces.module.ahs.util.FileUtils;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ConfiguratorImpl implements Configurator {

//    private static Logger log = Logger.getLogger(ConfiguratorImpl.class.getPackage().getName());

    public void configure(ConfigurationContext configurationContext) throws AddonException {

        ConfigurationLocations locs = new ConfigurationLocations(configurationContext);

        //Extract the application war from the configurator jar and
        //save it to the deploy directory
        try {
            FileUtils.extractAndSave(locs.getConfiguratorFile(),
                    ConfigurationLocations.APP_NAME,
                    locs.getDeployDir());
        } catch (Exception e) {
            String msg = "could not extract and save " +
                    ConfigurationLocations.APP_NAME + " to " +
                    locs.getDeployDir();
//            log.log(Level.SEVERE,msg,e);
            throw new AddonFatalException(msg, e);
        }

        //Modify the domain.xml file to include comet support
        DomainConfig config = new DomainConfig(locs.getDomainConfigFile());
        try {
            config.addCometSetting();
            config.addJMSTopics();
            config.save();
        } catch (Exception e) {
            String msg = "problems modifying " + locs.getDomainConfigFile();
//            log.log(Level.SEVERE,msg,e);
            throw new AddonFatalException(msg, e);
        }
    }

    public void unconfigure(ConfigurationContext configurationContext) throws AddonException {
        ConfigurationLocations locs = new ConfigurationLocations(configurationContext);

        //According to Sun (both the sparse documentation along with emails from Sun employees involved
        //with the Update Center), there are two ways to get "unconfigure" called.
        (new File(locs.getDeployDir() + File.separator + ConfigurationLocations.APP_NAME)).deleteOnExit();

        //Modify the domain.xml file to include comet support
        DomainConfig config = new DomainConfig(locs.getDomainConfigFile());
        try {
            config.removeJMSTopics();
            config.save();
        } catch (Exception e) {
            String msg = "problems modifying " + locs.getDomainConfigFile();
//            log.log(Level.SEVERE,msg,e);
            throw new AddonException(msg, e);
        }
    }

    public void disable(ConfigurationContext configurationContext) throws AddonException {
        String msg = "not yet implemented";
//        log.log(Level.SEVERE,msg);
        throw new AddonFatalException(msg);
    }

    public void enable(ConfigurationContext configurationContext) throws AddonException {
        String msg = "not yet implemented";
//        log.log(Level.SEVERE,msg);
        throw new AddonFatalException(msg);
    }

    public void upgrade(ConfigurationContext configurationContext, AddonVersion addonVersion) throws AddonException {
        String msg = "not yet implemented";
//        log.log(Level.SEVERE,msg);
        throw new AddonFatalException(msg);
    }

}
