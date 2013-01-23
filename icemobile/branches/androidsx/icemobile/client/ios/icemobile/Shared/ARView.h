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
#import <CoreLocation/CoreLocation.h>
#import <CoreMotion/CoreMotion.h>
@class PlaceLabel;
@class NativeInterface;

@interface ARView : UIView  <CLLocationManagerDelegate> {
    CLLocation *location;
    NativeInterface *nativeInterface;
    BOOL useCompass;
    NSMutableArray *addedSubviews;
}

@property (nonatomic, retain) NativeInterface *nativeInterface;
@property (nonatomic) BOOL useCompass;
@property (nonatomic, retain) CLLocation *location;
@property (nonatomic, retain) NSArray *placeLabels;
@property (nonatomic, retain) NSArray *moreLabels;
@property (nonatomic, retain) NSMutableArray *addedSubviews;

- (void)addPlace:(PlaceLabel *) place;
- (void)setCompass:(BOOL) value;
- (void)start;
- (void)stop;
- (void)removePlaceSubviews;

@end
