using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bridgeit.Models
{
    /// <summary>
    /// BridgeitParams stores the parsed values of know keys in the a Bridgeit url. 
    /// The know keys are as follows:
    /// <list>
    ///      id: id of the calling html element
    ///      c: command name with parameters
    ///      u: URL to POST command data
    ///      r: URL to return to in the browser
    ///      p: parameters for POST to server
    ///      o: options affecting overall request processing
    ///           width & height, known to date. 
    ///      s: splash screen parameters
    ///      h: echo value
    ///      ub: not sure on this one yet...
    /// </list>
    /// There are also params that are used to pass data back to the calling url.   
    /// </summary>
    public class BridgeItRequest
    {
        /// <summary>
        /// command name with parameters key.
        /// </summary>
        public const string MsKey = "ms";

        /// <summary>
        /// id of calling element name with parameters key.
        /// </summary>
        public const string IdKey = "id";

        /// <summary>
        /// Ub parameters key.
        /// </summary>
        public const string UbKey = "ub";

        /// <summary>
        /// command name with parameters key.
        /// </summary>
        public const string CKey = "c";

        /// <summary>
        /// URL to POST command data key
        /// </summary>
        public const string UKey = "u";
        
        /// <summary>
        ///URL to return to in the browser
        /// </summary>
        public const string RKey = "r";

        /// <summary>
        /// Parameters for POST to server key
        /// </summary>
        public const string PKey = "p";

        /// <summary>
        /// Options affecting overall request processing key
        /// </summary>
        public const string OKey = "o";

        /// <summary>
        /// Encoding key, base64 value or nothing 
        /// </summary>
        public const string OptionsEncodingKey = "enc";
        
        /// <summary>
        /// Width option for passing in most cases preview width size. 
        /// </summary>
        public const string OptionsWidthKey = "width";

        /// <summary>
        /// Height option for passing in most cases preview height size. 
        /// </summary>
        public const string OptionsHeightKey = "height";

        /// <summary>
        /// splash screen parameters
        /// </summary>
        public const string SKey = "s";

        /// <summary>
        /// echo value
        /// </summary>
        public const string HKey = "h";


        /// <summary>
        /// command name with parameters 'id='
        /// </summary>
        private string id;

        /// <summary>
        /// 'ub='
        /// </summary>
        private string ub;

        /// <summary>
        /// command name with parameters 'c:'
        /// </summary>
        private string commandName;

        /// <summary>
        /// URL to POST command data 'u:'
        /// </summary>
        private string postBackURL;

        /// <summary>
        /// parameters for POST to server 'p:' 
        /// </summary>
        private string parameters;

        /// <summary>
        /// URL to return to in the browser 'r:' 
        /// </summary>
        private string returnURL;

        /// <summary>
        /// options affecting overall request processing 'o:' 
        /// </summary>
        private IDictionary<string, string> options;

        /// <summary>
        /// splash screen parameters 's:' 
        /// </summary>
        private string splashScreen;

        /// <summary>
        /// Url hashcode fragement used for restoring 'h:'
        /// </summary>
        private string echoValue;

        /// <summary>
        /// Parse out the brideit specific paramaters. 
        /// </summary>
        /// <param name="queryStrings">NavigationContext.QueryString value </param>
        public BridgeItRequest(IDictionary<string, string> queryStrings, String decodedUrl)
        {
            // url hashcode fragement used for restoring 
            queryStrings.TryGetValue(IdKey, out id);
            queryStrings.TryGetValue(CKey, out commandName);
            queryStrings.TryGetValue(UKey, out postBackURL);
            queryStrings.TryGetValue(RKey, out returnURL);
            queryStrings.TryGetValue(PKey, out parameters);
            queryStrings.TryGetValue(SKey, out splashScreen);
            queryStrings.TryGetValue(HKey, out echoValue);

            // pull out the ms query string which we add via the AssociationUriMapper
            string msQueryString; 
            queryStrings.TryGetValue(MsKey, out msQueryString);
            Dictionary<string,string> subCommands = parseMsQueryString(msQueryString);
            if (subCommands != null)
            {
                subCommands.TryGetValue(IdKey, out id);
                subCommands.TryGetValue(UbKey, out ub);
            }

            // TODO: parse out the options to 
            String optionstring;
            queryStrings.TryGetValue(OKey, out optionstring);
            options = new Dictionary<string, string>();
            if (optionstring != null)
            {
                string[] pieces = optionstring.Split('=');
                if (pieces != null && pieces.Length > 0)
                {
                    for (int i = 0, max = pieces.Length; i < max; i += 2)
                    {
                        options.Add(pieces[i], pieces[i + 1]);
                    }
                }
            }
        }

        private Dictionary<string,string> parseMsQueryString(string msQueryString)
        {
            // deocde
            if (msQueryString != null)
            {
                msQueryString = System.Net.HttpUtility.UrlDecode(msQueryString);
                // split on the &
                string[] parts = msQueryString.Split('&');
                Dictionary<string, string> store = new Dictionary<string, string>();
                foreach (String nameValuePairs in parts)
                {
                    String[] pairs = nameValuePairs.Split('=');
                    if (pairs.Length == 2)
                    {
                        store.Add(pairs[0], pairs[1]);
                    }
                }
                return store;
            }
            return null;
        }

        /// <summary>
        /// <value>Property <c>CommandName</c> bridgeIt command call,  camera, microphone, etc.</value>
        /// </summary>
        public string Id
        {
            get { return id; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>Ub</c> bridgeIt var.</value>
        /// </summary>
        public string Ub
        {
            get { return Ub; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>CommandName</c> bridgeIt command call,  camera, microphone, etc.</value>
        /// </summary>
        public string CommandName
        {
            get { return commandName; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>PostBackURL</c> represents the post back url for data upload.</value>
        /// </summary>
        public string PostBackURL
        {
            get { return postBackURL; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>ReturnURL</c> URL to return to in the browser.</value>
        /// </summary>
        public string ReturnURL
        {
            get { return returnURL; }
            set { returnURL = value; }
        }

        /// <summary>
        /// <value>Property <c>Parameters</c> misc. paramaters that can be passed into request</value>
        /// </summary>
        public string Parameters
        {
            get { return parameters; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>Parameters</c> options affecting overall request processing
        /// </summary>
        public IDictionary<String,String> Options
        {
            get { return options; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>SplashScreen</c> splash screen parameters
        /// </summary>
        public string SplashScreen
        {
            get { return splashScreen; }
            private set { }
        }

        /// <summary>
        /// <value>Property <c>EchoValue</c> echo value
        /// </summary>
        public string EchoValue
        {
            get { return echoValue; }
            private set { }
        }
    }
}
