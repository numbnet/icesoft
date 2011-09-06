/*
* Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
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

#import "Preferences.h"
#import "MainViewController.h"

@implementation Preferences

@synthesize urlField;
@synthesize emailField;
@synthesize historyPicker;
@synthesize oldView;
@synthesize mainViewController;
@synthesize history;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        self.history = [prefs objectForKey:@"history"];
        if (nil == self.history)  {
            self.history = [[NSMutableArray alloc] init];
        }
    }
    return self;
}

- (void)dealloc
{
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    NSLog(@"Preferences viewDidLoad");
}


- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)update {
    NSString* currentURL = [[mainViewController getCurrentURL] absoluteString];
    self.urlField.text = currentURL;
    [historyPicker reloadAllComponents];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (IBAction) doGo {
    NSLog(@"Go pressed");
    [mainViewController loadURL:urlField.text];
    [self addIfUnique:urlField.text];
    [self dismiss];
}

- (IBAction) doQuit {
    NSLog(@"Quit pressed");
    exit(0);
}

- (IBAction) doReload {
    NSLog(@"Reload pressed");
    [mainViewController reloadCurrentPage];
    [self dismiss];
}

- (IBAction) doDone {
    NSLog(@"Done pressed");
    [self dismiss];
}

- (IBAction) doClear {
    NSLog(@"Clear History pressed");
    self.history = [[NSMutableArray alloc] init];
    [self.history addObject:@"http://www.icemobile.org/demos.html"];
    [historyPicker reloadAllComponents];
}

- (void) dismiss {
    if (nil != self.emailField.text)  {
        mainViewController.notificationEmail = self.emailField.text;
    }
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [prefs setObject:self.history forKey:@"history"];
    [prefs synchronize];
    UIView *containerView = self.view.superview;
    [UIView transitionWithView:containerView duration:0.5
		options:UIViewAnimationOptionTransitionFlipFromLeft
		animations:^ { [self.view removeFromSuperview]; 
        [containerView addSubview:self.oldView]; }
		completion:nil];
}

- (void) addIfUnique:(NSString *) url  {
    BOOL isNew = YES;
    for (NSString *existing in self.history) {
        if ([existing isEqualToString:url])  {
            isNew = NO;
            break;
        }
    }
    if (isNew)  {
        [self.history addObject:url];
    }

}

- (BOOL)textFieldShouldReturn:(UITextField *)textField  {
    [textField resignFirstResponder];
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField  {
    [textField resignFirstResponder];
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView  {
        return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
      numberOfRowsInComponent:(NSInteger)component  {
    return [self.history count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView 
    titleForRow:(NSInteger)row forComponent:(NSInteger)component  {
    NSString *title = [self.history objectAtIndex:row];
    if (NSNotFound == [title rangeOfString:@"http://"].location)  {
        return title;
    }
    return [title substringFromIndex:7];
}

- (void)pickerView:(UIPickerView *)pickerView 
    didSelectRow:(NSInteger)row inComponent:(NSInteger)component  {
    NSLog(@" pickerView didSelectRow %d", row);
    self.urlField.text = [self.history objectAtIndex:row];
}

@end
