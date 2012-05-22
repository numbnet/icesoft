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

#import "MapController.h"
#import "TouchRecognizer.h"
#import "PlaceLabel.h"
#import "ARView.h"

@interface MapController ()

@end

@implementation MapController
@synthesize mapView;
@synthesize arView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

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
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    TouchRecognizer *touchRecognizer = [[TouchRecognizer alloc] init];
    touchRecognizer.touchTarget = self;
    [self.mapView addGestureRecognizer:touchRecognizer];
}

- (IBAction) doDone  {
    NSLog(@"MapController doDone");
//    if (nil != self.mapPopover)  {
//        [self.mapPopover dismissPopoverAnimated:YES];
//    } else {
        [self dismissModalViewControllerAnimated:YES];
//    }
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event  {
    CLLocationCoordinate2D coord= [self.mapView convertPoint:[[touches anyObject] locationInView:self.mapView]
            toCoordinateFromView:self.mapView];
    NSLog(@"map touch: lat  %f",coord.latitude);
    NSLog(@"map touch: long %f",coord.longitude);
    [self.arView addPlace:[PlaceLabel placeLabelWithText:@"Spot" 
            initWithLatitude:coord.latitude longitude:coord.longitude]];
}

@end
