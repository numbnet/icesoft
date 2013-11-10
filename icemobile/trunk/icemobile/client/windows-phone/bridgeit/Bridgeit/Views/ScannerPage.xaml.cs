using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Devices;
using System.Windows.Threading;
using System.Windows.Media.Imaging;
using System.Windows.Input;
using ZXing;
using Bridgeit.ViewModels;
using Bridgeit.Resources;
using System.Text;

namespace Bridgeit.Views
{
    /// <summary>
    /// The <c>BarcodeReader</c> class loads the camera canvas as well
    /// as the xzing library for reading barcodes.  Once a bar code is 
    /// caputured the translated code and type is shown on the screen 
    /// and the user is give the choice to accept the code and return
    /// to the calling URL, rescan or return to the the calling page. 
    /// </summary>
    public partial class ScannerPage : BridgeitPhoneApplicationPage
    {
        /// <summary>
        /// Content type of return type. 
        /// </summary>
        public const string ContentType = "text/plain";

        private PhotoCamera _phoneCamera;
        private IBarcodeReader _barcodeReader;
        // timmer for firing the zxing library 
        private DispatcherTimer _scanTimer;
        private WriteableBitmap _previewBuffer;

        private ApplicationBarIconButton _acceptButton;

        public ScannerPage()
        {
            InitializeComponent();

            Loaded += BarcodeReader_Loaded;
        }


        void BarcodeReader_Loaded(object sender, RoutedEventArgs e)
        {
            // build the accept button    
            _bridgeitResponse.ContentType = ContentType;

            // build out the application bar. 
            // Set the page's ApplicationBar to a new instance of ApplicationBar.
            ApplicationBar = new ApplicationBar();

            // Create a the cammera shutter button and menu text
            _acceptButton = new ApplicationBarIconButton(new Uri("/Assets/common/check.png", UriKind.Relative));
            _acceptButton.Text = AppResources.BarCodeScannerAccept;
            _acceptButton.Click += _acceptButton_Click;
            _acceptButton.IsEnabled = false;
            ApplicationBar.Buttons.Add(_acceptButton);
        }

        /// <summary>
        /// Submit the bar code to bridgeit. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void _acceptButton_Click(object sender, EventArgs e)
        {

            // tack on contact info 
            buildBarcodeResponse();

            base.SubmitData_Click(sender, e);    
        }

        /// <summary>
        /// Build  the contct response used to send data back to bridgeit. 
        /// </summary>
        private void buildBarcodeResponse()
        {

            // add contact name for a test. 
            StringBuilder barcodeParams = new StringBuilder(tbBarcodeData.Text);
            
            // add the id=<element id>=UrlEncode<name=<contactName>&email=<contactEmail>&phone=<phoneNumber>
            _bridgeitResponse.Id = _bridgeitRequest.Id;
            _bridgeitResponse.IdValue = System.Net.HttpUtility.UrlEncode(barcodeParams.ToString());
            // replace the older '+' encoding with '%20'
            _bridgeitResponse.IdValue = _bridgeitResponse.IdValue.Replace("+", "%20");
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            // Initialize the camera object
            _phoneCamera = new PhotoCamera();
            _phoneCamera.Initialized += cam_Initialized;
            _phoneCamera.AutoFocusCompleted += _phoneCamera_AutoFocusCompleted;

            CameraButtons.ShutterKeyHalfPressed += CameraButtons_ShutterKeyHalfPressed;

            //Display the camera feed in the UI
            viewfinderBrush.SetSource(_phoneCamera);

            // This timer will be used to scan the camera buffer every 250ms and scan for any barcodes
            _scanTimer = new DispatcherTimer();
            _scanTimer.Interval = TimeSpan.FromMilliseconds(250);
            _scanTimer.Tick += (o, arg) => ScanForBarcode();
            // tap event for focus. 
            viewfinderCanvas.Tap += new EventHandler<GestureEventArgs>(focus_Tapped);

            base.OnNavigatedTo(e);
        }

        /// <summary>
        /// Hide the focus display brackets once focus has completed.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void _phoneCamera_AutoFocusCompleted(object sender, CameraOperationCompletedEventArgs e)
        {
            Deployment.Current.Dispatcher.BeginInvoke(delegate()
            {
                focusBrackets.Visibility = Visibility.Collapsed;
            });
        }

