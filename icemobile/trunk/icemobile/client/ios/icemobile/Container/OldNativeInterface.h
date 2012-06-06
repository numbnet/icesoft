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

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
@class MainViewController;
@class QRScanner;
@class ARViewController;

@interface OldNativeInterface : NSObject<UIImagePickerControllerDelegate, UINavigationControllerDelegate, AVAudioRecorderDelegate> {

	MainViewController *controller;
    NSString *activeDOMElementId;
    NSString *maxwidth;
    NSString *maxheight;
    NSString *soundFilePath;
    BOOL recording;
    BOOL uploading;
    QRScanner *qrScanner;
    AVAudioRecorder *soundRecorder;
    NSMutableData *receivedData;
    UIPopoverController *camPopover;
    UIPopoverController *scanPopover;
    UIPopoverController *audioPopover;
    UIPopoverController *augPopover;
    ARViewController *augController;
    CGRect popoverSource;
}

@property (retain) MainViewController *controller;
@property (retain) NSString *activeDOMElementId;
@property (retain) NSString *maxwidth;
@property (retain) NSString *maxheight;
@property (retain) NSString *soundFilePath;
@property (nonatomic, assign) BOOL recording;
@property (assign) BOOL uploading;
@property (retain) QRScanner *qrScanner;
@property (retain) AVAudioRecorder *soundRecorder;
@property (nonatomic, retain) UIPopoverController *camPopover;
@property (nonatomic, retain) UIPopoverController *scanPopover;
@property (retain) NSMutableData *receivedData;
@property (nonatomic, retain) UIPopoverController *audioPopover;
@property (nonatomic, retain) UIPopoverController *augPopover;
@property (nonatomic, retain) ARViewController *augController;
@property (nonatomic, assign) CGRect popoverSource;

- (void) applicationWillResignActive;
- (BOOL)dispatch: (NSString*)command;
- (BOOL)camera: (NSString*)cameraId maxwidth: (NSString*)maxw maxheight: (NSString*)maxh;
- (BOOL)camcorder: (NSString*)cameraId;
- (BOOL)upload: (NSString*)formId;
- (BOOL)play: (NSString*)audioId;
- (BOOL)scan: (NSString*)scanId;
- (NSMutableDictionary*)parseQuery: (NSString*)queryString;
- (void)showImagePicker: (UIImagePickerController*)picker;
- (void)dismissImagePicker;
- (void)setThumbnail: (UIImage*)image at: (NSString *)thumbId;
- (UIImage*)scaleImage: (UIImage*)image toSize: (int)finalSize;
- (UIImage*)scaleImage: (UIImage*)image maxWidth: (int)maxw maxHeight: (int)maxh ;
- (NSString*)saveImage: (UIImage*)image;
- (NSString *) base64StringFromData: (NSData *)data;
- (void)multipartPost: (NSDictionary *)parts toURL: (NSString *)url;

@end
