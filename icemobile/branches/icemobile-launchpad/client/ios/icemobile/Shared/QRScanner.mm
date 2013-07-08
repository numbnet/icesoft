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

#import "QRScanner.h"
#import "NativeInterface.h"
#import <QRCodeReader.h>

@implementation QRScanner

@synthesize nativeInterface;

- (id)init  {
    self = [super init];
    if (self) {
        // Initialization code here.
    }
    
    return self;
}

- (UIViewController*)scanController {
    NSLog(@"getting scanController ");
    ZXingWidgetController *widController = [[ZXingWidgetController alloc] 
            initWithDelegate:self 
            showCancel:YES OneDMode:NO];
    QRCodeReader* qrcodeReader = [[QRCodeReader alloc] init];
    NSSet *readers = [[NSSet alloc ] initWithObjects:qrcodeReader,nil];
    [qrcodeReader release];
    widController.readers = readers;
    [readers release];
    return widController;
}

- (void)zxingController:(ZXingWidgetController*)controller didScanResult:(NSString *)resultString {
    NSLog(@"didScanResult %@", resultString);
    [self.nativeInterface scanResult:resultString];
    [self.nativeInterface dismissScan];
}

- (void)zxingControllerDidCancel:(ZXingWidgetController*)controller {
    NSLog(@"zxingControllerDidCancel");
    [self.nativeInterface dismissScan];
}


@end
