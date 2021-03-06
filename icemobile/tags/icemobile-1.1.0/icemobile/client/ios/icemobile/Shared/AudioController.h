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

#import <UIKit/UIKit.h>

@class NativeInterface;

@interface AudioController : UIViewController  {

    NativeInterface *nativeInterface;
    UISegmentedControl *recordControl;
    UISegmentedControl *submitControl;

}

@property (retain) NativeInterface *nativeInterface;
@property (nonatomic, retain) IBOutlet UISegmentedControl *recordControl;
@property (nonatomic, retain) IBOutlet UISegmentedControl *submitControl;

- (IBAction) doRecord;
- (IBAction) doStop;
- (IBAction) doDone;
- (IBAction) doCancel;
- (IBAction) recordAction;
- (IBAction) submitAction;

@end
