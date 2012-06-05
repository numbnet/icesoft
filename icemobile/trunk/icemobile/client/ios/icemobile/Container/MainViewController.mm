/*
* Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions an
* limitations under the License.
*/

#import "MainViewController.h"
#import "OldNativeInterface.h"
#import "Preferences.h"
#import <QRCodeReader.h>
#import <ZXingWidgetController.h>
/* Will require AddressBook and AddressBookUI frameworks
#import <UniversalResultParser.h>
#import <ParsedResult.h>
*/

@implementation MainViewController

@synthesize webView;
@synthesize receivedData;
@synthesize currentRequest;
@synthesize currentResponse;
@synthesize currentChallenge;
@synthesize prefsButton;
@synthesize nativeInterface;
@synthesize preferences;
@synthesize userAgent;
@synthesize hexDeviceToken;
@synthesize notificationEmail;
@synthesize popover;
@synthesize scanPopover;
@synthesize canRetry;


- (void)viewDidLoad {
    [super viewDidLoad];
    self.webView.mediaPlaybackRequiresUserAction = NO;
    self.webView.allowsInlineMediaPlayback = YES;

    self.nativeInterface = [[OldNativeInterface alloc] init];
    self.nativeInterface.controller = self;
    self.nativeInterface.uploading = NO;
    self.canRetry = YES;

    self.preferences = [[Preferences alloc] init];
    self.preferences.mainViewController = self;
    [[NSBundle mainBundle] loadNibNamed:@"Preferences" owner:self.preferences options:nil];

    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        self.popover = [[UIPopoverController alloc] initWithContentViewController:preferences];
    }

    NSLog(@"MainViewController viewDidLoad");
}

