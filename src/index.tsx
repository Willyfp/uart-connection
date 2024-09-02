import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-uart-connection' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const UartConnection = NativeModules.UartConnection
  ? NativeModules.UartConnection
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return UartConnection.multiply(a, b);
}

export function list(): Promise<string[]> {
  return UartConnection.listDevices();
}

export function init(config: Object): Promise<void> {
  return UartConnection.init(config);
}

export function send(data: string): Promise<void> {
  return UartConnection.send(data);
}
