package com.example.liushuhua.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.liushuhua.opengltest.helper.MatrixHelper;
import com.example.liushuhua.opengltest.helper.TextureHelper;
import com.example.liushuhua.opengltest.mode.Mallet;
import com.example.liushuhua.opengltest.mode.Table;
import com.example.liushuhua.opengltest.program.ColorShaderProgram;
import com.example.liushuhua.opengltest.program.TextureShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by LiuShuHua on 2017/6/21.
 * description：GlSurfaceView会在一个单独的线程中调用渲染器的方法
 * 默认情况下会按设备的频率不断的渲染，可以自定义按请求渲染
 * 注意：只能在这个渲染线程中调用OpenGL，在Android-Ui线程中刷新页面
 * 通信：在主线程中的GLSurfaceView实例可以调用queueEvent（）方法传递
 * 一个Runnable给后台渲染，渲染线程可以调用Activity的runOnUIThread（）
 * 来传递事件（event）给主线程
 */

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private Context context;
    private Table table;
    private Mallet mallet;
    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;
    private int texture;

    public OpenGLRenderer(Context context) {
        this.context = context;
    }

    /**
     * Surface 创建时会调用，第一次运行和从其他页面返回到该页面（类似Activity的onResume）
     * 可能被多次调用
     *
     * @param gl     供GL1.0使用
     * @param config 配置
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        table = new Table();
        mallet = new Mallet();
        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    /**
     * 每次surface尺寸发生变化的时候，横竖屏来回切换也属于尺寸变化
     *
     * @param gl     供GL1.0使用
     * @param width  w
     * @param height h
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置视口（Viewport）尺寸,就是告诉OpenGL可以用来渲染Surface的大小
        glViewport(0, 0, width, height);
        //投影矩阵
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1.0f, 10f);
        setIdentityM(modelMatrix, 0);
        //z轴平移
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        //绕X旋转
        rotateM(modelMatrix, 0, -45f, 1f, 0f, 0f);
        //临时存储数据
        final float[] temp = new float[16];
        //矩阵相乘
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        //Copy数据
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    /**
     * 当绘制一帧的时候调用，在这个方法中一定要绘制东西，即使只是清空屏幕；因为，
     * 在这个方法返回后，渲染缓冲区会被交换并显示在屏幕上，如果什么都没有画，可能
     * 会显示糟糕的闪烁效果
     *
     * @param gl 供GL1.0使用
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //清空屏幕上的颜色，并且调用crete里面glClearColor（）设置的颜色填充屏幕
        glClear(GL_COLOR_BUFFER_BIT);
        textureShaderProgram.userProgram();
        textureShaderProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureShaderProgram);
        table.draw();
        colorShaderProgram.userProgram();
        colorShaderProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();
    }
}