- (void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event  {
    if (event.subtype == UIEventSubtypeMotionShake)  {
        NSLog(@"Detected shake.");
    }
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return YES;
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation  {
    NSLog(@"MainViewController didRotateFromInterfaceOrientation");
    prefsButton.center = CGPointMake(self.view.bounds.size.width - 20, 
            self.view.bounds.size.height - 30);
    if ((nil != self.popover) && ([self.popover isPopoverVisible])) {
        [self.popover presentPopoverFromRect:[prefsButton frame] 
            inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:NO];
    }
}

- (void)didBecomeActive  {
    NSLog(@"MainViewController didBecomeActive.");
    if (nil == [self getCurrentURL])  {
        self.userAgent = [self.webView stringByEvaluatingJavaScriptFromString:@"navigator.userAgent"];
        [self loadURL:@"http://www.icemobile.org/demos.html"];
    } else {
        [self.webView reload];
        [self.webView stringByEvaluatingJavaScriptFromString: 
                @"ice.push.resumeBlockingConnection()"];
    }
}

- (void)willResignActive  {
    NSLog(@"MainViewController didResignActive.");
    [webView stringByEvaluatingJavaScriptFromString: 
            @"ice.push.pauseBlockingConnection(ice.deviceToken)"];
    [webView stopLoading];
    [self.nativeInterface applicationWillResignActive];
}

- (void)setDeviceToken:(NSData *)deviceToken {
    const unsigned *tokenBytes = (unsigned int*) [deviceToken bytes];
    NSString *hexToken = [NSString stringWithFormat:
            @"%08x%08x%08x%08x%08x%08x%08x%08x",
            ntohl(tokenBytes[0]), ntohl(tokenBytes[1]), ntohl(tokenBytes[2]),
            ntohl(tokenBytes[3]), ntohl(tokenBytes[4]), ntohl(tokenBytes[5]),
            ntohl(tokenBytes[6]), ntohl(tokenBytes[7])];
    self.hexDeviceToken = hexToken;
    NSLog(@"MainViewController setDeviceToken %@", hexToken);
}

- (BOOL)webView:(UIWebView*)webView 
    shouldStartLoadWithRequest: (NSURLRequest*)req 
    navigationType:(UIWebViewNavigationType)navigationType {
    NSString *reqString = [[req URL] absoluteString];
    NSLog(@"shouldStartLoadWithRequest %@ ", reqString );
    
    if ([reqString hasPrefix:@"js-api:"])  {
        NSLog(@"Found API call %@ ", reqString );
        NSString *command = [reqString substringFromIndex:7];
        if ([self.nativeInterface dispatch:command]) {
            return NO;
        }
        //all js-api commands are consumed here
        return NO;
    }

    //add cookie for every domain
    [self setCustomCookie:req];

    return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)wView  {
    NSLog(@"webViewDidStartLoad %@", [webView.request mainDocumentURL]);
}

- (void)webViewDidFinishLoad:(UIWebView *)wView  {
    NSLog(@"webViewDidFinishLoad %@", [webView.request mainDocumentURL]);
    NSError *error = nil;
    //eval native-interface.js
    NSString *localPath = [[NSBundle mainBundle] 
            pathForResource:@"native-interface" ofType:@"js"];
    NSString *script = [[NSString alloc] initWithContentsOfFile:localPath
            encoding:NSUTF8StringEncoding error:&error];
    NSString *result = [wView stringByEvaluatingJavaScriptFromString: script];

    if ( (nil != self.notificationEmail) && 
         ([self.notificationEmail length] > 0) )  {
        NSLog(@"using email notification: %@", self.notificationEmail );
        NSString *scriptTemplate = 
                @"ice.deviceToken = \"mail:%@\";ice.push.parkInactivePushIds(ice.deviceToken);";
        script = [NSString stringWithFormat:scriptTemplate, self.notificationEmail];
    } else {
        NSLog(@"using apns notification: %@", self.hexDeviceToken );
        NSString *scriptTemplate = 
                @"ice.deviceToken = \"apns:%@\";ice.push.parkInactivePushIds(ice.deviceToken);";
        script = [NSString stringWithFormat:scriptTemplate, self.hexDeviceToken];
    }
    NSLog(@"ICEpush parking: %@", script );
    result = [wView stringByEvaluatingJavaScriptFromString: script];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error  {
    NSLog(@"didFailLoadWithError %@", [error localizedDescription]);
    NSLog(@"didFailLoadWithError %@", [self getCurrentURL]);
    if (self.canRetry)  {
        NSString *localPath = [[NSBundle mainBundle] 
                pathForResource:@"main" ofType:@"html"];
        NSURL *localURL = [NSURL fileURLWithPath:localPath];
        NSURLRequest *request = [NSURLRequest requestWithURL:localURL];
        [self.webView loadRequest:request];
        self.canRetry = NO;
    }
}

- (NSURLRequest *)connection:(NSURLConnection *)connection willSendRequest:(NSURLRequest *)request redirectResponse:(NSURLResponse *)redirectResponse  {
    NSLog(@"willSendRequest %@ ", [[request URL] absoluteString]);
    self.currentRequest = request;
    return request;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response  {
    self.currentResponse = response;
    [receivedData setLength:0];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data  {
    [receivedData appendData:data];
}

- (void)connection:(NSURLConnection *)connection didSendBodyData:(NSInteger)bytesWritten totalBytesWritten:(NSInteger)totalBytesWritten totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite  {
    NSLog(@"MainViewController didSendBodyData ");
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection  {
    [self.webView loadData:receivedData MIMEType:[self.currentResponse MIMEType] textEncodingName:[self.currentResponse textEncodingName] baseURL:[self.currentRequest URL ]];
    [connection release];
    [self.receivedData release];
}

- (void)connection:(NSURLConnection *)connection willSendRequestForAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge  {
    UIAlertView *alert = [[UIAlertView alloc] 
            initWithTitle:@"Authentication Required" 
            message:[[self.currentRequest URL] host]
            delegate:self cancelButtonTitle:@"Log In" 
            otherButtonTitles:@"Cancel",nil];
    alert.alertViewStyle = UIAlertViewStyleLoginAndPasswordInput;
    self.currentChallenge = challenge;
    [alert show];
    [alert release];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    NSLog(@"MainViewController connection:didFailWithError %@ ", error);
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    [[self.currentChallenge sender] useCredential:[NSURLCredential 
            credentialWithUser:[[alertView textFieldAtIndex:0] text] 
            password:[[alertView textFieldAtIndex:1] text] 
            persistence:NSURLCredentialPersistencePermanent] 
            forAuthenticationChallenge:self.currentChallenge];
}

- (void)completePost:(NSString *)value forComponent:(NSString *)componentID withName:(NSString *)componentName   {
NSLog(@"Warning: consider renaming to completeParameter given ICEmobile Container use");
    NSString *scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
    NSString *script = [NSString stringWithFormat:scriptTemplate, componentID, componentName, value];
    [self.webView stringByEvaluatingJavaScriptFromString: script];
}

- (void)completeFile:(NSString *)path forComponent:(NSString *)componentID withName:(NSString *)componentName   {
    NSString *scriptTemplate;
    NSString *script;
    NSString *result;

    scriptTemplate = @"ice.addHidden('%@', '%@', '%@', 'file');";
    script = [NSString stringWithFormat:scriptTemplate, componentID, componentName, path];
NSLog(@"completeFile %@", script);
    result = [self.webView stringByEvaluatingJavaScriptFromString: script];
}

- (NSString *) prepareUpload:(NSString *)formID  {
NSLog(@"Warning: prepareUpload should not be invoked with ICEmobile Container");
    return @"void";
}

- (void) register {
NSLog(@"Warning: register should not be invoked with ICEmobile Container");
}

- (void) setProgress:(NSInteger) percent {
NSLog(@"ICEmobile Container setProgress %d", percent);
    NSString *scriptTemplate = @"ice.progress(%d);";
    NSString *script = [NSString stringWithFormat:scriptTemplate, percent];
    [self.webView stringByEvaluatingJavaScriptFromString: script];
}

- (NSString *) getFormData:(NSString *)formID  {
NSLog(@"Warning: getFormData not implemented for ICEmobile Container");
    return @"";
}

- (void) play:(NSString *)audioID  {
NSLog(@"Warning: play not implemented for ICEmobile Container");
}

- (void) setThumbnail: (UIImage*)image at: (NSString *)thumbID  {
    NSData *scaledData =  UIImageJPEGRepresentation(image, 0.5);
    NSString *image64 = [self.nativeInterface  base64StringFromData:scaledData];
    NSString *dataURL = [@"data:image/jpg;base64," 
            stringByAppendingString:image64];
    NSLog(@"scaled and encoded thumbnail %d", [image64 length]);

    NSString *scriptTemplate;
    NSString *script;
    NSString *result;

    NSString *thumbName = [thumbID stringByAppendingString:@"-thumb"];
    scriptTemplate = @"ice.setThumbnail(\"%@\", \"%@\");";
    script = [NSString stringWithFormat:scriptTemplate, thumbName, dataURL];
    result = [self.webView stringByEvaluatingJavaScriptFromString: script];
}

- (void) handleResponse:(NSString *)responseString  {
NSLog(@"handleResponse for ICEmobile Container");
    NSString *scriptTemplate = @"ice.handleResponse(\"%@\");";
    NSString *script = [NSString stringWithFormat:scriptTemplate, [responseString 
            stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    [self.webView stringByEvaluatingJavaScriptFromString: script];
}

- (IBAction)doPreferences {
    NSLog(@"Preferences pressed");
    [self.preferences update];
    if (nil != self.popover) {
        [self.popover presentPopoverFromRect:[prefsButton frame] 
            inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    } else {
        [[UIApplication sharedApplication] setStatusBarHidden:YES];
        self.preferences.oldView = self.view;
        UIView *containerView = self.view.superview;
        [UIView transitionWithView:containerView duration:0.5
            options:UIViewAnimationOptionTransitionFlipFromRight
            animations:^ { [self.view removeFromSuperview]; 
            [containerView addSubview:self.preferences.view]; }
            completion:nil];
    }
}

- (void)scanQR {
    NSLog(@"scanQR ");
    ZXingWidgetController *widController = [[ZXingWidgetController alloc] initWithDelegate:self showCancel:YES OneDMode:NO];
    QRCodeReader* qrcodeReader = [[QRCodeReader alloc] init];
    NSSet *readers = [[NSSet alloc ] initWithObjects:qrcodeReader,nil];
    [qrcodeReader release];
    widController.readers = readers;
    [readers release];
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)  {
        if (nil == self.scanPopover)  {
            scanPopover = [[UIPopoverController alloc] 
                    initWithContentViewController:widController];
            self.scanPopover.popoverContentSize = CGSizeMake(320, 480);
        }
        //[picker release];
        [self.scanPopover presentPopoverFromRect:CGRectMake(200.0, 200.0, 0.0, 0.0) 
                                 inView:self.view
               permittedArrowDirections:UIPopoverArrowDirectionAny 
                               animated:YES];
    } else {
        [self presentModalViewController:widController animated:YES];
    }
    [widController release];
}

- (void)zxingController:(ZXingWidgetController*)controller didScanResult:(NSString *)resultString {
    NSLog(@"didScanResult %@", resultString);
    [self dismissScan];
    NSString *scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
    NSString *scanId = self.nativeInterface.activeDOMElementId;
    NSString *scanName = [scanId stringByAppendingString:@"-text"];
    NSString *script = [NSString stringWithFormat:scriptTemplate, scanId, scanName, resultString];
    [self.webView stringByEvaluatingJavaScriptFromString: script];

//    ParsedResult *parsedResult = [[UniversalResultParser parsedResultForString:resultString] retain];
//    NSLog(@"parsedResult %@", parsedResult);
}

- (void)zxingControllerDidCancel:(ZXingWidgetController*)controller {
    [self dismissScan];
}

- (void)dismissScan {
    if (nil != self.scanPopover)  {
        [self.scanPopover dismissPopoverAnimated:YES];
    } else {
        [self dismissModalViewControllerAnimated:YES];
    }
}

- (NSURL*)getCurrentURL {
    return [self.webView.request URL];
}

- (void)loadURL:(NSString*) url {
    self.canRetry = YES;
    if (![[url lowercaseString] hasPrefix:@"http://"]) {
        url = [@"http://" stringByAppendingString:url];
    }
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:
            [NSURL URLWithString:url] ]; 
    [self setCustomCookie:request];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [request setValue:self.userAgent forHTTPHeaderField:@"User-Agent"];
    self.currentRequest = request;

    NSURLConnection *theConnection = [[NSURLConnection alloc] 
            initWithRequest:request  delegate:self];
    if (theConnection) {
        receivedData = [[NSMutableData data] retain];
    } else {
        NSLog(@"unable to connect to %@", theConnection);
    }
}

- (void)setCustomCookie:(NSURLRequest*) req {
    NSDictionary *properties = [[NSDictionary alloc] initWithObjectsAndKeys:
            @"com.icesoft.user-agent", NSHTTPCookieName,
            @"HyperBrowser/1.0", NSHTTPCookieValue,
            @"/", NSHTTPCookiePath,
            [[req URL] host], NSHTTPCookieDomain,
            nil ];

    NSHTTPCookie *cookie = [NSHTTPCookie cookieWithProperties:properties];
    NSLog(@"setCookie %@ for request %@ ", cookie, [[req URL] absoluteString] );
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie: cookie];
}

- (void)reloadCurrentPage {
    NSLog(@"reloadCurrentPage %@", [self.webView.request URL]);
    [self.webView reload];
}

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    NSLog(@"viewDidUnload ");
}


- (void)dealloc {
    [super dealloc];
}


@end
