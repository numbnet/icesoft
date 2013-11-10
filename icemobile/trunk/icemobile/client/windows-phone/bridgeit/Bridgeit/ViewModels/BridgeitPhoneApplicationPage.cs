using Bridgeit.Models;
using Microsoft.Phone.Controls;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media.Imaging;

namespace Bridgeit.ViewModels
{
    public class BridgeitPhoneApplicationPage : PhoneApplicationPage
    {

        public const char Amp = '&';

        public const char EqualsChar = '=';

        /// <summary>
        ///  store of paramaters that made up the orginal bridgeIt request. 
        /// </summary>
        protected BridgeItRequest _bridgeitRequest;

        /// <summary>
        /// store of paramaters that will make up the pridgeIt repsonse.
        /// </summary>
        protected BridgeItResponse _bridgeitResponse;

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
                _bridgeitResponse = new BridgeItResponse();
            }
        }

        /// <summary>
        /// submit the data to the _bridgeitResponse
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected async void SubmitData_Click(object sender, EventArgs e)
        {
            // create an isnt of the Bridgeit Response
            _bridgeitResponse.EchoValue = _bridgeitRequest.EchoValue;

            String url = _bridgeitResponse.generateResponseUri(
                _bridgeitRequest.ReturnURL, _bridgeitRequest.Options);

            // go back to the calling URL. 
            var success = await Windows.System.Launcher.LaunchUriAsync(new Uri(url));
            if (success)
            {
                System.Diagnostics.Debug.WriteLine("Accepted device data going back... " + url);
            }
            else
            {
                System.Diagnostics.Debug.WriteLine("Failed to send device data  can't navigate ... " + url);
                await Windows.System.Launcher.LaunchUriAsync(new Uri(_bridgeitRequest.ReturnURL));
            }
        }

        protected async void Cancel_Click(object sender, EventArgs e)
        {
            // navigate back the 
            var options = new Windows.System.LauncherOptions();
            options.TreatAsUntrusted = true;
            string url = _bridgeitRequest.ReturnURL;
            var success = await Windows.System.Launcher.LaunchUriAsync(new Uri(url));
            if (success)
            {
                System.Diagnostics.Debug.WriteLine("Canceling going back... " + url);
            }
        }

        /// <summary>
        /// Resizes the given imageStream to the specified width and height and encodes the new images
        /// bite in base64 enocding 
        /// </summary>
        /// <param name="imageStream">Name of stream to scale</param>
        /// <param name="width">thumbnail width</param>
        /// <param name="height">thumbnail height</param>
        /// <returns>Thumbnail image sized to the specified width and height.</returns>
        protected string CreateResizedBase64Thumbnail(Stream imageStream, int width, int height)
        {
            WriteableBitmap wbOutput = Microsoft.Phone.PictureDecoder.DecodeJpeg(imageStream, width, height);
            System.IO.MemoryStream ms = new MemoryStream();
            wbOutput.SaveJpeg(ms, wbOutput.PixelWidth, wbOutput.PixelHeight, width, height);
            byte[] smallBinaryData = ms.GetBuffer();
            string encodedThumbNail = System.Convert.ToBase64String(smallBinaryData);
            ms.FlushAsync();
            return encodedThumbNail;
        }
    }
}
