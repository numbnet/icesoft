using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Media.Imaging;
using Microsoft.Phone.Controls;

// Directives
using Microsoft.Devices;
using System.IO;
using System.IO.IsolatedStorage;
using Microsoft.Xna.Framework.Media;

using Bridgeit.Models;
using Bridgeit.Models.FileHandlers;
using System.Threading;
using Microsoft.Phone.Shell;
using Bridgeit.Resources;
using Microsoft.Phone.Tasks;
using Windows.Storage.Streams;
using System.ComponentModel;
using Windows.Foundation;
using Bridgeit.ViewModels;

namespace Bridgeit.Views
{
    /// <summary>
    /// The <c>CamerPage</c> class is responsible for handling the work of taking a photo and returning the 
    /// the calling application.  A summary of the initilialization looks a bit like this. 
    /// <list type="table">
    /// <item></item>
    /// <item></item>
    /// </list>
    /// </summary>
    public partial class CameraPage : BridgeitPhoneApplicationPage
    {
        public const int ThumbnailWidth = 30;
        public const int ThumbnailHeight = 20;

        public const string PhotoNamePrefix = "photo_";
        public const string PhotoNamePostfix = ".jpg";
        public const string PhotoThumbnailNamePostfix = "_th";

        public const string ImageContentType = "image/jpeg";

        /// <summary>
        /// After the CaptureImageAvailable even is fired we have an image file that can be updated to the server. 
        /// This file name needs to be passed into the FilUploaded so it knows which file to upload. This
        /// is the file name of the last photo that was taken. 
        /// </summary>
        private string _currenLargeImageFileName;

        /// <summary>
        /// Save counter to help insure we have an unique file name
        /// </summary>
        private int _savedCounter = 0;

        /// <summary>
        /// Photo camera Object for accessing the devices cameras if present. 
        /// </summary>
        private PhotoCamera _photoCamera;

        /// <summary>
        /// Photo chooser task for selecting photos from the image real 
        /// </summary>
        private PhotoChooserTask _photoChooserTask;

        /// <summary>
        /// Persisted application settings. 
        /// </summary>
        private ApplicationSettings _settings;

        // camera control buttons.
        private ApplicationBarIconButton _cameraButton;
        private ApplicationBarIconButton _folderButton;
        private ApplicationBarIconButton _uploadButton;
        private ApplicationBarIconButton _cancelButton;
        private ApplicationBarIconButton _acceptButton;

        // file upload helper
        private FileUploadWorker _fileUploadWorker;

        public CameraPage()
        {
            // settins store. 
            _settings = new ApplicationSettings();

            InitializeComponent();

            Loaded += CameraPage_Loaded;
        }

        /// <summary>
        /// Camera page is build and ready to go. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void CameraPage_Loaded(object sender, RoutedEventArgs e)
        {
            // create the application bar icons uses to shoot, browse, upload and cancel/return. 
            BuildLocalizedApplicationBar();
        }

        /// <summary>
        /// Setup a new instance of the backgroundworker for uploading the captured photo.  
        /// </summary>
        private void InitializeBackgroundFileUploader()
        {
           
            // progress indicator found in page status bar. 
            SystemTray.ProgressIndicator = new ProgressIndicator();

            // kill the old one if it running for some reason
            if (_fileUploadWorker != null && _fileUploadWorker.IsBusy)
            {
                _fileUploadWorker.CancelAsync();
            }

            // basic file upload worker for background fileuploads
            _fileUploadWorker = new FileUploadWorker(ImageContentType);
            _fileUploadWorker.WorkerReportsProgress = true;
            _fileUploadWorker.WorkerSupportsCancellation = true;

            // set the even callback for keeping track of progresss. 
            _fileUploadWorker.DoWork += new DoWorkEventHandler(_fileUploadWorker.upload_DoWork);
            _fileUploadWorker.ProgressChanged += new ProgressChangedEventHandler(fileUploadWorker_ProgressChanged);
            _fileUploadWorker.RunWorkerCompleted += new RunWorkerCompletedEventHandler(fileUploadWorker_RunWorkerCompleted);
        }

        /// <summary>
        /// Called when the background worker completes is work/fileupload. This method is 
        /// called even if there is an error of the worker was cancelled. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void fileUploadWorker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if (e.Cancelled == true)
            {
                SystemTray.ProgressIndicator.Text = 
                    AppResources.CameraAppProgressIndicatorCanceled;
            }

