import { EmitterSubscription, NativeEventEmitter, NativeModules } from 'react-native';
import { EventName, ITtsOptions, SynthesizerData, SynthesizerResultData, SynthesizerResultError } from './types';

const BaiduSynthesizerModule = NativeModules.BaiduSynthesizerModule;
const eventEmitter = new NativeEventEmitter(BaiduSynthesizerModule);

/**
 * 提供百度语音合成 React Native 接口
 */
export default class BaiduSynthesizer {
  /**
   * 初始化百度语音引擎
   * @param options 鉴权信息
   */
  static initialTts(options?: ITtsOptions) {
    BaiduSynthesizerModule.initialTts(options);
  }

  /**+
   * 合成并播放
   * @param text 要进行语音合成的字符串
   * @param options 输入事件参数 详细参数解析看 {@see https://ai.baidu.com/ai-doc/SPEECH/Pk8446an5#%E5%90%88%E6%88%90%E5%8F%82%E6%95%B0}
   * @param callback status=0表示成功
   */
  static speak(text: string, options?: ITtsOptions, callback?: (status: number) => void, utteranceId?: string) {
    // BaiduSynthesizerModule.speak(text, options, callback, utteranceId);
    BaiduSynthesizerModule.speak(text, utteranceId, options, callback);
    // BaiduSynthesizerModule.speak(text, options, callback);
  }

  /**
   * 批量播放
   *
   * @param textArray 要进行语音合成的字符串数组 eg.['你好', '这里是家里蹲大学吗', '怎么样啊', '你来吗']
   * @param options 输入事件参数 详细参数解析看 {@see https://ai.baidu.com/ai-doc/SPEECH/Pk8446an5#%E5%90%88%E6%88%90%E5%8F%82%E6%95%B0}
   * @param callback status=0表示成功
   */
  static batchSpeak(textArray: string[], options?: ITtsOptions, callback?: (status: number) => void) {
    BaiduSynthesizerModule.batchSpeak(textArray, options, callback);
  }

  /**
   * 暂停播放。仅调用speak后生效
   *
   * @param callback status=0表示成功
   */
  static pause(callback?: (status: number) => void) {
    BaiduSynthesizerModule.pause(callback);
  }

  /**
   * 继续播放。仅调用speak后生效，调用pause生效
   *
   * @param callback status=0表示成功
   */
  static resume(callback?: (status: number) => void) {
    BaiduSynthesizerModule.resume(callback);
  }

  /**
   * 停止合成引擎。即停止播放，合成，清空内部合成队列。
   *
   * @param callback status=0表示成功
   */
  static stop(callback?: (status: number) => void) {
    BaiduSynthesizerModule.stop(callback);
  }

  /**
   * 释放资源
   * 下次需要再次使用的话必须再调用{@link initialTts}方法初始化引擎
   */
  static release() {
    BaiduSynthesizerModule.release();
  }

  /**
   * 状态回调
   * @param eventName
   * @param cb
   * @private
   */
  private static addListener(eventName: EventName, cb: (data: SynthesizerData) => void): EmitterSubscription {
    return eventEmitter.addListener(eventName, (data: SynthesizerData<string | undefined>) => {
      // java传过来的是字符串
      if (data.code && typeof data.data === 'string' && data.data.startsWith('{')) {
        try {
          data.data = JSON.parse(data.data);
        } catch (e) {
          console.log('BaiduSynthesizer.addListener JSON.parse error', e, data.data);
        }
      }
      cb(data);
    });
  }

  /**
   * 合成结果回调
   * @param callback
   */
  static addResultListener(
    callback: (data: SynthesizerData<SynthesizerResultData | string | undefined>) => void
  ): EmitterSubscription {
    return this.addListener(EventName.onSynthesizerResult, callback);
  }

  /**
   * 合成错误回调
   * @param callback
   */
  static addErrorListener(callback: (data: SynthesizerData<SynthesizerResultError>) => void): EmitterSubscription {
    return this.addListener(EventName.onSynthesizerError, callback);
  }
}
