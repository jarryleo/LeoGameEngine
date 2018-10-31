package cn.leo.engine.control;

import java.util.List;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.listener.OnCellStateChangeListener;
import cn.leo.engine.path.BasePath;

/**
 * @author : Jarry Leo
 * @date : 2018/10/30 10:10
 */
public interface CellControl extends ControlLife {
    /**
     * 添加元素到元素控制器
     *
     * @param cellName 元素名称
     * @param cell     元素对象
     * @return 元素属性包装类
     */
    CellProperty addCellToControl(String cellName, BaseCell cell);

    /**
     * 从元素控制器移除元素
     *
     * @param cellName 元素名称
     * @param cell     元素对象
     */
    void removeCellFromControl(String cellName, BaseCell cell);

    /**
     * 对同一类cell 设置事件监听
     *
     * @param cellName                cell关键字
     * @param cellStateChangeListener 事件监听
     */
    void setCellEventListener(String cellName, OnCellStateChangeListener cellStateChangeListener);

    /**
     * 统一给元素分组设置速度
     *
     * @param cellName 元素名
     * @param xSpeed   速度
     */
    void setCellXSpeed(String cellName, float xSpeed);

    /**
     * 统一给元素分组设置速度
     *
     * @param cellName 元素名
     * @param ySpeed   速度
     */
    void setCellYSpeed(String cellName, float ySpeed);

    /**
     * 给元素设置路径
     *
     * @param cellName 元素名称
     * @param path     路径
     */
    void setCellPath(String cellName, BasePath path);

    /**
     * 根据关键字获取元素包装属性集合
     *
     * @param cellName 元素名称
     * @return 元素包装属性集合
     */
    List<CellProperty> getCellProperty(String cellName);
}
