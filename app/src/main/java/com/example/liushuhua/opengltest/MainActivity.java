package com.example.liushuhua.opengltest;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new OpenGLRenderer());
        setContentView(glSurfaceView);
    }
}
