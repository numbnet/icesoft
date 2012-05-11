//
//  ARViewController.m
//  ICEmobile-SX
//
//  Created by Ted Goddard on 12-05-11.
//  Copyright (c) 2012 ICEsoft Technologies, Inc. All rights reserved.
//

#import "ARViewController.h"
#import "PlaceLabel.h"
#import "ARView.h"

@interface ARViewController ()

@end

@implementation ARViewController

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
	[arView start];
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
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}

- (void)setPlaceLabels:(NSArray *)places {
	ARView *arView = (ARView *)self.view;
	[arView setPlaceLabels:places];	
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event  {
NSLog(@"TODO: implement return to browser from label selection");
    [self dismissModalViewControllerAnimated:YES];
}

@end
