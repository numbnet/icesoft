//
//  AudioController.h
//  ICEmobile-SX
//
//  Created by Ted Goddard on 11-11-29.
//  Copyright (c) 2011 ICEsoft Technologies, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class NativeInterface;

@interface AudioController : UIViewController  {

	NativeInterface *nativeInterface;

}

@property (retain) NativeInterface *nativeInterface;

- (IBAction) doRecord;
- (IBAction) doStop;
- (IBAction) doDone;
- (IBAction) doCancel;

@end
