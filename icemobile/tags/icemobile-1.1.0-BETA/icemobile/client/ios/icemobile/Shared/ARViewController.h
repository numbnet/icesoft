/*
* Copyright 2004-2012 ICEsoft Technologies Canada Corp. (c)
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

#import <UIKit/UIKit.h>
#import "NativeInterface.h"

@interface ARViewController : UIViewController {
    NativeInterface *nativeInterface;
    NSString *selectedPlace;
    UISwitch *compassSwitch;
}

@property (retain) NativeInterface *nativeInterface;
@property (nonatomic, retain) NSString *selectedPlace;
@property (nonatomic, retain) IBOutlet UISwitch *compassSwitch;

- (void) setPlaceLabels:(NSArray *)places;
- (IBAction) doLocations;
- (IBAction) doCancel;
- (IBAction) compassChanged:(UISwitch *)theSwitch;

@end
