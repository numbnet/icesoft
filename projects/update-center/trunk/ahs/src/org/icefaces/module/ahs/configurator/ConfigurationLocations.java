package org.icefaces.module.ahs.configurator;

import com.sun.appserv.addons.ConfigurationContext;

import java.io.File;

public class ConfigurationLocations {

    //TODO - move these into a properties file of some kind
    public final static String CONFIGURATOR_NAME = "icefaces_ahs_configurator.jar";
    public final static String APP_NAME = "ahs-http-server.war";

    private final static String SEP = File.separator;

    private File domainDir;
    private File domainConfigFile;
    private File deployDir;

    private File configuratorFile;

    public ConfigurationLocations(ConfigurationContext configurationContext) {
        File installDir = configurationContext.getInstallationContext().getInstallationDirectory();
        configuratorFile = new File(installDir + SEP + "lib" + SEP + "addons" + SEP + CONFIGURATOR_NAME);

        domainDir = configurationContext.getDomainDirectory();
        domainConfigFile = new File(domainDir + SEP + "config" + SEP + "domain.xml");
        deployDir = new File(domainDir + SEP + "autodeploy");
    }

    public File getDeployDir() {
        return deployDir;
    }

    public File getDomainDir() {
        return domainDir;
    }

    public File getConfiguratorFile() {
        return configuratorFile;
    }

    public File getDomainConfigFile() {
        return domainConfigFile;
    }
}
