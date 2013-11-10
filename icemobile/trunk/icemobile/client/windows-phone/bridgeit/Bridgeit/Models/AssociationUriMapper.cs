using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Navigation;

namespace Bridgeit.Models
{
    /// <summary>
    /// Registers the icemobile namspace so the various Bridgeit Modules such 
    /// as camera can be loaded from the browser. 
    /// Supported Mappings. 
    ///     c: command
    ///        camera - lanches CamerPage.xaml 
    ///        fetchContacts - lanches ContctsPage.xaml 
    ///        fetchDeviceStatus - lanches DeviceStatus.xaml 
    /// </summary>
    class AssociationUriMapper : UriMapperBase
    {
        private const string MsKey = "ms=";

        /// TODO, application settings to turn on/off support for various hardwares support. 
        public override Uri MapUri(Uri uri)
        {
            // grab instace of settings. 
            ApplicationSettings setting = new ApplicationSettings();

            // uri is urlencoded twice, first decode is what we'll pass to the device page. 
            string tempUri = System.Net.HttpUtility.UrlDecode(uri.ToString());
            // second decode will give us the decode command which we'll pair up. 
            string decodedCommand = System.Net.HttpUtility.UrlDecode(tempUri);
            // URI association launch for contoso.
            if (decodedCommand.Contains("icemobile:c"))
            {
                // Get the 'c' or command from the param (after "c=").
                int commandIdIndex = decodedCommand.IndexOf("c=") + 2;
                string commandId = decodedCommand.Substring(commandIdIndex);
                // found a cammera cammand, we need to add our "ms=" command so we can get at the 
                // double encoded id=, ub= and other optional params 
                if (setting.cameraEnabledSetting && commandId.StartsWith("camera"))
                {
                    // Map the show products request to ShowProducts.xaml
                    // /Protocol?encodedLaunchUri=icemobile:c=camera%3F (48 chars to remove)
                    return new Uri("/Views/CameraPage.xaml?" + tempUri.Substring(48), UriKind.Relative);
                }
                else if (setting.contactsEnabledSetting && commandId.StartsWith("fetchContacts"))
                {
                    // Map the show products request to ShowProducts.xaml
                    // /Protocol?encodedLaunchUri=icemobile:c=fetchContacts%3F (55 chars to remove)
                    return new Uri("/Views/ContactsPage.xaml?" + MsKey + tempUri.Substring(55), UriKind.Relative);
                }
                else if (setting.scannerEnabledSetting && commandId.StartsWith("scan"))
                {
                    // Map the show products request to ShowProducts.xaml
                    // /Protocol?encodedLaunchUri=icemobile:c=scan%3F (46 chars to remove)
                    return new Uri("/Views/ScannerPage.xaml?" + MsKey + tempUri.Substring(46), UriKind.Relative);
                }
                else if (commandId.StartsWith("fetchDeviceStatus"))
                {
                    // Map the show products request to ShowProducts.xaml
                    return new Uri("/Views/DeviceStatusPage.xaml?" + tempUri, UriKind.Relative);
                }
                // TODO had other device integration xaml pages. 
            }
            // Otherwise return to the bridgeit main site. 
            return new Uri("/Views/Bounce.xaml?" + tempUri, UriKind.Relative);
            
        }
    }
}
