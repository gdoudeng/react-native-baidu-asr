import { NativeModules } from "react-native";

const BaiduAsrConstantModule = NativeModules.BaiduAsrConstantModule;

export interface InitOptions {
  APP_ID: string
  APP_KEY: string
  SECRET: string
}

/**
 * ASR_START 输入事件参数
 * {@see https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#asr_start-%E8%BE%93%E5%85%A5%E4%BA%8B%E4%BB%B6%E5%8F%82%E6%95%B0}
 * 无需再输入鉴权信息
 */
export interface AsrOptions {
  // 根据识别语种，输入法模型及是否需要在线语义，来选择PID。默认1537，即中文输入法模型，不带在线语义。PID具体值及说明见下一个表格。 其中输入法模型是指适用于长句的输入法模型模型适用于短语。
  PID?: number
  // 自训练平台上线后的模型Id，必须和自训练平台的PID连用。
  LM_ID?: number
  // 离在线的并行策略
  DECODER?: number
  // 语音活动检测， 根据静音时长自动断句。注意不开启长语音的情况下，SDK只能录制60s音频。长语音请设置BDS_ASR_ENABLE_LONG_SPEECH参数
  VAD?: string
  // 是否开启长语音。 即无静音超时断句，开启后需手动调用ASR_STOP停止录音。 请勿和VAD=touch联用！优先级大于VAD_ENDPOINT_TIMEOUT 设置
  BDS_ASR_ENABLE_LONG_SPEECH?: boolean
  // 静音超时断句及长语音
  VAD_ENDPOINT_TIMEOUT?: number
  // 自定义输入入口 不单纯是使用麦克风采集音频 可以输入本地文件或者音频流都都行 只有使用了这个参数百度语音才不会占用麦克风
  IN_FILE?: string
  // 保存识别过程产生的录音文件, 该参数需要开启ACCEPT_AUDIO_DATA后生效
  OUT_FILE?: string
  // 录音开始的时间点。用于唤醒+识别连续说。SDK有15s的录音缓存。如设置为(System.currentTimeMillis() - 1500),表示回溯1.5s的音频。
  AUDIO_MILLS?: number
  // 本地语义解析设置。必须设置ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH参数生效，无论网络状况，都可以有本地语义结果。并且本地语义结果会覆盖在线语义结果。本参数不控制在线语义输出，需要在线语义输出见PID参数
  NLU?: string
  // 用于支持本地语义解析的bsg文件，离线和在线都可使用。NLU开启生效，其它说明见NLU参数。注意bsg文件同时也用于ASR_KWS_LOAD_ENGINE离线命令词功能。
  ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH?: string
  SLOT_DATA?: string
  // 在选择1537开头的pid（输入法模式）的时候，是否禁用标点符号
  DISABLE_PUNCTUATION?: boolean
  // 在选择1537开头的pid（输入法模式）的时候，标点处理模式。需要设置DISABLE_PUNCTUATION为fasle生效
  PUNCTUATION_MODE?: number
  // 是否需要语音音频数据回调，开启后有CALLBACK_EVENT_ASR_AUDIO事件
  ACCEPT_AUDIO_DATA?: boolean
  // 是否需要语音音量数据回调，开启后有CALLBACK_EVENT_ASR_VOLUME事件回调
  ACCEPT_AUDIO_VOLUME?: boolean
  SOUND_START?: number
  SOUND_END?: number
  SOUND_SUCCESS?: number
  SOUND_ERROR?: number
  SOUND_CANCEL?: number
  SAMPLE_RATE?: number
  ASR_OFFLINE_ENGINE_LICENSE_FILE_PATH?: string
}

export enum EventName {
  // 识别错误回调
  onRecognizerError = "onRecognizerError",
  // 识别结果回调
  onRecognizerResult = "onRecognizerResult",
  // 音量变化回调
  onAsrVolume = "onAsrVolume"
}

export interface RecognizerData<T = any> {
  /**
   * 状态码
   */
  code: RecognizerStatusCode,
  /**
   * 消息
   */
  msg: string,
  /**
   * 数据
   */
  data: T
}

export enum RecognizerStatusCode {
  STATUS_NONE = BaiduAsrConstantModule.STATUS_NONE,
  STATUS_READY = BaiduAsrConstantModule.STATUS_READY,
  STATUS_SPEAKING = BaiduAsrConstantModule.STATUS_SPEAKING,
  STATUS_RECOGNITION = BaiduAsrConstantModule.STATUS_RECOGNITION,
  STATUS_FINISHED = BaiduAsrConstantModule.STATUS_FINISHED,
  STATUS_LONG_SPEECH_FINISHED = BaiduAsrConstantModule.STATUS_LONG_SPEECH_FINISHED,
  STATUS_ERROR = BaiduAsrConstantModule.STATUS_ERROR,
  STATUS_STOPPED = BaiduAsrConstantModule.STATUS_STOPPED,
  STATUS_WAKEUP_SUCCESS = BaiduAsrConstantModule.STATUS_WAKEUP_SUCCESS,
  STATUS_WAKEUP_EXIT = BaiduAsrConstantModule.STATUS_WAKEUP_EXIT,
  STATUS_WAITING_READY = BaiduAsrConstantModule.STATUS_WAITING_READY,
  WHAT_MESSAGE_STATUS = BaiduAsrConstantModule.WHAT_MESSAGE_STATUS
}

// 只有临时识别结果和结束识别会返回下面的数据
export interface RecognizerResultData {
  best_result: string,
  // 如无意外 取第一个值就是识别结果
  results_recognition: Array<string>,
  result_type: ResultType,
  origin_result: {
    corpus_no: number,
    err_no: number,
    raf: number,
    result: {
      word: Array<string>
    },
    sn: string
  },
  error: number,
  desc: string
}

export interface RecognizerResultError {
  errorCode: number,
  subErrorCode: number,
  descMessage: number
}

enum ResultType {
  // 识别结束最终的结果
  final_result = "final_result",
  // 临时识别结果
  partial_result = "partial_result",
  // 下面不知道啥
  nlu_result = "nlu_result"
}

export interface VolumeData {
  // 音量百分比
  volumePercent: number
  // 音量
  volume: number
}
