package cn.beemango.baidu.asr;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.IWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.RecogWakeupListener;
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
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAITING_READY;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAKEUP_EXIT;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAKEUP_SUCCESS;

/**
 * 集成文档： http://ai.baidu.com/docs#/ASR-Android-SDK/top 集成指南一节
 * 唤醒词功能
 */
public class BaiduWakeUpModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "BaiduWakeUpModule";

    // 唤醒器
    protected MyWakeup myWakeup;
    private final ReactApplicationContext mReactApplicationContext;
    private boolean isStart = false;
    private DeviceEventManagerModule.RCTDeviceEventEmitter mEventEmitter;

    // 百度语音鉴权的信息
    private String APP_ID;
    private String APP_KEY;
    private String SECRET;

    public BaiduWakeUpModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactApplicationContext = reactContext;
        reactContext.addLifecycleEventListener(this);
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    /**
     * 初始化百度语音唤醒 传入鉴权信息
     *
     * @param options 鉴权信息
     */
    @ReactMethod
    public void init(final ReadableMap options) {
        if (myWakeup != null || isStart || options == null) {
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

        // 基于DEMO唤醒词集成第1.1, 1.2, 1.3步骤
        IWakeupListener listener = new RecogWakeupListener(new BaiduWakeUpModule.WakeUpHandler(this));
        myWakeup = new MyWakeup(mReactApplicationContext, listener);
    }

    /**
     * 开始语音唤醒
     * 唤醒输入参数请参考百度语音文档
     * {@see https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#%E5%94%A4%E9%86%92%E8%BE%93%E5%85%A5%E5%8F%82%E6%95%B0}
     *
     * @param options 唤醒输入参数
     */
    @ReactMethod
    public void start(final ReadableMap options) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.APP_ID, APP_ID);
        params.put(SpeechConstant.APP_KEY, APP_KEY);
        params.put(SpeechConstant.SECRET, SECRET);

        if (options != null) {
            if (options.hasKey("WP_WORDS_FILE")) {
                // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
                params.put(SpeechConstant.WP_WORDS_FILE, options.getString("WP_WORDS_FILE"));
            }
            if (options.hasKey("IN_FILE")) {
                params.put(SpeechConstant.IN_FILE, options.getString("IN_FILE"));
            }
            if (options.hasKey("ACCEPT_AUDIO_DATA")) {
                params.put(SpeechConstant.ACCEPT_AUDIO_DATA, options.getBoolean("ACCEPT_AUDIO_DATA"));
            }
            if (options.hasKey("WP_ENGINE_LICENSE_FILE_PATH")) {
                params.put(SpeechConstant.WP_ENGINE_LICENSE_FILE_PATH, options.getString("WP_ENGINE_LICENSE_FILE_PATH"));
            }
            if (options.hasKey("SAMPLE_RATE")) {
                params.put(SpeechConstant.SAMPLE_RATE, options.getInt("SAMPLE_RATE"));
            }
        }

        if (myWakeup != null) {
            myWakeup.start(params);
            isStart = true;
        }
    }

    /**
     * 基于DEMO唤醒词集成第4.1 发送停止事件
     */
    @ReactMethod
    public void stop() {
        if (isStart) {
            myWakeup.stop();
            isStart = false;
        }
    }

    /**
     * 基于DEMO唤醒词集成第5 退出事件管理器
     */
    @ReactMethod
    public void release() {
        if (myWakeup != null) {
            myWakeup.release();
            myWakeup = null;
            isStart = false;
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
        release();
    }

    //释放资源
    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        release();
    }

    private void handleMsg(Message msg) {
        WritableMap params = Arguments.createMap();
        switch (msg.what) {
            case STATUS_WAITING_READY:
                params.putInt("code", STATUS_WAITING_READY);
                params.putString("msg", "初始状态");
                break;
            case STATUS_WAKEUP_SUCCESS:
                params.putInt("code", STATUS_WAKEUP_SUCCESS);
                params.putString("msg", "唤醒成功");
                break;
            case STATUS_WAKEUP_EXIT:
                params.putInt("code", STATUS_WAKEUP_EXIT);
                params.putString("msg", "唤醒词识别结束");
                break;
            case STATUS_ERROR:
                params.putInt("code", STATUS_ERROR);
                params.putString("msg", "唤醒错误");
            default:
                break;
        }

        if (msg.obj != null) {
            params.putString("data", msg.obj.toString());
        } else {
            params.putNull("data");
        }

        if (msg.what == STATUS_ERROR) {
            onJSEvent("onWakeUpError", params);
        } else {
            onJSEvent("onWakeUpResult", params);
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


    static class WakeUpHandler extends Handler {
        private final WeakReference<BaiduWakeUpModule> baiduWakeUpModuleWeakReference;

        public WakeUpHandler(BaiduWakeUpModule baiduWakeUpModule) {
            this.baiduWakeUpModuleWeakReference = new WeakReference<>(baiduWakeUpModule);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            BaiduWakeUpModule baiduWakeUpModule = this.baiduWakeUpModuleWeakReference.get();
            baiduWakeUpModule.handleMsg(msg);
        }
    }
}