            else if (!(e.Error == null))
            {
                SystemTray.ProgressIndicator.Text = String.Format(
                    AppResources.CameraAppProgresIndicatorFailure, e.Error.Message);
            }
            else
            {
                // copy the response over to the bridgeit resonse object
                _bridgeitResponse.ServerResponse = _fileUploadWorker.getResponse();

                // remove the cancel button and show the check button
                ApplicationBar.Buttons.Remove(_cancelButton);
                if (!ApplicationBar.Buttons.Contains(_acceptButton))
                {
                    ApplicationBar.Buttons.Add(_acceptButton);
                }
                SetProgressIndicator(false);
            }
            
        }

        /// <summary>
        /// Progress update event from the background worker doing the file upload. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void fileUploadWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            SystemTray.ProgressIndicator.Text = 
                String.Format(AppResources.CameraAppProgressIndicator,e.ProgressPercentage.ToString());
        }

        /// <summary>
        /// Build out the camera and upload controls using the application bar. 
        /// </summary>
        private void BuildLocalizedApplicationBar()
        {
            // Set the page's ApplicationBar to a new instance of ApplicationBar.
            ApplicationBar = new ApplicationBar();

            // Create a the cammera shutter button and menu text
            _cameraButton = new ApplicationBarIconButton(new Uri("/Assets/camera/camera.png", UriKind.Relative));
            _cameraButton.Text = AppResources.CameraAppBarCameraButtonText;
            _cameraButton.Click += CameraButton_Click;
            // enable when initialized event is fired. 
            _cameraButton.IsEnabled = false;
            ApplicationBar.Buttons.Add(_cameraButton);

            // Create a the media upload button and menu text
            _folderButton = new ApplicationBarIconButton(new Uri("/Assets/camera/folder.png", UriKind.Relative));
            _folderButton.Text = AppResources.CameraAppBarBrowseButtonText;
            _folderButton.Click += PhotoChooserButton_Click;
            ApplicationBar.Buttons.Add(_folderButton);

            // Create a the uplod button for sending the photo the the upload url in BridgeitRequest
            _uploadButton = new ApplicationBarIconButton(new Uri("/Assets/common/upload.png", UriKind.Relative));
            _uploadButton.Text = AppResources.CameraAppBarUploadButtonText;
            _uploadButton.IsEnabled = false;
            _uploadButton.Click += UploadButton_Click;
            ApplicationBar.Buttons.Add(_uploadButton);

            // Create a cancel button that is enable even during an upload, will cancel uplaod and return the user to the main page. 
            _cancelButton = new ApplicationBarIconButton(new Uri("/Assets/common/cancel.png", UriKind.Relative));
            _cancelButton.Text = AppResources.CameraAppBarUploadButtonText;
            _cancelButton.Click += CancelButton_Click;
            ApplicationBar.Buttons.Add(_cancelButton);

            // Create a check button that is enable even during an upload, will cancel uplaod and return the user to the main page. 
            _acceptButton = new ApplicationBarIconButton(new Uri("/Assets/common/check.png", UriKind.Relative));
            _acceptButton.Text = AppResources.CameraAppBarUploadButtonText;
            _acceptButton.Click += AcceptButton_Click;

            // Create a new menu item with the localized string from AppResources.
            ApplicationBarMenuItem appBarMenuItem = new ApplicationBarMenuItem(AppResources.CameraAppBarAboutMenuItemText);
            appBarMenuItem.Click += AppBarMenuItem_Click;
            ApplicationBar.MenuItems.Add(appBarMenuItem);
        }

        /// <summary>
        /// Camera button event, engages the shutter and writes the captured images to isolated storage.  
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void CameraButton_Click(object sender, EventArgs e)
        {
            // avoid second click
            _cameraButton.IsEnabled = false;

            // diable the check/accept button, as the user will need to re-upload the photo
            ApplicationBar.Buttons.Remove(_acceptButton);
            if (!ApplicationBar.Buttons.Contains(_cancelButton))
            {
                ApplicationBar.Buttons.Add(_cancelButton);
            }

            // take the picture, camera component fires a bunch of events for the image capture. 
            if (_photoCamera != null)
            {
                try
                {
                    // Start image capture.
                    _photoCamera.CaptureImage();

                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
        }

        /// <summary>
        /// Loads the browser view of the mediastore photo real. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void PhotoChooserButton_Click(object sender, EventArgs e)
        {
            // diable the check/accept button, as the user will need to re-upload the photo
            ApplicationBar.Buttons.Remove(_acceptButton);
            if (!ApplicationBar.Buttons.Contains(_cancelButton))
            {
                ApplicationBar.Buttons.Add(_cancelButton);
            }
            // disable the shutter button until we get the reinitialized event. 
            _cameraButton.IsEnabled = false;

            // browse the media photo role to pick a photo for upload. 
            _photoChooserTask = new PhotoChooserTask();
            _photoChooserTask.Completed += new EventHandler<PhotoResult>(photoChooserTask_Completed);
            _photoChooserTask.Show();
        }

        /// <summary>
        /// Upload the image that was taken by the camera or selected by the photo roll dialogue. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void UploadButton_Click(object sender, EventArgs e)
        {
            // disable the upload button
            _uploadButton.IsEnabled = false;

            // setup the progressbar that monitors a file upload. 
            InitializeBackgroundFileUploader();

            // setup the progress bar. 
            SetProgressIndicator(true);
            // SystemTray.ProgressIndicator.Text = AppResources.CameraAppProgressIndicator;

            // upload the selected photo
            string postBackURL = _bridgeitRequest.PostBackURL;
            string commandId = _bridgeitRequest.Id;
            _fileUploadWorker.setUploadLocation(_currenLargeImageFileName, commandId, postBackURL);
            
            bool failure = false;
            try
            {
                // start the upload and keep track of the upload progrees and reponse. 
                if (_fileUploadWorker.IsBusy != true)
                {
                    _fileUploadWorker.RunWorkerAsync();
                }
            }
            catch (WebException evt)
            {
                System.Diagnostics.Debug.WriteLine("Web Exception: " + evt.Message + "\nStatus: " + evt.Status);
                failure = true;
            }
            catch (Exception evt)
            {
                System.Diagnostics.Debug.WriteLine("General Exception: " + evt.Message);
                failure = true;
            }
            if (failure)
            {
                // change the button state so the cancel button is removed
                ApplicationBar.Buttons.Remove(_acceptButton);
                if (!ApplicationBar.Buttons.Contains(_cancelButton))
                {
                    ApplicationBar.Buttons.Add(_cancelButton);
                }
                // hide the progress bar
                SetProgressIndicator(false);

                // report the error in a toast popup
                MessageBox.Show(AppResources.CameraAppProgresIndicatorFailure2);
            }
            else
            {
               // MessageBox.Show(AppResources.CameraAppProgressIndicatorSuccess);
            }
        }

        /// <summary>
        /// Build out the callback url and navigate back to IE which will refresh
        /// the page givne the specified url. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void AcceptButton_Click(object sender, EventArgs e)
        {
            base.SubmitData_Click(sender, e);
        }

        /// <summary>
        /// Cancel button clicked navigate back but first cancel the background
        /// worker if it is running. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void CancelButton_Click(object sender, EventArgs e)
        {
            base.Cancel_Click(sender, e);
        }

        /// <summary>
        /// About menu item in application app bar, show s simple splash screen. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void AppBarMenuItem_Click(object sender, EventArgs e)
        {
            MessageBox.Show(AppResources.CameraAppBarAboutMenuItemMessage);
        }

        /// <summary>
        /// Completed event fires once a user selects a photo from the photo real. 
        /// The image is then converted to thumbnail preview and stored in base64 
        /// encoding back in the _bridgeitRequest. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void photoChooserTask_Completed(object sender, PhotoResult e)
        {
            if (e.TaskResult == TaskResult.OK)
            {
                string filePath = e.OriginalFileName;
                _currenLargeImageFileName = filePath.Substring(filePath.LastIndexOf('\\') + 1);
                System.Diagnostics.Debug.WriteLine("File choice" + _currenLargeImageFileName + " " + filePath);
                // seems to be an api bug,  file name is always appended with "_jpg" before the .jpg extention
                // only for this api call, other calls have the picture name correct TODO submit bug report. 
                _currenLargeImageFileName = _currenLargeImageFileName.Replace("_jpg", "");
                Stream imageStream = e.ChosenPhoto.AsInputStream().AsStreamForRead();
                try
                {
                    _bridgeitResponse.PreviewContent = CreateResizedBase64Thumbnail(imageStream, ThumbnailWidth, ThumbnailHeight);
                }
                catch (Exception)
                {
                    System.Diagnostics.Debug.WriteLine("Error converting image to thumbnail.");
                }
                finally
                {
                    // Close image stream
                    imageStream.Close();
                    imageStream.Dispose();

                    // enable the upload button 
                    _uploadButton.IsEnabled = true;
                }
            }
        }

        /// <summary>
        /// When pages is navigated to we parse out the queryString from the calling uri. Once complete
        /// we find an availiable camera if any and add the various listeners for taking a photo. 
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            // parse out the bridgeit url 
            if (_bridgeitRequest == null)
            {
                // the query string will strip out the # and everything after it. 
                string queryString = e.Uri.ToString();
                _bridgeitRequest = new BridgeItRequest(this.NavigationContext.QueryString,
                    System.Net.HttpUtility.UrlDecode(e.Uri.ToString()));
            }
            // read the response
            if (_bridgeitResponse == null)
            {
                _bridgeitResponse = new BridgeItResponse(ImageContentType);
            }

            // Check to see if the camera is available on the phone.
            if ((PhotoCamera.IsCameraTypeSupported(CameraType.Primary) == true) ||
                 (PhotoCamera.IsCameraTypeSupported(CameraType.FrontFacing) == true))
            {
                // Initialize the camera, when available.
                if (PhotoCamera.IsCameraTypeSupported(CameraType.Primary))
                {
                    // Use front-facing camera if available.
                    _photoCamera = new Microsoft.Devices.PhotoCamera(CameraType.Primary);
                }
                else
                {
                    // Otherwise, use standard camera on back of phone.
                    _photoCamera = new Microsoft.Devices.PhotoCamera(CameraType.FrontFacing);
                }
                // TODO setup some default properties via the config screen. 
                IEnumerable<System.Windows.Size> res = _photoCamera.AvailableResolutions;

                // Event is fired when the PhotoCamera object has been initialized.
                // write message to screen 
                _photoCamera.Initialized += new EventHandler<Microsoft.Devices.CameraOperationCompletedEventArgs>(camera_Initialized);

                // Event is fired when the capture sequence is complete.
                // increment file name count
                _photoCamera.CaptureCompleted += new EventHandler<CameraOperationCompletedEventArgs>(camera_CaptureCompleted);

                // Event is fired when the capture sequence is complete and an image is available.
                // save the main image to file
                _photoCamera.CaptureImageAvailable += new EventHandler<Microsoft.Devices.ContentReadyEventArgs>(camera_CaptureImageAvailable);

                // Event is fired when the capture sequence is complete and a thumbnail image is available.
                // save the thumnail (_th) image to disk. 
                _photoCamera.CaptureThumbnailAvailable += new EventHandler<ContentReadyEventArgs>(camera_CaptureThumbnailAvailable);

                //Set the VideoBrush source to the camera.
                viewfinderBrush.SetSource(_photoCamera);
            }
            else
            {
                // The camera is not supported on the phone.
                MessageBox.Show("A Camera is not available on this phone.");
                // Disable the shutter button. 
                _cameraButton.IsEnabled = false;
            }

        }

        /// <summary>
        /// Clean up the view and dispose of the camera. 
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatingFrom(System.Windows.Navigation.NavigatingCancelEventArgs e)
        {
            if (_photoCamera != null)
            {
                // Dispose camera to minimize power consumption and to expedite shutdown.
                _photoCamera.Dispose();

                // Release memory, ensure garbage collection.
                _photoCamera.Initialized -= camera_Initialized;
                _photoCamera.CaptureCompleted -= camera_CaptureCompleted;
                _photoCamera.CaptureImageAvailable -= camera_CaptureImageAvailable;
                _photoCamera.CaptureThumbnailAvailable -= camera_CaptureThumbnailAvailable;

                // make sure we close the worker, finished or not. 
                if (_fileUploadWorker != null && _fileUploadWorker.IsBusy)
                {
                    _fileUploadWorker.CancelAsync();
                }
            }
        }

        /// <summary>
        /// Ensure that the viewfinder is upright in LandscapeRight.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnOrientationChanged(OrientationChangedEventArgs e)
        {
              
            if (_photoCamera != null)
            {
                // LandscapeRight rotation when camera is on back of phone.
                int landscapeRightRotation = 180;

                // Change LandscapeRight rotation for front-facing camera.
                if (_photoCamera.CameraType == CameraType.FrontFacing) landscapeRightRotation = -180;

                // Rotate video brush from camera.
                if (e.Orientation == PageOrientation.LandscapeRight)
                {
                    // Rotate for LandscapeRight orientation.
                    viewfinderBrush.RelativeTransform =
                        new CompositeTransform() { CenterX = 0.5, CenterY = 0.5, Rotation = landscapeRightRotation };
                }
                else
                {
                    // Rotate for standard landscape orientation.
                    viewfinderBrush.RelativeTransform =
                        new CompositeTransform() { CenterX = 0.5, CenterY = 0.5, Rotation = 0 };
                }
            }
            
            base.OnOrientationChanged(e);
        }

        void camera_CaptureCompleted(object sender, CameraOperationCompletedEventArgs e)
        {
            // Increments the savedCounter variable used for generating JPEG file names.
            _savedCounter = _settings.FileNameCountSetting;
            _savedCounter++;
            _settings.FileNameCountSetting = _savedCounter;
            // enable the upload and button 
            _uploadButton.IsEnabled = true;
            _cameraButton.IsEnabled = true;
            
        }

        // Informs when full resolution photo has been taken, saves to local media library and the local folder.
        void camera_CaptureImageAvailable(object sender, Microsoft.Devices.ContentReadyEventArgs e)
        {
            // create the new filname. 
            _currenLargeImageFileName = PhotoNamePrefix + _settings.FileNameCountSetting + PhotoNamePostfix;

            try
            {   
                // TODO check network settings for max file size upload and scale image appropriately. 

                // Save photo to the singleton media library camera roll.
                MediaLibrary library = new MediaLibrary();
                Picture picture = library.SavePictureToCameraRoll(_currenLargeImageFileName, e.ImageStream);
                System.Diagnostics.Debug.WriteLine("Saving Picture to camera roll " + _currenLargeImageFileName);

                // Set the position of the stream back to start
                e.ImageStream.Seek(0, SeekOrigin.Begin);

                // Save photo as JPEG to the local folder.
                using (IsolatedStorageFile isStore = IsolatedStorageFile.GetUserStoreForApplication())
                {
                    using (IsolatedStorageFileStream targetStream = isStore.OpenFile(_currenLargeImageFileName, FileMode.Create, FileAccess.Write))
                    {
                        // Initialize the buffer for 4KB disk pages.
                        byte[] readBuffer = new byte[4096];
                        int bytesRead = -1;

                        // Copy the image to the local folder. 
                        while ((bytesRead = e.ImageStream.Read(readBuffer, 0, readBuffer.Length)) > 0)
                        {
                            targetStream.Write(readBuffer, 0, bytesRead);
                        }
                    }
                }
            }
            finally
            {
                // Close image stream
                e.ImageStream.Close();
                e.ImageStream.Dispose();
            }

        }

        // <summary>
        // Informs when thumbnail photo has been taken, saves to the local folder
        // User will select this image in the Photos Hub to bring up the full-resolution. 
        // </summary>
        public void camera_CaptureThumbnailAvailable(object sender, ContentReadyEventArgs e)
        {
            string fileName = PhotoNamePrefix + _savedCounter + PhotoThumbnailNamePostfix + PhotoNamePostfix;
            System.Diagnostics.Debug.WriteLine("Saving picture thumbnail " + fileName);
            try
            {
                // Save thumbnail as JPEG to the local folder.
                using (IsolatedStorageFile isStore = IsolatedStorageFile.GetUserStoreForApplication())
                {
                    // write the image to disk
                    using (IsolatedStorageFileStream targetStream = isStore.OpenFile(fileName, FileMode.Create, FileAccess.Write))
                    {
                        // Initialize the buffer for 4KB disk pages.
                        byte[] readBuffer = new byte[4096];
                        int bytesRead = -1;

                        // Copy the thumbnail to the local folder. 
                        while ((bytesRead = e.ImageStream.Read(readBuffer, 0, readBuffer.Length)) > 0)
                        {
                            targetStream.Write(readBuffer, 0, bytesRead);
                        }
                    }
                    // encode the thumbnail as a base64 encoded string
                    using (IsolatedStorageFileStream imageStream = isStore.OpenFile(fileName, FileMode.Open, FileAccess.ReadWrite))
                    {
                        // we need to scale the thumbnail to a smaller size so that we can sent it back via the postback. 
                        using (System.Threading.AutoResetEvent are = new System.Threading.AutoResetEvent(false))
                        {
                            // The image resize work nees to be down on the main ui thread so we setup up this blocking 
                            // call. 
                            System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
                            {
                                _bridgeitResponse.PreviewContent = CreateResizedBase64Thumbnail(
                                    imageStream, ThumbnailWidth, ThumbnailHeight);
                                are.Set();
                            });
                            Thread thread = System.Threading.Thread.CurrentThread;
                            are.WaitOne();
                        }
                    }
                }
            }
            finally
            {
                // Close image stream
                e.ImageStream.Close();
                e.ImageStream.Dispose();
            }
        }

        /// <summary>
        ///  Update the UI if initialization succeeds.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void camera_Initialized(object sender, Microsoft.Devices.CameraOperationCompletedEventArgs e)
        {
            if (e.Succeeded)
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    // reinable the camera shutter button. 
                    _cameraButton.IsEnabled = true;
                });
            }
        }

    }

}