//
//  SettingsViewController.m
//  ICEmobile-SX
//
//  Created by Ted Goddard on 2013-10-01.
//  Copyright (c) 2013 ICEsoft Technologies, Inc. All rights reserved.
//

#import "ConsoleViewController.h"
#import "IceUtil.h"

@interface ConsoleViewController ()

@end

@implementation ConsoleViewController
@synthesize labelFont;

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

    self.labelFont = [UIFont preferredFontForTextStyle:UIFontTextStyleBody];

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
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if (0 == section)  {
        return [self.logLines count];
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
        [self populateLogCell:cell atIndexPath:indexPath];
    } 

    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView 
        heightForRowAtIndexPath:(NSIndexPath *)indexPath  {
    CGSize maximumSize = CGSizeMake(tableView.frame.size.width, 9999);
    NSString *line = [self.logLines objectAtIndex: 
            [indexPath indexAtPosition:1]];
    CGSize lineSize  = [line sizeWithFont:self.labelFont 
            constrainedToSize:maximumSize
            lineBreakMode:NSLineBreakByWordWrapping];
    return (lineSize.height + 50);
}

- (void)loadLog  {
    NSError *error = nil;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *logPath = [documentsDirectory 
            stringByAppendingPathComponent:@"console.log"];
    NSString *logText = [[NSString alloc] initWithContentsOfFile:logPath
            encoding:NSASCIIStringEncoding error:&error];
    self.logLines = [IceUtil logLinesFromString:logText]; 
}

- (void)populateLogCell:(UITableViewCell *)cell 
    atIndexPath:(NSIndexPath *)indexPath  {
    NSString *text = [self.logLines objectAtIndex:
            [indexPath indexAtPosition:1] ];
    [cell.textLabel setText:text];
    cell.textLabel.numberOfLines = 0;
    cell.textLabel.lineBreakMode = NSLineBreakByWordWrapping;
}


- (NSString *)tableView:(UITableView *)tableView 
        titleForHeaderInSection:(NSInteger)section  {
    if (0 == section)  {
        return @"Console";
    }
    return @"untitled";
}

- (IBAction)doDone:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
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

/*
#pragma mark - Table view delegate

// In a xib-based application, navigation from a table can be handled in -tableView:didSelectRowAtIndexPath:
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here, for example:
    // Create the next view controller.
    DetailViewController *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];

    // Pass the selected object to the new view controller.
    
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
}
 
 */

@end
