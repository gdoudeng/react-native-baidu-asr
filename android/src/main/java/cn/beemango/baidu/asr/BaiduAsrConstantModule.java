package cn.beemango.baidu.asr;

import com.baidu.speech.asr.SpeechConstant;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_ERROR;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_FINISHED;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_LONG_SPEECH_FINISHED;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_NONE;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_READY;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_RECOGNITION;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_SPEAKING;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_STOPPED;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAITING_READY;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAKEUP_EXIT;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.STATUS_WAKEUP_SUCCESS;
import static com.baidu.aip.asrwakeup3.core.recog.IStatus.WHAT_MESSAGE_STATUS;

public class BaiduAsrConstantModule extends ReactContextBaseJavaModule {

    public BaiduAsrConstantModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "BaiduAsrConstantModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        // 自定义常量
        constants.put("STATUS_NONE", STATUS_NONE);
        constants.put("STATUS_READY", STATUS_READY);
        constants.put("STATUS_SPEAKING", STATUS_SPEAKING);
        constants.put("STATUS_RECOGNITION", STATUS_RECOGNITION);
        constants.put("STATUS_FINISHED", STATUS_FINISHED);
        constants.put("STATUS_LONG_SPEECH_FINISHED", STATUS_LONG_SPEECH_FINISHED);
        constants.put("STATUS_STOPPED", STATUS_STOPPED);
        constants.put("STATUS_ERROR", STATUS_ERROR);
        constants.put("STATUS_WAITING_READY", STATUS_WAITING_READY);
        constants.put("WHAT_MESSAGE_STATUS", WHAT_MESSAGE_STATUS);
        constants.put("STATUS_WAKEUP_SUCCESS", STATUS_WAKEUP_SUCCESS);
        constants.put("STATUS_WAKEUP_EXIT", STATUS_WAKEUP_EXIT);
        // 百度语音识别所有的常量
        constants.put("ASR_START", SpeechConstant.ASR_START);
        constants.put("ASR_STOP", SpeechConstant.ASR_STOP);
        constants.put("ASR_CANCEL", SpeechConstant.ASR_CANCEL);
        constants.put("ASR_KWS_LOAD_ENGINE", SpeechConstant.ASR_KWS_LOAD_ENGINE);
        constants.put("ASR_KWS_UNLOAD_ENGINE", SpeechConstant.ASR_KWS_UNLOAD_ENGINE);
        constants.put("ASR_UPLOAD_WORDS", SpeechConstant.ASR_UPLOAD_WORDS);
        constants.put("ASR_UPLOAD_CANCEL", SpeechConstant.ASR_UPLOAD_CANCEL);
        constants.put("WAKEUP_START", SpeechConstant.WAKEUP_START);
        constants.put("WAKEUP_STOP", SpeechConstant.WAKEUP_STOP);
        constants.put("WAKEUP_LOAD_ENGINE", SpeechConstant.WAKEUP_LOAD_ENGINE);
        constants.put("WAKEUP_UNLOAD_ENGINE", SpeechConstant.WAKEUP_UNLOAD_ENGINE);
        constants.put("ASR_UPLOAD_CONTRACT", SpeechConstant.ASR_UPLOAD_CONTRACT);
        constants.put("UPLOADER_START", SpeechConstant.UPLOADER_START);
        constants.put("UPLOADER_CANCEL", SpeechConstant.UPLOADER_CANCEL);
        constants.put("CALLBACK_EVENT_ASR_READY", SpeechConstant.CALLBACK_EVENT_ASR_READY);
        constants.put("CALLBACK_EVENT_ASR_BEGIN", SpeechConstant.CALLBACK_EVENT_ASR_BEGIN);
        constants.put("CALLBACK_EVENT_ASR_AUDIO", SpeechConstant.CALLBACK_EVENT_ASR_AUDIO);
        constants.put("CALLBACK_EVENT_ASR_VOLUME", SpeechConstant.CALLBACK_EVENT_ASR_VOLUME);
        constants.put("CALLBACK_EVENT_ASR_END", SpeechConstant.CALLBACK_EVENT_ASR_END);
        constants.put("CALLBACK_EVENT_ASR_PARTIAL", SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL);
        constants.put("CALLBACK_EVENT_ASR_FINISH", SpeechConstant.CALLBACK_EVENT_ASR_FINISH);
        constants.put("CALLBACK_EVENT_ASR_EXIT", SpeechConstant.CALLBACK_EVENT_ASR_EXIT);
        constants.put("CALLBACK_EVENT_ASR_CANCEL", SpeechConstant.CALLBACK_EVENT_ASR_CANCEL);
        constants.put("CALLBACK_EVENT_ASR_ERROR", SpeechConstant.CALLBACK_EVENT_ASR_ERROR);
        constants.put("CALLBACK_EVENT_ASR_LOADED", SpeechConstant.CALLBACK_EVENT_ASR_LOADED);
        constants.put("CALLBACK_EVENT_ASR_UNLOADED", SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED);
        constants.put("CALLBACK_EVENT_ASR_SERIALNUMBER", SpeechConstant.CALLBACK_EVENT_ASR_SERIALNUMBER);
        constants.put("CALLBACK_EVENT_ASR_LOG", SpeechConstant.CALLBACK_EVENT_ASR_LOG);
        constants.put("CALLBACK_EVENT_UPLOAD_COMPLETE", SpeechConstant.CALLBACK_EVENT_UPLOAD_COMPLETE);
        constants.put("CALLBACK_EVENT_ASR_LONG_SPEECH", SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH);
        constants.put("ASR_CALLBACk_NAME", SpeechConstant.ASR_CALLBACk_NAME);
        constants.put("WAKEUP_CALLBACK_NAME", SpeechConstant.WAKEUP_CALLBACK_NAME);
        constants.put("UPLOAD_CALLBACK_NAME", SpeechConstant.UPLOAD_CALLBACK_NAME);
        constants.put("CALLBACK_ASR_STATUS", SpeechConstant.CALLBACK_ASR_STATUS);
        constants.put("strCALLBACK_ASR_LEVEL", SpeechConstant.strCALLBACK_ASR_LEVEL);
        constants.put("CALLBACK_ASR_RESULT", SpeechConstant.CALLBACK_ASR_RESULT);
        constants.put("CALLBACK_WAK_STATUS", SpeechConstant.CALLBACK_WAK_STATUS);
        constants.put("CALLBACK_WAK_RESULT", SpeechConstant.CALLBACK_WAK_RESULT);
        constants.put("CALLBACK_ERROR_DOMAIN", SpeechConstant.CALLBACK_ERROR_DOMAIN);
        constants.put("CALLBACK_ERROR_CODE", SpeechConstant.CALLBACK_ERROR_CODE);
        constants.put("CALLBACK_ERROR_DESC", SpeechConstant.CALLBACK_ERROR_DESC);
        constants.put("CALLBACK_EVENT_UNIT_FINISH", SpeechConstant.CALLBACK_EVENT_UNIT_FINISH);
        constants.put("SOUND_START", SpeechConstant.SOUND_START);
        constants.put("SOUND_END", SpeechConstant.SOUND_END);
        constants.put("SOUND_SUCCESS", SpeechConstant.SOUND_SUCCESS);
        constants.put("SOUND_ERROR", SpeechConstant.SOUND_ERROR);
        constants.put("SOUND_CANCEL", SpeechConstant.SOUND_CANCEL);
        constants.put("CALLBACK_EVENT_WAKEUP_STARTED", SpeechConstant.CALLBACK_EVENT_WAKEUP_STARTED);
        constants.put("CALLBACK_EVENT_WAKEUP_READY", SpeechConstant.CALLBACK_EVENT_WAKEUP_READY);
        constants.put("CALLBACK_EVENT_WAKEUP_STOPED", SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED);
        constants.put("CALLBACK_EVENT_WAKEUP_LOADED", SpeechConstant.CALLBACK_EVENT_WAKEUP_LOADED);
        constants.put("CALLBACK_EVENT_WAKEUP_UNLOADED", SpeechConstant.CALLBACK_EVENT_WAKEUP_UNLOADED);
        constants.put("CALLBACK_EVENT_WAKEUP_ERROR", SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR);
        constants.put("CALLBACK_EVENT_WAKEUP_SUCCESS", SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS);
        constants.put("CALLBACK_EVENT_WAKEUP_AUDIO", SpeechConstant.CALLBACK_EVENT_WAKEUP_AUDIO);
        constants.put("CALLBACK_EVENT_UPLOAD_FINISH", SpeechConstant.CALLBACK_EVENT_UPLOAD_FINISH);
        constants.put("LOG_LEVEL", SpeechConstant.LOG_LEVEL);
        constants.put("LANGUAGE", SpeechConstant.LANGUAGE);
        constants.put("CONTACT", SpeechConstant.CONTACT);
        constants.put("VAD", SpeechConstant.VAD);
        constants.put("VAD_MFE", SpeechConstant.VAD_MFE);
        constants.put("VAD_MODEL", SpeechConstant.VAD_MODEL);
        constants.put("VAD_DNN", SpeechConstant.VAD_DNN);
        constants.put("VAD_TOUCH", SpeechConstant.VAD_TOUCH);
        constants.put("SAMPLE_RATE", SpeechConstant.SAMPLE_RATE);
        constants.put("PAM", SpeechConstant.PAM);
        constants.put("NLU", SpeechConstant.NLU);
        constants.put("PROP", SpeechConstant.PROP);
        constants.put("IN_FILE", SpeechConstant.IN_FILE);
        constants.put("AUDIO_MILLS", SpeechConstant.AUDIO_MILLS);
        constants.put("AUDIO_SOURCE", SpeechConstant.AUDIO_SOURCE);
        constants.put("OUT_FILE", SpeechConstant.OUT_FILE);
        constants.put("ACCEPT_AUDIO_DATA", SpeechConstant.ACCEPT_AUDIO_DATA);
        constants.put("ACCEPT_AUDIO_VOLUME", SpeechConstant.ACCEPT_AUDIO_VOLUME);
        constants.put("APP_KEY", SpeechConstant.APP_KEY);
        constants.put("SECRET", SpeechConstant.SECRET);
        constants.put("URL", SpeechConstant.URL);
        constants.put("PID", SpeechConstant.PID);
        constants.put("LMID", SpeechConstant.LMID);
        constants.put("APP_NAME", SpeechConstant.APP_NAME);
        constants.put("URL_NEW", SpeechConstant.URL_NEW);
        constants.put("URL_OLD", SpeechConstant.URL_OLD);
        constants.put("DEC_TYPE", SpeechConstant.DEC_TYPE);
        constants.put("DECODER", SpeechConstant.DECODER);
        constants.put("ASR_VAD_RES_FILE_PATH", SpeechConstant.ASR_VAD_RES_FILE_PATH);
        constants.put("VAD_ENDPOINT_TIMEOUT", SpeechConstant.VAD_ENDPOINT_TIMEOUT);
        constants.put("BDS_ASR_ENABLE_LONG_SPEECH", SpeechConstant.BDS_ASR_ENABLE_LONG_SPEECH);
        constants.put("ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH", SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH);
        constants.put("ASR_OFFLINE_ENGINE_DAT_FILE_PATH", SpeechConstant.ASR_OFFLINE_ENGINE_DAT_FILE_PATH);
        constants.put("ASR_OFFLINE_ENGINE_LICENSE_FILE_PATH", SpeechConstant.ASR_OFFLINE_ENGINE_LICENSE_FILE_PATH);
        constants.put("SLOT_DATA", SpeechConstant.SLOT_DATA);
        constants.put("DISABLE_PUNCTUATION", SpeechConstant.DISABLE_PUNCTUATION);
        constants.put("KWS_TYPE", SpeechConstant.KWS_TYPE);
        constants.put("APP_ID", SpeechConstant.APP_ID);
        constants.put("DEV", SpeechConstant.DEV);
        constants.put("WP_DAT_FILEPATH", SpeechConstant.WP_DAT_FILEPATH);
        constants.put("WP_WORDS_FILE", SpeechConstant.WP_WORDS_FILE);
        constants.put("WP_WORDS", SpeechConstant.WP_WORDS);
        constants.put("ENABLE_HTTPDNS", SpeechConstant.ENABLE_HTTPDNS);
        constants.put("WP_VAD_ENABLE", SpeechConstant.WP_VAD_ENABLE);
        constants.put("WP_ENGINE_LICENSE_FILE_PATH", SpeechConstant.WP_ENGINE_LICENSE_FILE_PATH);
        constants.put("BOT_SESSION_LIST", SpeechConstant.BOT_SESSION_LIST);
        constants.put("BOT_ID", SpeechConstant.BOT_ID);
        constants.put("BOT_SESSION", SpeechConstant.BOT_SESSION);
        constants.put("BOT_SESSION_ID", SpeechConstant.BOT_SESSION_ID);
        constants.put("ASR_ENABLE_NUMBERFORMAT", SpeechConstant.ASR_ENABLE_NUMBERFORMAT);
        constants.put("ASR_PUNCTUATION_MODE", SpeechConstant.ASR_PUNCTUATION_MODE);
        return constants;
    }
}