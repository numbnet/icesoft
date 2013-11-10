using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Phone.UserData;
using Bridgeit.Resources;
using Bridgeit.ViewModels;
using System.Text;

namespace Bridgeit.Views
{
    public partial class ContactsPage : BridgeitPhoneApplicationPage
    {

        /// <summary>
        /// Contact image preview thumbnail width
        /// </summary>
        public const int ThumbWidth = 32;

        /// <summary>
        /// Contact image preview thumbnail height
        /// </summary>
        public const int ThumbHeight = 32;

        /// <summary>
        /// Content type of contact preview. 
        /// </summary>
        public const string ContentType = "image/jpg";

        /// <summary>
        /// contact name key. 
        /// </summary>
        public const string ContactDisplayName_key = "name=";
        /// <summary>
        /// phone number name key. 
        /// </summary>
        public const string ContactPhone_key = "phone=";

        /// <summary>
        /// email name key. 
        /// </summary>
        public const string ContactEmail_key = "email=";

        private Contact _selectedContact;

        // search button
        private ApplicationBarIconButton _searchButton;
        // accept button 
        private ApplicationBarIconButton _acceptButton;

        // toggle state for search textbox
        private bool _searchFieldVisible = false;

        /// <summary>
        /// Create a new instance of <c>ContactsPage</c> and starts does an
        /// empty search so that all contact are displayed. Once displayed 
        /// the search feaatures can be used to filter the list. 
        /// </summary>
        public ContactsPage()
        {
            InitializeComponent();

            // setup buttons and load intial contacts data set. 
            Loaded += ContactsPage_Loaded;

            // enables back button to hide search input before navigation back. 
            BackKeyPress += OnBackKeyPress;
        }

        /// <summary>
        /// Builds the application bar buttons and starts the initial contacts lookup.  
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ContactsPage_Loaded(object sender, RoutedEventArgs e)
        {
            _bridgeitResponse.ContentType = ContentType;

            // build out the application bar. 
            // Set the page's ApplicationBar to a new instance of ApplicationBar.
            ApplicationBar = new ApplicationBar();

            // Create a the cammera shutter button and menu text
            _searchButton = new ApplicationBarIconButton(new Uri("/Assets/common/search.png", UriKind.Relative));
            _searchButton.Text = AppResources.ContactsSearch;
            _searchButton.Click += SearchButton_Click;
            _searchButton.IsEnabled = false;
            ApplicationBar.Buttons.Add(_searchButton);

            // create the accept button
            _acceptButton = new ApplicationBarIconButton(new Uri("/Assets/common/check.png", UriKind.Relative));
            _acceptButton.Text = AppResources.ContractsCancel;
            _acceptButton.Click += AcceptButton_Click;
            _acceptButton.IsEnabled = false;
            ApplicationBar.Buttons.Add(_acceptButton);

            // do initial empty query of contacts for display
            searchContacts("");
        }

        /// <summary>
        /// Changes the visiblity of the textBox used for search query
        /// input.  When made vsibily focus is given to the textBox. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void SearchButton_Click(object sender, EventArgs e)
        {
            // show textbox in the header bar. 
            if (_searchFieldVisible)
            {
                setSearchFieldVisibility(false);
            }
            else
            {
                setSearchFieldVisibility(true);
                searchQuery.Focus();
            }

        }

        /// <summary>
        /// The accept button setups the bridgeIt response by appending the contact image 
        /// preview if any and building otu the contact querty string. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void AcceptButton_Click(object sender, EventArgs e)
        {
            // add preview image to resonse
            buildContactPreviewImage();

            // tack on contact info 
            buildContactResponse();

            // make the base call to navigate back to the calling page. 
            // submit the device status data as a response to the post backk data. 
            base.SubmitData_Click(sender, e);
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
            // first bck button we toggle back to the hidden view.  
            if (_searchFieldVisible)
            {
                setSearchFieldVisibility(false);
                e.Cancel = true;
            }
            // if no search field then we go back as per usual
            else
            {
                base.OnBackKeyPress(e);
            }
        }

        /// <summary>
        /// Toggles the visiblity of the search field text fields and 
        /// the page title text fields.  
        /// </summary>
        /// <param name="searchFieldVisible"></param>
        private void setSearchFieldVisibility(bool searchFieldVisible)
        {
            if (searchFieldVisible)
            {
                contactTitleGrid.Visibility = Visibility.Collapsed;
                searchQuery.Visibility = Visibility.Visible;
                _searchFieldVisible = true;
            }
            else
            {
                contactTitleGrid.Visibility = Visibility.Visible;
                searchQuery.Visibility = Visibility.Collapsed;
                _searchFieldVisible = false;
            }
        }

