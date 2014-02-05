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

#import <UIKit/UIKit.h>
#import "NativeInterfaceViewController.h"
@class NativeInterface;
@class Preferences;

@interface MainViewController : UIViewController<NativeInterfaceViewController,UIWebViewDelegate,
    UINavigationControllerDelegate,UIActionSheetDelegate> {

    UIWebView *webView;
    UIProgressView *uploadProgress;
    NSURLRequest *currentRequest;
    NSURLResponse *currentResponse;
    NSMutableData *receivedData;
    NSURLAuthenticationChallenge *currentChallenge;
    NativeInterface *nativeInterface;
    Preferences *preferences;
    NSString *userAgent;
    NSString *hexDeviceToken;
    NSString *notificationEmail;
    UIButton *prefsButton;
    UIPopoverController *popover;
    UIPopoverController *scanPopover;
    BOOL canRetry;
    BOOL refreshCurrentView;
}

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) IBOutlet UIButton *prefsButton;
@property (nonatomic, retain) IBOutlet UIProgressView *uploadProgress;
@property (retain) NSURLRequest *currentRequest;
@property (retain) NSURLResponse *currentResponse;
@property (retain) NSMutableData *receivedData;
@property (retain) NSURLAuthenticationChallenge *currentChallenge;
@property (retain) NativeInterface *nativeInterface;
@property (retain) Preferences *preferences;
@property (nonatomic, retain) NSString *userAgent;
@property (nonatomic, retain) NSString *hexDeviceToken;
@property (nonatomic, retain) NSString *notificationEmail;
@property (nonatomic, retain) UIPopoverController *popover;
@property (nonatomic, retain) UIPopoverController *scanPopover;
@property (nonatomic, assign) BOOL canRetry;
@property (nonatomic, assign) BOOL refreshCurrentView;

- (void)didBecomeActive;
- (void)willResignActive;
- (void)setDeviceToken:(NSData *)deviceToken;
- (IBAction)doPreferences;
- (void)dismissScan;
- (void)doCancel;
- (void)reloadCurrentPage;
- (NSURL*)getCurrentURL;
- (void)loadURL:(NSString*) url;

@end
