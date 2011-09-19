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

#import "NativeInterface.h"
#import "MainViewController.h"
#import "MobileCoreServices/MobileCoreServices.h"
#import "MediaPlayer/MediaPlayer.h"


@implementation NativeInterface

@synthesize controller;
@synthesize activeDOMElementId;
@synthesize maxwidth;
@synthesize maxheight;
@synthesize recording;
@synthesize uploading;
@synthesize soundRecorder;
@synthesize receivedData;

static char base64EncodingTable[64] = {
  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
  'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
  'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
  'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
};

/*Return YES to indicate that the command was successfully dispatched
*/
- (BOOL)dispatch: (NSString*)command  {
    NSLog(@"NativeInterface dispatch %@ ", command);
    NSArray *queryParts = [command componentsSeparatedByString:@"?"];
    NSString *commandName = [queryParts objectAtIndex:0];
    NSDictionary *params;
    if ([queryParts count] > 1) {
        params = [self parseQuery:[queryParts objectAtIndex:1]];
    }
    if ([@"camera" isEqualToString:commandName])  {
        [self camera:[params objectForKey:@"id"] 
                  maxwidth:[params objectForKey:@"maxwidth"]
                  maxheight:[params objectForKey:@"maxheight"] ];
    } else if ([@"camcorder" isEqualToString:commandName])  {
        [self camcorder:[params objectForKey:@"id"]];
    } else if ([@"upload" isEqualToString:commandName])  {
        [self upload:[params objectForKey:@"id"]];
    } else if ([@"microphone" isEqualToString:commandName])  {
        [self microphone:[params objectForKey:@"id"]];
    } else if ([@"play" isEqualToString:commandName])  {
        [self play:[params objectForKey:@"id"]];
    } else if ([@"scan" isEqualToString:commandName])  {
        [self scan:[params objectForKey:@"id"]];
    }
    return YES;
}

- (BOOL)play: (NSString*)audioId  {
    NSString *scriptTemplate = @"document.getElementById(\"%@\").src;";
    NSString *script = [NSString stringWithFormat:scriptTemplate, audioId];
    NSString *result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];

    NSString *srcString = result;
    
    scriptTemplate = @"document.location.href;";
    script = [NSString stringWithFormat:scriptTemplate, audioId];
    result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];

    NSString *baseString = result;
    NSURL *baseURL = [NSURL URLWithString:baseString];
    NSURL *fullURL = [NSURL URLWithString:srcString relativeToURL:baseURL];

    NSString *soundPath = [NSTemporaryDirectory() 
            stringByAppendingPathComponent:@"remotesound"];
    NSData *soundData = [NSData dataWithContentsOfURL:fullURL];
    [soundData writeToFile:soundPath atomically:YES];
    NSLog(@"will play sound %@", [fullURL absoluteURL]);

    NSError* err;
    AVAudioPlayer *player = [[AVAudioPlayer alloc] initWithContentsOfURL:
            [NSURL fileURLWithPath:soundPath] error:&err];
    [player play];

    if (nil != err)  {
        NSLog(@"error playing sound %@", err);
    }

    return YES;
}

