using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.ComponentModel;

using Microsoft.Phone.Shell;
using Microsoft.Xna.Framework.Media;
using System.Windows;

namespace Bridgeit.Models.FileHandlers
{
    /// <summary>
    /// <c>FileUploaderWorkder</c> does the lifting of uploading a file name found in isoloated storage
    /// to the specified uploadURL. 
    ///    Maximum upload file sizes for Phone
    ///         Over cellular connection - 5 MB
    ///         Over Wi-Fi connection with battery power - 20 MB
    ///         Over Wi-Fi connection with external power - 100 MB
    /// TODO - register for data cap events and and max file size events. 
    /// </summary>
    class FileUploadWorker : BackgroundWorker
    {
        // lock to make sure both the request and response thread complete before 
        // upload_DoWork returns. 
        private ManualResetEvent requestDone = new ManualResetEvent(false);

        // file upload buffer size. 
        private const int BUFFER_SIZE = 1024;

        // file upload properties
        private string _fileName;
        private string _idName;
        private string _contentType;
        private string _uploadUrl;
        // running boundary string used to speerated the image data into blocks. 
        private string _boundary;

        // return string from server after upload has finished. 
        private string _responseString;

        public FileUploadWorker(string _contentType)
        {
            this._contentType = _contentType;
        }

        /// <summary>
        /// Sets the upload url and file name used for the multipart response. 
        /// </summary>
        /// <param name="fileName">filename used in request</param>
        /// <param name="idName">id of the calling html elmeent</param>
        /// <param name="uploadURL">url to send request to</param>
        public void setUploadLocation(string fileName, string idName, string uploadURL)
        {
            this._fileName = fileName;
            this._uploadUrl = uploadURL;
            this._idName = idName;
        }

        /// <summary>
        /// Get the response returned after a successfull upload.  The response
        /// is returned in JSON format and can contain many different value pairs. 
        /// </summary>
        /// <returns>server response. </returns>
        public string getResponse()
        {
            return _responseString;
        }

        /// <summary>
        /// Initialize the synchronous upload, this method will block until file upload is complete or 
        /// returns with an error message.  During the upload the ReportProgress events are fired 
        /// for the upload progress. The work will also check to see if a cancel has been requested
        /// during the upload and cancel the request. 
        /// </summary>
        public void upload_DoWork(object sender, DoWorkEventArgs e)
        {
            // check to make sure the bridgeit and the app send a valid url. 
            Uri uri = null;
            try { 
                uri = new Uri(_uploadUrl);
            }
            catch (Exception)
            {
                MessageBox.Show(Bridgeit.Resources.AppResources.CameraAppProgresIndicatorFailure2 + _uploadUrl);
            }

            if (uri != null) { 
                // Create a boundary for HTTP request.
                _boundary = "----------------------------" + DateTime.Now.Ticks.ToString("x");

                // Create a HttpWebrequest object to the desired URL.
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);

                // add test headers
                WebHeaderCollection header = request.Headers;
                // Create an instance of the RequestState and assign the previous myHttpWebRequest1
                // object to it's request field.  
                request.ContentType = "multipart/form-data; boundary=" + _boundary;
                request.Method = "POST";
                request.BeginGetRequestStream(new AsyncCallback(GetRequestStreamCallback), request);
                // wait until the response comes back. 
                requestDone.WaitOne();

                // clean up the listener
                (sender as BackgroundWorker).DoWork -= upload_DoWork;
            }
        }

