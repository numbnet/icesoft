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
using Bridgeit.Models;
using System.Text;
using Bridgeit.ViewModels;

namespace Bridgeit.Views
{
    /// <summary>
    /// The <c>DeviceStatus</c> class fetches the device status settings of the phoen and will 
    /// return found proeprties as a url encoded string which can be parsed by the bridgeit
    /// callback funtions. 
    /// </summary>
    public partial class DeviceStatus : BridgeitPhoneApplicationPage //: PhoneApplicationPage, BridgeitPhoneApplicationPage
    {

        public const string ContentType = "text/plain";
        
        /// <summary>
        /// Current memory usage key used in the bridgeit response. 
        /// </summary>
        public const string ApplicationCurrentMemoryUsage_key = "&u=";

        /// <summary>
        /// Peak memory usage key used in the bridgeit response. 
        /// </summary>
        public const string ApplicationPeakMemoryUsage_key = "&p=";

        /// <summary>
        /// Memory usage limit key used in the bridgeit response. 
        /// </summary>
        public const string ApplicationMemoryUsageLimit_Key = "&l=";

        /// <summary>
        /// Device firmware key used in the bridgeit response. 
        /// </summary>
        public const string DeviceFirmwareVersion_Key = "&f=";
        
        /// <summary>
        /// Hardware version key used in the bridgeit response. 
        /// </summary>
        public const string DeviceHardwareVersion_Key = "&h=";

        /// <summary>
        /// Manufacturer key used in the bridgeit response. 
        /// </summary>
        public const string DeviceManufacturer_Key = "&m=";

        /// <summary>
        /// Total device memory key used in the bridgeit response. 
        /// </summary>
        public const string DeviceTotalMemory_Key = "&t=";

        /// <summary>
        /// Device name key used in the bridgeit response. 
        /// </summary>
        public const string DeviceName_Key = "&n=";

        /// <summary>
        /// Power source key used in the bridgeit response. 
        /// </summary>
        public const string PowerSource_Key = "&s=";
        
        /// <summary>
        /// Is physical keyboard present key used in the bridgeit response. 
        /// </summary>
        public const string KeyboardPresent_Key = "&k=";

        /// <summary>
        /// Multi-resolution support key used in the bridgeit response. 
        /// </summary>
        public const string MultiResolutionSupported_Key = "&r=";

        /// <summary>
        /// Create new instance of <c>DeviceStatus</c> view model. 
        /// </summary>
        public DeviceStatus() 
        {
            InitializeComponent();
            // delay the device lookup out to loaded event
            Loaded += DeviceStatus_Loaded;
        }

        /// <summary>
        /// Creates a new isntance of <c>Models.DeviceStatus.DeviceStatus</c> and copies
        /// the parameters to textBox values in the UI. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void DeviceStatus_Loaded(object sender, RoutedEventArgs e)
        {
            _bridgeitResponse.ContentType = ContentType;

            Models.DeviceStatus.DeviceStatus deviceStatus = new Models.DeviceStatus.DeviceStatus();
            currentMemoryUsage.Text = deviceStatus.ApplicationCurrentMemoryUsage + " KB";
            peakMemoryUsage.Text = deviceStatus.ApplicationPeakMemoryUsage + " KB";
            memoryUsageLimit.Text = deviceStatus.ApplicationMemoryUsageLimit + " KB";
            firmware.Text = deviceStatus.DeviceFirmwareVersion;
            hardwareVersion.Text = deviceStatus.DeviceHardwareVersion;
            manufacturer.Text = deviceStatus.DeviceManufacturer;
            totalMemory.Text = deviceStatus.DeviceTotalMemory;
            devicName.Text = deviceStatus.DeviceName;
            powerSource.Text = deviceStatus.PowerSource;
            keyboard.Text = (deviceStatus.IsKeyboardPresent ? AppResources.DeviceStatusYes : AppResources.DeviceStatusNo);
            multiResolution.Text = (deviceStatus.MultiResolutionSupported ? AppResources.DeviceStatusYes : AppResources.DeviceStatusNo);

            // store the data in the sesponse. 
            StringBuilder serverResponse = new StringBuilder();
            serverResponse.Append(ApplicationPeakMemoryUsage_key).Append(deviceStatus.ApplicationCurrentMemoryUsage);
            serverResponse.Append(ApplicationMemoryUsageLimit_Key).Append(deviceStatus.ApplicationMemoryUsageLimit);
            serverResponse.Append(DeviceFirmwareVersion_Key).Append(deviceStatus.DeviceFirmwareVersion);
            serverResponse.Append(DeviceHardwareVersion_Key).Append(deviceStatus.DeviceHardwareVersion);
            serverResponse.Append(DeviceManufacturer_Key).Append(deviceStatus.DeviceManufacturer);
            serverResponse.Append(DeviceTotalMemory_Key).Append(deviceStatus.DeviceTotalMemory);
            serverResponse.Append(DeviceName_Key).Append(deviceStatus.DeviceName);
            serverResponse.Append(PowerSource_Key).Append(deviceStatus.PowerSource);
            serverResponse.Append(KeyboardPresent_Key).Append(deviceStatus.IsKeyboardPresent);
            serverResponse.Append(MultiResolutionSupported_Key).Append(deviceStatus.MultiResolutionSupported);
            _bridgeitRequest.ReturnURL = serverResponse.ToString();
        }

        private void Cancel_Click(object sender, RoutedEventArgs e)
        {
            // navigate back to the calling url. 
            base.Cancel_Click(sender, e);    
        }

        private void SubmitData_Click(object sender, RoutedEventArgs e)
        {
            // submit the device status data as a response to the post backk data. 
            base.SubmitData_Click(sender, e);
        }

    }
}