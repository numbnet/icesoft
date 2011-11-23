//
//  ViewController.h
//  ICEmobileHitch
//
//  Created by Ted Goddard on 11-11-04.
//  Copyright (c) 2011 ICEsoft Technologies, Inc. All rights reserved.
//

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
    UIProgressView *uploadProgress;
}

@property (retain) NativeInterface *nativeInterface;
@property (retain) NSString *currentURL;
@property (retain) NSString *returnURL;
@property (retain) NSString *currentParameters;
@property (retain) NSString *currentCommand;
@property (retain) NSString *currentSessionId;
@property (nonatomic, retain) IBOutlet UIProgressView *uploadProgress;
- (void) dispatchCurrentCommand;

@end