        /// <summary>
        /// Search contacts starts a new search query with the given search string. 
        /// </summary>
        /// <param name="searchString">term to search for in the context query, can be 
        /// empty string.</param>
        private void searchContacts(string searchString)
        {
            // diable search button during a search. 
            _searchButton.IsEnabled = false;

            // instance of devices contacts
            Contacts contactSearch = new Contacts();

            // show the searching text. 
            ContactResultsLabel.Visibility = Visibility.Visible;

            // Identify the method that runs after the asynchronous search completes.
            contactSearch.SearchCompleted += new EventHandler<ContactsSearchEventArgs>(Contacts_SearchCompleted);

            // Start the asynchronous search.
            contactSearch.SearchAsync(searchString, FilterKind.DisplayName, "Contact Search");
        }

        /// <summary>
        /// Search completed event. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Contacts_SearchCompleted(object sender, ContactsSearchEventArgs e)
        {
            try
            {
                // enable the the search button for first load 
                _searchButton.IsEnabled = true;

                // Bind the results to the user interface.
                ContactResultsData.DataContext = e.Results;
            }
            catch (System.Exception)
            {
                System.Diagnostics.Debug.WriteLine("Error getting search results.");
            }

            // if now data show the no results messge. 
            if (!ContactResultsData.Items.Any())
            {
                ContactResultsLabel.Text = AppResources.ContactsNoResults;
                ContactResultsLabel.Visibility = Visibility.Visible;
            }
            // hide the results message
            else
            {
                ContactResultsLabel.Visibility = Visibility.Collapsed;
            }

        }

        /// <summary>
        /// A search contct results was tapped.  The tapped contact is set as the 
        /// selected contact and will be used to generate the contact response. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void contact_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            _selectedContact = (sender as ListBox).SelectedItem as Contact;
            _acceptButton.IsEnabled = true;
        }

        /// <summary>
        /// Text change event on the search textBox, each event will generate a 
        /// ne search query. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void search_TextChanged(object sender, TextChangedEventArgs e)
        {
            searchContacts(searchQuery.Text);
        }

        /// <summary>
        /// Build  the contct response used to send data back to bridgeit. 
        /// </summary>
        private void buildContactResponse()
        {
            
            // add contact name for a test. 
            StringBuilder contactParams = new StringBuilder();
            contactParams.Append(ContactDisplayName_key).Append(_selectedContact.DisplayName);
            // add an email if present
            IEnumerable<ContactEmailAddress> emails = _selectedContact.EmailAddresses;
            if (emails.Any())
            {
                contactParams.Append(Amp).Append(ContactEmail_key).Append(emails.First().EmailAddress);
            }
            // add a phone number if present
            IEnumerable<ContactPhoneNumber> phoneNumbers = _selectedContact.PhoneNumbers;
            if (phoneNumbers.Any())
            {
                contactParams.Append(Amp).Append(ContactPhone_key).Append(phoneNumbers.First().PhoneNumber);
            }

            // add the id=<element id>=UrlEncode<name=<contactName>&email=<contactEmail>&phone=<phoneNumber>
            _bridgeitResponse.Id = _bridgeitRequest.Id;
            _bridgeitResponse.IdValue = System.Net.HttpUtility.UrlEncode(contactParams.ToString());
            // replace the older '+' encoding with '%20'
            _bridgeitResponse.IdValue = _bridgeitResponse.IdValue.Replace("+", "%20");
        }

        /// <summary>
        /// Build an image preview of the contacts picture 
        /// </summary>
        private void buildContactPreviewImage()
        {
            // build the preview image. 
            System.IO.Stream imageStream = _selectedContact.GetPicture();
            if (imageStream != null)
            {
                _bridgeitResponse.PreviewContent = CreateResizedBase64Thumbnail(imageStream, ThumbWidth, ThumbHeight);
            }
        }
    }

    /// <summary>
    /// ValueConvert for generating the output for the contact pictures tream is present. 
    /// </summary>
    public class ContactPictureConverter : System.Windows.Data.IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            Contact c = value as Contact;
            if (c == null) return null;

            System.IO.Stream imageStream = c.GetPicture();
            if (null != imageStream)
            {
                return Microsoft.Phone.PictureDecoder.DecodeJpeg(imageStream);
            }
            return null;
        }

        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }

}