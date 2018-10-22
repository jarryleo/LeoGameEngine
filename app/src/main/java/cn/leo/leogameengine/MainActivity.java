package cn.leo.leogameengine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.leo.engine.LeoEngine;
import cn.leo.leogameengine.game.FirstScene;

public class MainActivity extends AppCompatActivity {

    private LeoEngine mGameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取引擎View
        mGameEngine = findViewById(R.id.GameEngineView);
        //创建场景
        FirstScene firstScene = new FirstScene(this);
        //场景加载到引擎
        mGameEngine.loadScene(firstScene);
    }
}
