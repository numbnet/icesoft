using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Bridgeit.Resources;

namespace Bridgeit.Views
{
    /// <summary>
    /// Simply bounces the user to the http://www.bridgeit.mobi/ main site. 
    /// </summary>
    public partial class Bouncer : PhoneApplicationPage
    {
        public Bouncer()
        {
            InitializeComponent();
            // setup the navigation to http://www.bridgeit.mobi/
            string site = AppResources.LaunchURL;
            var success =  Windows.System.Launcher.LaunchUriAsync(new Uri(site));
        }
    }
}