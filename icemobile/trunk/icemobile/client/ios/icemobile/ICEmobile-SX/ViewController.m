/*
* Copyright 2004-2013 ICEsoft Technologies Canada Corp. (c)
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
#define LOGGING_ENABLED     1
#define LOGGING_LEVEL_TRACE 0
#define LOGGING_LEVEL_DEBUG 1
#define LOGGING_LEVEL_INFO  1
#define LOGGING_LEVEL_ERROR 1

#import "ViewController.h"
#import "NativeInterface.h"
#import "IceUtil.h"
#import "SettingsViewController.h"
#import "Logging.h"

@implementation ViewController
@synthesize nativeInterface;
@synthesize isResponding;
@synthesize currentURL;
@synthesize returnURL;
@synthesize currentParameters;
@synthesize currentCommand;
@synthesize currentSessionId;
@synthesize currentEncodedThumbnail;
@synthesize splashParameters;
@synthesize returnHash;
@synthesize uploadProgress;
@synthesize uploadLabel;
@synthesize linkView;
@synthesize urlField;
@synthesize actionSelector;
@synthesize geospyButton;
@synthesize smsButton;
@synthesize cameraButton;
@synthesize camcorderButton;
@synthesize microphoneButton;
@synthesize qrButton;
@synthesize openButton;
@synthesize arButton;
@synthesize contactsButton;
@synthesize cloudPushId;
@synthesize deviceToken;
@synthesize confirmMessages;
@synthesize confirmTitles;
@synthesize commandNames;
@synthesize launchedFromApp;
@synthesize splashImage;
@synthesize licensePage;


- (void) dealloc  {
    [_settingsButton release];
    [self.confirmTitles dealloc];
    [self.confirmMessages dealloc];
    [self.commandNames dealloc];
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (void)register   {
    NSMutableDictionary *params = [nativeInterface parseQuery:currentParameters];

    if (nil != self.deviceToken)  {
        const unsigned *tokenBytes = (unsigned int*) [self.deviceToken bytes];
        NSString *hexToken = [NSString stringWithFormat:
                @"apns:%08x%08x%08x%08x%08x%08x%08x%08x",
                ntohl(tokenBytes[0]), ntohl(tokenBytes[1]), ntohl(tokenBytes[2]),
                ntohl(tokenBytes[3]), ntohl(tokenBytes[4]), ntohl(tokenBytes[5]),
                ntohl(tokenBytes[6]), ntohl(tokenBytes[7])];

        self.cloudPushId = hexToken;
        [params setValue:hexToken forKey:@"iceCloudPushId"];
        NSLog(@"register with cloud push deviceToken %@", hexToken);
    }
    [self decorateParams: params];
    [nativeInterface multipartPost:params toURL:self.currentURL];
}

- (void)completeSmallPost:(NSString *)value forComponent:(NSString *)componentID withName:(NSString *)componentName   {
NSLog(@"completeSmallPost %@", self.returnURL);
    if ([self.returnURL hasSuffix:@"#icemobilesx"])  {
        self.returnURL = [self.returnURL stringByAppendingString:@"_"];
        self.returnURL = [self.returnURL stringByAppendingString:
            [componentID stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
        self.returnURL = [self.returnURL stringByAppendingString:
            [@"=" stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
        NSString *escValue = [value stringByReplacingOccurrencesOfString:
                @"&" withString:@"%26"];
        escValue = [escValue stringByReplacingOccurrencesOfString:
                @"@" withString:@"%40"];
        self.returnURL = [self.returnURL stringByAppendingString:
                [escValue stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
        if (nil != self.currentEncodedThumbnail)  {
            self.returnURL = [self.returnURL stringByAppendingString:@"&!p="];
            self.returnURL = [self.returnURL stringByAppendingString:
                    [self.currentEncodedThumbnail
                    stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
            //thumbnail is consumed
            self.currentEncodedThumbnail = nil;
        }
        if (nil != self.returnHash)  {
            self.returnURL = [self.returnURL stringByAppendingString:@"&!h="];
            self.returnURL = [self.returnURL stringByAppendingString:
                    [[self.returnHash
                    stringByReplacingOccurrencesOfString:@"&" withString:@"%26"]
                    stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
        }
        if (nil != self.cloudPushId)  {
            self.returnURL = [self.returnURL stringByAppendingString:@"&!c="];
            self.returnURL = [self.returnURL stringByAppendingString:
                    [self.cloudPushId
                    stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
        }
        return;
    }

    if (YES == self.isResponding)  {
        //already uploading so no need for fall through
        return;
    }

    [self completePost:value forComponent:componentID withName:componentName];
}

- (void)completePost:(NSString *)value forComponent:(NSString *)componentID withName:(NSString *)componentName   {
    [self completePostRaw:value forComponent:componentID
            withName:[@"text-" stringByAppendingString:componentName]];
}

- (void)completePostRaw:(NSString *)value forComponent:(NSString *)componentID withName:(NSString *)componentName   {
    NSMutableDictionary *params = [nativeInterface parseQuery:self.currentParameters];
    [self decorateParams: params];
    if (nil != componentName)  {
        [params setValue:value forKey:componentName];
    } else {
        NSLog(@"Warning: upload without componentName");
    }
    [nativeInterface multipartPost:params toURL:self.currentURL];
}

- (void) reloadCurrentURL  {
    NSString *safariURL = self.currentURL;
    if (nil != self.returnURL)  {
        safariURL = self.returnURL;
    }

    NSLog(@"ICEmobile-SX will open %@", safariURL);
    [[UIApplication sharedApplication]
            openURL:[NSURL URLWithString:safariURL]];
}

- (void)completeFile:(NSString *)path forComponent:(NSString *)componentID withName:(NSString *)componentName   {
    [self completePostRaw:path forComponent:componentID
            withName:[@"file-" stringByAppendingString: componentName]];
}

- (NSString *) prepareUpload:(NSString *)formID  {
    NSString *scriptTemplate = @"document.getElementById(\"%@\").action;";
    NSString *script = [NSString stringWithFormat:scriptTemplate, formID];
NSLog(@"Hitch just upload what would have been scripted %@", script);
    
    scriptTemplate = @"document.location.href;";
    script = [NSString stringWithFormat:scriptTemplate, formID];
NSLog(@"Hitch just upload what would have been scripted %@", script);

NSLog(@"Hitch just upload what would have been scripted %@", script);

    return @"unknown";
}

- (void) decorateParams:(NSMutableDictionary*) params {
    NSMutableDictionary *temp = [params copy];
    for (NSString *key in temp)  {
        NSString *value = [params objectForKey:key];
        [params removeObjectForKey:key];
        [params setValue:value forKey:[@"hidden-" stringByAppendingString:key]];
    }
    [temp release];
}

- (NSString *) getFormData:(NSString *)formID  {
    NSString *scriptTemplate = @"ice.getCurrentSerialized();";
    NSString *script = [NSString stringWithFormat:scriptTemplate, formID];
    LogDebug(@"Hitch just upload what would have been scripted %@", script);

    return @"unkown";
}

- (void) doCancel  {
NSLog(@"doCancel reloadCurrentURL %d", self.launchedFromApp);
    if (!self.launchedFromApp)  {
        [self reloadCurrentURL];
    }
}

- (void) hideControls  {
NSLog(@"hideControls");
    self.linkView.hidden = YES;
    self.uploadProgress.hidden = YES;
}

- (void) showControls  {
    LogDebug(@"showControls");
    self.linkView.hidden = NO;
}

- (void) hideProgress  {
    LogDebug(@"hideProgress");
    self.uploadLabel.hidden = YES;
    self.uploadProgress.hidden = YES;
    self.linkView.hidden = NO;
}

- (void) setProgress:(NSInteger)percent  {
    linkView.hidden = YES;
    uploadLabel.hidden = NO;
    uploadProgress.hidden = NO;
    [uploadProgress setProgress:percent / 100.0f];
    LogDebug(@"Native progress display %d", percent);
}

- (void) setProgressLabel:(NSString*)labelText  {
    uploadLabel.text = labelText;
}

- (void) returnToBrowser  {
    if (!self.launchedFromApp)  {
        [self reloadCurrentURL];
    }
}

- (void) handleResponse:(NSString *)responseString  {
NSLog(@"handleResponse reloadCurrentURL %d", self.launchedFromApp);
    self.isResponding = YES;
    if (!self.launchedFromApp)  {
        if (responseString.length < 1500)  {
            [self completeSmallPost:responseString
                    forComponent:@"!r" withName:@"!r"];
        } else {
            [self completeSmallPost:@"Response size limit exceeded."
                    forComponent:@"!r" withName:@"!r"];
        }
        [self reloadCurrentURL];
    } else {
        [self hideProgress];
        [self showControls];
    }
}

- (void)play: (NSString*)audioId  {
    LogDebug(@"Hitch cant play audio from an ID in the page");
}

- (void) setThumbnail: (UIImage*)image at: (NSString *)thumbID  {
    NSData *scaledData =  UIImageJPEGRepresentation(image, 0.5);
    NSString *image64 = [self.nativeInterface  base64StringFromData:scaledData];
    NSString *dataURL = [@"data:image/jpg;base64," 
            stringByAppendingString:image64];
    self.currentEncodedThumbnail = dataURL;
    NSLog(@"scaled and encoded thumbnail %d", [image64 length]);
}


- (void) dispatchCurrentCommand  {
    NSArray *queryParts = [self.currentCommand
            componentsSeparatedByString:@"?"];
    NSString *commandName = [queryParts objectAtIndex:0];
    NSString *targetURL = self.currentURL;
    if (nil == targetURL)  {
        targetURL = self.returnURL;
    }

    NSString *title = [self.confirmTitles objectForKey:commandName];
    NSString *message = [self.confirmMessages objectForKey:commandName];
    NSString *host = [[NSURL URLWithString:targetURL] host];
    if (nil == host)  {
        UIAlertView *alert = [[UIAlertView alloc] 
                initWithTitle:@"Invalid URL"
                message:[@"Upload URL not valid: "
                        stringByAppendingString:targetURL]
                delegate:nil cancelButtonTitle:@"OK"
                otherButtonTitles:nil];
        [alert show];
        [alert release];
        LogError(@"Upload URL not valid: %@", targetURL);
        return;
    }

    if ([self.settingsView canTrustHost:host])  {
        [self completeDispatch];
        return;
    }

    message = [[message stringByAppendingString:host] 
            stringByAppendingString:@"?" ];

    if (nil != self.nativeInterface.customAlertText)  {
        message = self.nativeInterface.customAlertText;
        self.nativeInterface.customAlertText = nil;
    }

    if (nil == title)  {
        LogError(@"Command not valid %@", self.currentCommand);
        return;
    }

    UIAlertView *alert = [[UIAlertView alloc] 
            initWithTitle:title 
            message:message 
            delegate:self cancelButtonTitle:@"OK" 
            otherButtonTitles:@"Trust", @"Cancel",nil];
    [alert show];
    [alert release];
}

- (void) completeDispatch  {
    if (nil != self.currentURL)  {
        NSURL *theURL = [NSURL URLWithString:self.currentURL];
        NSString *host = [theURL host];
        NSString *contextPath = [[theURL pathComponents] objectAtIndex:1];
        NSString *cookiePath = [[@"/" stringByAppendingString:contextPath]
                stringByAppendingString:@"/"];
        LogDebug(@"setCookie contextPath %@ ", contextPath );

        NSDictionary *properties = [[NSDictionary alloc] initWithObjectsAndKeys:
                @"JSESSIONID", NSHTTPCookieName,
                currentSessionId, NSHTTPCookieValue,
                cookiePath, NSHTTPCookiePath,
                host, NSHTTPCookieDomain,
                nil ];

        NSHTTPCookie *cookie = [NSHTTPCookie cookieWithProperties:properties];
        LogDebug(@"setCookie %@ ", cookie );
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie: cookie];
        LogDebug(@"currentCookies %@ ", [NSHTTPCookieStorage sharedHTTPCookieStorage].cookies );
    }

    self.isResponding = NO;
    [nativeInterface dispatch:self.currentCommand];
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    if ([@"ICEmobile-SX License" isEqualToString: alertView.title])  {
        if (buttonIndex == 1) {
            self.licensePage++;
            [self showLicense];
            return;
        }
        return;
    }

    if ([@"Welcome" isEqualToString: alertView.title])  {
        if (buttonIndex == 0)  {
            [self doGoBack];
        }
        return;
    }

    if (buttonIndex == 0) {
        [self completeDispatch];
    } else if (buttonIndex == 1)  {
        if (nil != self.currentURL)  {
            NSURL *theURL = [NSURL URLWithString:self.currentURL];
            NSString *host = [theURL host];
            [self.settingsView setHost:host trustSetting:YES];
            [self.settingsView saveSettings];
        }
        [self completeDispatch];
    } else if (buttonIndex == 2)  {
        [self doCancel];
    }
    LogDebug(@"Alert dismissed via button %d", buttonIndex);

}

- (IBAction) showLicense  {
    LogDebug(@"ViewController showLicense");
    NSError *error = nil;
    NSString *localPath = [[NSBundle mainBundle] 
            pathForResource:@"license" ofType:@"txt"];
    NSString *licenseText = [[NSString alloc] initWithContentsOfFile:localPath
            encoding:NSASCIIStringEncoding error:&error];
    NSString* displayText = [IceUtil pageFromString:licenseText 
            atPage:self.licensePage];
    if (nil == displayText)  {
        self.licensePage = 0;
        return;
    }

    UIAlertView *alert = [[UIAlertView alloc] 
            initWithTitle:@"ICEmobile-SX License" 
            message:displayText 
            delegate:self cancelButtonTitle:@"OK" 
            otherButtonTitles:@"More", nil];
    [alert show];
    [alert release];
}

- (IBAction) showSettings  {
    NSLog(@"showSettings ");
    [self.settingsView loadSettings];
    [self presentModalViewController:self.settingsView animated:YES];
}

- (IBAction) doMediacast  {
    LogDebug(@"ViewController doMediacast");
    [[UIApplication sharedApplication] 
            openURL:[NSURL 
                    URLWithString:@"http://mediacast.icemobile.org"]];
}

- (IBAction) doMobileshowcase  {
    LogDebug(@"ViewController doMobileshowcase");
    [[UIApplication sharedApplication] 
            openURL:[NSURL 
                    URLWithString:@"http://mobileshowcase.icemobile.org"]];
}

- (IBAction) doGoBack  {
    NSLog(@"ViewController doGoBack");
    [[UIApplication sharedApplication] 
            openURL:[NSURL 
                    URLWithString:@"http://www.icesoft.org/java/initicemobilesx.html"]];
}


- (IBAction) returnPressed  {
    LogDebug(@"ViewController returnPressed");
}

- (void) flipButton:(UIButton*) theButton  {
   NSLog(@"ViewController flipping %d", theButton.selected);
   theButton.selected = !theButton.selected;
   if (theButton.selected)  {
       [IceUtil pushFancyButton:theButton];
   } else {
       [IceUtil makeFancyButton:theButton];
   }
}

- (IBAction) actionPressed:(id) sender  {
    UIButton *theButton = (UIButton*) sender;
    NSString *theCommand = [self.commandNames 
            objectAtIndex:[sender tag]];
    NSString *params = @"";
    NSLog(@"ViewController actionPressed %@", theCommand);

    if ([@"geospy" isEqualToString:theCommand])  {
        [self flipButton:theButton];
        if (!theButton.selected)  {
            params = @"&strategy=stop";
            self.nativeInterface.customAlertText = @"Stop geolocation updates?";
        }
    }

    self.currentURL = urlField.text;
    [urlField resignFirstResponder];
    self.returnURL = self.currentURL;
    self.currentParameters = nil;

    if ([@"open" isEqualToString:theCommand])  {
        params = [@"&url=" stringByAppendingString:
            [self.currentURL  stringByAddingPercentEscapesUsingEncoding:
                    NSASCIIStringEncoding]];
    }

    CGRect selectionFrame = theButton.frame;
    selectionFrame = [theButton.superview convertRect:selectionFrame
            toView:self.view];
    self.nativeInterface.popoverSource = 
            [self.urlField convertRect:self.urlField.frame 
                toView:nativeInterface.controller.view];
    self.currentCommand = [NSString stringWithFormat:
            @"%@?id=undefined%@", theCommand, params];
    self.launchedFromApp = YES;
    [self dispatchCurrentCommand];

}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.

    [IceUtil makeFancyButton:self.cameraButton];
    self.cameraButton.tag = 0;
    [IceUtil makeFancyButton:self.camcorderButton];
    self.camcorderButton.tag = 1;
    [IceUtil makeFancyButton:self.microphoneButton];
    self.microphoneButton.tag = 2;
    [IceUtil makeFancyButton:self.qrButton];
    self.qrButton.tag = 3;
    [IceUtil makeFancyButton:self.arButton];
    self.arButton.tag = 4;
    [IceUtil makeFancyButton:self.contactsButton];
    self.contactsButton.tag = 5;
    [IceUtil makeFancyButton:self.smsButton];
    self.smsButton.tag = 6;
    [IceUtil makeFancyButton:self.geospyButton];
    self.geospyButton.tag = 7;
    [IceUtil makeFancyButton:self.openButton];
    self.openButton.tag = 8;

    [self hideProgress];
    [self showControls];
    self.nativeInterface = [[NativeInterface alloc] init];
    self.nativeInterface.controller = self;
    self.nativeInterface.uploading = NO;
    self.confirmTitles = [NSDictionary dictionaryWithObjectsAndKeys:
            @"Register", @"register", 
            @"Photo Upload", @"camera", 
            @"Video Upload", @"camcorder", 
            @"Audio Upload", @"microphone", 
            @"QR Code Scan", @"scan", 
            @"Augmented Reality View", @"aug", 
            @"Address Book", @"fetchContacts", 
            @"Send SMS", @"sms", 
            @"Location Upload", @"geospy", 
            @"Open File", @"open", 
            nil];
    self.confirmMessages = [NSDictionary dictionaryWithObjectsAndKeys:
            @"Register with server ", @"register", 
            @"Upload photo to ", @"camera", 
            @"Upload video to ", @"camcorder", 
            @"Upload audio recording to ", @"microphone", 
            @"Send QR Code to ", @"scan", 
            @"Send augmented reality location to ", @"aug", 
            @"Send contact to ", @"fetchContacts", 
            @"Send SMS", @"sms", 
            @"Periodically send location to ", @"geospy", 
            @"Open file referenced by ", @"open", 
            nil];
    self.commandNames = [NSArray arrayWithObjects:
            @"camera", 
            @"camcorder", 
            @"microphone", 
            @"scan", 
            @"aug", 
            @"fetchContacts", 
            @"sms", 
            @"geospy", 
            @"open", 
            nil];
    if (![[NSUserDefaults standardUserDefaults] boolForKey:@"initialized"])  {
        NSLog(@"firstLaunch detected");
        [[NSUserDefaults standardUserDefaults]
                setBool:YES forKey:@"initialized"];
        UIAlertView *alert = [[UIAlertView alloc] 
                initWithTitle:@"Welcome"
                message:@"Thank you for using ICEmobile-SX. Return to your web page?"
                delegate:self cancelButtonTitle:@"OK" 
                otherButtonTitles:@"Cancel",nil];
        [alert show];
        [alert release];
    }

    self.settingsView = [[SettingsViewController alloc] init];
    [self.settingsView loadSettings];

}

- (void)updateGeoSpyButton  {
    if (self.nativeInterface.monitoringLocation)  {
        NSLog(@"geospy ACTIVE");
        self.geospyButton.selected = YES;
        [IceUtil pushFancyButton:self.geospyButton];
    } 
}


- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self updateGeoSpyButton];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self updateGeoSpyButton];

}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
    } else {
        return YES;
    }
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event  {
    [urlField resignFirstResponder];
}

- (void)applicationDidBecomeActive {
    NSString* storedField = [[NSUserDefaults standardUserDefaults] 
            stringForKey:@"urlField"];
    if (nil != storedField)  {
        self.urlField.text = storedField;
    }
}

- (void)applicationWillResignActive {
    [self.nativeInterface applicationWillResignActive];
}

- (void)applicationWillEnterForeground {
    [self updateGeoSpyButton];
}

- (void) applicationDidEnterBackground  {
    [[NSUserDefaults standardUserDefaults]
            setValue:self.urlField.text forKey:@"urlField"];
    
    [self showControls];
    [self hideProgress];
}

@end
