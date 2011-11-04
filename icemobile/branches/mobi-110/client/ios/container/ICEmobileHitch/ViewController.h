//
//  ViewController.h
//  ICEmobileHitch
//
//  Created by Ted Goddard on 11-11-04.
//  Copyright (c) 2011 ICEsoft Technologies, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
@class NativeInterface;

@interface ViewController : UIViewController  {

	NativeInterface *nativeInterface;
}

@property (retain) NativeInterface *nativeInterface;

@end
