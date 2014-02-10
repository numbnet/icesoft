using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Bridgeit.ViewModels;
using Bridgeit.Resources;
using Coding4Fun.Toolkit.Audio;
using Coding4Fun.Toolkit.Audio.Helpers;
using System.IO;
using System.IO.IsolatedStorage;
using Bridgeit.Models.FileHandlers;
using System.ComponentModel;
using Bridgeit.Models;

namespace Bridgeit.Views
{
    /// <summary>
    /// The <c>MicrophonePage</c> class takes care of recording sound bites nd 
    /// submitting them to the specified url.  Most of the code is just 
    /// for UI button state and the the FileUploadWorder does the rest of the 
    /// work for the actual upload.  
    /// </summary>
    public partial class MicrophonePage : BridgeitPhoneApplicationPage
    {
        // recorder and storage nmes. 
        private MicrophoneRecorder _recorder = new MicrophoneRecorder();
        private IsolatedStorageFileStream _audioStream;
        private string _tempFileName = "bridgeitwav.wav";

        // upload
        private string _contentType = "audio/x-wav";

        // upload button 
        private ApplicationBarIconButton _uploadButton;

        // accept button 
        private ApplicationBarIconButton _acceptButton;

        // cancel button 
        private ApplicationBarIconButton _cancelButton;

        // file upload helper
        private FileUploadWorker _fileUploadWorker;

        public MicrophonePage()
        {
            InitializeComponent();

            // setup buttons and load intial contacts data set. 
            BuildLocalizedApplicationBar();

            // enables back button to hide search input before navigation back. 
            BackKeyPress += OnBackKeyPress;
        }

        private void BuildLocalizedApplicationBar()
        {
            // build out the application bar. 
            // Set the page's ApplicationBar to a new instance of ApplicationBar.
            ApplicationBar = new ApplicationBar();

            // Create a the cammera shutter button and menu text
            _uploadButton = new ApplicationBarIconButton(new Uri("/Assets/common/upload.png", UriKind.Relative));
            _uploadButton.Text = AppResources.MicrophoneUploadBtn;
            _uploadButton.Click += UploadButton_Click;
            _uploadButton.IsEnabled = false;
            ApplicationBar.Buttons.Add(_uploadButton);

            // Create a cancel button that is enable even during an upload, will cancel uplaod and return the user to the main page. 
            _cancelButton = new ApplicationBarIconButton(new Uri("/Assets/common/cancel.png", UriKind.Relative));
            _cancelButton.Text = AppResources.MicrophoneCancelBtn;
            _cancelButton.Click += CancelButton_Click;
            ApplicationBar.Buttons.Add(_cancelButton);

            // create the accept button
            _acceptButton = new ApplicationBarIconButton(new Uri("/Assets/common/check.png", UriKind.Relative));
            _acceptButton.Text = AppResources.MicrophoneAcceptBtn;
            _acceptButton.Click += AcceptButton_Click;

            // disable play button on start. 
            PlayAudio.IsEnabled = false;
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
            _fileUploadWorker = new FileUploadWorker(_contentType);
            _fileUploadWorker.WorkerReportsProgress = true;
            _fileUploadWorker.WorkerSupportsCancellation = true;

            // set the even callback for keeping track of progresss. 
            _fileUploadWorker.DoWork += new DoWorkEventHandler(_fileUploadWorker.upload_DoWork);
            _fileUploadWorker.ProgressChanged += new ProgressChangedEventHandler(fileUploadWorker_ProgressChanged);
            _fileUploadWorker.RunWorkerCompleted += new RunWorkerCompletedEventHandler(fileUploadWorker_RunWorkerCompleted);
        }

        /// <summary>
        /// Progress update event from the background worker doing the file upload. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void fileUploadWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            SystemTray.ProgressIndicator.Text =
                String.Format(AppResources.MicrophoneProgressIndicator, e.ProgressPercentage.ToString());
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
                    AppResources.MicrophoneProgressIndicatorCanceled;
            }

