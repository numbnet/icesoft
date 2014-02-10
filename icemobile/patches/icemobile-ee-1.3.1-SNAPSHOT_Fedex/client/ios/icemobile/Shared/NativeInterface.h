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

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import <CoreMotion/CoreMotion.h>
#import <CoreLocation/CoreLocation.h>
#import <AddressBookUI/AddressBookUI.h>
#import <MessageUI/MessageUI.h>
#import "NativeInterfaceViewController.h"

@class MainViewController;
@class ARViewController;

@interface NativeInterface : NSObject<
        UIImagePickerControllerDelegate,
        UINavigationControllerDelegate,
        ABPeoplePickerNavigationControllerDelegate,
        MFMessageComposeViewControllerDelegate,
        CLLocationManagerDelegate, UIAlertViewDelegate> {

	UIViewController<NativeInterfaceViewController> *controller;
    UIDocumentInteractionController *documentController;
    NSString *userAgent;
    NSString *activeDOMElementId;
    NSString *geospyName;
    NSString *geospyURL;
    NSDictionary *geospyProperties;
    NSDate *geospyStop;
    NSString *maxwidth;
    NSString *maxheight;
    NSString *soundFilePath;
    NSInteger nextFileIndex;
    BOOL recording;
    BOOL uploading;
    BOOL monitoringLocation;
    BOOL showConnectionFailure;
    NSString *customAlertText;
    NSMutableData *receivedData;
    UIImagePickerController *currentPicker;
    AVAudioRecorder *soundRecorder;
    UIPopoverController *camPopover;
    UIPopoverController *scanPopover;
    UIPopoverController *audioPopover;
    UIPopoverController *augPopover;
    ARViewController *augController;
    CMMotionManager *motionManager;
    CLLocationManager *locationManager;
    CGRect popoverSource;
}

@property (retain) UIViewController<NativeInterfaceViewController> *controller;
@property (nonatomic, retain) UIDocumentInteractionController *documentController;
@property (retain) NSString *userAgent;
@property (retain) NSString *activeDOMElementId;
@property (retain) NSString *geospyName;
@property (retain) NSString *geospyURL;
@property (retain) NSDictionary *geospyProperties;
@property (retain) NSDate *geospyStop;
@property (retain) NSString *maxwidth;
@property (retain) NSString *maxheight;
@property (retain) NSString *soundFilePath;
@property (nonatomic, assign) NSInteger nextFileIndex;
@property (nonatomic, assign) BOOL recording;
@property (atomic, assign) BOOL uploading;
@property (atomic, assign) BOOL monitoringLocation;
@property (atomic, assign) BOOL showConnectionFailure;
@property (retain) NSString *customAlertText;
@property (atomic, retain) NSMutableData *receivedData;
@property (nonatomic, retain) UIImagePickerController *currentPicker;
@property (retain) AVAudioRecorder *soundRecorder;
@property (nonatomic, retain) UIPopoverController *camPopover;
@property (nonatomic, retain) UIPopoverController *scanPopover;
@property (nonatomic, retain) UIPopoverController *audioPopover;
@property (nonatomic, retain) UIPopoverController *augPopover;
@property (nonatomic, retain) ARViewController *augController;
@property (nonatomic, retain) CMMotionManager *motionManager;
@property (nonatomic, retain) CLLocationManager *locationManager;
@property (nonatomic, assign) CGRect popoverSource;

- (void) applicationWillResignActive;
- (BOOL)dispatch: (NSString*)command;
- (BOOL)camera: (NSString*)cameraId maxwidth: (NSString*)maxw maxheight: (NSString*)maxh;
- (BOOL)register;
- (BOOL)scan: (NSString*)scanId;
- (BOOL)upload: (NSString*)formId;
- (BOOL)camcorder: (NSString*)cameraId;
- (BOOL)microphone: (NSString*)micId;
- (BOOL)address: (NSString*)contactId;
- (BOOL)aug:(NSString*)augId locations:(NSDictionary*)places;
- (void)augMarkerView:(NSString*)augId withMarkers:(NSDictionary*)markers;
- (void)augHide;
- (void)augDone;
- (void)augFormDone:(NSString*)result;
- (void)augDismiss;
- (void)augMarkerDismiss;
- (void)startMotionManager;
- (void)stopMotionManager;
- (void)open:(NSString*)openId url:(NSString*)url;
- (void)recordStart;
- (void)recordStop;
- (void)recordDismiss;
- (void)recordCancel;
- (void)recordDone;
- (void)setProgress:(NSInteger)percent withLabel:(NSString*)labelText;
- (NSMutableDictionary*)parseQuery: (NSString*)queryString;
- (void)showImagePicker: (UIImagePickerController*)picker;
- (void)dismissImagePicker;
- (void)doAlbumButton:(id)sender;
- (void)setThumbnail: (UIImage*)image at: (NSString *)thumbId;
- (UIImage*)scaleImage: (UIImage*)image toSize: (int)finalSize;
- (UIImage*)scaleImage: (UIImage*)image maxWidth: (int)maxw maxHeight: (int)maxh ;
- (NSString*)saveImage: (UIImage*)image;
- (void)scanResult: (NSString*)text;
- (void)dismissScan;
- (NSString *) base64StringFromData: (NSData *)data;
- (void)multipartPost: (NSDictionary *)parts toURL: (NSString *)url;
- (NSString*)deviceID;

@end
