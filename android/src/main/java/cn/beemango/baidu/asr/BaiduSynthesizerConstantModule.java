package cn.beemango.baidu.asr;

import androidx.annotation.NonNull;

import com.baidu.tts.client.SpeechSynthesizer;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

import cn.beemango.baidu.asr.util.MainHandlerConstant;

public class BaiduSynthesizerConstantModule extends ReactContextBaseJavaModule {

    public BaiduSynthesizerConstantModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "BaiduSynthesizerConstantModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        // 自定义常量
        constants.put("STATUS_NONE", MainHandlerConstant.STATUS_NONE);
        constants.put("INIT_SUCCESS", MainHandlerConstant.INIT_SUCCESS);
        constants.put("STATUS_SYNTHESIZE_START", MainHandlerConstant.STATUS_SYNTHESIZE_START);
        constants.put("STATUS_SYNTHESIZE_PROCESSING", MainHandlerConstant.STATUS_SYNTHESIZE_PROCESSING);
        constants.put("STATUS_SYNTHESIZE_FINISH", MainHandlerConstant.STATUS_SYNTHESIZE_FINISH);
        constants.put("STATUS_SPEAK_START", MainHandlerConstant.STATUS_SPEAK_START);
        constants.put("STATUS_SPEAKING", MainHandlerConstant.STATUS_SPEAKING);
        constants.put("STATUS_SPEAK_FINISH", MainHandlerConstant.STATUS_SPEAK_FINISH);
        constants.put("STATUS_ERROR", MainHandlerConstant.STATUS_ERROR);
        // 百度语音合成所有的常量
        constants.put("VersionName", SpeechSynthesizer.VersionName);
        constants.put("VersionName_CODE", SpeechSynthesizer.VersionName_CODE);
        constants.put("ERROR_QUEUE_IS_FULL", SpeechSynthesizer.ERROR_QUEUE_IS_FULL);
        constants.put("ERROR_LIST_IS_TOO_LONG", SpeechSynthesizer.ERROR_LIST_IS_TOO_LONG);
        constants.put("ERROR_TEXT_IS_EMPTY", SpeechSynthesizer.ERROR_TEXT_IS_EMPTY);
        constants.put("ERROR_TEXT_IS_TOO_LONG", SpeechSynthesizer.ERROR_TEXT_IS_TOO_LONG);
        constants.put("ERROR_TEXT_ENCODE_IS_WRONG", SpeechSynthesizer.ERROR_TEXT_ENCODE_IS_WRONG);
        constants.put("ERROR_APP_ID_IS_INVALID", SpeechSynthesizer.ERROR_APP_ID_IS_INVALID);
        constants.put("MAX_QUEUE_SIZE", SpeechSynthesizer.MAX_QUEUE_SIZE);
        constants.put("MAX_LIST_SIZE", SpeechSynthesizer.MAX_LIST_SIZE);
        constants.put("PARAM_REQUEST_PROTOCOL", SpeechSynthesizer.PARAM_REQUEST_PROTOCOL);
        constants.put("PARAM_REQUEST_ENABLE_DNS", SpeechSynthesizer.PARAM_REQUEST_ENABLE_DNS);
        constants.put("REQUEST_DNS_OFF", SpeechSynthesizer.REQUEST_DNS_OFF);
        constants.put("REQUEST_DNS_ON", SpeechSynthesizer.REQUEST_DNS_ON);
        constants.put("REQUEST_PROTOCOL_HTTP", SpeechSynthesizer.REQUEST_PROTOCOL_HTTP);
        constants.put("REQUEST_PROTOCOL_HTTPS", SpeechSynthesizer.REQUEST_PROTOCOL_HTTPS);
        constants.put("PARAM_PROXY_HOST", SpeechSynthesizer.PARAM_PROXY_HOST);
        constants.put("PARAM_PROXY_PORT", SpeechSynthesizer.PARAM_PROXY_PORT);
        constants.put("PARAM_URL", SpeechSynthesizer.PARAM_URL);
        constants.put("PARAM_AUDIO_CTRL", SpeechSynthesizer.PARAM_AUDIO_CTRL);
        constants.put("PARAM_TEXT_CTRL", SpeechSynthesizer.PARAM_TEXT_CTRL);
        constants.put("PARAM_SPEED", SpeechSynthesizer.PARAM_SPEED);
        constants.put("PARAM_PITCH", SpeechSynthesizer.PARAM_PITCH);
        constants.put("PARAM_VOLUME", SpeechSynthesizer.PARAM_VOLUME);
        constants.put("PARAM_SPEC", SpeechSynthesizer.PARAM_SPEC);
        constants.put("PARAM_TTS_TEXT_MODEL_FILE", SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE);
        constants.put("PARAM_TTS_SPEECH_MODEL_FILE", SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE);
        constants.put("PARAM_TTS_LICENCE_FILE", SpeechSynthesizer.PARAM_TTS_LICENCE_FILE);
        constants.put("PARAM_VOCODER_OPTIM_LEVEL", SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL);
        constants.put("PARAM_CUSTOM_SYNTH", SpeechSynthesizer.PARAM_CUSTOM_SYNTH);
        constants.put("PARAM_OPEN_XML", SpeechSynthesizer.PARAM_OPEN_XML);
        constants.put("PARAM_BILINGUAL_MODE", SpeechSynthesizer.PARAM_BILINGUAL_MODE);
        constants.put("PARAM_PRODUCT_ID", SpeechSynthesizer.PARAM_PRODUCT_ID);
        constants.put("PARAM_KEY", SpeechSynthesizer.PARAM_KEY);
        constants.put("PARAM_AUTH_SN", SpeechSynthesizer.PARAM_AUTH_SN);
        constants.put("PARAM_LANGUAGE", SpeechSynthesizer.PARAM_LANGUAGE);
        constants.put("PARAM_AUDIO_ENCODE", SpeechSynthesizer.PARAM_AUDIO_ENCODE);
        constants.put("PARAM_AUDIO_RATE", SpeechSynthesizer.PARAM_AUDIO_RATE);
        constants.put("PARAM_SPEAKER", SpeechSynthesizer.PARAM_SPEAKER);
        constants.put("PARAM_MIX_MODE", SpeechSynthesizer.PARAM_MIX_MODE);
        constants.put("MIX_MODE_DEFAULT", SpeechSynthesizer.MIX_MODE_DEFAULT);
        constants.put("MIX_MODE_HIGH_SPEED_NETWORK", SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
        constants.put("PARAM_MIX_MODE_TIMEOUT", SpeechSynthesizer.PARAM_MIX_MODE_TIMEOUT);
        constants.put("PARAM_MIX_TIMEOUT_TWO_SECOND", SpeechSynthesizer.PARAM_MIX_TIMEOUT_TWO_SECOND);
        constants.put("PARAM_MIX_TIMEOUT_THREE_SECOND", SpeechSynthesizer.PARAM_MIX_TIMEOUT_THREE_SECOND);
        constants.put("PARAM_MIX_TIMEOUT_FOUR_SECOND", SpeechSynthesizer.PARAM_MIX_TIMEOUT_FOUR_SECOND);
        constants.put("MIX_MODE_HIGH_SPEED_SYNTHESIZE", SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE);
        constants.put("MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI", SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
        constants.put("LANGUAGE_ZH", SpeechSynthesizer.LANGUAGE_ZH);
        constants.put("LANGUAGE_EN", SpeechSynthesizer.LANGUAGE_EN);
        constants.put("TEXT_ENCODE_GBK", SpeechSynthesizer.TEXT_ENCODE_GBK);
        constants.put("TEXT_ENCODE_BIG5", SpeechSynthesizer.TEXT_ENCODE_BIG5);
        constants.put("TEXT_ENCODE_UTF8", SpeechSynthesizer.TEXT_ENCODE_UTF8);
        constants.put("AUDIO_ENCODE_BV", SpeechSynthesizer.AUDIO_ENCODE_BV);
        constants.put("AUDIO_ENCODE_AMR", SpeechSynthesizer.AUDIO_ENCODE_AMR);
        constants.put("AUDIO_ENCODE_OPUS", SpeechSynthesizer.AUDIO_ENCODE_OPUS);
        constants.put("AUDIO_ENCODE_MP3", SpeechSynthesizer.AUDIO_ENCODE_MP3);
        constants.put("AUDIO_ENCODE_PCM", SpeechSynthesizer.AUDIO_ENCODE_PCM);
        constants.put("AUDIO_SAMPLERATE_8K", SpeechSynthesizer.AUDIO_SAMPLERATE_8K);
        constants.put("AUDIO_SAMPLERATE_16K", SpeechSynthesizer.AUDIO_SAMPLERATE_16K);
        constants.put("AUDIO_SAMPLERATE_24K", SpeechSynthesizer.AUDIO_SAMPLERATE_24K);
        constants.put("AUDIO_SAMPLERATE_48K", SpeechSynthesizer.AUDIO_SAMPLERATE_48K);
        constants.put("AUDIO_BITRATE_BV_16K", SpeechSynthesizer.AUDIO_BITRATE_BV_16K);
        constants.put("AUDIO_BITRATE_AMR_6K6", SpeechSynthesizer.AUDIO_BITRATE_AMR_6K6);
        constants.put("AUDIO_BITRATE_AMR_8K85", SpeechSynthesizer.AUDIO_BITRATE_AMR_8K85);
        constants.put("AUDIO_BITRATE_AMR_12K65", SpeechSynthesizer.AUDIO_BITRATE_AMR_12K65);
        constants.put("AUDIO_BITRATE_AMR_14K25", SpeechSynthesizer.AUDIO_BITRATE_AMR_14K25);
        constants.put("AUDIO_BITRATE_AMR_15K85", SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
        constants.put("AUDIO_BITRATE_AMR_18K25", SpeechSynthesizer.AUDIO_BITRATE_AMR_18K25);
        constants.put("AUDIO_BITRATE_AMR_19K85", SpeechSynthesizer.AUDIO_BITRATE_AMR_19K85);
        constants.put("AUDIO_BITRATE_AMR_23K05", SpeechSynthesizer.AUDIO_BITRATE_AMR_23K05);
        constants.put("AUDIO_BITRATE_AMR_23K85", SpeechSynthesizer.AUDIO_BITRATE_AMR_23K85);
        constants.put("AUDIO_BITRATE_PCM", SpeechSynthesizer.AUDIO_BITRATE_PCM);
        constants.put("AUDIO_BITRATE_OPUS_32K", SpeechSynthesizer.AUDIO_BITRATE_OPUS_32K);
        constants.put("AUDIO_BITRATE_OPUS_24K", SpeechSynthesizer.AUDIO_BITRATE_OPUS_24K);
        constants.put("AUDIO_BITRATE_OPUS_20K", SpeechSynthesizer.AUDIO_BITRATE_OPUS_20K);
        constants.put("AUDIO_BITRATE_OPUS_18K", SpeechSynthesizer.AUDIO_BITRATE_OPUS_18K);
        constants.put("AUDIO_BITRATE_OPUS_16K", SpeechSynthesizer.AUDIO_BITRATE_OPUS_16K);
        constants.put("AUDIO_BITRATE_OPUS_8K", SpeechSynthesizer.AUDIO_BITRATE_OPUS_8K);
        return constants;
    }
}