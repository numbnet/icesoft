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

#import "IceUtil.h"
#import <QuartzCore/QuartzCore.h>

@implementation IceUtil

+ (void)makeFancyButton:(UIButton*)button  {
    CAGradientLayer *gradientLayer = [[CAGradientLayer alloc] init];
    [gradientLayer setBounds:[button bounds]];
    [gradientLayer setPosition:
                CGPointMake([button bounds].size.width/2,
                       [button bounds].size.height/2)];
        [gradientLayer setColors:
                     [NSArray arrayWithObjects:
                            (id)[[UIColor whiteColor] CGColor], 
                            (id)[[UIColor grayColor] CGColor], nil]];
    [[button layer] insertSublayer:gradientLayer atIndex:0];
    [gradientLayer release];

    [[button layer] setCornerRadius:8.0f];
    [[button layer] setMasksToBounds:YES];
    [[button layer] setBorderWidth:1.0f];
}

@end
