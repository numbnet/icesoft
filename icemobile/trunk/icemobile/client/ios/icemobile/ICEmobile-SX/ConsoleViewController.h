//
//  ConsoleViewController.h
//  ICEmobile-SX
//
//  Created by Ted Goddard on 2013-10-01.
//  Copyright (c) 2013 ICEsoft Technologies, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ConsoleViewController : UIViewController  
    <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, retain) UIFont *labelFont;
@property (retain) NSArray *logLines;

- (IBAction)doDone:(id)sender;
- (void)loadLog;

@end
