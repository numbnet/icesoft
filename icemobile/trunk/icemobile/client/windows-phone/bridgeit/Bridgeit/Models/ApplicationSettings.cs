using System;
using System.Diagnostics;
using System.Collections.Generic;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Bridgeit.Resources;

namespace Bridgeit.Models
{
    /// <summary>
    /// Settings constants for reading and writing persistant properties of the application. 
    /// </summary>
    public class ApplicationSettings
    {

        // misc. app settings. 
        /// <summary>
        /// Key to store the default URL used to lauch a test application from BridgeIt UI. 
        /// </summary>
        public const string FileNameCountKey = "fileNameCount";
        public int FileNameCountDefault = 0;

        // Launch Settings
        
        /// <summary>
        /// Key to store the default URL used to lauch a test application from BridgeIt UI. 
        /// </summary>
        public const string LaunchUrlKey = "launchUrlKey";
        public string LaunchUrlDefault = AppResources.SettingsLaunchUriValue;

        // General Settings 

        /// <summary>
        /// Show splash screen key,  enable disable splash screen 
        /// </summary>
        public const string SplashScreenKey = "splashScreenEnabled";
        public const bool SplashScreenDefault = false;

        // Camera Settings

        /// <summary>
        /// Camera Quality, low, medium, high setting.  We need to respect the max file upload
        /// constrainst of the environement ass well data caps 
        /// </summary>
        public const string CameraQualityKey = "cameraQuaility";
        public const string CameraQualitylDefault = "medium";

        /// <summary>
        /// Caution message that will warn user of data usage. 
        /// </summary>
        public const string DataUploadMessageKey = "dataUploadMessageEnabled";
        public const bool DataUploadMessageDefault = true;

        /// <summary>
        /// Enable/disable the camera uri command launching, on by default. 
        /// </summary>
        public const string CameraSupportedKey = "cameraSupportEnabled";
        public const bool CameraSupportedDefault = true;

        /// <summary>
        /// Enable/disable the fetchContacts uri command launching, on by default. 
        /// </summary>
        public const string ContactSupportedKey = "contactSupportEnabled";
        public const bool ContactSupportedDefault = true;

        /// <summary>
        /// Enable/disable the fetchContacts uri command launching, on by default. 
        /// </summary>
        public const string ScannerSupportedKey = "scannerEnabledSetting";
        public const bool ScannerSupportedDefault = true;
        

        // Our settings. 
        private IsolatedStorageSettings settings;

        /// <summary>
        /// Initilize the application isoladed storage location. 
        /// </summary>
        public ApplicationSettings()
        {
            settings = IsolatedStorageSettings.ApplicationSettings;
        }

        /// <summary>
        /// Update a setting value for our application. If the setting does not
        /// exist, then add the setting.
        /// </summary>
        /// <param name="Key"></param>
        /// <param name="value"></param>
        /// <returns></returns>
        public bool AddOrUpdateValue(string Key, Object value)
        {
            bool valueChanged = false;

            // If the key exists
            if (settings.Contains(Key))
            {
                // If the value has changed
                if (settings[Key] != value)
                {
                    // Store the new value
                    settings[Key] = value;
                    valueChanged = true;
                }
            }
            // Otherwise create the key.
            else
            {
                settings.Add(Key, value);
                valueChanged = true;
            }
           return valueChanged;
        }

        /// <summary>
        /// Get the current value of the setting, or if it is not found, set the 
        /// setting to the default setting.
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="Key"></param>
        /// <param name="defaultValue"></param>
        /// <returns></returns>
        public T GetValueOrDefault<T>(string Key, T defaultValue)
        {
            T value;

            // If the key exists, retrieve the value.
            if (settings.Contains(Key))
            {
                value = (T)settings[Key];
            }
            // Otherwise, use the default value.
            else
            {
                value = defaultValue;
            }
            return value;
        }

        /// <summary>
        /// Save the settings.
        /// </summary>
        public void Save()
        {
            settings.Save();
        }

        /// <summary>
        /// Property to get and set a the default URI used to launch a Bridgeit application.
        /// </summary>
        public string LaunchUrlSetting
        {
            get
            {
                return GetValueOrDefault<string>(LaunchUrlKey, LaunchUrlDefault);
            }
            set
            {
                if (AddOrUpdateValue(LaunchUrlKey, value))
                {
                    Save();
                }
            }
        }

