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
#import "NativeInterfaceViewController.h"

@class MainViewController;
@class QRScanner;

@interface NativeInterface : NSObject<UIImagePickerControllerDelegate, UINavigationControllerDelegate> {

	UIViewController<NativeInterfaceViewController> *controller;
    NSString *activeDOMElementId;
    NSString *maxwidth;
    NSString *maxheight;
    BOOL recording;
    BOOL uploading;
    NSMutableData *receivedData;
    QRScanner *qrScanner;
    UIPopoverController *camPopover;
    UIPopoverController *scanPopover;
    UIPopoverController *audioPopover;
}

@property (retain) UIViewController<NativeInterfaceViewController> *controller;
@property (retain) NSString *activeDOMElementId;
@property (retain) NSString *maxwidth;
@property (retain) NSString *maxheight;
@property (nonatomic, assign) BOOL recording;
@property (assign) BOOL uploading;
@property (retain) NSMutableData *receivedData;
@property (retain) QRScanner *qrScanner;
@property (nonatomic, retain) UIPopoverController *camPopover;
@property (nonatomic, retain) UIPopoverController *scanPopover;
@property (nonatomic, retain) UIPopoverController *audioPopover;

- (BOOL)dispatch: (NSString*)command;
- (BOOL)camera: (NSString*)cameraId maxwidth: (NSString*)maxw maxheight: (NSString*)maxh;
- (BOOL)register;
- (BOOL)scan: (NSString*)scanId;
- (BOOL)upload: (NSString*)formId;
- (BOOL)camcorder: (NSString*)cameraId;
- (BOOL)microphone: (NSString*)micId;
- (NSMutableDictionary*)parseQuery: (NSString*)queryString;
- (void)showImagePicker: (UIImagePickerController*)picker;
- (void)dismissImagePicker;
- (void)setThumbnail: (UIImage*)image at: (NSString *)thumbId;
- (UIImage*)scaleImage: (UIImage*)image toSize: (int)finalSize;
- (UIImage*)scaleImage: (UIImage*)image maxWidth: (int)maxw maxHeight: (int)maxh ;
- (NSString*)saveImage: (UIImage*)image;
- (void)scanResult: (NSString*)text;
- (void)dismissScan;
- (NSString *) base64StringFromData: (NSData *)data;
- (void)multipartPost: (NSDictionary *)parts toURL: (NSString *)url;

@end
