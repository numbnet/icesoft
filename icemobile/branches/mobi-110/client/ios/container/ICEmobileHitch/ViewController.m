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
@synthesize currentParameters;

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (void)completeFile:(NSString *)path forComponent:(NSString *)componentID withName:(NSString *)componentName   {
        NSString *scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
        NSString *script = [NSString stringWithFormat:scriptTemplate, 
                componentID, componentName, path];
NSLog(@"Hitch just upload what would have been scripted %@", script);
    NSMutableDictionary *params = [nativeInterface parseQuery:currentParameters];
    [params setValue:path forKey:componentName];
    [nativeInterface multipartPost:params toURL:self.currentURL];

    [[UIApplication sharedApplication] 
            openURL:[NSURL URLWithString:self.currentURL]];
NSLog(@"Hitch opened safari currentURL");
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
    NSString *scriptTemplate = @"ice.progress(%d);";
    NSString *script = [NSString stringWithFormat:scriptTemplate, percent];
NSLog(@"Hitch just upload what would have been scripted %@", script);
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

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    self.nativeInterface = [[NativeInterface alloc] init];
    self.nativeInterface.controller = self;
    self.nativeInterface.uploading = NO;
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
