LEO游戏引擎框架介绍

结构:

    -引擎主程序
    -场景模块
    -图层模块
    -元素模块
    -控制模块
    -声音模块
    
场景模块作用: 
     
    -游戏的一个关卡相当于一个场景    
    场景由多个图层,声音,和各种控制器组成
    主引擎只对场景负责,加载一个场景才开始绘制图像,播放声音
    并加载控制器;切换关卡就是加载新的场景;
    
图层模块作用:

    -每个场景的图形显示都由图层堆积;比方说背景图,建筑,静物,怪物,人物
     可分为多个图层,表层图层遮挡底层图层;图层之间可视差滚动等;
    
     
元素模块的作用:

    引擎可绘制显示的基本单元,每个图层可由多个元素组成,比方说背景图层,
    可以由,单个的花,草,树,等元素堆积;
    现阶段元素类型有:
    图片,动画,文字,自定义;
    
控制模块的作用:
    
    屏幕手势点击事件等的交互,npc的自动控制,声音的播放停止,
    场景的切换,机关的运作,等等

声音模块的作用:
    
    负责游戏内声音的播放处理,与安卓的声音系统交互;
    
物理效果模块

    负责元素的碰撞,加速度,重力等物理效果模块
    
    
引擎主程序,负责加载场景,绘制场景内的图层和元素,
执行场景的交互逻辑,调用声音模块执行声音播放等;    
    