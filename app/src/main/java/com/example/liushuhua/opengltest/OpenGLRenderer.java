package com.example.liushuhua.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.liushuhua.opengltest.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

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
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer verticesData;
    private Context context;

    public OpenGLRenderer(Context context) {
        this.context = context;
        float[] tableVertices = {
                0f, 0f, 9f, 14f, 0f, 14f,//左边的三角形
                0f, 0f, 9f, 0f, 9f, 14f, //右边的三角形
                0f, 7f, 9f, 7f,//中间分割线
                4.5f, 2f, 4.5f, 12f,//上下半场的两个点(木槌)
        };
        //防止顶点数据被java垃圾回收机制回收
        verticesData = ByteBuffer.
                allocateDirect(tableVertices.length * BYTES_PER_FLOAT).//分配多大内存
                order(ByteOrder.nativeOrder()).//保证同一个平台使用一个排序
                asFloatBuffer();
        verticesData.put(tableVertices);//把数据从Dalvik虚拟机内存复制到本地内存，进程结束时释放
        //读取对应的shader文件
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
    }

    /**
     * Surface 创建时会调用，第一次运行和从其他页面返回到该页面（类似Activity的onResume）
     * 可能被多次调用
     *
     * @param gl     供GL1.0使用
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
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
    }
}
