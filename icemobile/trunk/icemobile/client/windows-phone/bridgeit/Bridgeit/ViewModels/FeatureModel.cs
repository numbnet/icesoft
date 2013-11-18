using Bridgeit.Models;
using Bridgeit.Resources;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bridgeit.ViewModels
{
    public class FeatureModel
    {
        public List<FeatureData> Items { get; set; }

        public bool IsDataLoaded { get; set; }

        public FeatureModel()
        {
            Items = new List<FeatureData>();
        }

        public void LoadData()
        {
            // Load data into the model
            LoadFeatures();

            IsDataLoaded = true;
        }

        private void LoadFeatures()
        {
            Items.Add(new FeatureData
            {
                Title = AppResources.LauncherCamera,
                Icon = "/Assets/camera/camera.png",
                Command = AssociationUriMapper.CameraCommnd
            });
            Items.Add(new FeatureData
            {
                Title = AppResources.LauncherMicrophone,
                Icon = "/Assets/common/mic.png",
                Command = AssociationUriMapper.MicrophoneCommnd
            });
            Items.Add(new FeatureData
            {
                Title = AppResources.LauncherBarcode,
                Icon = "/Assets/common/scanner.png",
                Command = AssociationUriMapper.ScanCommnd
            });
            Items.Add(new FeatureData
            {
                Title = AppResources.LauncherContacts,
                Icon = "/Assets/common/contact.png",
                Command = AssociationUriMapper.FetchCommnd
            });

        }
    }
}
