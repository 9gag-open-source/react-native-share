//
//  NSString+Encoded.m
//  RNShare
//
//  Created by Lung on 20/1/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "NSString+Encoded.h"

@implementation NSString (Encoded)

- (NSString *)urlencode {
    return (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                                                                                 NULL,
                                                                                 (CFStringRef)self,
                                                                                 NULL,
                                                                                 (CFStringRef)@"!*'();:@&=+$,/?%#[]",
                                                                                 kCFStringEncodingUTF8 ));
}

@end
