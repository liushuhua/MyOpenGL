package com.example.liushuhua.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.liushuhua.opengltest.helper.MatrixHelper;
import com.example.liushuhua.opengltest.helper.ShaderHelper;
import com.example.liushuhua.opengltest.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
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
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private final FloatBuffer verticesData;
    private Context context;
    private static final String A_POSITION = "a_Position";
    //颜色
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final String A_COLOR = "a_Color";
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    //矩阵
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;
    private final float[] modelMatrix = new float[16];

    public OpenGLRenderer(Context context) {
        this.context = context;
        float[] tableVertices = {
                //Triangle Fan
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.6f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.6f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.6f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.6f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.6f, 0.7f, 0.7f, 0.7f,
                //中间分割线
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,
                //上下半场的两个点(木槌)
                0f, -0.3f, 0f, 0f, 1f,
                0f, 0.3f, 1f, 0f, 0f,
        };
        //防止顶点数据被java垃圾回收机制回收
        verticesData = ByteBuffer.
                allocateDirect(tableVertices.length * BYTES_PER_FLOAT).//分配多大内存
                order(ByteOrder.nativeOrder()).//保证同一个平台使用一个排序
                asFloatBuffer();
        verticesData.put(tableVertices);//把数据从Dalvik虚拟机内存复制到本地内存，进程结束时释放
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
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        //读取对应的shader文件
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        //获取Shader对象
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        //把顶点shader和片shader关联起来
        int programObject = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        //验证程序是否可用，可以不加
        ShaderHelper.validateStatus(programObject);
        //告诉屏幕绘制任何东西都要使用这里的自定义程序
        glUseProgram(programObject);
        //去shader文件中对应的属性
        int aColorLocation = glGetAttribLocation(programObject, A_COLOR);
        int aPositionLocation = glGetAttribLocation(programObject, A_POSITION);
        uMatrixLocation = glGetUniformLocation(programObject, U_MATRIX);
        //确保从头开始读取
        verticesData.position(0);
        //告诉OPenGL可以在verticesData缓冲区找到a_position
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, verticesData);
        glEnableVertexAttribArray(aPositionLocation);

        //颜色绑定，跳过顶点
        verticesData.position(POSITION_COMPONENT_COUNT);
        //告诉OPenGL可以在verticesData缓冲区找到a_Color
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, verticesData);
        glEnableVertexAttribArray(aColorLocation);
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
        //矩阵
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        //告诉OpenGL要画的图形（三角形），从顶点数组的开头开始读取，告诉OPenGl有6个顶点
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        //画中间分隔线
        glDrawArrays(GL_LINES, 6, 2);
        //画两个棒槌
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
