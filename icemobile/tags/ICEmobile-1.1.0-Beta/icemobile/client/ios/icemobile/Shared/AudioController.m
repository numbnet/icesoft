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

#import "AudioController.h"
#import "NativeInterface.h"

@implementation AudioController

@synthesize nativeInterface;
@synthesize recordControl;
@synthesize submitControl;

- (IBAction) doRecord  {
    NSLog(@"AudioController doRecord");
    [self.nativeInterface recordStart];
}

- (IBAction) doStop  {
    NSLog(@"AudioController doStop");
    [self.nativeInterface recordStop];
}

- (IBAction) doDone  {
    NSLog(@"AudioController doDone");
    [self.nativeInterface recordDone];
}

- (IBAction) doCancel  {
    NSLog(@"AudioController doCancel");
    [self.nativeInterface recordDismiss];
}

- (IBAction) recordAction  {
    NSLog(@"AudioController recordAction ");
    if (0 == recordControl.selectedSegmentIndex)  {
        [self doRecord];
        return;
    }
    if (1 == recordControl.selectedSegmentIndex)  {
        [self doStop];
        [recordControl setSelectedSegmentIndex:-1];
        return;
    }
}

- (IBAction) submitAction  {
    NSLog(@"AudioController submitAction");
    if (0 == submitControl.selectedSegmentIndex)  {
        [self doCancel];
        [submitControl setSelectedSegmentIndex:-1];
        return;
    }
    if (1 == submitControl.selectedSegmentIndex)  {
        [self doDone];
        [submitControl setSelectedSegmentIndex:-1];
        return;
    }
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
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
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
