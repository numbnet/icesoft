//
//  NativeInterfaceViewController.h
//  ICEmobile
//
//  Created by Ted Goddard on 11-11-07.
//  Copyright (c) 2011 ICEsoft Technologies, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol NativeInterfaceViewController <NSObject>

- (void) completeFile:(NSString *)path forComponent:(NSString *)componentID withName:(NSString *)componentName;
- (NSString *) prepareUpload:(NSString *)formID;
- (NSString *) getFormData:(NSString *)formID;
- (void) play:(NSString *)audioID;
- (void)setThumbnail: (UIImage*)image at: (NSString *)thumbID;
- (void) handleResponse:(NSString *)responseString;
- (void) setProgress:(NSInteger)percent;

@end