        /// <summary>
        /// Property to get and set a the default URI used to launch a Bridgeit application.
        /// </summary>
        public int FileNameCountSetting
        {
            get
            {
                return GetValueOrDefault<int>(FileNameCountKey, FileNameCountDefault);
            }
            set
            {
                if (AddOrUpdateValue(FileNameCountKey, value))
                {
                    Save();
                }
            }
        }


        /// <summary>
        /// Property to get and set the cameraSupportedKey.
        /// </summary>
        public bool cameraEnabledSetting
        {
            get
            {
                return GetValueOrDefault<bool>(CameraSupportedKey, CameraSupportedDefault);
            }
            set
            {
                if (AddOrUpdateValue(CameraSupportedKey, value))
                {
                    Save();
                }
            }
        }

        /// <summary>
        /// Property to get and set the cameraSupportedKey.
        /// </summary>
        public bool contactsEnabledSetting
        {
            get
            {
                return GetValueOrDefault<bool>(ContactSupportedKey, ContactSupportedDefault);
            }
            set
            {
                if (AddOrUpdateValue(ContactSupportedKey, value))
                {
                    Save();
                }
            }
        }

        /// <summary>
        /// Property to get and set the cameraSupportedKey.
        /// </summary>
        public bool scannerEnabledSetting
        {
            get
            {
                return GetValueOrDefault<bool>(ScannerSupportedKey, ContactSupportedDefault);
            }
            set
            {
                if (AddOrUpdateValue(ScannerSupportedKey, value))
                {
                    Save();
                }
            }
        }

        /*
       
                public int ListBoxSetting
                {
                    get
                    {
                        return GetValueOrDefault<int>(ListBoxSettingKeyName, ListBoxSettingDefault);
                    }
                    set
                    {
                        if (AddOrUpdateValue(ListBoxSettingKeyName, value))
                        {
                           Save();
                        }
                    }
                }


                /// <summary>
                /// Property to get and set a RadioButton Setting Key.
                /// </summary>
                public bool RadioButton1Setting
                {
                    get
                    {
                        return GetValueOrDefault<bool>(RadioButton1SettingKeyName, RadioButton1SettingDefault);
                    }
                    set
                    {
                        if (AddOrUpdateValue(RadioButton1SettingKeyName, value))
                        {    
                            Save();
                        }
                    }
                }


                /// <summary>
                /// Property to get and set a RadioButton Setting Key.
                /// </summary>
                public bool RadioButton2Setting
                {
                    get
                    {
                        return GetValueOrDefault<bool>(RadioButton2SettingKeyName, RadioButton2SettingDefault);
                    }
                    set
                    {
                        if (AddOrUpdateValue(RadioButton2SettingKeyName, value))
                        {
                            Save();
                        }
                    }
                }

                /// <summary>
                /// Property to get and set a RadioButton Setting Key.
                /// </summary>
                public bool RadioButton3Setting
                {
                    get
                    {
                        return GetValueOrDefault<bool>(RadioButton3SettingKeyName, RadioButton3SettingDefault);
                    }
                    set
                    {
                        if (AddOrUpdateValue(RadioButton3SettingKeyName, value))
                        {
                            Save();
                        }
                    }
                }

                /// <summary>
                /// Property to get and set a Username Setting Key.
                /// </summary>
                public string UsernameSetting
                {
                    get
                    {
                        return GetValueOrDefault<string>(UsernameSettingKeyName, UsernameSettingDefault);
                    }
                    set
                    {
                        if (AddOrUpdateValue(UsernameSettingKeyName, value))
                        {
                            Save();
                        }
                    }
                }

                /// <summary>
                /// Property to get and set a Password Setting Key.
                /// </summary>
                public string PasswordSetting
                {
                    get
                    {
                        return GetValueOrDefault<string>(PasswordSettingKeyName, PasswordSettingDefault);
                    }
                    set
                    {
                        if (AddOrUpdateValue(PasswordSettingKeyName, value))
                        {
                            Save();
                        }
                    }
                }
          */
    }
}
