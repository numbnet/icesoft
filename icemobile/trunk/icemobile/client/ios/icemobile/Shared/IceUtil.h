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
#import <QuartzCore/QuartzCore.h>

@interface IceUtil : NSObject

+ (void)makeFancyButton:(UIButton*)button withLayer:(CAGradientLayer*)fancyLayer;
+ (void)makeFancyButton:(UIButton*)button withColor:(UIColor*)color;
+ (void)makeFancyButton:(UIButton*)button;
+ (void)pushFancyButton:(UIButton*)button;
+ (NSArray*) linesFromString:(NSString*)text;
+ (NSString*) pageFromString:(NSString*)text atPage:(NSInteger)pageNum;

@end
