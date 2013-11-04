using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bridgeit.Models
{
    /// <summary>
    /// The <c>BridgeItResponse</c> encapsulates the name/values pairs needed
    /// to generate a response uri when navigating away from a successfull
    /// bridgeIt native component interaction.  The values will then be 
    /// processed by the bridgeIt api and applied to the website using the 
    /// the echo value the h request parameter.  Or in other words will 
    /// execute the h value as  JavaScript function on the client side 
    /// <list>
    /// <item><id>= result of command associated with the id.</item>
    /// <item>!r= server response text.</item>
    /// <item>!p= preview/thumbnail of captured content (data-url, base64 encoded)</item>
    /// <item>!h= echo value of h request parameter, generally JavaScript function to call on the post back</item>
    /// <item>!c= cloudPushId</item>
    /// <item>If request options contains enc=base64, the entire response URL should be encoded as 
    ///       #icemobilesx_<data> where <data> is in Base64 format with "." in place of "/" and "~" in place of "=".</item>
    /// </list>
    /// An example reuest would look like: 
    /// 
    /// 
    /// </summary>
    class BridgeItResponse
    {
        /// <summary>
        /// Server response text key.
        /// </summary>
        private const string RKey = "&!r=";

        /// <summary>
        /// Preview/thumbnail of captured content (data-url) key.
        /// </summary>
        private const string PKey = "&!p=";
        
        /// <summary>
        /// echo value of h request parameter key
        /// </summary>
        private const string HKey = "&!h=";

        /// <summary>
        /// cloudPushId key
        /// </summary>
        private const string CKey = "!c=";

        // uri building chars. 
        private const char Amp = '&';
        private const char Equals = '=';
        private const char SemiColon = ';';
        private const char Comma = ',';
        private const string Namespace = "#icemobilesx_";
        private const string DataContentType = "data:";
        private const string Base64Encoding = "base64";

        /// <summary>
        ///result of command associated with id 'id='
        /// </summary>
        public string Id { get; set; }

        /// <summary>
        /// server response text '!r='
        /// </summary>
        public string ServerResponse { get; set; }

        /// <summary>
        /// Preview data stream in base64 encoding. '!p='
        /// </summary>
        public string PreviewContent { get; set; }

        /// <summary>
        /// Echo value. '!h='
        /// </summary>
        public string EchoValue { get; set; }

        // <summary>
        /// Cloud push id '!c='
        /// </summary>
        public string CloudPushId;

        /// <summary>
        /// Content type of preview data. 
        /// </summary>
        private string contentType;

        /// <summary>
        /// Create a new BridgeItRepsonse. 
        /// </summary>
        /// <param name="contentType">contentType of the data privew !p= data. </param>
        public BridgeItResponse(string contentType)
        {
            this.contentType = contentType;
        }

        /// <summary>
        /// Given the the member variable values a corresponding bridgeit request
        /// URL is created and can be passed as to the appropriate handles to navigate
        /// the application back to the calling application. 
        /// </summary>
        /// <param name="postBackUrl"></param>
        /// <param name="isBase64"></param>
        /// <returns></returns>
        public string generateResponseUri(string postBackUrl, IDictionary<String,String> options)
        {
            // base url construction
            StringBuilder responseUrl = new StringBuilder(postBackUrl);
            // the responseURL allready ends with #icemobilesx but the callback is expecting #icemobilesx_
            responseUrl.Append(Namespace);
            StringBuilder responseParams = new StringBuilder();
            // append id if specified by the calling component 
            if (Id != null)
            {
                responseParams.Append(Id).Append(Equals);
            }
            // servers response text from data upload
            if (ServerResponse != null)
            {
                //// json, {data:<server-response-value>}
                responseParams.Append(RKey).Append(ServerResponse);
            }
            // echo value of h request param
            if (EchoValue != null)
            {
                EchoValue = EchoValue.Substring(1);
                EchoValue = EchoValue.Replace("=", "%3d");
                responseParams.Append(HKey).Append(EchoValue);
            }
            // cloud push id. 
            if (CloudPushId != null)
            {
                responseParams.Append(CKey).Append(CloudPushId);
            }
            // preview/thumbnail of capture data, where applicable 
            if (PreviewContent != null)
            {
                // Microsoft Internet Explorer has a maximum uniform resource locator (URL) length of 2,083 characters. 
                // Internet Explorer also has a maximum path length of 2,048 characters. This limit applies to both 
                // POST request and GET request URLs. 
                int length = PreviewContent.Length;
                System.Diagnostics.Debug.WriteLine("Preview Length " + length);
                responseParams.Append(PKey).Append(DataContentType).Append(contentType).Append(SemiColon)
                    .Append(Base64Encoding).Append(Comma).Append(PreviewContent);
            }

            String responseParameters = responseParams.ToString(); 
            // check if we need to encode the url as base 64
            string encoding = null;
            bool found = options.TryGetValue(BridgeItRequest.OptionsEncodingKey, out encoding);
            // check for en-base64 in the options map. 
            if (found && encoding.Equals(Base64Encoding))
            {
                //responseParameters = System.Convert.ToBase64String(GetBytes(responseParameters));
                responseParameters = System.Convert.ToBase64String(System.Text.UTF8Encoding.UTF8.GetBytes(responseParameters));
                // jquery specific encodings.   
                responseParameters = responseParameters.Replace('/', '.');
                responseParameters = responseParameters.Replace('=', '~');
            }
            string response = responseUrl.ToString() + responseParameters;
            return response;
        }

        /// <summary>
        /// Simple string to byte[] that doesn't touch the encoding. 
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        private static byte[] GetBytes(string str)
        {
            byte[] bytes = new byte[str.Length * sizeof(char)];
            System.Buffer.BlockCopy(str.ToCharArray(), 0, bytes, 0, bytes.Length);
            return bytes;
        }
    }
}
