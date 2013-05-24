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

#import "ARViewController.h"
#import "PlaceLabel.h"
#import "ARView.h"
#import "MapController.h"
#import "IceUtil.h"

@interface ARViewController ()

@end

@implementation ARViewController
@synthesize oldView;
@synthesize nativeInterface;
@synthesize selectedPlace;
@synthesize compassSwitch;
@synthesize mapButton;
@synthesize cancelButton;
@synthesize compassPref;
@synthesize toolbar;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated  {
	[super viewWillAppear:animated];
	ARView *arView = (ARView *)self.view;
    arView.nativeInterface = self.nativeInterface; 
    [self.toolbar setBackgroundColor:[UIColor colorWithPatternImage:
            [UIImage imageNamed:@"bar.png"]] ];
	[arView start];
    //compass markers still getting stuck
//    self.compassSwitch.on = self.compassPref;
//    arView.useCompass = compassSwitch.on; 
}

- (void)viewDidAppear:(BOOL)animated  {
	[super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated  {
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated  {
	[super viewDidDisappear:animated];
	ARView *arView = (ARView *)self.view;
	[arView stop];
}

- (void)viewDidLoad
{
	[super viewDidLoad];
    self.toolbar.frame = CGRectMake(0, 
        self.view.frame.size.height - self.toolbar.frame.size.height, 
        self.view.frame.size.width, self.view.frame.size.height);
    [IceUtil makeFancyButton:mapButton];
    [IceUtil makeFancyButton:cancelButton];
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

- (void)setPlaceLabels:(NSArray *)places {
	ARView *arView = (ARView *)self.view;
	[arView setPlaceLabels:places];	
}

- (IBAction) doLocations  {
    NSLog(@"ARViewController doLocations");
	ARView *arView = (ARView *)self.view;
	NSLog(@"ARViewController currentLocation, %f,%f", arView.location.coordinate.latitude, arView.location.coordinate.longitude);
    //bring up map view
    MapController *mapController = [[MapController alloc] init];
    [[NSBundle mainBundle] loadNibNamed:@"MapController" 
            owner:mapController options:nil];
//    mapController.nativeInterface = self;
    mapController.arView = arView;
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)  {
        if (nil != self.nativeInterface.augPopover)  {
            mapController.popover = self.nativeInterface.augPopover;
            [self.nativeInterface.augPopover setContentViewController:mapController animated:YES];
        }
    } else {
        [self presentModalViewController:mapController animated:YES];
    }
    [mapController.mapView setCenterCoordinate: arView.location.coordinate];
    [mapController release];
}

- (void)stop  {
	ARView *arView = (ARView *)self.view;
    self.compassSwitch.on = NO;
    [arView setCompass:NO];
    [arView stop];
}

- (IBAction) doCancel  {
    [self stop];
    [self.nativeInterface augDismiss];
}

- (IBAction) compassChanged:(UISwitch *)theSwitch {
	ARView *arView = (ARView *)self.view;
    self.compassPref = theSwitch.on;
    NSLog(@"compassPref %d", self.compassPref);
    [arView setCompass:theSwitch.on];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event  {
	ARView *arView = (ARView *)self.view;
    UITouch *touch = [touches anyObject];
    CGPoint touchPoint = [touch locationInView:self.view];
	for (PlaceLabel *place in arView.placeLabels) {
        if (place.view.hidden)  {
            continue;
        }
        CGPoint placeCenter = place.view.center;
        CGPoint offTouch = CGPointMake(touchPoint.x - 200, touchPoint.y - 200);
        //the following does not work due to 200,200 offset in PlaceLabel
//        BOOL inPlace = [place.view 
//                pointInside:offTouch withEvent:event];
        BOOL inPlace = ( (fabs(offTouch.x - placeCenter.x) < 25) && 
                         (fabs(offTouch.y - placeCenter.y) < 25) );
        if (inPlace)  {
            self.selectedPlace = place.placeName;
            [arView stop];
            [self.nativeInterface augDone];
            return;
        }
    }
}

@end
