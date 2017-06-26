package com.example.liushuhua.opengltest.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.liushuhua.opengltest.OpenGLRenderer;

public class MainActivity extends BaseActivity {
    private GLSurfaceView glSurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new OpenGLRenderer(this));
        setContentView(glSurfaceView);
    }

    /**
     * 设置onPause/onResume方法后，surface视图才能正常的暂停并继续后台渲染线程
     * 同时释放OpenGl的上下文，如果没有设置可能会崩溃，并且被Android系统终止，同
     * 时这些方法设置的前提是Renderer已经设置过（glSurfaceView.
     * setRenderer(new OpenGLRenderer());）
     */
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
