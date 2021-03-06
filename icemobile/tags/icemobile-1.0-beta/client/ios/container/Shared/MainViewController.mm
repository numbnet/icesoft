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
#import "NativeInterface.h"
#import "Preferences.h"
#import <QRCodeReader.h>
#import <ZXingWidgetController.h>
/* Will require AddressBook and AddressBookUI frameworks
#import <UniversalResultParser.h>
#import <ParsedResult.h>
*/

@implementation MainViewController

@synthesize webView;
@synthesize prefsButton;
@synthesize nativeInterface;
@synthesize preferences;
@synthesize hexDeviceToken;
@synthesize notificationEmail;
@synthesize popover;
@synthesize scanPopover;


- (void)viewDidLoad {
    [super viewDidLoad];
    self.webView.mediaPlaybackRequiresUserAction = NO;
    self.webView.allowsInlineMediaPlayback = YES;

    self.nativeInterface = [[NativeInterface alloc] init];
    self.nativeInterface.controller = self;
    self.nativeInterface.uploading = NO;

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
        [self.webView loadRequest: [NSURLRequest requestWithURL:[NSURL 
                URLWithString:@"http://www.icemobile.org/demos.html"] ]];
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
    NSDictionary *properties = [[NSDictionary alloc] initWithObjectsAndKeys:
            @"com.icesoft.user-agent", NSHTTPCookieName,
            @"HyperBrowser/1.0", NSHTTPCookieValue,
            @"/", NSHTTPCookiePath,
            [[req URL] host], NSHTTPCookieDomain,
            nil ];

    NSHTTPCookie *cookie = [NSHTTPCookie cookieWithProperties:properties];
    NSLog(@"setCookie %@ for request %@ ", cookie, reqString );
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie: cookie];

    return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView  {
    NSLog(@"webViewDidFinishLoad ");
    //eval native-interface.js
    NSString *localPath = [[NSBundle mainBundle] 
            pathForResource:@"native-interface" ofType:@"js"];
    NSString *script = [[NSString alloc] initWithContentsOfFile:localPath];
    NSString *result = [webView stringByEvaluatingJavaScriptFromString: script];

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
    result = [webView stringByEvaluatingJavaScriptFromString: script];
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error  {
    NSLog(@"didFailLoadWithError %@", [error localizedDescription]);
    NSLog(@"didFailLoadWithError %@", [self getCurrentURL]);
    NSString *localPath = [[NSBundle mainBundle] 
            pathForResource:@"main" ofType:@"html"];
    NSURL *localURL = [NSURL fileURLWithPath:localPath];
    NSURLRequest *request = [NSURLRequest requestWithURL:localURL];
    [self.webView loadRequest:request];
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
	[self.webView loadRequest: [NSURLRequest requestWithURL:[NSURL 
            URLWithString:url] ]];
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
