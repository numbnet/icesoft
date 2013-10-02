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

#import <Foundation/Foundation.h>

@protocol NativeInterfaceViewController <NSObject>

- (void) completeFile:(NSString *)path forComponent:(NSString *)componentID withName:(NSString *)componentName;
- (void) completePost:(NSString *)value forComponent:(NSString *)componentID withName:(NSString *)componentName;
- (void) completeSmallPost:(NSString *)value forComponent:(NSString *)componentID withName:(NSString *)componentName;
- (void) doCancel;
- (void) register;
- (NSString *) prepareUpload:(NSString *)formID;
- (NSString *) getFormData:(NSString *)formID;
- (void) play:(NSString *)audioID;
- (void) setThumbnail: (UIImage*)image at: (NSString *)thumbID;
- (void) returnToBrowser;
- (void) handleResponse:(NSString *)responseString;
- (void) setProgress:(NSInteger)percent;
- (void) setProgressLabel:(NSString*)labelText;

@property (retain) NSString *currentURL;
@property (atomic) BOOL isResponding;

@end
