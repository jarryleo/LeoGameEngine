package cn.leo.leogameengine;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import cn.leo.engine.LeoEngine;
import cn.leo.leogameengine.game.FirstScene;

/**
 * @author Leo
 */
public class MainActivity extends AppCompatActivity {

    private LeoEngine mGameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取引擎View
        mGameEngine = findViewById(R.id.GameEngineView);
        //设置全屏和竖屏方向
        mGameEngine.setFullScreen().setPortrait();
        //创建场景
        FirstScene firstScene = new FirstScene(mGameEngine);
        //开场
        firstScene.start();
    }
}