- (BOOL)microphone: (NSString*)micId  {
    self.activeDOMElementId = micId;
    NSString *micName = [micId stringByAppendingString:@"-file"];
    NSLog(@"called microphone for %@", micId);

    NSString *tempDir = NSTemporaryDirectory ();
    NSString *soundFilePath =
                [tempDir stringByAppendingString: @"sound.mp4"];
    NSURL *soundFileURL = [[NSURL alloc] initFileURLWithPath: soundFilePath];

    if (self.recording)  {
        self.recording = NO;
        NSLog(@"recording stopped");
        [self.soundRecorder stop];
        NSString *scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
        NSString *script = [NSString stringWithFormat:scriptTemplate, 
                micId, micName, soundFilePath];
        NSString *result = [controller.webView 
                stringByEvaluatingJavaScriptFromString: script];
        return YES;
    }
 
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    audioSession.delegate = self;
    [audioSession setActive: YES error: nil];

    [[AVAudioSession sharedInstance]
            setCategory: AVAudioSessionCategoryRecord error: nil];
 
    NSDictionary *recordSettings =
        [[NSDictionary alloc] initWithObjectsAndKeys:
            [NSNumber numberWithFloat: 44100.0], AVSampleRateKey,
            [NSNumber numberWithInt: kAudioFormatMPEG4AAC], AVFormatIDKey,
//             [NSNumber numberWithInt: kAudioFormatLinearPCM], AVFormatIDKey,
//             [NSNumber numberWithInt: kAudioFormatAppleIMA4], AVFormatIDKey,
//             [NSNumber numberWithInt: kAudioFormatAppleLossless], AVFormatIDKey,
            [NSNumber numberWithInt: 1], AVNumberOfChannelsKey,
            [NSNumber numberWithInt: AVAudioQualityMax], AVEncoderAudioQualityKey,
            nil];
 
    AVAudioRecorder *soundRecorder =
        [[AVAudioRecorder alloc] initWithURL: soundFileURL
                                    settings: recordSettings
                                       error: nil];
    self.soundRecorder = soundRecorder;
    soundRecorder.delegate = self;
    [soundRecorder prepareToRecord];
    NSLog(@"recording started");
    [soundRecorder record];
    self.recording = YES;

    return YES;
}


- (BOOL)camera: (NSString*)cameraId maxwidth: (NSString*)maxw 
        maxheight: (NSString*)maxh {
    self.activeDOMElementId = cameraId;
    self.maxwidth = maxw;
    self.maxheight = maxh;
    UIImagePickerController* picker = [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.allowsEditing = YES;
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera ])  {
        picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    }

    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)  {
        UIPopoverController *popover = [[UIPopoverController alloc] initWithContentViewController:picker];
        //[picker release];
        [popover presentPopoverFromRect:CGRectMake(200.0, 200.0, 0.0, 0.0) 
                                 inView:self.controller.view
               permittedArrowDirections:UIPopoverArrowDirectionAny 
                               animated:YES];
    } else {
        [controller presentModalViewController:picker animated:YES];
    }
    
    return YES;
}

- (BOOL)camcorder: (NSString*)cameraId  {
    self.activeDOMElementId = cameraId;
    UIImagePickerController* picker = [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.allowsEditing = YES;
    picker.mediaTypes =  [[NSArray alloc] initWithObjects: (NSString *) kUTTypeMovie, nil];
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera ])  {
        picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    }
 
    [controller presentModalViewController:picker animated:YES];
    
    return YES;
}

- (BOOL)scan: (NSString*)scanId  {
    [self.controller scanQR];
    return YES;
}

- (void)imagePickerController:(UIImagePickerController *)picker
                    didFinishPickingImage:(UIImage *)image
                    editingInfo:(NSDictionary *)editingInfo  {
    NSString *cameraId = self.activeDOMElementId;
    NSString *cameraName = [cameraId stringByAppendingString:@"-file"];

    UIImage *uploadImage = image;
    if ((nil != self.maxwidth) && (nil != self.maxheight))  {
        int maxwI = [self.maxwidth intValue];
        int maxhI = [self.maxheight intValue];
        self.maxwidth = nil;
        self.maxheight = nil;
        uploadImage = [self scaleImage:image maxWidth:maxwI maxHeight:maxhI];
    }

    NSString *savedPath = [self saveImage:uploadImage];
    NSLog(@"called camera and saved %@ for %@", savedPath, cameraId);
    // Remove the picker interface and release the picker object.
    [controller dismissModalViewControllerAnimated:YES];
    [picker release];

    UIImage *scaledImage = [self scaleImage:image toSize:64];
    [self setThumbnail:scaledImage at:cameraId];

    NSString *scriptTemplate;
    NSString *script;
    NSString *result;

    scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
    script = [NSString stringWithFormat:scriptTemplate, cameraId, cameraName, savedPath];
    result = [controller.webView stringByEvaluatingJavaScriptFromString: script];
}

