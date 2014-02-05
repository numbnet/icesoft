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
    public class BridgeItResponse
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
        public const char Amp = '&';
        public const char EqualsChar = '=';
        public const char SemiColon = ';';
        public const char Comma = ',';
        public const string Namespace = "#icemobilesx";
        public const string Underscore = "_";
        public const string DataContentType = "data:";
        public const string Base64Encoding = "base64";

        /// <summary>
        ///result of command associated with id 'id='
        /// </summary>
        public string Id { get; set; }
        public string IdValue { get; set; }

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
        public string ContentType { get; set; }

        /// <summary>
        /// Create a new BridgeItRepsonse. 
        /// </summary>
        /// <param name="contentType">contentType of the data privew !p= data. </param>
        public BridgeItResponse(string contentType)
        {
            this.ContentType = contentType;
        }

        public BridgeItResponse()
        {

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

            // check if we need to encode the url as base 64
            string encoding = null;
            bool found = options.TryGetValue(BridgeItRequest.OptionsEncodingKey, out encoding);
            bool base64Encoding = false;
            // check for en-base64 in the options map. 
            if (found && encoding.Equals(Base64Encoding))
            {
                base64Encoding = true;
            }

            // base url construction
            StringBuilder baseUrl = new StringBuilder(postBackUrl);
            // the responseURL should ends with #icemobilesx but the callback is expecting #icemobilesx_
            // but we only to this if the icemobilesx was found as it means our javascript made the call. 
            if (postBackUrl != null && postBackUrl.EndsWith(Namespace)) baseUrl.Append(Underscore);

            // url params to be appended to the basue url. 
            StringBuilder responseParams = new StringBuilder();
            // append id if specified by the calling component 
            if (Id != null)
            {
                if (!base64Encoding) {
                    responseParams.Append(Id).Append(EqualsChar).Append(IdValue);
                }
                // double encode for base64 enocding 
                else
                {
                    String id = Id + EqualsChar + IdValue;
                    responseParams.Append(System.Net.HttpUtility.UrlEncode(id));
                }
            }
            // servers response text from data upload
            if (ServerResponse != null)
            {
                //// plain text server response copy
                responseParams.Append(RKey).Append(ServerResponse);
            }
            // echo value of h request param
            if (EchoValue != null)
            {
                EchoValue = EchoValue.Substring(1);
                // quick encoding of the '=' in the 
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
                responseParams.Append(PKey).Append(DataContentType).Append(ContentType).Append(SemiColon)
                    .Append(Base64Encoding).Append(Comma).Append(PreviewContent);
            }
            // finalize the response string.  
            String responseParameters = responseParams.ToString();
            // check for en-base64 in the options map.
            if (base64Encoding)
            {
                //responseParameters = System.Convert.ToBase64String(GetBytes(responseParameters));
                responseParameters = System.Convert.ToBase64String(System.Text.UTF8Encoding.UTF8.GetBytes(responseParameters));
                // jquery specific encodings.   
                responseParameters = responseParameters.Replace('/', '.');
                responseParameters = responseParameters.Replace('=', '~');
            }
            string responseUrl = baseUrl.ToString() + responseParameters;
            return responseUrl;
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
