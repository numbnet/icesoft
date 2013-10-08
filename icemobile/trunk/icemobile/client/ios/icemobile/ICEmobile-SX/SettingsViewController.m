//
//  SettingsViewController.m
//  ICEmobile-SX
//
//  Created by Ted Goddard on 2013-10-01.
//  Copyright (c) 2013 ICEsoft Technologies, Inc. All rights reserved.
//

#import "SettingsViewController.h"
#import "NamedSwitch.h"
#import "ConsoleViewController.h"
#import "IceUtil.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController
@synthesize logPage;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if (0 == section)  {
        return [self.settings count];
    } 
    if (1 == section)  {
        return 1;
    } 
    
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }

    NSInteger section = [indexPath indexAtPosition:0];
    if (0 == section)  {
        [self populateHostsCell:cell atIndexPath:indexPath];
    } else if (1 == section)  {
        [self populateDiagnosticsCell:cell atIndexPath:indexPath];
    }

    return cell;
}

- (void)populateDiagnosticsCell:(UITableViewCell *)cell 
    atIndexPath:(NSIndexPath *)indexPath  {
    [[cell textLabel] setText:@"Console log"];
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
}

- (void)populateHostsCell:(UITableViewCell *)cell 
        atIndexPath:(NSIndexPath *)indexPath  {

    NSInteger index = [indexPath indexAtPosition:1];
    if (index < [self.settings count])  {
        NSString *key = [[self.settings allKeys] objectAtIndex:index];
        [[cell textLabel] setText:key];
        NamedSwitch *switchView = [[NamedSwitch alloc] initWithFrame:CGRectZero];
        cell.accessoryView = switchView;
        [switchView setOn:[self canTrustHost:key] animated:NO];
        switchView.name = key;
        [switchView addTarget:self action:@selector(switchChanged:) forControlEvents:UIControlEventValueChanged];
        [switchView release];
    } else {
        [[cell textLabel] setText:@"out of bounds"];
    }

}


- (NSString *)tableView:(UITableView *)tableView 
        titleForHeaderInSection:(NSInteger)section  {
    if (0 == section)  {
        return @"Trusted hosts";
    }
    if (1 == section)  {
        return @"Diagnostics";
    }
    return @"untitled";
}

- (void) switchChanged:(id)sender {
    UISwitch* switchControl = sender;
    NSString* host = ((NamedSwitch*)sender).name;
    [self setHost:host trustSetting:switchControl.on];
}

- (IBAction)doDone:(id)sender {
    [self saveSettings];
    [self dismissModalViewControllerAnimated:YES];
}

- (void) loadSettings {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, YES);  
    NSString *documentsPath = [paths objectAtIndex:0];

    NSString *settingsFile = [documentsPath 
            stringByAppendingPathComponent:@"settings.plist"];

    self.settings = [[NSMutableDictionary alloc] init];
    if ([[NSFileManager defaultManager] fileExistsAtPath:settingsFile])  {
        self.settings = [self.settings initWithContentsOfFile:settingsFile];
    }
    [self.tableView reloadData];
    NSLog(@"Settings loaded %@", self.settings);
}

- (void) saveSettings {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, YES);  
    NSString *documentsPath = [paths objectAtIndex:0];

    NSString *settingsFile = [documentsPath 
            stringByAppendingPathComponent:@"settings.plist"]; 

    [self.settings writeToFile:settingsFile atomically:YES];
}

- (void) setHost:(NSString*)name trustSetting:(BOOL)trust  {
    [self.settings setValue:[NSNumber numberWithBool:trust] forKey:name];
}

- (BOOL) canTrustHost:(NSString*)name  {
    return [[self.settings objectForKey:name] boolValue];
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

// In a xib-based application, navigation from a table can be handled in -tableView:didSelectRowAtIndexPath:
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
        NSInteger section = [indexPath indexAtPosition:0];
        NSInteger row = [indexPath indexAtPosition:1];
        if ((1 == section) && (0 == row)) {
        } else {
            return;
        }
    // Navigation logic may go here, for example:
    // Create the next view controller.
     ConsoleViewController *consoleViewController = 
            [[ConsoleViewController alloc] 
                    initWithNibName:@"ConsoleViewController" bundle:nil];

    // Pass the selected object to the new view controller.
    [consoleViewController loadLog];
    [self presentModalViewController:consoleViewController animated:YES];
}

@end
