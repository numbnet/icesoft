//
//  AVScanViewController.m
//  ICEmobile-SX
//
//  Created by Ted Goddard on 2013-08-21.
//  Copyright (c) 2013 ICEsoft Technologies, Inc. All rights reserved.
//

#import "AVScanViewController.h"
#import "NativeInterface.h"

//derived from QRchestra

@interface AVScanViewController ()<AVCaptureMetadataOutputObjectsDelegate>

@property (strong, nonatomic) IBOutlet UIView *previewView;
@property (strong, nonatomic) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, retain) CALayer *barcodeTargetLayer;
@property (strong, nonatomic) AVCaptureSession *captureSession;
@property (strong, nonatomic) AVCaptureDevice *videoDevice;
@property (strong, nonatomic) AVCaptureDeviceInput *videoIn;

@property (nonatomic, retain) NSTimer *barcodeTimer;
@property (strong, nonatomic) NSArray *barcodes;
@property (nonatomic,retain) AVCaptureMetadataOutput *metadataOutput;

@property (atomic) BOOL isScanned;

@end

@implementation AVScanViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (IBAction)doCancel:(id)sender {
    [self completeScan:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    // Do any additional setup after loading the view from its nib.
    
    self.isScanned = NO;

    self.captureSession = [[AVCaptureSession alloc] init];
//	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(captureSessionNotification:) 
//            name:nil object:self.captureSession];

	/* Video */
	AVCaptureDevice *videoDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
	AVCaptureDeviceInput *videoIn = [[AVCaptureDeviceInput alloc] initWithDevice:videoDevice error:nil];
	if ([self.captureSession canAddInput:videoIn])
		[self.captureSession addInput:videoIn];

	[self setMetadataOutput:[[AVCaptureMetadataOutput alloc] init]];
//	[[self metadataOutput] setMetadataObjectTypes:[NSArray arrayWithObjects:AVMetadataObjectTypeQRCode, nil]];
	dispatch_queue_t metadataQueue = dispatch_queue_create("com.AVCam.metadata", NULL);
	[[self metadataOutput] setMetadataObjectsDelegate:self queue:metadataQueue];
//	dispatch_release(metadataQueue);
	
	if ([self.captureSession canAddOutput:[self metadataOutput]]) {
		[self.captureSession addOutput:[self metadataOutput]];
	}


    [self.captureSession startRunning];
    [[self metadataOutput] setMetadataObjectTypes:[NSArray arrayWithObjects:AVMetadataObjectTypeQRCode, AVMetadataObjectTypeUPCECode, AVMetadataObjectTypeEAN13Code, nil]];



	AVCaptureVideoPreviewLayer *previewLayer = [[AVCaptureVideoPreviewLayer alloc] initWithSession:self.captureSession];
	[previewLayer setFrame:self.previewView.bounds];
//	[previewLayer setVideoGravity:AVLayerVideoGravityResizeAspectFill];
//	if ([[previewLayer connection] isVideoOrientationSupported]) {
//		[[previewLayer connection] setVideoOrientation:AVCaptureVideoOrientationLandscapeLeft];
//	}
	[self.previewView.layer addSublayer:previewLayer];
	[self.previewView.layer setMasksToBounds:YES];
	[self setPreviewLayer:previewLayer];

	// Configure barcode overlay
	CALayer* barcodeTargetLayer = [[CALayer alloc] init];
	CGRect r = self.view.layer.bounds;
	barcodeTargetLayer.frame = r;
	self.barcodeTargetLayer = barcodeTargetLayer;
	[self.view.layer addSublayer:self.barcodeTargetLayer];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//- (void)captureSessionNotification:(NSNotification *)notification
//{
//NSLog(@"captureSessionNotification %@", notification);
//}
//

- (BOOL)shouldAutorotate  {
    return NO;
}

- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection
{
	@synchronized(self) {
		self.barcodes = metadataObjects;

		AVMetadataMachineReadableCodeObject *barcode = [self.barcodes lastObject];
		
		// Draw overlay
		[self.barcodeTimer invalidate];
		self.barcodeTimer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(removeDetectedBarcodeUI) userInfo:nil repeats:NO];
		AVMetadataMachineReadableCodeObject 
                *transformedBarcode = (AVMetadataMachineReadableCodeObject*)[
                self.previewLayer transformedMetadataObjectForMetadataObject:barcode];
		CGPathRef barcodeBoundary = [self createPathForPoints:transformedBarcode.corners];

		[CATransaction begin];
		[CATransaction setDisableActions:YES];
		[self removeDetectedBarcodeUI];
		[self.barcodeTargetLayer addSublayer:
                [self barcodeOverlayLayerForPath:barcodeBoundary 
                withColor:[UIColor blueColor]]];
		[CATransaction commit];
		CFRelease(barcodeBoundary);

NSLog(@"scanned barcode %@", barcode.stringValue);


        dispatch_after(
                dispatch_time(DISPATCH_TIME_NOW, 0.5 * NSEC_PER_SEC), 
                dispatch_get_main_queue(), 
                ^(void){
                    [self completeScan:barcode.stringValue];
                });

	}
}

- (void)removeDetectedBarcodeUI
{
	[self removeAllSublayersFromLayer:self.barcodeTargetLayer];
}

- (CAShapeLayer*)barcodeOverlayLayerForPath:(CGPathRef)path withColor:(UIColor*)color
{
	CAShapeLayer *maskLayer = [CAShapeLayer layer];
	
	[maskLayer setPath:path];
	[maskLayer setLineJoin:kCALineJoinRound];
	[maskLayer setLineWidth:2.0];
	[maskLayer setStrokeColor:[color CGColor]];
	[maskLayer setFillColor:[[color colorWithAlphaComponent:0.20] CGColor]];
	
	return maskLayer;
}

- (void)removeAllSublayersFromLayer:(CALayer *)layer
{
	if (layer) {
		NSArray* sublayers = [[layer sublayers] copy];
		for( CALayer* l in sublayers ) {
			[l removeFromSuperlayer];
		}
	}
}

- (void)completeScan:(NSString *)text {
    if (!self.isScanned)  {
        self.isScanned = YES;
        [self.captureSession stopRunning];
        [self.nativeInterface scanResult:text];
        [self.nativeInterface dismissScan];
    }
}

- (CGMutablePathRef) createPathForPoints:(NSArray*) points  {
	CGMutablePathRef path = CGPathCreateMutable();
	CGPoint point;
	
	if ([points count] > 0) {
		CGPointMakeWithDictionaryRepresentation((CFDictionaryRef)[points objectAtIndex:0], &point);
		CGPathMoveToPoint(path, nil, point.x, point.y);
		
		int i = 1;
		while (i < [points count]) {
			CGPointMakeWithDictionaryRepresentation((CFDictionaryRef)[points objectAtIndex:i], &point);
			CGPathAddLineToPoint(path, nil, point.x, point.y);
			i++;
		}
		
		CGPathCloseSubpath(path);
	}
	
	return path;
}

@end