        /// <summary>
        ///     Send a File with initialized request.
        /// </summary>
        private void GetRequestStreamCallback(IAsyncResult asynchronousResult)
        {
            string contentType = _contentType;
            HttpWebRequest request = (HttpWebRequest)asynchronousResult.AsyncState;
            Stream memStream = request.EndGetRequestStream(asynchronousResult);
            byte[] boundarybytes = System.Text.Encoding.UTF8.GetBytes("\r\n--" + _boundary + "\r\n");
            memStream.Write(boundarybytes, 0, boundarybytes.Length);

            // Send headers.
            string headerTemplate = "Content-Disposition: form-data; ";
            headerTemplate += "name=\"{0}\"; filename=\"{1}\"\r\nContent-Type: " + contentType + "\r\n\r\n";
            string header = string.Format(headerTemplate, _idName, _fileName);
            byte[] headerbytes = System.Text.Encoding.UTF8.GetBytes(header);
            memStream.Write(headerbytes, 0, headerbytes.Length);

            // check if the fileName is in the istore. 
            using (IsolatedStorageFile isStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (isStore.FileExists(_fileName))
                {
                    using (IsolatedStorageFileStream targetStream = 
                        isStore.OpenFile(_fileName, FileMode.Open, FileAccess.Read))
                    {
                        // Initialize the buffer for 4KB disk pages.
                        byte[] readBuffer = new byte[4096];
                        int bytesRead = -1;
                        long read = 0;
                        long length = targetStream.Length;
                        int percent = 0;
                        this.WorkerReportsProgress = true;
                        // Copy the image to the local folder. 
                        while ((bytesRead = targetStream.Read(readBuffer, 0, readBuffer.Length)) > 0
                                    && this.CancellationPending != true)
                        {
                            read += bytesRead;
                            memStream.Write(readBuffer, 0, bytesRead);
                            percent = (int)Math.Ceiling(((double)read / length) * 100);
                            //System.Diagnostics.Debug.WriteLine("uploading.... " + percent);
                            // fire the progress upload event. 
                            this.ReportProgress(percent);
                        }
                        System.Diagnostics.Debug.WriteLine("URL: " + request.RequestUri);
                        System.Diagnostics.Debug.WriteLine("File upload complete. " + 
                            ((double)length / 1024 / 1024) + " MB");
                    }
                }
                else
                {
                    // copy the file stream bytes
                    findFilePathinMediaLibrary(_fileName, memStream);
                }
            }

            // Send last boudary of the file ( the footer) for specify post request is finish.
            byte[] boundarybytesend = System.Text.Encoding.UTF8.GetBytes("\r\n--" + _boundary + "--\r\n");
            memStream.Write(boundarybytesend, 0, boundarybytesend.Length);
            memStream.Flush();
            memStream.Close();

            // Start the asynchronous operation to get the response
            if (this.CancellationPending == false)
            {
                request.BeginGetResponse(new AsyncCallback(GetResponseCallback), request);
            }
        }

        /// <summary>
        /// Get the Response server.
        /// </summary>
        private void GetResponseCallback(IAsyncResult asynchronousResult)
        {
            try
            {
                HttpWebRequest request = (HttpWebRequest)asynchronousResult.AsyncState;
                // End the operation
                HttpWebResponse response = (HttpWebResponse)request.EndGetResponse(asynchronousResult);
                Stream streamResponse = response.GetResponseStream();
                StreamReader streamRead = new StreamReader(streamResponse);
                _responseString = streamRead.ReadToEnd();
                System.Diagnostics.Debug.WriteLine("Response received" + _responseString);

                // Close the stream object
                streamResponse.Close();
                streamRead.Close();

                // Release the HttpWebResponse
                response.Close();
                requestDone.Set();
            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error gettting response " + e.Message);
            }
            return;
        }

        /// <summary>
        /// Ttry and load the _fileName by looking at the media library pictures album paths.
        /// then copy the found images bytes to our uplod stream. 
        /// </summary>
        private void findFilePathinMediaLibrary(string _fileName, Stream memStream)
        {
            // if not then try and fine the photo in the media store. 
            Stream imageStream = null;
            try
            {
                MediaLibrary mediaLibrary = new MediaLibrary();
                // debug, print out the picutre names in each album
                PictureAlbumCollection pictureCollections = mediaLibrary.RootPictureAlbum.Albums;
                // try and find the _fileName in one of the albums. 
                foreach (var pictureAlbum in pictureCollections)
                {

                    PictureCollection photoFromLibrary = pictureAlbum.Pictures;
                    IEnumerable<Picture> photoQuery = from photo in photoFromLibrary
                                                      where photo.Name == _fileName
                                                      select photo;
                    if (photoQuery.Any())
                    {
                        Picture foundPicture = photoQuery.First();
                        if (foundPicture != null)
                        {
                            imageStream = foundPicture.GetImage().AsInputStream().AsStreamForRead();
                            // Initialize the buffer for 4KB disk pages.
                            byte[] readBuffer = new byte[4096];
                            int bytesRead = -1;
                            long length = imageStream.Length;
                            int percent = 0;
                            int read = 0;

                            // stream the bights. 
                            while ((bytesRead = imageStream.Read(readBuffer, 0, readBuffer.Length)) > 0)
                            {
                                memStream.Write(readBuffer, 0, bytesRead);
                                read += bytesRead;
                                percent = (int)Math.Ceiling(((double)read / length) * 100);
                                this.ReportProgress(percent);
                            }
                            System.Diagnostics.Debug.WriteLine("File upload complete. " + ((double)length / 1024 / 1024) + " MB");

                            break;
                        }
                    }
                    else
                    {
                        System.Diagnostics.Debug.WriteLine("No media found for :" + _fileName);
                    }
                }

            }
            catch (Exception e)
            {
                System.Diagnostics.Debug.WriteLine("Error finding media file path." + e.Message);
            }
            finally
            {
                if (imageStream != null)
                {
                    imageStream.Close();
                    imageStream.Dispose();
                }
            }
        }
    }
}
