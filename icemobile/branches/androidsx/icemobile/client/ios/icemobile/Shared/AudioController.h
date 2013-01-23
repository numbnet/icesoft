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

#import <UIKit/UIKit.h>

@class NativeInterface;

@interface AudioController : UIViewController  {

    NativeInterface *nativeInterface;
    BOOL isRecording;
    UIButton *recordStopButton;
    UIButton *cancelButton;
    UIButton *useButton;

}

@property (retain) NativeInterface *nativeInterface;
@property (nonatomic)  BOOL isRecording;
@property (nonatomic, retain) IBOutlet UIButton *recordStopButton;
@property (nonatomic, retain) IBOutlet UIButton *cancelButton;
@property (nonatomic, retain) IBOutlet UIButton *useButton;

- (IBAction) doRecordStop;
- (IBAction) doRecord;
- (IBAction) doStop;
- (IBAction) doDone;
- (IBAction) doCancel;

@end
