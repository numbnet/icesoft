//
//  ButtonCell.m
//  BridgeIt
//
//  Created by Ted Goddard on 2013-10-08.
//  Copyright (c) 2013 ICEsoft Technologies, Inc. All rights reserved.
//

#import "ButtonCell.h"

@implementation ButtonCell

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.button = [[UIButton alloc] initWithFrame:
                CGRectMake(0.0, 0.0, frame.size.width, frame.size.height)];
        [[self.button layer] setCornerRadius:8.0f];
        [[self.button layer] setBorderColor:[[UIColor blackColor] CGColor]];
        [[self.button layer] setBorderWidth:1.0f];
        [self.contentView addSubview:self.button];;
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
