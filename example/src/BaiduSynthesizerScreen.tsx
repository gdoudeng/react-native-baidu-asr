import React, {Component} from 'react';
import {
  Button,
  StyleSheet,
  Text,
  TextInput,
  ToastAndroid,
  View,
} from 'react-native';
import {
  BaiduSynthesizer,
  SynthesizerData,
  SynthesizerResultData,
  SynthesizerResultError,
} from 'react-native-baidu-asr';

class BaiduSynthesizerScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: '百度语音合成适用于泛阅读、订单播报、智能硬件等应用场景，让您的应用、设备开口说话，更具个性。',
      status: '☆语音合成☆',
    };
  }

  componentDidMount() {
    BaiduSynthesizer.initialTts();
    this.resultListener = BaiduSynthesizer.addResultListener(
      this.onSynthesizerResult,
    );
    this.errorListener = BaiduSynthesizer.addErrorListener(
      this.onSynthesizerError,
    );
  }

  componentWillUnmount() {
    this.resultListener?.remove();
    this.errorListener?.remove();
    // fixme: 如果你调用了release方法 等下重新reload页面的时候 会报: "MySynthesizer 对象里面 SpeechSynthesizer还未释放，请勿新建一个新对象。" 的错误
    // 但是我确保是已经调用的了 现在我认为是合成器release不是立即的 在毫秒级别内重新new一个会报错 所以在测试的时候可以先不释放 上线后在加上
    if (!__DEV__) {
      BaiduSynthesizer.release();
    }
  }

  onSynthesizerResult = (
    data: SynthesizerData<SynthesizerResultData | string | undefined>,
  ) => {
    console.log(data);
    this.setState({status: data.msg});
  };

  onSynthesizerError = (data: SynthesizerData<SynthesizerResultError>) => {
    this.setState({status: data.msg});
    ToastAndroid.show(
      `${data.msg}，错误码: 【${data.data.code}】，${data.data.description}`,
      ToastAndroid.LONG,
    );
    console.log('onSynthesizerError ', JSON.stringify(data));
  };

  speak = () => {
    BaiduSynthesizer.speak(
      this.state.text,
      {
        PARAM_SPEAKER: '1',
      },
      status => {
        console.log('speak --> ', status);
      },
    );
  };

  batchSpeak = () => {
    BaiduSynthesizer.batchSpeak(
      [
        '开始批量播放',
        '123456',
        '欢迎使用百度语音',
        '重(chong2)量这个是多音字示例',
      ],
      {
        PARAM_SPEAKER: '1',
      },
      status => {
        console.log('batchSpeak --> ', status);
      },
    );
  };

  handleTextChange = (text: string) => {
    this.setState({text});
  };

  pause = () => {
    BaiduSynthesizer.pause(status => {
      console.log('pause --> ', status);
    });
  };

  resume = () => {
    BaiduSynthesizer.resume(status => {
      console.log('resume --> ', status);
    });
  };

  stop = () => {
    BaiduSynthesizer.stop(status => {
      console.log('stop --> ', status);
    });
  };

  render() {
    const {text, status} = this.state;
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>{status}</Text>

        <View style={styles.actionView}>
          <View style={styles.itemView}>
            <Button title={'合成并播放'} onPress={this.speak} />
          </View>
          <View style={styles.itemView}>
            <Button title={'批量合成并播放'} onPress={this.batchSpeak} />
          </View>
          <View style={styles.itemView}>
            <Button title={'播放暂停'} onPress={this.pause} />
          </View>
          <View style={styles.itemView}>
            <Button title={'播放恢复'} onPress={this.resume} />
          </View>
          <View style={styles.itemView}>
            <Button title={'停止合成引擎'} onPress={this.stop} />
          </View>
        </View>

        <TextInput
          style={styles.textInput}
          value={text}
          multiline
          onChangeText={this.handleTextChange}
        />
      </View>
    );
  }
}

export default BaiduSynthesizerScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 16,
    textAlign: 'center',
    margin: 10,
  },
  actionView: {
    marginTop: 10,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    flexWrap: 'wrap',
  },
  itemView: {
    marginHorizontal: 5,
    marginBottom: 5,
  },
  textInput: {
    fontSize: 16,
    marginHorizontal: 5,
    marginTop: 10,
    borderBottomWidth: 1,
    borderColor: '#afafaf',
  },
});
