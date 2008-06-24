package org.icefaces.module.ahs.installer;

import com.sun.appserv.addons.*;
import org.icefaces.module.ahs.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class InstallerImpl implements Installer {

    public void install(InstallationContext installationContext) throws AddonException {

        //Utility for tracking the directories and files we need to use
        InstallerLocations locs = new InstallerLocations(installationContext);

        try {
            //Extract the configurator jar from the installer jar and
            //save it to the lib/addons directory
            FileUtils.extractAndSave(locs.getInstallerFile(),
                    InstallerLocations.CONFIGURATOR_NAME,
                    locs.getAddOnsLibDir());
        } catch (IOException e) {
            throw new AddonFatalException("could not extract and save " +
                    InstallerLocations.CONFIGURATOR_NAME + " to " +
                    locs.getAddOnsLibDir(), e);
        }
    }

    public void uninstall(InstallationContext installationContext) throws AddonException {
        InstallerLocations locs = new InstallerLocations(installationContext);
        locs.getInstallerFile().deleteOnExit();

        /**
         * In order to have the Configurator addon run the unconfigure() code (thereby
         * uninstalling the AHS server).  You need to do one of two things:
         *
         * 1) Unconfiguring for all domains

         If the addon needs to be unconfigured across all domains which use this
         installation then move addon (e.g. lib/addons/configurator-name.jar to
         lib/addons/.deleted directory. One catch with this is that on Windows
         systems you must stop all server instances in order to be able to move
         the configurator jar file into .deleted directory.

         2) Unconfiguring for a specific domain

         Each server will maintain a properties file called domain-registry in
         the [domain-instance]/config directory. The registry contains information
         about the state of the addon as properties.  For example:

         addon_name_configurator_01_01_00.configured=true
         addon_name_configurator_01_01_00.enabled=true

         By changing the state of the addon in this file, the next time when the
         server is restarted, the corresponding operation will actually become
         effective.  So to have unconfigure called, change the configured state
         to false.

         For our purposes, we'll do it for all domains (#1).  First, because I'm not really
         sure how you're supposed to know which domain it was installed in to begin with
         and, second, to modify the domain-registry file from the Installer, you have to
         have the path to the domain (which again, would be hardcoded since other than the
         default domain1, I'm not sure how you're supposed to know which domain to modify).

         Simply deleting the file (e.g. locs.getConfiguratorFile().deleteOnExit(); ) will
         not properly run the lifecycle and call Configurator.unconfigure.

         */

        File unconfigureDir = locs.getConfiguratorDeleteDir();
        if (!unconfigureDir.exists()) {
            if (!unconfigureDir.mkdir()) {
                throw new AddonException("could not create configurator delete directory " +
                        unconfigureDir);
            }
        }
        FileUtils.move(locs.getConfiguratorFile(), locs.getConfiguratorDeleteDir());
    }

    public void upgrade(InstallationContext installationContext, AddonVersion addonVersion) throws AddonException {
        throw new AddonException(new UnsupportedOperationException("not yet implemented"));
    }

}
