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
#import "NativeInterfaceViewController.h"

@class NativeInterface;

@interface ViewController : UIViewController<NativeInterfaceViewController, UIAlertViewDelegate> {

	NativeInterface *nativeInterface;
    NSString *currentURL;
    NSString *returnURL;
    NSString *currentParameters;
    NSString *currentCommand;
    NSString *currentSessionId;
    NSData *deviceToken;
    NSDictionary *confirmMessages;
    NSDictionary *confirmTitles;
    UIProgressView *uploadProgress;
    UILabel *uploadLabel;
}

@property (retain) NativeInterface *nativeInterface;
@property (retain) NSString *currentURL;
@property (retain) NSString *returnURL;
@property (retain) NSString *currentParameters;
@property (retain) NSString *currentCommand;
@property (retain) NSString *currentSessionId;
@property (retain) NSData *deviceToken;
@property (retain) NSDictionary *confirmMessages;
@property (retain) NSDictionary *confirmTitles;
@property (nonatomic, retain) IBOutlet UIProgressView *uploadProgress;
@property (nonatomic, retain) IBOutlet UILabel *uploadLabel;

- (void) dispatchCurrentCommand;
- (void) reloadCurrentURL;
- (void) hideProgress;

@end
