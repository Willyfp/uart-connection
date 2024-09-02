import { useEffect } from 'react';
import {
  StyleSheet,
  View,
  NativeEventEmitter,
  NativeModules,
  Button,
} from 'react-native';
import { init, list, send } from 'react-native-uart-connection';

export default function App() {
  const event = new NativeEventEmitter(NativeModules.UartConnection);

  event.addListener('onDataReceived', (data) => {
    console.log(data);
  });

  useEffect(() => {
    list().then(console.log);

    init({ sPort: 'ttyS0', baudRate: '115200' })
      .then(console.log)
      .catch(console.error);
  }, []);

  return (
    <View style={styles.container}>
      <Button
        onPress={() => send('Hello World').then(console.log)}
        title="Enviar"
      ></Button>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
