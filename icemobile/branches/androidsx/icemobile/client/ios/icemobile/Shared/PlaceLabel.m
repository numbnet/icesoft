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

#import "PlaceLabel.h"

@implementation PlaceLabel
@synthesize placeName;
@synthesize view;
@synthesize location;
@synthesize heading;
@synthesize currentDistance;

- (id)init
{
    self = [super init];
    if (self) {
			view = nil;
			location = nil;
            heading = -1.0f;
    }    
    return self;
}

- (void)dealloc
{
	[view release];
	[location release];
	[super dealloc];
}

+ (PlaceLabel *)placeLabelWithView:(UIView *)view at:(CLLocation *)location  {
	PlaceLabel *place = [[[PlaceLabel alloc] init] autorelease];
	place.view = view;
	place.location = location;
	return place;
}

+ (PlaceLabel *)placeLabelWithText:(NSString *)text andImage:(UIImage *)image initWithLatitude:(double)lat longitude:(double)lon  {
    NSLog(@"placeLabelWithText:andImage: %@ %@ %f,%f", text, image, lat, lon);
    UIView *outerView = [[[UIView alloc] init] autorelease];
    if (nil != image)  {
        UIView *imageView = [[UIImageView alloc] initWithImage: image];
        imageView.center = CGPointMake(200.0f, 200.0f);
        [outerView addSubview:imageView];
    }
    UILabel *label = [[[UILabel alloc] init] autorelease];
    label.adjustsFontSizeToFitWidth = NO;
    label.opaque = NO;
    label.backgroundColor = [UIColor colorWithRed:0.1f green:0.1f blue:0.1f alpha:0.5f];
    label.center = CGPointMake(200.0f, 200.0f);
    label.textAlignment = UITextAlignmentCenter;
    label.textColor = [UIColor whiteColor];
    label.text = text;		
    CGSize size = [label.text sizeWithFont:label.font];
    label.bounds = CGRectMake(0.0f, 0.0f, size.width, size.height);
    [outerView addSubview:label];
            
    PlaceLabel *place = [PlaceLabel placeLabelWithView:outerView 
            at:[[[CLLocation alloc] initWithLatitude:lat longitude:lon] 
            autorelease]];
    place.placeName = text;
    return place;
}

+ (PlaceLabel *)placeLabelWithText:(NSString *)text 
        initWithLatitude:(double) lat longitude: (double) lon  {
    return [self placeLabelWithText:text andImage:nil 
            initWithLatitude:lat longitude:lon];
}

@end
