package org.icefaces.module.ahs.installer;

import com.sun.appserv.addons.InstallationContext;

import java.io.File;

public class InstallerLocations {

    //TODO - move these into a properties file of some kind
    public final static String INSTALLER_NAME = "icefaces_ahs_installer.jar";
    public final static String CONFIGURATOR_NAME = "icefaces_ahs_configurator.jar";

    private final static String SEP = File.separator;

    private File installDir;
    private File addOnsDir;
    private File addOnsLibDir;

    private File installerFile;
    private File configuratorFile;
    private File configuratorDeleteDir;

    public InstallerLocations(InstallationContext ic) {
        installDir = ic.getInstallationDirectory();

        String installDirPath = installDir.getAbsolutePath();
        addOnsDir = new File(installDirPath + SEP + "addons");
        installerFile = new File(addOnsDir + SEP + INSTALLER_NAME);
        addOnsLibDir = new File(installDirPath + SEP + "lib" + SEP + "addons");
        configuratorFile = new File(addOnsLibDir + SEP + CONFIGURATOR_NAME);

        configuratorDeleteDir = new File(addOnsLibDir + SEP + ".deleted");
    }

    public File getAddOnsDir() {
        return addOnsDir;
    }

    public File getAddOnsLibDir() {
        return addOnsLibDir;
    }

    public File getConfiguratorFile() {
        return configuratorFile;
    }

    public File getInstallDir() {
        return installDir;
    }

    public File getInstallerFile() {
        return installerFile;
    }

    public File getConfiguratorDeleteDir() {
        return configuratorDeleteDir;
    }
}
