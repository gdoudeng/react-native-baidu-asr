package cn.beemango.baidu.asr.listener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.tts.client.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 在 MessageListener的基础上，和UI配合。
 * Created by fujiayi on 2017/9/14.
 */

public class UiMessageListener extends MessageListener {

    private Handler mainHandler;

    private static final String TAG = "UiMessageListener";

    public UiMessageListener(Handler mainHandler) {
        super();
        this.mainHandler = mainHandler;
    }

    /**
     * 开始语音合成
     *
     * @param utteranceId 话语id
     */
    @Override
    public void onSynthesizeStart(String utteranceId) {
        super.onSynthesizeStart(utteranceId);
        sendMessage(utteranceId);
    }

    /**
     * 合成结束回调
     *
     * @param utteranceId 话语id
     */
    @Override
    public void onSynthesizeFinish(String utteranceId) {
        super.onSynthesizeFinish(utteranceId);
        sendMessage(utteranceId);
    }

    /**
     * 开始播放回调
     *
     * @param utteranceId 话语id
     */
    @Override
    public void onSpeechStart(String utteranceId) {
        super.onSpeechStart(utteranceId);
        sendMessage(utteranceId);
    }

    /**
     * 播放结束回调
     *
     * @param utteranceId 话语id
     */
    @Override
    public void onSpeechFinish(String utteranceId) {
        super.onSpeechFinish(utteranceId);
        sendMessage(utteranceId);
    }

    /**
     * 合成失败回调
     *
     * @param utteranceId 话语id
     * @param speechError 包含错误码和错误信息
     */
    @Override
    public void onError(String utteranceId, SpeechError speechError) {
        super.onError(utteranceId, speechError);
        try {
            JSONObject message = new JSONObject();
            message.put("utteranceId", utteranceId);
            message.put("code", speechError.code);
            message.put("description", speechError.description);
            sendMessage(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 语音流 16K采样率 16bits编码 单声道 。
     * <p>
     * 合成数据和进度的回调接口，分多次回调。
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId 话语id
     * @param data        合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     *                    engineType  下版本提供 1:音频数据由离线引擎合成； 0：音频数据由在线引擎（百度服务器）合成。
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
        super.onSynthesizeDataArrived(utteranceId, data, progress);
        try {
            JSONObject message = new JSONObject();
            message.put("utteranceId", utteranceId);
            message.put("progress", progress);
            sendMessage(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放进度回调接口，分多次回调
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId 话语
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        super.onSpeechProgressChanged(utteranceId, progress);
        try {
            JSONObject message = new JSONObject();
            message.put("utteranceId", utteranceId);
            message.put("progress", progress);
            sendMessage(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(String message) {
        if (mainHandler == null) {
            Log.i(TAG, message);
            return;
        }
        Message msg = Message.obtain();
        msg.what = status;
        msg.arg1 = status;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }
}
