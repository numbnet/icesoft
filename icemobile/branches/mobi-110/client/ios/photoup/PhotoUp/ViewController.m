//
//  ViewController.m
//  ICEmobileHitch
//
//  Created by Ted Goddard on 11-11-04.
//  Copyright (c) 2011 ICEsoft Technologies, Inc. All rights reserved.
//

#import "ViewController.h"
#import "NativeInterface.h"

@implementation ViewController
@synthesize nativeInterface;
@synthesize currentURL;
@synthesize returnURL;
@synthesize currentParameters;
@synthesize currentCommand;
@synthesize currentSessionId;
@synthesize uploadProgress;
@synthesize deviceToken;
@synthesize confirmMessages;
@synthesize confirmTitles;


- (void) dealloc  {
    [self.confirmTitles dealloc];
    [self.confirmMessages dealloc];
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

        [params setValue:hexToken forKey:@"iceCloudPushId"];
    }
    [nativeInterface multipartPost:params toURL:self.currentURL];

    NSString *safariURL = self.currentURL;
    if (nil != self.returnURL)  {
        safariURL = self.returnURL;
    }
    [[UIApplication sharedApplication] 
            openURL:[NSURL URLWithString:safariURL]];
NSLog(@"PhotoUp registered and will open safari currentURL %@", safariURL);
}

- (void)completeFile:(NSString *)path forComponent:(NSString *)componentID withName:(NSString *)componentName   {
        NSString *scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
        NSString *script = [NSString stringWithFormat:scriptTemplate, 
                componentID, componentName, path];
NSLog(@"Hitch just upload what would have been scripted %@", script);
    NSMutableDictionary *params = [nativeInterface parseQuery:currentParameters];
    [params setValue:path forKey:componentName];
    [nativeInterface multipartPost:params toURL:self.currentURL];

    NSString *safariURL = self.currentURL;
    if (nil != self.returnURL)  {
        safariURL = self.returnURL;
    }
    [[UIApplication sharedApplication] 
            openURL:[NSURL URLWithString:safariURL]];
NSLog(@"Hitch opened safari currentURL %@", safariURL);
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

- (NSString *) getFormData:(NSString *)formID  {
    NSString *scriptTemplate = @"ice.getCurrentSerialized();";
    NSString *script = [NSString stringWithFormat:scriptTemplate, formID];
NSLog(@"Hitch just upload what would have been scripted %@", script);

    return @"unkown";
}

- (void) setProgress:(NSInteger)percent  {
    [uploadProgress setProgress:percent / 100.0f];
NSLog(@"Native progress display %d", percent);
}

- (void) handleResponse:(NSString *)responseString  {
//    NSString *scriptTemplate = @"ice.handleResponse(\"%@\");";
//    NSString *script = [NSString stringWithFormat:scriptTemplate, [responseString 
//            stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
NSLog(@"ICEmobile would have executed ice.handleResponse on %@", responseString);
}

- (void)play: (NSString*)audioId  {
NSLog(@"Hitch cant play audio from an ID in the page");
}

- (void)setThumbnail: (UIImage*)image at: (NSString *)thumbID  {
NSLog(@"Hitch would show a thumbnail");
}


- (void) dispatchCurrentCommand  {
    NSArray *queryParts = [self.currentCommand 
            componentsSeparatedByString:@"?"];
    NSString *commandName = [queryParts objectAtIndex:0];

    NSString *title = [self.confirmTitles objectForKey:commandName];
    NSString *message = [self.confirmMessages objectForKey:commandName];
    NSString *host = [[NSURL URLWithString:self.currentURL] host];
    message = [[message stringByAppendingString:host] 
            stringByAppendingString:@"?" ];

    if (nil == title)  {
        NSLog(@"Command not valid %@", self.currentCommand);
        return;
    }

    UIAlertView *alert = [[UIAlertView alloc] 
            initWithTitle:title 
            message:message 
            delegate:self cancelButtonTitle:@"OK" 
            otherButtonTitles:@"Cancel",nil];
    [alert show];
    [alert release];
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {

    if (buttonIndex == 0){
        NSString *host = [[NSURL URLWithString:self.currentURL] host];
        NSDictionary *properties = [[NSDictionary alloc] initWithObjectsAndKeys:
                @"JSESSIONID", NSHTTPCookieName,
                currentSessionId, NSHTTPCookieValue,
                @"/", NSHTTPCookiePath,
                host, NSHTTPCookieDomain,
                nil ];

        NSHTTPCookie *cookie = [NSHTTPCookie cookieWithProperties:properties];
        NSLog(@"setCookie %@ ", cookie );
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie: cookie];

        [nativeInterface dispatch:self.currentCommand];
    }
NSLog(@"Alert dismissed via button %d", buttonIndex);

}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    self.nativeInterface = [[NativeInterface alloc] init];
    self.nativeInterface.controller = self;
    self.nativeInterface.uploading = NO;
    self.confirmTitles = [NSDictionary dictionaryWithObjectsAndKeys:
            @"Register", @"register", 
            @"Photo Upload", @"camera", 
            @"Video Upload", @"camcorder", 
            @"Audio Upload", @"microphone", 
            @"QR Code Scan", @"scan", 
            nil];
    self.confirmMessages = [NSDictionary dictionaryWithObjectsAndKeys:
            @"Register with server ", @"register", 
            @"Upload photo to ", @"camera", 
            @"Upload video to ", @"camcorder", 
            @"Upload audio recording to ", @"microphone", 
            @"Send QR Code to ", @"scan", 
            nil];
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
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
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

@end
