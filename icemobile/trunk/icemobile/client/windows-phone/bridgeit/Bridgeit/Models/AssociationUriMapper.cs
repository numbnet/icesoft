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
    ///        camera - lunches CamerPage.xaml 
    /// </summary>
    class AssociationUriMapper : UriMapperBase
    {
        public override Uri MapUri(Uri uri)
        {
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
                // found a cammera cammand. 
                if (commandId.StartsWith("camera"))
                {
                    // Map the show products request to ShowProducts.xaml
                    return new Uri("/Views/CameraPage.xaml?" + tempUri, UriKind.Relative);
                }
                // TODO had other device integration xaml pages. 
            }
            // Otherwise return to the bridgeit main site. 
            return new Uri("/Views/Bounce.xaml?" + tempUri, UriKind.Relative);
            
        }
    }
}