            else if (!(e.Error == null))
            {
                SystemTray.ProgressIndicator.Text = String.Format(
                    AppResources.MicrophoneAppProgresIndicatorFailure, e.Error.Message);
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
        /// User accepts the uploaded sound and is returned to the calling page. Only active
        /// if a file has been uploaded. 
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
        /// Upload the recorded sound file to the echo services as defined in the bridgeit request. 
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
            SystemTray.ProgressIndicator.Text = "";

            // upload the selected photo
            string postBackURL = _bridgeitRequest.PostBackURL;
            string commandId = _bridgeitRequest.Id;
            _fileUploadWorker.setUploadLocation(_tempFileName, commandId, postBackURL);

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
                MessageBox.Show(AppResources.MicrophoneProgresIndicatorFailure2);
            }
            else
            {
                // MessageBox.Show(AppResources.CameraAppProgressIndicatorSuccess);
            }
        }

        /// <summary>
        /// Supresses the back button functionality if the search textbox is visible.  If 
        /// visible the backbutton will hide the search textbox and a subsequent back button
        /// click will exit the application. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void OnBackKeyPress(object sender, System.ComponentModel.CancelEventArgs e)
        {
            // clean up the temp file. 

            // first bck button we toggle back to the hidden view.  
            if (_bridgeitRequest != null)
            {
                // return to the calling url. 
                base.Cancel_Click(sender, e);
                e.Cancel = true;
            }
            // if no request the we simply return via the normal back mechanism. 
            else
            {
                base.OnBackKeyPress(e);
            }
        }

        /// <summary>
        /// When pages is navigated to we parse out the queryString from the calling uri. 
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
                _bridgeitResponse = new BridgeItResponse(_contentType);
            }

        }

        /// <summary>
        /// Clean up the view and dispose of the player resources. 
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatingFrom(System.Windows.Navigation.NavigatingCancelEventArgs e)
        {
            if (AudioPlayer != null)
            {
                // Dispose media player. 
                cleanupAudioPlayer();

                // make sure we close the worker, finished or not. 
                if (_fileUploadWorker != null && _fileUploadWorker.IsBusy)
                {
                    _fileUploadWorker.CancelAsync();
                }
            }
        }

        /// <summary>
        /// Start the recorder. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RecordButton_Checked(object sender, RoutedEventArgs e)
        {
            PlayAudio.IsEnabled = false;
            // diable the check/accept button, as the user will need to re-upload the photo
            ApplicationBar.Buttons.Remove(_acceptButton);
            _cancelButton.IsEnabled = false;
            _uploadButton.IsEnabled = false;
            if (!ApplicationBar.Buttons.Contains(_cancelButton))
            {
                ApplicationBar.Buttons.Add(_cancelButton);
            }
            _recorder.Start();
            RotateCircle.Begin();
        }

        /// <summary>
        /// Stop the recorder and setup button state for upload. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RecordButton_Unchecked(object sender, RoutedEventArgs e)
        {
            _recorder.Stop();
            SaveTempAudio(_recorder.Buffer);
            // renable the play button and stop the annimation. 
            PlayAudio.IsEnabled = true;
            RotateCircle.Stop();
            // enable upload button 
            _uploadButton.IsEnabled = true;
            _cancelButton.IsEnabled = true;

            // diable the check/accept button, as the user will need to re-upload the photo
            ApplicationBar.Buttons.Remove(_acceptButton);
            if (!ApplicationBar.Buttons.Contains(_cancelButton))
            {
                ApplicationBar.Buttons.Add(_cancelButton);
            }
        }

        /// <summary>
        /// Play button is activated and will play the temp file sound bite.  This
        /// button should only be active if there is a sound bite to playback. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void PlayButton_Checked(object sender, RoutedEventArgs e)
        {
            RecordAudio.IsEnabled = false;
            AudioPlayer.Play();
            RotateCircle.Begin();
        }

        /// <summary>
        /// Play button is deactivated and will stop playing the sound bite mid
        /// stream.  Subsequent playbacks will start at the begining of the file. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void PlayButton_Unchecked(object sender, RoutedEventArgs e)
        {
            AudioPlayer.Stop();
            RecordAudio.IsEnabled = true;
            RotateCircle.Stop();
        }

        /// <summary>
        /// Utility to save the sound bite to isolated storage.  Only one sound 
        /// file is saved to locale storage.  Subsequent calls will delete the 
        /// previously created call. 
        /// </summary>
        /// <param name="soundStream"></param>
        private void SaveTempAudio(MemoryStream soundStream)
        {
            if (soundStream == null)
            {
                System.Diagnostics.Debug.WriteLine("The sound stream is empty, cannot save.");
                return;
            }

            // check the audio player doesn't have a hold
            cleanupAudioPlayer();

            byte[] bytes = soundStream.GetWavAsByteArray(_recorder.SampleRate);

            using (IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isoStore.FileExists(_tempFileName))
                {
                    isoStore.DeleteFile(_tempFileName);
                }

                _tempFileName = string.Format("{0}.wav", DateTime.Now.ToFileTime());

                // copy the sounds bytes to disk
                _audioStream = isoStore.OpenFile(_tempFileName, FileMode.OpenOrCreate, FileAccess.ReadWrite);
                _audioStream.Write(bytes, 0, bytes.Length);

                // ready the playback component
                try
                {
                    AudioPlayer.MediaEnded += AudioPlayer_MediaEnded;
                    AudioPlayer.MediaFailed += AudioPlayer_MediaFailed;
                    AudioPlayer.SetSource(_audioStream);
                }
                catch (Exception ex)
                {
                    System.Diagnostics.Debug.WriteLine("Error setting player media" + ex.ToString());
                    _audioStream.Close();
                }

            }
        }

        /// <summary>
        /// Cleans up the audio player resources and closes any open audio streams. 
        /// </summary>
        private void cleanupAudioPlayer()
        {
            if (_audioStream != null)
            {
                // clean up the 
                AudioPlayer.Stop();
                AudioPlayer.Source = null;
                AudioPlayer.MediaEnded -= AudioPlayer_MediaEnded;
                AudioPlayer.MediaFailed -= AudioPlayer_MediaFailed;

                _audioStream.Close();
                _audioStream.Dispose();
            }
        }

        /// <summary>
        /// Audio recorder event thrown when the sound bite has finished playing
        /// and the PlayAudio button is turned to an untoggled state.  
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void AudioPlayer_MediaEnded(object sender, RoutedEventArgs e)
        {
            PlayAudio.IsChecked = false;
            System.Diagnostics.Debug.WriteLine("Media Playback ended. ");
        }

        /// <summary>
        /// Error event if the audio recorder comes up against one. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void AudioPlayer_MediaFailed(object sender, ExceptionRoutedEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("Error playing media." + e.ToString());
        }
    }
}