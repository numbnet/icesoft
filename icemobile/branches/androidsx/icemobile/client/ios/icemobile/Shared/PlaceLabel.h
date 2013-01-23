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

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
@class ARViewController;

@interface PlaceLabel : NSObject  {
    NSString *placeName;
    UIView *view;
    CLLocation *location;
    long heading;
    long currentDistance;
}

@property (nonatomic, retain) NSString *placeName;
@property (nonatomic, retain) UIView *view;
@property (nonatomic, retain) CLLocation *location;
@property (nonatomic) long heading;
@property (nonatomic) long currentDistance;

+ (PlaceLabel *)placeLabelWithView:(UIView *)view at:(CLLocation *)location;
+ (PlaceLabel *)placeLabelWithText:(NSString *)text 
        initWithLatitude:(double) lat longitude: (double) lon;
+ (PlaceLabel *)placeLabelWithText:(NSString *)text andImage:(UIImage *)image
        initWithLatitude:(double) lat longitude: (double) lon;

@end
