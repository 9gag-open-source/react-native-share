//#import <MessageUI/MessageUI.h>
#import "RNShare.h"

#if __has_include(<React/RCTConvert.h>)
#import <React/RCTConvert.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <React/RCTLog.h>
#import <React/RCTUtils.h>
#else
#import "RCTConvert.h"
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTLog.h"
#import "RCTUtils.h"
#endif

#import "GenericShare.h"
#import "WhatsAppShare.h"
#import "GooglePlusShare.h"
#import "EmailShare.h"
#import "SMSShare.h"
#import "NGCopyLinkActivity.h"
#import "NGSafariActivity.h"

NSString * const NGCopyLinkActivityType = @"com.9gag.RNShare.activity.copylink";
NSString * const NGOpenInSafariActivityType = @"com.9gag.RNShare.activity.openInSafari";

#pragma mark Private

static NSString *RCTKeyForInstance(id instance)
{
    return [NSString stringWithFormat:@"%p", instance];
}

@implementation RNShare
{
    NSMutableDictionary *_callbacks;
}

- (instancetype)init
{
    if ((self = [super init])) {
        _callbacks = [[NSMutableDictionary alloc] init];
    }
    return self;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

/*
 * The `anchor` option takes a view to set as the anchor for the share
 * popup to point to, on iPads running iOS 8. If it is not passed, it
 * defaults to centering the share popup on screen without any arrows.
 */
- (CGRect)sourceRectInView:(UIView *)sourceView
             anchorViewTag:(NSNumber *)anchorViewTag
{
    if (anchorViewTag) {
        UIView *anchorView = [self.bridge.uiManager viewForReactTag:anchorViewTag];
        return [anchorView convertRect:anchorView.bounds toView:sourceView];
    } else {
        return (CGRect){sourceView.center, {1, 1}};
    }
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(shareSingle:(NSDictionary *)options
                  failureCallback:(RCTResponseErrorBlock)failureCallback
                  successCallback:(RCTResponseSenderBlock)successCallback)
{
    NSString *social = [RCTConvert NSString:options[@"social"]];
    if (social) {
        NSLog(social);
        if([social isEqualToString:@"whatsapp"]) {
            WhatsAppShare *shareCtl = [[WhatsAppShare alloc] init];
            [shareCtl shareSingle:options failureCallback:failureCallback successCallback:successCallback];
        } else if([social isEqualToString:@"facebook"]) {
            GenericShare *shareCtl = [[GenericShare alloc] init];
            [shareCtl shareSingle:options failureCallback:failureCallback successCallback:successCallback serviceType:SLServiceTypeFacebook];
        } else if([social isEqualToString:@"sms"]) {
            SMSShare *shareCtl = [[SMSShare alloc] init];
            [shareCtl shareSingle:options
               fromViewController:[[[[UIApplication sharedApplication] delegate] window] rootViewController]
                 composerDelegate:self
                  failureCallback:failureCallback];
            if (successCallback){
                _callbacks[RCTKeyForInstance(@"sms")] = successCallback;
            }
        } else if([social isEqualToString:@"email"]) {
            EmailShare *shareCtl = [[EmailShare alloc] init];
            [shareCtl shareSingle:options
               fromViewController:[[[[UIApplication sharedApplication] delegate] window] rootViewController]
                 composerDelegate:self
                  failureCallback:failureCallback];
            if (successCallback){
                _callbacks[RCTKeyForInstance(@"email")] = successCallback;
            }
        } else if([social isEqualToString:@"twitter"]) {
            GenericShare *shareCtl = [[GenericShare alloc] init];
            [shareCtl shareSingle:options failureCallback:failureCallback successCallback:successCallback serviceType:SLServiceTypeTwitter];
        } else if([social isEqualToString:@"googleplus"]) {
            GooglePlusShare *shareCtl = [[GooglePlusShare alloc] init];
            [shareCtl shareSingle:options failureCallback:failureCallback successCallback:successCallback];
        }
    } else {
        RCTLogError(@"No exists social key");
        return;
    }
}

RCT_EXPORT_METHOD(open:(NSDictionary *)options
                  failureCallback:(RCTResponseErrorBlock)failureCallback
                  successCallback:(RCTResponseSenderBlock)successCallback)
{
    
//{
//    excludedActivityTypes = (
//         "com.apple.UIKit.activity.AssignToContact",
//         "com.apple.UIKit.activity.Print",
//         "com.apple.UIKit.activity.AddToReadingList",
//         "com.apple.UIKit.activity.SaveToCameraRoll",
//         "com.apple.UIKit.activity.CopyToPasteboard"
//     );
//    includedActivityTypes = (
//     {
//         name = "Copy Link";
//         type = "com.9gag.RNShare.activity.copylink";
//     },
//     {
//         name = "Open In Safari";
//         type = "com.9gag.RNShare.activity.openInSafari";
//     }
//     );
//    message = "Hola mundo";
//    subject = "Share Link";
//    title = "React Native";
//    url = "http://facebook.github.io/react-native/";
//}
    
    NSMutableArray<id> *items = [NSMutableArray array];
    
    NSString *message = [RCTConvert NSString:options[@"message"]];
    if (message) {
        [items addObject:message];
    }
    
    NSURL *url = [RCTConvert NSURL:options[@"url"]];
    if (url) {
        if ([url.scheme.lowercaseString isEqualToString:@"data"]) {
            NSError *error;
            NSData *data = [NSData dataWithContentsOfURL:url
                                                 options:(NSDataReadingOptions)0
                                                   error:&error];
            if (!data) {
                failureCallback(error);
                return;
            }
            [items addObject:data];
        } else {
            [items addObject:url];
        }
    }
    if (items.count == 0) {
        RCTLogError(@"No `url` or `message` to share");
        return;
    }
    
    NSMutableArray<id> *activities = [NSMutableArray array];
    NSArray *includedActivityTypes = [RCTConvert NSDictionaryArray:options[@"includedActivityTypes"]];
    if (includedActivityTypes) {
        for (NSDictionary *includedActvities in includedActivityTypes) {
            NSString *type = [includedActvities objectForKey:@"type"];
            NSString *title = [includedActvities objectForKey:@"title"];
            if (title) {
                if ([type isEqualToString:NGCopyLinkActivityType]) {
                    NGCopyLinkActivity *copyLinkActivity = [[NGCopyLinkActivity alloc] init];
                    copyLinkActivity.title = title;
                    copyLinkActivity.image = [UIImage imageNamed:@"libRNShareResources.bundle/activity-copy"];
                    [activities addObject:copyLinkActivity];
                } else if ([type isEqualToString:NGOpenInSafariActivityType]) {
                    NGSafariActivity *safariActivity = [[NGSafariActivity alloc] init];
                    safariActivity.title = title;
                    safariActivity.image = [UIImage imageNamed:@"libRNShareResources.bundle/activity-safari"];
                    [activities addObject:safariActivity];
                }
            }
        }
    }
    
    UIActivityViewController *shareController = [[UIActivityViewController alloc] initWithActivityItems:items applicationActivities:activities];
    
    NSString *subject = [RCTConvert NSString:options[@"subject"]];
    if (subject) {
        [shareController setValue:subject forKey:@"subject"];
    }
    
    NSArray *excludedActivityTypes = [RCTConvert NSStringArray:options[@"excludedActivityTypes"]];
    if (excludedActivityTypes) {
        shareController.excludedActivityTypes = excludedActivityTypes;
    }
    
    UIViewController *controller = RCTPresentedViewController();
    shareController.completionWithItemsHandler = ^(NSString *activityType, BOOL completed, __unused NSArray *returnedItems, NSError *activityError) {
        if (activityError) {
            failureCallback(activityError);
        } else {
            successCallback(@[@(completed), RCTNullIfNil(activityType)]);
        }
    };
    
    shareController.modalPresentationStyle = UIModalPresentationPopover;
    NSNumber *anchorViewTag = [RCTConvert NSNumber:options[@"anchor"]];
    if (!anchorViewTag) {
        shareController.popoverPresentationController.permittedArrowDirections = 0;
    }
    shareController.popoverPresentationController.sourceView = controller.view;
    shareController.popoverPresentationController.sourceRect = [self sourceRectInView:controller.view anchorViewTag:anchorViewTag];
    
    [controller presentViewController:shareController animated:YES completion:nil];
    
    shareController.view.tintColor = [RCTConvert UIColor:options[@"tintColor"]];
}

#pragma mark - MFMailComposeViewControllerDelegate

- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error {
    
    NSString *key = @"email";
    
    switch (result) {
        case MFMailComposeResultCancelled:
            break;
        case MFMailComposeResultSaved:
            break;
        case MFMailComposeResultSent:
        {
            RCTResponseSenderBlock callback = _callbacks[RCTKeyForInstance(key)];
            if (callback) {
                callback(@[key]);
                [_callbacks removeObjectForKey:key];
            }
        }
            break;
        case MFMailComposeResultFailed:
            break;
        default:
            break;
    }
    
    UIViewController *ctrl = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    [ctrl dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - MFMessageComposeViewControllerDelegate

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result {
    
    NSString *key = @"sms";
    
    switch (result) {
        case MessageComposeResultCancelled:
            break;
        case MessageComposeResultFailed:
            break;
        case MessageComposeResultSent:
        {
            RCTResponseSenderBlock callback = _callbacks[RCTKeyForInstance(key)];
            if (callback) {
                callback(@[key]);
                [_callbacks removeObjectForKey:key];
            }
        }
            break;
        default:
            break;
    }
    UIViewController *ctrl = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    [ctrl dismissViewControllerAnimated:YES completion:nil];
}

@end
