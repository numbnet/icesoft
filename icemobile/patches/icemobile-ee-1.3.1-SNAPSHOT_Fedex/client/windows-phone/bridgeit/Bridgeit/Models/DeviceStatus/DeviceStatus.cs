using Microsoft.Phone.Info;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bridgeit.Models.DeviceStatus
{
    public class DeviceStatus
    {

        public string  ApplicationCurrentMemoryUsage { get; private set; }

        public string ApplicationPeakMemoryUsage { get; private set; }

        public string ApplicationMemoryUsageLimit { get; private set; }

        public string DeviceFirmwareVersion { get; private set; }

        public string DeviceHardwareVersion { get; private set; }

        public string DeviceManufacturer { get; private set; }

        public string DeviceTotalMemory { get; private set; }

        public string DeviceName { get; private set; }

        public string PowerSource { get; private set; }

        public bool IsKeyboardPresent { get; private set; }

        public bool MultiResolutionSupported { get; private set; }


        public DeviceStatus()
        {
            ApplicationCurrentMemoryUsage = Microsoft.Phone.Info.DeviceStatus.ApplicationCurrentMemoryUsage.ToString();
            ApplicationPeakMemoryUsage = Microsoft.Phone.Info.DeviceStatus.ApplicationPeakMemoryUsage.ToString();
            ApplicationMemoryUsageLimit = Microsoft.Phone.Info.DeviceStatus.ApplicationMemoryUsageLimit.ToString();

            DeviceFirmwareVersion = Microsoft.Phone.Info.DeviceStatus.DeviceFirmwareVersion.ToString();
            DeviceHardwareVersion = Microsoft.Phone.Info.DeviceStatus.DeviceHardwareVersion.ToString();
            DeviceManufacturer = Microsoft.Phone.Info.DeviceStatus.DeviceManufacturer.ToString();
            DeviceName = Microsoft.Phone.Info.DeviceStatus.DeviceName.ToString();
            DeviceTotalMemory = Microsoft.Phone.Info.DeviceStatus.DeviceTotalMemory.ToString();

            IsKeyboardPresent = Microsoft.Phone.Info.DeviceStatus.IsKeyboardPresent;
            PowerSource = Microsoft.Phone.Info.DeviceStatus.PowerSource.ToString();

            MultiResolutionSupported = Microsoft.Phone.Info.MediaCapabilities.IsMultiResolutionVideoSupported;
        }
    }
}
