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
    /// The <c>SettingsPage</c> acts as a settings panel for each of the BridgeIt Components. 
    /// The parent xaml page's markup uses a pivot to move from one settings screen to the next. 
    /// <list type="table">
    ///     <item>Launch - test page for launching a BridgeIt url. </item>
    ///     <item>Common - common settings, splash screen...</item>
    ///     <item>Camera - camera specific settings. </item>
    /// </list>
    /// </summary>
    public partial class SettingsPage : PhoneApplicationPage
    {

        /// <summary>
        /// Inialize the new instance of settings page. 
        /// </summary>
        public SettingsPage()
        {
            InitializeComponent();

            // load persisted settings from isoloated storage. 
            Loaded += SettingsPage_Loaded;
        }

        /// <summary>
        /// Loads the settings stored for this application. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void SettingsPage_Loaded(object sender, RoutedEventArgs e)
        {
            /// TODO load persisted settings. 

        }

        /// <summary>
        /// Launch the test URL in Internet explorer. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private async void GoButton_Click(object sender, RoutedEventArgs e)
        {
            // grab the text from the page scoped URLTextBox component. 
            string site = URLTextBox.Text;
            var success = await Windows.System.Launcher.LaunchUriAsync(new Uri(site));
            if (success)
            {
                System.Diagnostics.Debug.WriteLine("launching URI " + site);
            }
        }
    }
}