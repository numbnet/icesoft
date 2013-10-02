//
//  SettingsViewController.h
//  ICEmobile-SX
//
//  Created by Ted Goddard on 2013-10-01.
//  Copyright (c) 2013 ICEsoft Technologies, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SettingsViewController : UIViewController  
    <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (retain) NSDictionary *settings;

- (void) loadSettings;
- (void) saveSettings;
- (IBAction)doDone:(id)sender;
- (void) setHost:(NSString*)name trustSetting:(BOOL)trust;
- (BOOL) canTrustHost:(NSString*)name;

@end