- (void) imagePickerController: (UIImagePickerController *)picker
            didFinishPickingMediaWithInfo: (NSDictionary *)info  {
    NSLog(@"imagePickerController didFinishPickingMediaWithInfo %@", info);
    UIImage *editedImage = (UIImage *) [info objectForKey:
                    UIImagePickerControllerEditedImage];
    if (nil != editedImage)  {
        [self imagePickerController:picker didFinishPickingImage:editedImage editingInfo:info];
        return;
    }
    NSString *cameraId = self.activeDOMElementId;
    NSString *cameraName = [cameraId stringByAppendingString:@"-file"];
    NSURL *movieURL = [info objectForKey: UIImagePickerControllerMediaURL];
    NSString *moviePath = [movieURL path];

    NSString *scriptTemplate;
    NSString *script;
    NSString *result;

    MPMoviePlayerController *movieController = 
            [[MPMoviePlayerController alloc] initWithContentURL:movieURL];
    movieController.shouldAutoplay = NO;
    movieController.initialPlaybackTime = 0;
    movieController.currentPlaybackTime = 0;
    UIImage *image = [movieController thumbnailImageAtTime:0 
                           timeOption:MPMovieTimeOptionNearestKeyFrame];

    UIImage *scaledImage = [self scaleImage:image toSize:64];
    [self setThumbnail:scaledImage at:cameraId];

    scriptTemplate = @"ice.addHidden(\"%@\", \"%@\", \"%@\");";
    script = [NSString stringWithFormat:scriptTemplate, cameraId, cameraName, moviePath];
    result = [controller.webView stringByEvaluatingJavaScriptFromString: script];

    [controller dismissModalViewControllerAnimated:YES];
    [picker release];
}

- (void)setThumbnail: (UIImage*)image at: (NSString *)thumbId  {
    NSData *scaledData =  UIImageJPEGRepresentation(image, 0.5);
    NSString *image64 = [self  base64StringFromData:scaledData];
    NSString *dataURL = [@"data:image/jpg;base64," 
            stringByAppendingString:image64];
    NSLog(@"scaled and encoded thumbnail %d", [image64 length]);

    NSString *scriptTemplate;
    NSString *script;
    NSString *result;

    NSString *thumbName = [thumbId stringByAppendingString:@"-thumb"];
    scriptTemplate = @"ice.setThumbnail(\"%@\", \"%@\");";
    script = [NSString stringWithFormat:scriptTemplate, thumbName, dataURL];
    result = [controller.webView stringByEvaluatingJavaScriptFromString: script];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    NSLog(@"imagePickerControllerDidCancel");
    [controller dismissModalViewControllerAnimated:YES];
    [picker release];
}

- (UIImage*)scaleImage: (UIImage*)image maxWidth: (int)maxw maxHeight: (int)maxh  {
    CGSize imageSize = [image size];
    CGFloat maxwF = (CGFloat) maxw;
    CGFloat maxhF = (CGFloat) maxh;
    CGFloat factorX = 1.0;
    CGFloat factorY = 1.0;
    if (imageSize.width > maxw)  {
        factorX = maxwF / imageSize.width;
    }
    if (imageSize.height > maxh)  {
        factorY = maxhF / imageSize.height;
    }

    CGFloat factor = MIN(factorX, factorY);
    
    NSLog(@"scaling image with factor %f %f", factorX, factorY);
    CGSize bufSize = CGSizeMake(maxwF, maxhF);
    UIGraphicsBeginImageContext(bufSize);
    [image drawInRect:CGRectMake(0, 0, imageSize.width * factor, 
            imageSize.height * factor)];
    UIImage* scaledImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return scaledImage;

}

- (UIImage*)scaleImage: (UIImage*)image toSize: (int)finalSize  {
    return [self scaleImage:image maxWidth:finalSize maxHeight:finalSize];
}

- (NSString*)saveImage: (UIImage*)image  {
    NSString *imagePath = [NSTemporaryDirectory() 
            stringByAppendingPathComponent:@"test.jpg"];
    [UIImageJPEGRepresentation(image, 0.7) writeToFile:imagePath atomically:NO];
    return imagePath;
}