        /// <summary>
        /// Show/draw the focus bars on tap event. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void focus_Tapped(object sender, GestureEventArgs e)
        {
            if (_phoneCamera != null)
            {
                if (_phoneCamera.IsFocusAtPointSupported == true)
                {
                    // Determine the location of the tap.
                    Point tapLocation = e.GetPosition(viewfinderCanvas);

                    // Position the focus brackets with the estimated offsets.
                    focusBrackets.SetValue(Canvas.LeftProperty, tapLocation.X - 30);
                    focusBrackets.SetValue(Canvas.TopProperty, tapLocation.Y - 28);

                    // Determine the focus point.
                    double focusXPercentage = tapLocation.X / viewfinderCanvas.ActualWidth;
                    double focusYPercentage = tapLocation.Y / viewfinderCanvas.ActualHeight;

                    // Show the focus brackets and focus at point.
                    focusBrackets.Visibility = Visibility.Visible;
                    _phoneCamera.FocusAtPoint(focusXPercentage, focusYPercentage);
                }
            }
        }

        /// <summary>
        /// Half press event, call the focus method  and thus will throw a focused event at a later time. . 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void CameraButtons_ShutterKeyHalfPressed(object sender, EventArgs e)
        {
            _phoneCamera.Focus();
        }

        /// <summary>
        /// On navigate back, cancel the scan timer and dispose of the camera. 
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatingFrom(System.Windows.Navigation.NavigatingCancelEventArgs e)
        {
            //we're navigating away from this page, we won't be scanning any barcodes
            _scanTimer.Stop();

            if (_phoneCamera != null)
            {
                // Cleanup
                _phoneCamera.Dispose();
                _phoneCamera.Initialized -= cam_Initialized;
                CameraButtons.ShutterKeyHalfPressed -= CameraButtons_ShutterKeyHalfPressed;
            }
        }

        /// <summary>
        /// Camera initialized evcent. We support all barcode types at this point but could make
        /// support configurable in the future. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void cam_Initialized(object sender, Microsoft.Devices.CameraOperationCompletedEventArgs e)
        {
            if (e.Succeeded)
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    _phoneCamera.FlashMode = FlashMode.Off;
                    _previewBuffer = new WriteableBitmap((int)_phoneCamera.PreviewResolution.Width,
                                (int)_phoneCamera.PreviewResolution.Height);

                    _barcodeReader = new ZXing.BarcodeReader();

                    // By default, BarcodeReader will scan every supported barcode type
                    // If we want to limit the type of barcodes our app can read, 
                    // we can do it by adding each format to this list object

                    //var supportedBarcodeFormats = new List<BarcodeFormat>();
                    //supportedBarcodeFormats.Add(BarcodeFormat.QR_CODE);
                    //supportedBarcodeFormats.Add(BarcodeFormat.DATA_MATRIX);
                    //_bcReader.PossibleFormats = supportedBarcodeFormats;

                    // try harder...
                    //_barcodeReader.Options.TryHarder = true;

                    _barcodeReader.ResultFound += _bcReader_ResultFound;
                    _scanTimer.Start();
                });
            }
            else
            {
                Dispatcher.BeginInvoke(() =>
                {
                    MessageBox.Show(AppResources.BarCodeScannerCameraError);
                });
            }
        }

        /// <summary>
        /// Result found,  update the screen with format and code text.  Show the accept button
        /// that allows user to submit the code back to bridgeit. 
        /// </summary>
        /// <param name="obj"></param>
        void _bcReader_ResultFound(Result obj)
        {
            // If a new barcode is found, vibrate the device and display the barcode details in the UI
            if (!obj.Text.Equals(tbBarcodeData.Text))
            {
                VibrateController.Default.Start(TimeSpan.FromMilliseconds(100));
                tbBarcodeType.Text = obj.BarcodeFormat.ToString();
                tbBarcodeData.Text = obj.Text;
                // update the submit value
                _acceptButton.IsEnabled = true;
            }
        }

        /// <summary>
        /// Timer calls this method every interval to try and find a valid barcode with what 
        /// evet image is in the viewfinder. 
        /// </summary>
        private void ScanForBarcode()
        {
            //grab a camera snapshot
            _phoneCamera.GetPreviewBufferArgb32(_previewBuffer.Pixels);
            _previewBuffer.Invalidate();

            //scan the captured snapshot for barcodes
            //if a barcode is found, the ResultFound event will fire
            _barcodeReader.Decode(_previewBuffer);

        }
    }
}