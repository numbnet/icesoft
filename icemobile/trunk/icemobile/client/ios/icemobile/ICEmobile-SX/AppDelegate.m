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

#import "AppDelegate.h"

#import "ViewController.h"
#import "NativeInterface.h"
#import "Logging.h"

@implementation AppDelegate

@synthesize window = _window;
@synthesize viewController = _viewController;

- (void)dealloc
{
    [_window release];
    [_viewController release];
    [super dealloc];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]] autorelease];
    // Override point for customization after application launch.
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        self.viewController = [[[ViewController alloc] initWithNibName:@"ViewController_iPhone" bundle:nil] autorelease];
    } else {
        self.viewController = [[[ViewController alloc] initWithNibName:@"ViewController_iPad" bundle:nil] autorelease];
    }
    self.window.rootViewController = self.viewController;
    [self.window makeKeyAndVisible];
    if (nil != [launchOptions objectForKey: UIApplicationLaunchOptionsRemoteNotificationKey])  {
        LogInfo(@"ICEmobile-SX launched via notification %@", [launchOptions objectForKey: UIApplicationLaunchOptionsRemoteNotificationKey]);
    }
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
    [self.viewController applicationWillResignActive];
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     */
    [self.viewController applicationDidEnterBackground];
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    /*
     Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
     */
    [self.viewController applicationWillEnterForeground];
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
    [self.viewController applicationDidBecomeActive];
    [application registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert];
    [self redirectConsoleLog];
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    /*
     Called when the application is about to terminate.
     Save data if appropriate.
     See also applicationDidEnterBackground:.
     */
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation  {
    [self.viewController hideControls];
    self.viewController.launchedFromApp = NO;
    NSLog(@"handleOpenURL %@ %@", sourceApplication, url);
    NSString *reqString = [url absoluteString];
    NSString *body = [reqString substringFromIndex:[@"icemobile:" length]];
    if ([body hasPrefix:@"//"])  {
        body = [body substringFromIndex:[@"//" length]];
    }
    NSDictionary *params = 
            [self.viewController.nativeInterface parseQuery:body];

    LogInfo(@"found command %@", [params objectForKey:@"c"]);
    LogInfo(@"found url %@", [params objectForKey:@"u"]);
    //if the URL to POST to and the URL to reload in Safari are different,
    //specify u to POST and r to return
    self.viewController.currentURL = [params objectForKey:@"u"];
    self.viewController.returnURL = [params objectForKey:@"r"];
    self.viewController.currentParameters = [params objectForKey:@"p"];
    self.viewController.currentCommand = [params objectForKey:@"c"];
    self.viewController.splashParameters = [params objectForKey:@"s"];
    self.viewController.returnHash = [params objectForKey:@"h"];
    self.viewController.currentSessionId = [params objectForKey:@"JSESSIONID"];
    LogDebug(@"found JSESSIONID %@", [params objectForKey:@"JSESSIONID"]);

    NSDictionary *splashParts = 
       [self.viewController.nativeInterface parseQuery:self.viewController.splashParameters];
    NSString *splashImageURL = [splashParts objectForKey:@"i"];
    if (nil != splashImageURL)  {
NSLog(@"splash image URL %@", splashImageURL);
        NSURL *imageURL = [NSURL URLWithString:splashImageURL];
        NSData *imageData = [NSData dataWithContentsOfURL:imageURL];
        UIImage *image = [UIImage imageWithData:imageData];
        [self.viewController.splashImage setImage:image];
    }

    [self.viewController dismissModalViewControllerAnimated:NO];
    [self.viewController dispatchCurrentCommand];

    return YES;
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    LogDebug(@"didRegisterForRemoteNotificationsWithDeviceToken %@", deviceToken);
    self.viewController.deviceToken = deviceToken;

}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error  {
    LogDebug(@"didFailToRegisterForRemoteNotificationsWithError %@", error);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo  {
    LogInfo(@"didReceiveRemoteNotification %@", userInfo);
    [self.viewController reloadCurrentURL];
}

- (void) redirectConsoleLog {
  NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString *documentsDirectory = [paths objectAtIndex:0];
  NSString *logPath = [documentsDirectory stringByAppendingPathComponent:@"console.log"];
  freopen([logPath cStringUsingEncoding:NSASCIIStringEncoding],"w",stderr);
}

@end
