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

@class NativeInterface, SettingsViewController;

@interface ViewController : UIViewController<NativeInterfaceViewController, 
    UIAlertViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout> {

	NativeInterface *nativeInterface;
    NSString *currentURL;
    NSString *returnURL;
    NSString *currentParameters;
    NSString *currentCommand;
    NSString *currentSessionId;
    NSString *currentEncodedThumbnail;
    NSString *splashParameters;
    NSString *returnHash;
    NSString *cloudPushId;
    NSData *deviceToken;
    NSDictionary *confirmMessages;
    NSDictionary *confirmTitles;
    NSArray *commandNames;
    BOOL launchedFromApp;
    BOOL isResponding;
    UIProgressView *uploadProgress;
    UILabel *uploadLabel;
    UIView *linkView;
    UITextField *urlField;
    UISegmentedControl *actionSelector;
    UIButton *arButton;
    UIButton *contactsButton;
    UIButton *geospyButton;
    UIButton *smsButton;
    UIButton *cameraButton;
    UIButton *camcorderButton;
    UIButton *microphoneButton;
    UIButton *qrButton;
    UIButton *openButton;
    UIImageView *splashImage;
}

@property (retain) NativeInterface *nativeInterface;
@property (retain) NSString *currentURL;
@property (retain) NSString *returnURL;
@property (retain) NSString *currentParameters;
@property (retain) NSString *currentCommand;
@property (retain) NSString *currentSessionId;
@property (retain) NSString *currentEncodedThumbnail;
@property (retain) NSString *splashParameters;
@property (retain) NSString *returnHash;
@property (retain) NSString *cloudPushId;
@property (retain) NSData *deviceToken;
@property (retain) NSDictionary *confirmMessages;
@property (retain) NSDictionary *confirmTitles;
@property (retain) NSArray *commandNames;
@property (retain) NSArray *buttonImages;
@property (nonatomic) NSInteger licensePage;
@property (nonatomic) BOOL launchedFromApp;
@property (nonatomic, retain) IBOutlet UIProgressView *uploadProgress;
@property (nonatomic, retain) IBOutlet UILabel *uploadLabel;
@property (nonatomic, retain) IBOutlet UIView *linkView;
@property (nonatomic, retain) IBOutlet UITextField *urlField;
@property (nonatomic, retain) IBOutlet UISegmentedControl *actionSelector;
@property (nonatomic, retain) IBOutlet UIButton *arButton;
@property (nonatomic, retain) IBOutlet UIButton *contactsButton;
@property (nonatomic, retain) IBOutlet UIButton *geospyButton;
@property (nonatomic, retain) IBOutlet UIButton *smsButton;
@property (nonatomic, retain) IBOutlet UIButton *cameraButton;
@property (nonatomic, retain) IBOutlet UIButton *camcorderButton;
@property (nonatomic, retain) IBOutlet UIButton *microphoneButton;
@property (nonatomic, retain) IBOutlet UIButton *qrButton;
@property (nonatomic, retain) IBOutlet UIButton *openButton;
@property (retain, nonatomic) IBOutlet UIButton *settingsButton;
@property (nonatomic, retain) IBOutlet UIImageView *splashImage;
@property (nonatomic, retain) SettingsViewController *settingsView;
@property (retain, nonatomic) IBOutlet UICollectionView *buttonsView;

- (void) applicationWillResignActive;
- (void) applicationDidBecomeActive;
- (void) applicationWillEnterForeground;
- (void) applicationDidEnterBackground;
- (void) dispatchCurrentCommand;
- (void) reloadCurrentURL;
- (void) hideControls;
- (void) showControls;
- (void) hideProgress;
- (void) decorateParams:(NSMutableDictionary*) params;
- (void) flipButton:(UIButton*)theButton;
- (IBAction) doMediacast;
- (IBAction) doMobileshowcase;
- (IBAction) returnPressed;
- (IBAction) actionPressed:(id)sender;
- (IBAction) showLicense;
- (IBAction) showSettings;

@end
