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

    if (nil !=[button currentImage])  {
        CALayer* imageLayer = [CALayer layer];
        imageLayer.contents = (id) [button currentImage].CGImage;

        CGRect buttonFrame = [button layer].frame;

        CGFloat nativeWidth = CGImageGetWidth([button currentImage].CGImage);
        CGFloat nativeHeight = CGImageGetHeight([button currentImage].CGImage);
        CGRect imageFrame = CGRectMake(0.0, 0.0, nativeWidth / 2, nativeHeight / 2);
        CGFloat offX = (buttonFrame.size.width - imageFrame.size.width) / 2;
        CGFloat offY = (buttonFrame.size.height - imageFrame.size.height) / 2;
        imageFrame.origin.x = offX;
        imageFrame.origin.y = offY;
        imageLayer.frame = imageFrame;

        [[button layer] insertSublayer:imageLayer atIndex:1];
    }

    [[button layer] setCornerRadius:8.0f];
    [[button layer] setMasksToBounds:YES];
    [[button layer] setBorderColor:[[UIColor lightGrayColor] CGColor]];
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

    [IceUtil makeFancyButton:button withLayer:gradientLayer];

    [gradientLayer release];

}


+ (void)pushFancyButton:(UIButton *)button  {

    CAGradientLayer *gradientLayer = [[CAGradientLayer alloc] init];
    [gradientLayer setBounds:[button bounds]];
    gradientLayer.name = @"fancyGradient";
    [gradientLayer setBorderColor:[[UIColor darkGrayColor] CGColor]];
    [gradientLayer setCornerRadius:8.0f];
    [gradientLayer setBorderWidth:3.0f];
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
    [IceUtil makeFancyButton:button withColor:[UIColor lightGrayColor]];
}

+ (NSArray*) logLinesFromString:(NSString*) text  {
    NSMutableArray *lines = [[NSMutableArray alloc] init];
    NSScanner *scanner = [NSScanner scannerWithString:text];
    while (![scanner isAtEnd]) {
        NSString *line = nil;
        [scanner scanUpToCharactersFromSet:[NSCharacterSet newlineCharacterSet] 
            intoString:&line];
        if (line) {
            //line starts with date
            if (0 == [line rangeOfString:@"\\d+-\\d+-\\d+ " 
                    options:NSRegularExpressionSearch].location)  {
                [lines addObject:line];
            } else {
                NSString *cat = [NSString stringWithFormat:@"%@%@",
                        [lines lastObject], line];
                [lines removeLastObject];
                [lines addObject:cat];
           }
        }
    }
    return lines;
}

+ (NSString*) pageFromString:(NSString*)text atPage:(NSInteger)pageNum {
    NSInteger MAX_PAGE = 400;
    NSMutableString* page = [NSMutableString stringWithCapacity:1000];
    NSScanner *scanner = [NSScanner scannerWithString:text];
    NSInteger currentLine = 0;
    while (![scanner isAtEnd]) {
        NSString *line = nil;
        [scanner scanUpToCharactersFromSet:[NSCharacterSet newlineCharacterSet] 
            intoString:&line];
        if (line) {
            currentLine++;
            NSInteger currentPage = currentLine / MAX_PAGE;
            if (currentPage == pageNum) {
                [page appendString:line];
                [page appendString:@"\n"];
            }
            if (currentPage > pageNum)  {
                break;
            }
        }
    }
    if (0 == [page length])  {
        return nil;
    }
    return page;
}

@end