- (BOOL)upload: (NSString*)formId  {
    if (self.uploading)  {
    NSLog(@" already uploading, ignoring request");
        return NO;
    }
    self.uploading = YES;
    self.activeDOMElementId = formId;
    
    NSString *scriptTemplate;
    NSString *script; 
    NSString *result;
    
    scriptTemplate = @"document.getElementById(\"%@\").action;";
    script = [NSString stringWithFormat:scriptTemplate, formId];
    result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];

    NSString *actionString = result;
    
    scriptTemplate = @"document.location.href;";
    script = [NSString stringWithFormat:scriptTemplate, formId];
    result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];

    NSString *baseString = result;
    NSURL *baseURL = [NSURL URLWithString:baseString];
    NSURL *actionURL = [NSURL URLWithString:actionString relativeToURL:baseURL];
    NSLog(@"upload will post to actionURL %@", [actionURL absoluteString] );

    result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: @"ice.progress(0);"];

    scriptTemplate = @"ice.getCurrentSerialized();";
    script = [NSString stringWithFormat:scriptTemplate, formId];
    result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];

    NSDictionary *parts = [self parseQuery:result];
    [self multipartPost:parts toURL:[actionURL absoluteString]];

    return YES;
}

- (NSString *) guessMimeType:(NSString *)path {
    if ([path hasSuffix:@".jpg"])  {
        return @"Content-Type: image/jpeg\r\n\r\n";
    }
    if ([path hasSuffix:@".m4a"])  {
        return @"Content-Type: audio/m4a\r\n\r\n";
    }
    if ([path hasSuffix:@".mp4"])  {
        return @"Content-Type: audio/mp4\r\n\r\n";
    }
    if ([path hasSuffix:@".caf"])  {
        return @"Content-Type: audio/x-caf\r\n\r\n";
    }
    if ([path hasSuffix:@".wav"])  {
        return @"Content-Type: audio/x-wav\r\n\r\n";
    }
    if ([path hasSuffix:@".MOV"])  {
        return @"Content-Type: video/mp4\r\n\r\n";
    }
    return "text/plain";
}

