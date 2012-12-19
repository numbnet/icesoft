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
@synthesize oldView;
@synthesize mainViewController;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
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
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (IBAction) doGo {
    NSLog(@"Go pressed");
    [mainViewController loadURL:urlField.text];
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

- (void) dismiss {
    if (nil != self.emailField.text)  {
        mainViewController.notificationEmail = self.emailField.text;
    } 
    UIView *containerView = self.view.superview;
    [UIView transitionWithView:containerView duration:0.5
		options:UIViewAnimationOptionTransitionFlipFromLeft
		animations:^ { [self.view removeFromSuperview]; 
        [containerView addSubview:self.oldView]; }
		completion:nil];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField  {
    [textField resignFirstResponder];
    return YES;
}

@end
