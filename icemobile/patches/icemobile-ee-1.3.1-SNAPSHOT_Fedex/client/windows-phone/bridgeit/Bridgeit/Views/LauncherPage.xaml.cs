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
using Bridgeit.ViewModels;
using Bridgeit.Models;

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
    public partial class LauncherPage : PhoneApplicationPage
    {

        /// <summary>
        /// Inialize the new instance of settings page. 
        /// </summary>
        public LauncherPage()
        {
            InitializeComponent();

            // Set the data context of the listbox control to the sample data
            DataContext = App.ViewModel;

            // load persisted settings from isoloated storage. 
            Loaded += SettingsPage_Loaded;
        }

        // Load data for the ViewModel Items
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            if (!App.ViewModel.IsDataLoaded)
            {
                App.ViewModel.LoadData();
            }
        }

        /// <summary>
        /// Loads the settings stored for this application. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void SettingsPage_Loaded(object sender, RoutedEventArgs e)
        {
           
        }


        private void LongListSelector_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            LongListSelector selector = sender as LongListSelector;

            // verifying our sender is actually a LongListSelector
            if (selector == null)
                return;

            // get the url
            FeatureData data = selector.SelectedItem as FeatureData;
            if (data != null && data.Command != null)
            {
                ApplicationSettings settings = new ApplicationSettings();
                
                // System.Net.HttpUtility.UrlEncode  
                String url = data.Command + "?&u={0}";
                url = String.Format(url, System.Net.HttpUtility.UrlEncode(settings.LaunchUrlSetting));
                url = AssociationUriMapper.BaseCommnd + "=" + url;

                NavigationService.Navigate(new Uri(url));
            }
        }

        private void insertUlr_tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            URLTextBox.Text = "http://mediacast.icemobile.org/mediacast/upload/";
        }
    }
}