/*Return NSDictionary of query parameters
*/
- (NSDictionary*)parseQuery: (NSString*)queryString  {
    if ((nil == queryString) || (0 == [queryString length]))  {
        return [NSDictionary dictionary];
    }
    NSArray *pairs = [queryString componentsSeparatedByString:@"&"];
    NSMutableDictionary *pairDict = [NSMutableDictionary dictionary];
    for (NSString *pair in pairs) {
        NSArray *pairPair = [pair componentsSeparatedByString:@"="];
        NSString *name = [[pairPair objectAtIndex:0] 
                stringByReplacingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
        NSString *value = [[pairPair objectAtIndex:1] 
            stringByReplacingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
        [pairDict setObject:value forKey:name];
    }

    return pairDict;
}

- (NSString *) base64StringFromData: (NSData *)data {
    int lentext = [data length]; 
    if (lentext < 1) return @"";

    char *outbuf = malloc(lentext*4/3+4); // add 4 to be sure

    if ( !outbuf ) return nil;

    const unsigned char *raw = [data bytes];

    int inp = 0;
    int outp = 0;
    int do_now = lentext - (lentext%3);

    for ( outp = 0, inp = 0; inp < do_now; inp += 3 )
    {
        outbuf[outp++] = base64EncodingTable[(raw[inp] & 0xFC) >> 2];
        outbuf[outp++] = base64EncodingTable[((raw[inp] & 0x03) << 4) | ((raw[inp+1] & 0xF0) >> 4)];
        outbuf[outp++] = base64EncodingTable[((raw[inp+1] & 0x0F) << 2) | ((raw[inp+2] & 0xC0) >> 6)];
        outbuf[outp++] = base64EncodingTable[raw[inp+2] & 0x3F];
    }

    if ( do_now < lentext )
    {
        char tmpbuf[2] = {0,0};
        int left = lentext%3;
        for ( int i=0; i < left; i++ )
        {
            tmpbuf[i] = raw[do_now+i];
        }
        raw = tmpbuf;
        outbuf[outp++] = base64EncodingTable[(raw[inp] & 0xFC) >> 2];
        outbuf[outp++] = base64EncodingTable[((raw[inp] & 0x03) << 4) | ((raw[inp+1] & 0xF0) >> 4)];
        if ( left == 2 ) outbuf[outp++] = base64EncodingTable[((raw[inp+1] & 0x0F) << 2) | ((raw[inp+2] & 0xC0) >> 6)];
    }

    int padLen = ((lentext + 2) - ((lentext + 2) % 3)) / 3 * 4;
    int needPad = padLen - outp;
    if (1 == needPad)  {
        outbuf[outp++] = '=';
    } else if (2 == needPad)  {
        outbuf[outp++] = '=';
        outbuf[outp++] = '=';
    }

    NSString *ret = [[[NSString alloc] initWithBytes:outbuf length:outp encoding:NSASCIIStringEncoding] autorelease];
    free(outbuf);

    return ret;
}

- (void)multipartPost: (NSDictionary *)parts toURL: (NSString *)url  {
    NSString *filename = @"changethisvalue.tmp";
    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] init] autorelease];
    [request setURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:@"POST"];
    CFUUIDRef uuid = CFUUIDCreate(NULL);
    CFStringRef uuidCFString = CFUUIDCreateString(NULL, uuid);
    NSString *uuidString = [(NSString *) uuidCFString autorelease];
    CFRelease(uuid);
    NSString *boundary = [NSString stringWithFormat:@"----%@----",uuidString];
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
    [request addValue:contentType forHTTPHeaderField: @"Content-Type"];
    [request addValue:@"partial/ajax" forHTTPHeaderField:@"Faces-Request"];
    NSMutableData *postbody = [NSMutableData data];
    for (NSString *key in parts)  {
        NSLog(@"multipartPost part %@ equals %@", key, [parts objectForKey:key] );
        NSString *value = [parts objectForKey:key];
        NSString *mimeTypeHeader = [self guessMimeType:value];
        if ([key hasSuffix:@"-file"] ) {
            NSLog(@"    writing file part for %@ %@", key, filename );
            [postbody appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            [postbody appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"; filename=\"%@\"\r\n", key, filename] dataUsingEncoding:NSUTF8StringEncoding]];
            [postbody appendData:[[NSString stringWithString:mimeTypeHeader] dataUsingEncoding:NSUTF8StringEncoding]];
            [postbody appendData:[NSData dataWithContentsOfFile:value]];
        } else  {
            //normal form field
            //Content-Disposition: form-data; name="text1"
            NSLog(@"    writing normal part for %@", key );
            [postbody appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            [postbody appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", key] dataUsingEncoding:NSUTF8StringEncoding]];
            [postbody appendData:[value dataUsingEncoding:NSUTF8StringEncoding]];
        }
    }
    [postbody appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];

    [request setHTTPBody:postbody];

    NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:request delegate:self];
    if (theConnection) {
        receivedData = [[NSMutableData data] retain];
    } else {
        NSLog(@"unable to connect to %@", theConnection);
    }

    return;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response  {
    [receivedData setLength:0];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data  {
    [receivedData appendData:data];
}

- (void)connection:(NSURLConnection *)connection didSendBodyData:(NSInteger)bytesWritten totalBytesWritten:(NSInteger)totalBytesWritten totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite  {
    NSLog(@"didSendBodyData %d bytes of %d",totalBytesWritten, totalBytesExpectedToWrite);
    NSInteger percentProgress = (totalBytesWritten * 100) / totalBytesExpectedToWrite;
    NSString *scriptTemplate = @"ice.progress(%d);";
    NSString *script = [NSString stringWithFormat:scriptTemplate, percentProgress];
    NSString *result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection  {
    NSLog(@"connectionDidFinishLoading %d bytes of data",[receivedData length]);

    NSString *responseString = [[NSString alloc] initWithData:receivedData
            encoding:NSUTF8StringEncoding];

    [controller.webView 
            stringByEvaluatingJavaScriptFromString: @"ice.progress(100);"];

    NSString *scriptTemplate = @"ice.handleResponse(\"%@\");";
    NSString *script = [NSString stringWithFormat:scriptTemplate, [responseString 
            stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSString *result = [controller.webView 
            stringByEvaluatingJavaScriptFromString: script];

    // release the connection, and the data object
    [connection release];
    [receivedData release];
    self.uploading = NO;
}

@end
