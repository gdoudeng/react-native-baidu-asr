// BaiduAsrModule.java

package cn.beemango.baidu.asr;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.listener.IRecogListener;
import com.baidu.aip.asrwakeup3.core.recog.listener.MessageStatusRecogListener;
import com.baidu.speech.asr.SpeechConstant;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_ASR_VOLUME;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_ERROR;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_FINISHED;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_LONG_SPEECH_FINISHED;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_NONE;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_READY;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_RECOGNITION;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_SPEAKING;

/**
 * 提供百度语音识别 React Native 接口
 */
public class BaiduAsrModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "BaiduAsrModule";

    // 识别控制器，使用MyRecognizer控制识别的流程
    protected MyRecognizer myRecognizer;
    private final ReactApplicationContext mReactApplicationContext;
    private boolean isListening = false;
    private DeviceEventManagerModule.RCTDeviceEventEmitter mEventEmitter;

    // 百度语音鉴权的信息
    private String APP_ID;
    private String APP_KEY;
    private String SECRET;

    public BaiduAsrModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactApplicationContext = reactContext;
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return TAG;
    }

    /**
     * 初始化百度语音识别 传入鉴权信息
     *
     * @param options 鉴权信息
     */
    @ReactMethod
    public void init(final ReadableMap options) {
        if (myRecognizer != null || isListening || options == null) {
            return;
        }
        // 基于DEMO集成第1.1, 1.2, 1.3 步骤 初始化EventManager类并注册自定义输出事件
        if (options.hasKey("APP_ID")) {
            APP_ID = options.getString("APP_ID");
        } else {
            throw new RuntimeException("缺少鉴权信息APP_ID");
        }
        if (options.hasKey("APP_KEY")) {
            APP_KEY = options.getString("APP_KEY");
        } else {
            throw new RuntimeException("缺少鉴权信息APP_KEY");
        }
        if (options.hasKey("SECRET")) {
            SECRET = options.getString("SECRET");
        } else {
            throw new RuntimeException("缺少鉴权信息SECRET");
        }

        // DEMO集成步骤 1.2 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        IRecogListener listener = new MessageStatusRecogListener(new ListenHandler(this));
        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        myRecognizer = new MyRecognizer(mReactApplicationContext, listener);

        // BluetoothUtil.start(this,BluetoothUtil.FULL_MODE); // 蓝牙耳机开始，注意一部分手机这段代码无效
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     * 基于DEMO集成2.1, 2.2 设置识别参数并发送开始事件
     * <p>
     * https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#asr_start-%E8%BE%93%E5%85%A5%E4%BA%8B%E4%BB%B6%E5%8F%82%E6%95%B0
     *
     * @param options 识别输入参数
     */
    @ReactMethod
    public void start(final ReadableMap options) {
        if (isListening) return;
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = new HashMap<>();
        params.put(SpeechConstant.APP_ID, APP_ID);
        params.put(SpeechConstant.APP_KEY, APP_KEY);
        params.put(SpeechConstant.SECRET, SECRET);
        // 下面根据可能传进来的自定义参数put进去
        // 根据识别语种，输入法模型及是否需要在线语义，来选择PID。默认1537，即中文输入法模型，不带在线语义。PID具体值及说明见下一个表格。 其中输入法模型是指适用于长句的输入法模型模型适用于短语。
        if (options.hasKey("PID")) {
            params.put(SpeechConstant.PID, options.getInt("PID"));
        }
        // 自训练平台上线后的模型Id，必须和自训练平台的PID连用。
        if (options.hasKey("LM_ID")) {
            params.put(SpeechConstant.LMID, options.getInt("LM_ID"));
        }
        // 离在线的并行策略
        if (options.hasKey("DECODER")) {
            params.put(SpeechConstant.DECODER, options.getInt("DECODER"));
        }
        // 语音活动检测， 根据静音时长自动断句。注意不开启长语音的情况下，SDK只能录制60s音频。长语音请设置BDS_ASR_ENABLE_LONG_SPEECH参数
        if (options.hasKey("VAD")) {
            params.put(SpeechConstant.VAD, options.getString("VAD"));
        }
        // 是否开启长语音。 即无静音超时断句，开启后需手动调用ASR_STOP停止录音。 请勿和VAD=touch联用！优先级大于VAD_ENDPOINT_TIMEOUT 设置
        if (options.hasKey("BDS_ASR_ENABLE_LONG_SPEECH")) {
            params.put(SpeechConstant.BDS_ASR_ENABLE_LONG_SPEECH, options.getBoolean("BDS_ASR_ENABLE_LONG_SPEECH"));
        }
        // 静音超时断句及长语音 0是长语音
        if (options.hasKey("VAD_ENDPOINT_TIMEOUT")) {
            params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, options.getInt("VAD_ENDPOINT_TIMEOUT"));
        }
        // 自定义输入入口 不单纯是使用麦克风采集音频 可以输入本地文件或者音频流都都行 只有使用了这个参数百度语音才不会占用麦克风
        if (options.hasKey("IN_FILE")) {
            params.put(SpeechConstant.IN_FILE, options.getString("IN_FILE"));
        }
        // 保存识别过程产生的录音文件, 该参数需要开启ACCEPT_AUDIO_DATA后生效
        if (options.hasKey("OUT_FILE")) {
            params.put(SpeechConstant.OUT_FILE, options.getString("OUT_FILE"));
        }
        if (options.hasKey("AUDIO_MILLS")) {
            params.put(SpeechConstant.AUDIO_MILLS, options.getInt("AUDIO_MILLS"));
        }
        if (options.hasKey("NLU")) {
            params.put(SpeechConstant.NLU, options.getString("NLU"));
        }
        if (options.hasKey("ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH")) {
            params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, options.getString("ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH"));
        }
        if (options.hasKey("SLOT_DATA")) {
            params.put(SpeechConstant.SLOT_DATA, options.getString("SLOT_DATA"));
        }
        if (options.hasKey("DISABLE_PUNCTUATION")) {
            params.put(SpeechConstant.DISABLE_PUNCTUATION, options.getBoolean("DISABLE_PUNCTUATION"));
        }
        if (options.hasKey("PUNCTUATION_MODE")) {
            params.put(SpeechConstant.ASR_PUNCTUATION_MODE, options.getInt("PUNCTUATION_MODE"));
        }
        if (options.hasKey("ACCEPT_AUDIO_DATA")) {
            params.put(SpeechConstant.ACCEPT_AUDIO_DATA, options.getBoolean("ACCEPT_AUDIO_DATA"));
        }
        // 是否需要语音音量数据回调，开启后有CALLBACK_EVENT_ASR_VOLUME事件回调
        if (options.hasKey("ACCEPT_AUDIO_VOLUME")) {
            params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, options.getBoolean("ACCEPT_AUDIO_VOLUME"));
        }
        if (options.hasKey("SOUND_START")) {
            params.put(SpeechConstant.SOUND_START, options.getInt("SOUND_START"));
        }
        if (options.hasKey("SOUND_END")) {
            params.put(SpeechConstant.SOUND_END, options.getInt("SOUND_END"));
        }
        if (options.hasKey("SOUND_SUCCESS")) {
            params.put(SpeechConstant.SOUND_SUCCESS, options.getInt("SOUND_SUCCESS"));
        }
        if (options.hasKey("SOUND_ERROR")) {
            params.put(SpeechConstant.SOUND_ERROR, options.getInt("SOUND_ERROR"));
        }
        if (options.hasKey("SOUND_CANCEL")) {
            params.put(SpeechConstant.SOUND_CANCEL, options.getInt("SOUND_CANCEL"));
        }
        // 采样率 ，固定及默认值16000
        if (options.hasKey("SAMPLE_RATE")) {
            params.put(SpeechConstant.SAMPLE_RATE, options.getInt("SAMPLE_RATE"));
        }
        if (options.hasKey("ASR_OFFLINE_ENGINE_LICENSE_FILE_PATH")) {
            params.put(SpeechConstant.ASR_OFFLINE_ENGINE_LICENSE_FILE_PATH, options.getString("ASR_OFFLINE_ENGINE_LICENSE_FILE_PATH"));
        }

        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
        Log.i(TAG, "设置的start输入参数：" + params);
        // 复制此段可以自动检测常规错误
        (new AutoCheck(mReactApplicationContext.getApplicationContext(), new MyHandler(), false)).checkAsr(params);

        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        if (myRecognizer != null) {
            myRecognizer.start(params);
            isListening = true;
        }
    }

    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     * 基于DEMO集成4.1 发送停止事件 停止录音
     */
    @ReactMethod
    public void stop() {
        if (isListening) {
            myRecognizer.stop();
            isListening = false;
        }
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     * 基于DEMO集成4.2 发送取消事件 取消本次识别
     */
    @ReactMethod
    public void cancel() {
        if (isListening) {
            myRecognizer.cancel();
            isListening = false;
        }
    }

    @ReactMethod
    public void release() {
        if (myRecognizer != null) {
            myRecognizer.release();
            myRecognizer = null;
            isListening = false;
        }
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        // 基于DEMO5.1 卸载离线资源(离线时使用) release()方法中封装了卸载离线资源的过程
        // 基于DEMO的5.2 退出事件管理器
        release();
        // BluetoothUtil.destory(this); // 蓝牙关闭
    }

    //释放资源
    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        release();
    }

    /**
     * 处理语音识别回调结果
     *
     * @param msg 语音识别结果
     */
    private void handleMsg(Message msg) {
        WritableMap params = Arguments.createMap();
        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case STATUS_NONE:
                params.putInt("code", STATUS_NONE);
                params.putString("msg", "初始状态");
                break;
            case STATUS_FINISHED:
                params.putInt("code", STATUS_FINISHED);
                params.putString("msg", "识别一段话结束");
                break;
            case STATUS_READY:
                params.putInt("code", STATUS_READY);
                params.putString("msg", "引擎就绪，可以开始说话");
                break;
            case STATUS_SPEAKING:
                params.putInt("code", STATUS_SPEAKING);
                params.putString("msg", "检测到用户说话");
                break;
            case STATUS_RECOGNITION:
                params.putInt("code", STATUS_RECOGNITION);
                params.putString("msg", "检测到用户说话结束");
                break;
            case STATUS_LONG_SPEECH_FINISHED:
                params.putInt("code", STATUS_LONG_SPEECH_FINISHED);
                params.putString("msg", "长语音识别结束");
                break;
            case STATUS_ERROR:
                params.putInt("code", STATUS_ERROR);
                params.putString("msg", "语音识别错误");
            default:
                break;
        }

        if (msg.obj != null) {
            params.putString("data", msg.obj.toString());
        } else if (msg.what != STATUS_ASR_VOLUME) {
            params.putNull("data");
        }

        if (msg.what == STATUS_ERROR) {
            onJSEvent("onRecognizerError", params);
        } else if (msg.what == STATUS_ASR_VOLUME) {
            params.putInt("volumePercent", msg.arg1);
            params.putInt("volume", msg.arg2);
            onJSEvent("onAsrVolume", params);
        } else {
            onJSEvent("onRecognizerResult", params);
        }
    }

    /**
     * 发送事件给js层
     *
     * @param eventName 事件名
     * @param data      数据
     */
    private void onJSEvent(String eventName, Object data) {
        if (mEventEmitter == null) {
            mEventEmitter = mReactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        }
        mEventEmitter.emit(eventName, data);
    }

    /**
     * 静态匿名内部类不会持有一个对外部类的隐式引用，因此Activity将不会被泄露
     */
    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                AutoCheck autoCheck = (AutoCheck) msg.obj;
                synchronized (autoCheck) {
                    String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                    // 可以用下面一行替代，在logcat中查看代码
                    Log.d(TAG, "AutoCheckMessage " + message);
                }
            }
        }
    }

    static class ListenHandler extends Handler {
        private final WeakReference<BaiduAsrModule> baiduAsrModuleWeakReference;

        public ListenHandler(BaiduAsrModule mSpeechRecognizer) {
            this.baiduAsrModuleWeakReference = new WeakReference<>(mSpeechRecognizer);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            BaiduAsrModule baiduAsrModule = baiduAsrModuleWeakReference.get();
            baiduAsrModule.handleMsg(msg);
        }
    }
}
