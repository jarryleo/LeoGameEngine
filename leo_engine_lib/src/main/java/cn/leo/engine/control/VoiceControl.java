package cn.leo.engine.control;

/**
 * @author : Jarry Leo
 * @date : 2018/10/30 15:08
 */
public interface VoiceControl extends ControlLife {
    /**
     * 提前批量加载声音资源
     *
     * @param rsId 声音资源id数组 可变长度
     */
    void loadSounds(int... rsId);

    /**
     * 播放声音无参 （若无提前加载声音资源，第一次播放会延迟）
     *
     * @param rsID 资源ID
     */
    void playSound(int rsID, boolean loop);

    /**
     * 全部静音
     */
    void soundOff();

    /**
     * 开启声音
     */
    void soundOn();

    /**
     * 停止播放音乐
     *
     * @param rsID
     */
    void stopSound(int rsID);

    /**
     * 暂停指定的音乐
     *
     * @param rsID
     */
    void pauseSound(int rsID);

    /**
     * 恢复暂停的声音
     *
     * @param rsID
     */
    void resumeSound(int rsID);
}
