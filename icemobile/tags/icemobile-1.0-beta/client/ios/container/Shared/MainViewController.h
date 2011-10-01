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

#import <UIKit/UIKit.h>
#import "ZXingWidgetController.h"
@class NativeInterface;
@class Preferences;

@interface MainViewController : UIViewController<UIWebViewDelegate,
    ZXingDelegate,UINavigationControllerDelegate,UIActionSheetDelegate> {

	UIWebView *webView;
	NativeInterface *nativeInterface;
	Preferences *preferences;
    NSString *hexDeviceToken;
    NSString *notificationEmail;
    UIButton *prefsButton;
    UIPopoverController *popover;
    UIPopoverController *scanPopover;
}

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) IBOutlet UIButton *prefsButton;
@property (retain) NativeInterface *nativeInterface;
@property (retain) Preferences *preferences;
@property (nonatomic, retain) NSString *hexDeviceToken;
@property (nonatomic, retain) NSString *notificationEmail;
@property (nonatomic, retain) UIPopoverController *popover;
@property (nonatomic, retain) UIPopoverController *scanPopover;

- (void)didBecomeActive;
- (void)willResignActive;
- (void)setDeviceToken:(NSData *)deviceToken;
- (IBAction)doPreferences;
- (void)scanQR;
- (void)dismissScan;
- (void)reloadCurrentPage;
- (NSURL*)getCurrentURL;
- (void)loadURL:(NSString*) url;

@end
