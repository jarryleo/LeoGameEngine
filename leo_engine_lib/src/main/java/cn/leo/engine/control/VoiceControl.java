package cn.leo.engine.control;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:54
 * <p>
 * 声音控制器
 * <p>
 * 背景音乐,元素特效声音 等
 */
public class VoiceControl {

    private final int MAX_STREAMS = 100;
    /**
     * 所有声音单元集合
     */
    private ConcurrentHashMap<Integer, Integer> sounds = new ConcurrentHashMap<>();
    /**
     * 停止的声音id
     */
    private ConcurrentHashMap<Integer, Integer> soundsStop = new ConcurrentHashMap<>();
    /**
     * 声音池
     */
    private SoundPool mSoundPool;
    /**
     * 声音开关
     */
    private boolean mHasSound;
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 有参构造
     *
     * @param context 上下文
     */
    public VoiceControl(Context context) {
        mContext = context;
        initSoundPool();
    }

    /**
     * 初始化声音池
     */
    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME);
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(MAX_STREAMS)
                    .setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        } else {
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
    }

    /**
     * 提前加载声音资源，在第一次播放的时候就不会延迟
     *
     * @param rsID 声音资源id
     */
    public void loadSoundRsID(int rsID) {
        int id = mSoundPool.load(mContext, rsID, 1);
        sounds.put(rsID, id);
    }

    /**
     * 提前批量加载声音资源
     *
     * @param rsId 声音资源id数组 可变长度
     */
    public void loadSounds(int... rsId) {
        for (int i = 0; i < rsId.length; i++) {
            loadSoundRsID(rsId[i]);
        }
    }

    /**
     * 播放声音无参 （若无提前加载声音资源，第一次播放会延迟）
     *
     * @param rsID 资源ID
     */
    public void playSound(int rsID, boolean loop) {
        int l = loop ? -1 : 0;
        playSound(rsID, 1.0f, 1.0f, 0, l, 1.0f);
    }

    /**
     * 带参数的声音播放方法
     *
     * @param rsID        资源id
     * @param leftVolume  左声道音量大小 0.0f - 1.0f
     * @param rightVolume 右声道音量大小 0.0f - 1.0f
     * @param priority    优先级 默认0
     * @param loop        循环播放-1 不循环0
     * @param rate        声音倍速 0.5f - 2.0f ,正常倍速 1.0f
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public void playSound(final int rsID, final float leftVolume, final float rightVolume,
                          final int priority, final int loop, final float rate) {
        if (!mHasSound) {
            return;
        }
        if (sounds.containsKey(rsID)) {
            int playId = mSoundPool.play(sounds.get(rsID), leftVolume, rightVolume, priority, loop, rate);
            soundsStop.put(rsID, playId);
        } else {
            final int id = mSoundPool.load(mContext, rsID, 1);
            sounds.put(rsID, id);
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    int playId = soundPool.play(id, leftVolume, rightVolume, priority, loop, rate);
                    soundsStop.put(rsID, playId);
                    mSoundPool.setOnLoadCompleteListener(null);
                }
            });
        }
    }

    /**
     * 声音暂停
     */
    private void soundPause() {
        Set<Integer> ids = sounds.keySet();
        for (Integer id : ids) {
            mSoundPool.pause(sounds.get(id));
        }
        Set<Integer> idss = soundsStop.keySet();
        for (Integer id : idss) {
            mSoundPool.pause(soundsStop.get(id));
        }
    }

    /**
     * 声音继续
     */
    private void soundResume() {
        Set<Integer> ids = sounds.keySet();
        for (Integer id : ids) {
            mSoundPool.resume(sounds.get(id));
        }
        Set<Integer> idss = soundsStop.keySet();
        for (Integer id : idss) {
            mSoundPool.resume(soundsStop.get(id));
        }
    }

    /**
     * 全部静音
     */
    public void soundOff() {
        mHasSound = false;
        soundPause();
    }

    /**
     * 开启声音
     */
    public void soundOn() {
        mHasSound = true;
        soundResume();
    }

    /**
     * 停止播放音乐
     *
     * @param rsID
     */
    public void stopSound(int rsID) {
        mSoundPool.stop(soundsStop.remove(rsID));
    }

    /**
     * 暂停指定的音乐
     *
     * @param rsID
     */
    public void pauseSound(int rsID) {
        mSoundPool.pause(sounds.get(rsID));
    }

    /**
     * 恢复暂停的声音
     *
     * @param rsID
     */
    public void resumeSound(int rsID) {
        mSoundPool.resume(sounds.get(rsID));
    }

    /**
     * 清空所有声音单元
     */
    public void clearSounds() {
        for (int sd : sounds.keySet()) {
            mSoundPool.unload(sounds.get(sd));
        }
        sounds.clear();
    }

    /**
     * 销毁场景,回收资源
     */
    public void onDestroy() {
        clearSounds();
    }
}
