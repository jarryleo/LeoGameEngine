package cn.leo.engine.path;

/**
 * @author : Jarry Leo
 * @date : 2018/10/26 9:34
 */
public interface CellInterpolator {
    /**
     * 获取通过插值器计算后的x值
     *
     * @param inputX 输入的值
     * @return 计算后的值
     */
    float getX(float inputX);

    /**
     * 获取通过插值器计算后的x值
     *
     * @param inputY 输入的值
     * @return 计算后的值
     */
    float getY(float inputY);

    /**
     * 获取通过插值器计算后的x值
     *
     * @param inputZ 输入的值
     * @return 计算后的值
     */
    float getZ(float inputZ);

    /**
     * 获取通过插值器计算后的x值
     *
     * @param inputRotate 输入的值
     * @return 计算后的值
     */
    float getRotate(float inputRotate);

}
