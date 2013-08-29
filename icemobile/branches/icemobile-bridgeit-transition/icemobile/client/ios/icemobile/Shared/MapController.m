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

#import "MapController.h"
#import "PlaceLabel.h"
#import "ARView.h"
#import "IceUtil.h"

@interface MapController ()

@end

@implementation MapController
@synthesize mapView;
@synthesize arView;
@synthesize popover;
@synthesize doneButton;

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

    [IceUtil makeFancyButton:doneButton];
    UILongPressGestureRecognizer *gestureRec = 
            [[UILongPressGestureRecognizer alloc] 
    initWithTarget:self action:@selector(handleGesture:)];
    [self.mapView addGestureRecognizer:gestureRec];
    [gestureRec release];

    [self.mapView setUserTrackingMode:MKUserTrackingModeFollow];

	for (PlaceLabel *place in self.arView.placeLabels) {
        MKPointAnnotation *annot = [[MKPointAnnotation alloc] init];
        annot.coordinate = place.location.coordinate;
        annot.title = place.placeName;
        [self.mapView addAnnotation:annot];
        [annot release];
    }
}

- (IBAction) doDone  {
    NSLog(@"MapController doDone");
    if (nil != self.popover)  {
        [self.popover dismissPopoverAnimated:YES];
    } else {
        [self dismissModalViewControllerAnimated:YES];
    }
}

- (void)handleGesture:(UIGestureRecognizer *)gestureRecognizer {
    if (gestureRecognizer.state != UIGestureRecognizerStateBegan)
        return;

    CGPoint touchPoint = [gestureRecognizer locationInView:self.mapView];   
    CLLocationCoordinate2D coord = 
        [self.mapView convertPoint:touchPoint toCoordinateFromView:self.mapView];
    NSLog(@"GEST map touch: lat  %f",coord.latitude);
    NSLog(@"GEST map touch: long %f",coord.longitude);
    [self.arView addPlace:[PlaceLabel placeLabelWithText:@"Spot" 
            andImage:[UIImage imageNamed:@"ar.png"]
            initWithLatitude:coord.latitude longitude:coord.longitude]];
            
    MKPointAnnotation *annot = [[MKPointAnnotation alloc] init];
    annot.coordinate = coord;
    [self.mapView addAnnotation:annot];
    [annot release];
}

@end
