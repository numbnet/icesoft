/*
* Copyright 2004-2012 ICEsoft Technologies Canada Corp. (c)
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

@synthesize view;
@synthesize location;

- (id)init
{
    self = [super init];
    if (self) {
			view = nil;
			location = nil;
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

+ (PlaceLabel *)placeLabelWithText:(NSString *)text 
        initWithLatitude:(double) lat longitude: (double) lon  {
NSLog(@"placeLabelWithText %@ %f,%f", text, lat, lon);
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
            
    PlaceLabel *place = [PlaceLabel placeLabelWithView:label 
            at:[[[CLLocation alloc] initWithLatitude:lat longitude:lon] 
            autorelease]];
    
    return place;
}

@end
