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

@interface ViewController : UIViewController<NativeInterfaceViewController> {

	NativeInterface *nativeInterface;
    NSString *currentURL;
}

@property (retain) NativeInterface *nativeInterface;
@property (retain) NSString *currentURL;

@end
