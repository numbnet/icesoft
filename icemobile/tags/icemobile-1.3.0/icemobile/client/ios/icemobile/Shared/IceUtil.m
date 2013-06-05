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

#import "IceUtil.h"

@implementation IceUtil

+ (void)makeFancyButton:(UIButton*)button withLayer:(CALayer*)fancyLayer  {
    CALayer *oldLayer = nil;
    for (CALayer *theLayer in [[button layer] sublayers]) {
        if ([@"fancyGradient" isEqualToString:theLayer.name ])  {
            oldLayer = theLayer;
        }
    }
    if (nil != oldLayer)  {
        [oldLayer removeFromSuperlayer];
    }
    [[button layer] insertSublayer:fancyLayer atIndex:0];

    [[button layer] setCornerRadius:8.0f];
    [[button layer] setMasksToBounds:YES];
    [[button layer] setBorderWidth:1.0f];
}

+ (void)makeFancyButton:(UIButton*)button withColor:(UIColor*)color  {

    CAGradientLayer *gradientLayer = [[CAGradientLayer alloc] init];
    [gradientLayer setBounds:[button bounds]];
    gradientLayer.name = @"fancyGradient";
    [gradientLayer setPosition:
                CGPointMake([button bounds].size.width/2,
                       [button bounds].size.height/2)];
        [gradientLayer setColors:
                     [NSArray arrayWithObjects:
                            (id)[[UIColor whiteColor] CGColor], 
                            (id)[color CGColor], nil]];
    NSLog(@"makeFancyButton layers %@", [[button layer] sublayers]);

    [IceUtil makeFancyButton:button withLayer:gradientLayer];

    [gradientLayer release];

}


+ (void)pushFancyButton:(UIButton *)button  {

    CAGradientLayer *gradientLayer = [[CAGradientLayer alloc] init];
    [gradientLayer setBounds:[button bounds]];
    gradientLayer.name = @"fancyGradient";
    [gradientLayer setBorderColor:[[UIColor grayColor] CGColor]];
    [gradientLayer setCornerRadius:8.0f];
    [gradientLayer setBorderWidth:4.0f];
    [gradientLayer setPosition:
                CGPointMake([button bounds].size.width/2,
                       [button bounds].size.height/2)];
        [gradientLayer setColors:
                     [NSArray arrayWithObjects:
                            (id)[[UIColor darkGrayColor] CGColor],
                            (id)[[UIColor grayColor] CGColor],
                            (id)[[UIColor lightGrayColor] CGColor], nil]]; 


    [IceUtil makeFancyButton:button withLayer:gradientLayer];
    [gradientLayer release];

}

+ (void)makeFancyButton:(UIButton*)button  {
    [IceUtil makeFancyButton:button withColor:[UIColor grayColor]];
}


@end
