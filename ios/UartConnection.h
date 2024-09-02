
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNUartConnectionSpec.h"

@interface UartConnection : NSObject <NativeUartConnectionSpec>
#else
#import <React/RCTBridgeModule.h>

@interface UartConnection : NSObject <RCTBridgeModule>
#endif

@end
